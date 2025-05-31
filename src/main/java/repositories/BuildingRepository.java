package repositories;

import domain.BuildingType;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Repository responsible for managing all available building types in the simulation.
 * Provides methods to retrieve the full list of building types or search for a specific one by name.
 *
 * The building types are initialized with predefined names, costs, effects, and availability years.
 */
public class BuildingRepository implements Serializable {

    private final List<BuildingType> buildingTypes = new ArrayList<>();

    /**
     * Constructs a {@code BuildingRepository} and preloads it with a predefined list
     * of building types available in the simulation.
     * Each building type includes a name, cost, effect, and the year it becomes available.
     */
    public BuildingRepository() {
        buildingTypes.add(new BuildingType("Telegraph", 50, "Improves communication", 1850));
        buildingTypes.add(new BuildingType("Telephone", 80, "Replaces Telegraph", 1920));
        buildingTypes.add(new BuildingType("Small Cafe", 30, "Slightly improves satisfaction", 1880));
        buildingTypes.add(new BuildingType("Large Cafe", 50, "Greatly improves satisfaction", 1880));
        buildingTypes.add(new BuildingType("Post Office", 60, "Handles mail", 1870));
        buildingTypes.add(new BuildingType("Small Hotel", 100, "Short-term cargo", 1860));
        buildingTypes.add(new BuildingType("Large Hotel", 150, "Long-term cargo", 1890));
        buildingTypes.add(new BuildingType("Silo", 40, "Stores grain", 1855));
        buildingTypes.add(new BuildingType("Liquid Storage", 70, "Stores liquids", 1900));
        buildingTypes.add(new BuildingType("Customs", 90, "Facilitates international trade", 1865));
    }

    /**
     * Retrieves the complete list of building types available in the repository.
     *
     * @return A list of {@code BuildingType} objects representing all predefined building types.
     */
    public List<BuildingType> getBuildingTypes() {
        return buildingTypes;
    }

    /**
     * Searches for a building type by its name (case-insensitive).
     *
     * @param name The name of the building type to search for.
     * @return The matching {@code BuildingType}, or {@code null} if no match is found.
     */
    public BuildingType getBuildingType(String name) {
        for (BuildingType bt : buildingTypes) {
            if (bt.getName().equalsIgnoreCase(name)) return bt;
        }
        return null;
    }

    public void copyFrom(BuildingRepository bd2){
        this.buildingTypes.clear();
        this.buildingTypes.addAll(bd2.getBuildingTypes());
    }
}
