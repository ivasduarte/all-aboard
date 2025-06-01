package controller;

import domain.Map;
import repositories.Repositories;

/**
 * The CreateMapController class provides functionality to create a new map with specified dimensions
 * and add it to a repository. This ensures that each map is unique by name and validates the dimensions
 * before adding the map.
 */
public class CreateMapController {
    /**
     * Creates a new map with the specified name, width, height, and adds it to the map repository.
     * Ensures that the map name is unique and throws an exception if a map with the same name already exists.
     *
     * @param name      The name of the map to be created. Must be non-null and non-empty.
     * @param width     The width of the map. Must be a positive integer.
     * @param height    The height of the map. Must be a positive integer.
     * @return          A newly created {@code Map} instance with the specified name and dimensions.
     * @throws          IllegalArgumentException if a map with the same name already exists or if the dimensions are invalid.
     */
    public Map createMap(String name, int width, int height, int size){
        if(Repositories.getInstance().getMapRepository().existsByName(name)){
            throw new IllegalArgumentException("Map with this name already exists.");
        }
        Map map = new Map(name, width, height, size);
        Repositories.getInstance().getMapRepository().add(map);
        return map;
    }
}