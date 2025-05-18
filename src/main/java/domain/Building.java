package domain;

/**
 * Represents a specific building instance that can be constructed at a station.
 * A {@code Building} is based on a {@code BuildingType}, which defines its
 * name, cost, effect, and availability year.
 *
 * This class acts as a wrapper around {@code BuildingType} and is used to
 * associate buildings with stations in the simulation.
 */
public class Building {

    private final BuildingType type;

    /**
     * Constructs a new building instance using the specified building type.
     *
     * @param type The type of building to construct. Must not be null.
     * @throws IllegalArgumentException if the provided {@code type} is null.
     */
    public Building(BuildingType type) {
        if (type == null) throw new IllegalArgumentException("Invalid building type");
        this.type = type;
    }

    /**
     * Gets the name of the building's type.
     *
     * @return The name of the building (e.g., "Post Office", "Silo").
     */
    public String getBuildingType() {
        return type.getName();
    }

    /**
     * Gets the cost of the building.
     *
     * @return The cost in euros to build this building.
     */
    public int getCost() {
        return type.getCost();
    }

    /**
     * Gets the gameplay effect of the building.
     *
     * @return A description of the building's effect (e.g., "Handles mail").
     */
    public String getEffect() {
        return type.getEffect();
    }
}
