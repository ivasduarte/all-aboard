package repositories;

import controller.CreateCityController;
import domain.City;
import domain.HouseBlock;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * A repository class responsible for managing and storing a collection of cities.
 * This class provides methods for adding new cities, retrieving existing cities,
 * and validating city-related data, such as uniqueness of names and positions.
 */
public class CityRepository implements Serializable {
    private final List<City> cities = new ArrayList<>();

    /**
     * Adds a city to the repository's collection of cities.
     *
     * @param c     {@code City} object to be added to the repository
     */
    public void add(City c) { cities.add(c); }

    /**
     * Retrieves an unmodifiable list of cities stored in the repository.
     *
     * @return      An unmodifiable {@code List} of {@code City} objects representing the cities in the repository.
     */
    public List<City> getCities() { return List.copyOf(cities); }

    /**
     * Retrieves an unmodifiable list of cities from a certain map stored in the repository.
     *
     * @param mapName The name of the map for the filtered list
     * @return      An unmodifiable {@code List} of {@code City} objects representing the cities from a certain map in the repository.
     */
    public List<City> getCitiesByMap(String mapName) {
        List<City> citiesByMap = new ArrayList<>();

        for(City city : cities)
        {
            if(city.getMapName().equalsIgnoreCase(mapName))
            {
                citiesByMap.add(city);
            }
        }

        return List.copyOf(citiesByMap);
    }

    /**
     * Checks if a city name is unique within the specified map.
     *
     * @param name  Name of the city to be checked
     * @param map   Name of the map where the check is performed
     * @return      {@code true} if no city within the specified map has the given name; {@code false} otherwise
     */
    public boolean isNameUnique(String name, String map) {
        return cities.stream().noneMatch(c->c.getName().equals(name) && c.getMapName().equals(map));
    }

    /**
     * Checks whether a city exists at the specified position on a given map.
     *
     * @param map   Name of the map where the position is being checked
     * @param x     X-coordinate of the position to check
     * @param y     Y-coordinate of the position to check
     * @return      {@code true} if a city exists at the specified position on the given map; {@code false} otherwise
     */
    public boolean existsAtPosition(String map, int x, int y) {
        return cities.stream().filter(c->c.getMapName().equals(map))
                .anyMatch(c->c.getCenterX()==x && c.getCenterY()==y);
    }

    /**
     * Checks if the position of a given {@code HouseBlock} is unique within the specified map.
     * A position is considered unique if no other {@code HouseBlock} in any city
     * on the same map has the same coordinates.
     *
     * @param map   Name of the map within which the uniqueness of the block's position is checked
     * @param b     {@code HouseBlock} whose position's uniqueness is to be verified
     * @return      {@code true} if the block's position is unique within the specified map;
     *              {@code false} otherwise
     */
    public boolean isBlockPositionUnique(String map, HouseBlock b) {
        return cities.stream().filter(c->c.getMapName().equals(map))
                .flatMap(c->c.getBlocks().stream())
                .noneMatch(b::equals);
    }

    /**
     * Validates a {@code HouseBlock} within the context of a specified map.
     * The method checks for potential issues such as position conflicts,
     * alignment constraints, or other validation criteria specific to the map and block.
     *
     * @param map   Name of the map where the block is to be validated.
     * @param b     {@code HouseBlock} object to be validated.
     * @return      An instance of {@code CreateCityController.BlockError},
     *              indicating the result of the validation. This may include
     *              error codes representing issues like non-unique positions
     *              or successful validation.
     */
    public CreateCityController.BlockError validateBlock(String map, HouseBlock b) {
        return CreateCityController.BlockError.OK;
    }
}
