package controller;

import domain.City;
import domain.HouseBlock;
import domain.Map;
import repositories.CityRepository;
import repositories.MapRepository;
import repositories.Repositories;

import java.util.Optional;

/**
 * This controller is responsible for the creation of cities and their validation on maps.
 *
 * Provides methods to validate the city creation, map names, center and block positions,
 * to create cities and add blocks - manually or automatically.
 */
public class CreateCityController {
    private final MapRepository mapRepo;
    private final CityRepository cityRepo;

    /**
     * The possible errors that can occur when creating a city.
     */
    public enum NameMapError { OK, MAP_NOT_FOUND, NAME_DUPLICATE }
    public enum CenterError { OK, OUT_OF_BOUNDS, POSITION_TAKEN, BLOCK_PRESENT }
    public enum BlockError { OK, OUT_OF_BOUNDS, COLLIDES_CITY_CENTER, COLLIDES_BLOCK }

    /**
     * The constructor of the class, initializing repositories to be used.
     */
    public CreateCityController() {
        var repos = Repositories.getInstance();
        this.mapRepo = repos.getMapRepository();
        this.cityRepo = repos.getCityRepository();
    }

    /**
     * Validates the name and map of the city to be created.
     *
     * @param name  Name of the city to be created.
     * @param map   Map to be used.
     * @return      NameMapError.OK if the city can be created, or one of the following:
     *              NameMapError.MAP_NOT_FOUND if the map does not exist;
     *              NameMapError.NAME_DUPLICATE if the name is already used by another city in the map.
     */
    public NameMapError validateNameMap(String name, String map) {
        Optional<Map> m = mapRepo.getMap(map);
        if (m.isEmpty()) return NameMapError.MAP_NOT_FOUND;
        if (!cityRepo.isNameUnique(name, map)) return NameMapError.NAME_DUPLICATE;
        return NameMapError.OK;
    }

    /**
     * Validates the center position of the city to be created.
     *
     * @param name  Name of city-to-be-created.
     * @param map   Map where the city will be created.
     * @param x     The X-coordinate of the city center.
     * @param y     The Y-coordinate of the city center.
     * @return      CenterError.OK if the city can be created, or one of the following:
     *              CenterError.OUT_OF_BOUNDS if the coordinates are outside the map bounds;
     *              CenterError.POSITION_TAKEN if the coordinates are already occupied by another city;
     *              CenterError.BLOCK_PRESENT if the coordinates are already occupied by a block.
     */
    public CenterError validateCenter(String name, String map, int x, int y) {
        Optional<domain.Map> m = mapRepo.getMap(map);
        if (m.isEmpty()) return CenterError.OUT_OF_BOUNDS;
        var mp = m.get();
        if (x < 1 || x >= mp.getWidth() || y < 1 || y >= mp.getHeight()) return CenterError.OUT_OF_BOUNDS;
        if (cityRepo.existsAtPosition(map, x, y)) return CenterError.POSITION_TAKEN;
        HouseBlock hb = new HouseBlock(x, y);
        if (!cityRepo.isBlockPositionUnique(map, hb)) return CenterError.BLOCK_PRESENT;
        return CenterError.OK;
    }

    /**
     * Validates the block position of the city to be created.
     *
     * @param map   Map where the block will be placed.
     * @param b     House block to be validated.
     * @return      BlockError.OK if the block can be placed, or one of the following:
     *              BlockError.OUT_OF_BOUNDS if the coordinates are outside the map bounds;
     *              BlockError.COLLIDES_CITY_CENTER if the coordinates are already occupied by another city;
     *              BlockError.COLLIDES_BLOCK if the coordinates are already occupied by another block.
     */
    public BlockError validateBlock(String map, HouseBlock b) {
        Optional<domain.Map> m = mapRepo.getMap(map);
        if (m.isEmpty()) return BlockError.OUT_OF_BOUNDS;
        var mp = m.get();
        int x = b.getX(), y = b.getY();
        if (x < 1 || x >= mp.getWidth() || y < 1 || y >= mp.getHeight()) return BlockError.OUT_OF_BOUNDS;
        if (cityRepo.existsAtPosition(map, x, y)) return BlockError.COLLIDES_CITY_CENTER;
        if (!cityRepo.isBlockPositionUnique(map, b)) return BlockError.COLLIDES_BLOCK;
        return BlockError.OK;
    }

    /**
     * Creates a new city in the given map, at the given coordinates.
     *
     * @param name  Name of the city to be created.
     * @param map   Map where the city will be created.
     * @param x     Center X coordinate of the city.
     * @param y     Center Y coordinate of the city.
     * @return      Created city.
     * @throws      IllegalStateException if the city cannot be created for any reason.
     */
    public City createCity(String name, String map, int x, int y) {
        if (validateNameMap(name, map) != NameMapError.OK || validateCenter(name, map, x, y) != CenterError.OK) {
            throw new IllegalStateException("Cannot create city: validation failed");
        }
        City c = new City(name, map, x, y);
        cityRepo.add(c);
        return c;
    }


    /**
     * Manually adds a house block to the specified city, after validating its position.
     * The validation checks if the block's position is within the map boundaries,
     * does not collide with another city's center, and does not conflict with existing blocks.
     *
     * @param city   {@code City} where the house block will be added
     * @param b      {@code HouseBlock} representing the block to be added
     * @return       {@code true} if the block was successfully added, or {@code false} if the validation fails
     */
    public boolean addBlockManual(City city, HouseBlock b) {
        BlockError err = validateBlock(city.getMapName(), b);
        if (err != BlockError.OK) return false;
        city.addBlockManual(b);
        return true;
    }

    /**
     * Automatically adds blocks to the given city.
     * The number of blocks to be added is specified by the count parameter.
     *
     * @param city      City to which the blocks will be added.
     * @param count     Number of blocks to be added.
     * @param sigma     Standard deviation of the Gaussian distribution used to generate the coordinates of the blocks.
     */
    public void assignBlocksAuto(City city, int count, double sigma) {
        java.util.Random rnd = new java.util.Random();
        for (int i = 0; i < count; i++) {
            HouseBlock hb;
            BlockError err;
            do {
                int x = city.getCenterX() + (int) (rnd.nextGaussian() * sigma);
                int y = city.getCenterY() + (int) (rnd.nextGaussian() * sigma);
                hb = new HouseBlock(x, y);
                err = validateBlock(city.getMapName(), hb);
            } while (err != BlockError.OK);
            city.addBlockManual(hb);
        }
    }
}
