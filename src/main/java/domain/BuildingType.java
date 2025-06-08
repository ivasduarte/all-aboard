package domain;

import java.io.Serializable;
/**
 * Represents a building that can be constructed at a station.
 * Each building type includes a name, cost, in-game effect, and the year it becomes available.
 * Building types are used to determine upgrade options for stations based on the scenario year.
 */
public class BuildingType implements Serializable {
    private final String name;
    private final int cost;
    private final String effect;
    private final int AvailableYear;

    /**
     * Constructs a new BuildingType with the specified properties.
     *
     * @param name           The name of the building (e.g., "Telegraph", "Silo").
     * @param cost           The cost to build this building in euros.
     * @param effect         A short description of the gameplay effect of this building.
     * @param AvailableYear  The year when this building type becomes available in the simulation.
     */
    public BuildingType(String name, int cost, String effect, int AvailableYear) {
        this.name = name;
        this.cost = cost;
        this.effect = effect;
        this.AvailableYear = AvailableYear;
    }

    /**
     * Gets the name of the building type.
     *
     * @return The name of the building.
     */
    public String getName() {
        return name;
    }

    /**
     * Gets the cost of the building type.
     *
     * @return The cost to build this building.
     */
    public int getCost() {
        return cost;
    }

    /**
     * Gets the effect of the building type.
     *
     * @return A short description of the building's gameplay effect.
     */
    public String getEffect() {
        return effect;
    }

    /**
     * Gets the year when the building type becomes available in the simulation.
     *
     * @return The year of availability.
     */
    public int getAvailableYear() {
        return AvailableYear;
    }

    @Override
    public String toString() {
        return name + " (" + cost + "€)";
    }
}
