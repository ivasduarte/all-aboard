package repositories;

import domain.Map;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * The MapRepository class is used to manage a collection of Map instances.
 * It provides methods to add, retrieve, and perform operations on the stored maps.
 */

public class MapRepository{
    private List<Map> maps;


    /**
     * Constructs a new instance of the {@code MapRepository} class.
     * This initializes the internal collection used to store maps.
     */
    public MapRepository() {
        maps = new ArrayList<>();
    }


    /**
     * Adds a new map to the repository if it passes validation.
     *
     * @param map The Map instance to be added to the repository. It must not already exist in the collection.
     */
    public void add(Map map){
        if(validateMap(map)){
            maps.add(map);
        }
    }


    /**
     * Validates if the given map instance is not already present in the repository's collection.
     *
     * @param map   The Map instance to validate. It must not be null.
     * @return      True if the map is not already contained in the repository's collection; false otherwise.
     */
    private boolean validateMap(Map map){
        return !maps.contains(map);
    }


    /**
     * Checks if a map with the specified name exists in the repository.
     *
     * @param name The name of the map to check for existence. Must not be null.
     * @return {@code true} if a map with the given name exists in the repository;
     *         {@code false} otherwise.
     */
    public boolean existsByName(String name) {
        for (int i = 0; i < maps.size(); i++) {
            if (maps.get(i).getMapName().equalsIgnoreCase(name)) {
                return true;
            }
        }
        return false;
    }


    /**
     * Retrieves an unmodifiable list of maps stored in the repository.
     * The returned list cannot be modified and reflects the current state
     * of the internal collection of maps.
     *
     * @return An unmodifiable list containing all maps currently stored in the repository.
     */
    public List<Map> getMapList() {
        return List.copyOf(maps);
    }


    /**
     * Retrieves a map from the repository by its name.
     *
     * This method searches through the collection of maps in the repository and
     * attempts to locate a map instance that matches the specified name. If a
     * matching map is found, it is returned wrapped in an {@code Optional}. If no
     * map is found with the given name, an empty {@code Optional} is returned.
     *
     * @param name  The name of the map to retrieve. Must not be null.
     * @return      An {@code Optional} containing the matching {@code Map} if found,
     *              or an empty {@code Optional} if no match is found.
     */
    public Optional<Map> getMap(String name) {
        return maps.stream().filter(m -> m.getMapName().equals(name)).findFirst();
    }

}