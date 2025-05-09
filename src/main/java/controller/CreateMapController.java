package controller;

import domain.Map;
import repositories.Repositories;


/**
 * This class is responsible for creating new map instances and adding them
 * to the repository. It ensures that maps with duplicate names are not created and
 * stored within the repository.
 */
public class CreateMapController {


    /**
     * Creates a new map with the specified name, width, and height, and adds it to the repository.
     * If a map with the same name already exists, an {@code IllegalArgumentException} is thrown.
     *
     * @param name      The name of the map to be created. Cannot be null or empty.
     * @param width     The width of the map. Must be a positive integer.
     * @param height    The height of the map. Must be a positive integer.
     * @return          A new {@code Map} instance with the specified name, width, and height.
     * @throws          IllegalArgumentException if a map with the specified name already exists,
     *                  or if the dimensions are invalid.
     */
    public Map createMap(String name, int width, int height){
        if(Repositories.getInstance().getMapRepository().existsByName(name)){
            throw new IllegalArgumentException("Map with this name already exists.");
        }
        Map map = new Map(name, width, height);
        Repositories.getInstance().getMapRepository().add(map);
        return map;
    }
}