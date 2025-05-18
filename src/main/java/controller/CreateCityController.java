package controller;

import domain.City;
import domain.HouseBlock;
import domain.Map;
import repositories.CityRepository;
import repositories.MapRepository;
import repositories.Repositories;

import java.util.Optional;

/**
 * This class is responsible for the process of creating cities and assigning blocks to them. It validates mechanisms
 * for city creation and block placement to ensure proper consistency with the associated map and repository.
 *
 * This class interacts with the {@link MapRepository} and {@link CityRepository} to perform operations such as:
 * name validation; map existence checks; and city/block assignment.
 */
public class CreateCityController {
    private final MapRepository mapRepo;
    private final CityRepository cityRepo;

    public enum NameMapError { OK, MAP_NOT_FOUND, NAME_DUPLICATE }
    public enum CenterError { OK, OUT_OF_BOUNDS, POSITION_TAKEN, BLOCK_PRESENT }
    public enum BlockError { OK, OUT_OF_BOUNDS, COLLIDES_CITY_CENTER, COLLIDES_BLOCK }

    /**
     * CreateCityController's contructor.
     * It initializes the controller with required repository instances for managing city and map data.
     */
    public CreateCityController() {
        var repos = Repositories.getInstance();
        this.mapRepo = repos.getMapRepository();
        this.cityRepo = repos.getCityRepository();
    }

    /**
     * Validates the uniqueness of a city name within the context of a specified map by checking if the city name
     * is unique in the context of a specified map.
     *
     * @param name      The name of the city to validate. Cannot be null or empty.
     * @param map       The name of the map in which the validation is to be performed. Cannot be null or empty.
     * @return          A {@code NameMapError} enumeration value representing the result of the validation:
     *                  - {@code NameMapError.MAP_NOT_FOUND}: If the specified map does not exist in the repository.
     *                  - {@code NameMapError.NAME_DUPLICATE}: If the city name is not unique within the specified map.
     *                  - {@code NameMapError.OK}: If the validation is successful.
     */
    public NameMapError validateNameMap(String name, String map) {
        Optional<Map> m = mapRepo.getMap(map);
        if (m.isEmpty()) return NameMapError.MAP_NOT_FOUND;
        if (!cityRepo.isNameUnique(name, map)) return NameMapError.NAME_DUPLICATE;
        return NameMapError.OK;
    }

    /**
     * Validates the center position for creating a city on a specified map and checks constraints such as boundaries,
     * position availability, and block uniqueness.
     *
     * @param name      The name of the city to be validated. Cannot be null or empty.
     * @param map       The name of the map where the validation is performed. Cannot be null or empty.
     * @param x         The x-coordinate of the proposed city center on the map. Must be within map boundaries.
     * @param y         The y-coordinate of the proposed city center on the map. Must be within map boundaries.
     * @return          A {@code CenterError} enumeration value indicating the result of the validation:
     *                  - {@code CenterError.OUT_OF_BOUNDS}: If the coordinates are outside the map boundaries.
     *                  - {@code CenterError.POSITION_TAKEN}: If the position is already occupied by a city.
     *                  - {@code CenterError.BLOCK_PRESENT}: If a block already exists at the specified position.
     *                  - {@code CenterError.OK}: If the center position is valid.
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
     * Validates the placement of a house block within a specified map according to constraints:
     * map boundaries; block position uniqueness; collision with existing city centers.
     *
     * @param map       The name of the map where the block's placement is to be validated. Cannot be null or empty.
     * @param b         The {@code HouseBlock} to validate, representing its position within the map.
     *                  Must not be null and should have valid coordinates.
     * @return          A {@code BlockError} enumeration value indicating the result of the validation:
     *                  - {@code BlockError.OUT_OF_BOUNDS}: If the block's coordinates are outside the map boundaries.
     *                  - {@code BlockError.COLLIDES_CITY_CENTER}: If the block collides with an existing city center.
     *                  - {@code BlockError.COLLIDES_BLOCK}: If the block's position is not unique within the map.
     *                  - {@code BlockError.OK}: If the block placement is valid.
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
     * Creates a new city with the specified name, map, and center coordinates by validating the city name and map,
     * as well as the center position.
     *
     * @param name      The name of the city to be created. Must be unique within the given map.
     * @param map       The name of the map where the city will be created. Must exist in the repository.
     * @param x         The x-coordinate of the city's center on the map. Must be within map boundaries.
     * @param y         The y-coordinate of the city's center on the map. Must be within map boundaries.
     * @return          The {@code City} object representing the newly created city.
     * @throws          IllegalStateException if the city name, map, or center coordinates fail validation.
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
     * Adds a house block manually to the specified city if the block passes validation.
     *
     * @param city      The {@code City} to which the house block should be added. Cannot be null.
     * @param b         The {@code HouseBlock} to be added to the city. Must not be null and should have valid coordinates.
     * @return          {@code true} if the block is successfully added after validation; {@code false} otherwise.
     */
    public boolean addBlockManual(City city, HouseBlock b) {
        BlockError err = validateBlock(city.getMapName(), b);
        if (err != BlockError.OK) return false;
        city.addBlockManual(b);
        return true;
    }

    /**
     * Automatically assigns a specified number of house blocks to a city. The method uses Gaussian distribution around
     * the city's center to determine the positions of house blocks and ensures that every block passes validation
     * before being added.
     *
     * @param city      The {@code City} object to which the house blocks will be added. Must not be null.
     * @param count     The number of house blocks to be assigned. Must be a non-negative integer.
     * @param sigma     The standard deviation used for Gaussian distribution to calculate house block positions. Must be positive.
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

    /**
     * Retrieves the CityRepository instance that's used for managing and storing a collection of cities.
     * This repository provides functionality for handling city-related operations such as
     * adding cities, validating uniqueness, and performing position checks.
     *
     * @return          The {@code CityRepository} instance associated with the controller
     */
    public CityRepository getCityRepo(){
        var repos = Repositories.getInstance();
        return repos.getCityRepository();
    }
}
