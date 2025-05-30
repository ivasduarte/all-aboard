package domain;

import java.io.Serializable;
import java.util.List;

/**
 * Service class responsible for handling the logic of upgrading a station with buildings.
 * Validates the availability of buildings based on the current year, enforces mutual exclusivity rules,
 * and delegates the actual addition of buildings to the station.
 */
public class StationUpgradeService implements Serializable {

    /**
     * Attempts to upgrade a station by adding the specified building, after verifying:
     * - If the building is available in the current year
     * - If it violates any mutual exclusivity constraints (e.g., hotel types)
     *
     * @param station      The station to upgrade.
     * @param building     The building to add.
     * @param currentYear  The current simulation year used for availability checks.
     * @return {@code true} if the upgrade was successful, {@code false} otherwise.
     * @throws IllegalArgumentException if the station or building is {@code null}.
     */
    public boolean upgradeStation(Station station, Building building, int currentYear) {
        if (station == null || building == null) {
            throw new IllegalArgumentException("Invalid station or building");
        }

        String newBuildingType = building.getBuildingType();

        if (!validateYear(newBuildingType, currentYear)) {
            System.out.println("ERROR: The building type '" + newBuildingType + "' is not available in the current year.");
            return false;
        }

        List<Building> existingBuildings = station.getBuildings();

        String conflict = checkMutualExclusivity(existingBuildings, newBuildingType);
        if (conflict != null) {
            System.out.println("ERROR: Cannot add building '" + newBuildingType +
                    "' because it conflicts with existing building '" + conflict + "'.");
            return false;
        }

        return station.addBuilding(building);
    }

    /**
     * Validates whether a building type can be constructed in the given year.
     * Specifically, the Telephone is only allowed from 1920 onward.
     *
     * @param type  The name of the building type.
     * @param year  The current simulation year.
     * @return {@code true} if the building type is allowed in this year; {@code false} otherwise.
     */
    private boolean validateYear(String type, int year) {
        return !type.equalsIgnoreCase("Telephone") || year >= 1920;
    }

    /**
     * Checks whether the new building type violates mutual exclusivity rules with existing buildings.
     * Currently, only one hotel-type building (small or large) is allowed per station.
     *
     * @param existingBuildings  The list of buildings already present in the station.
     * @param newType            The new building type being added.
     * @return The name of the conflicting building type if a conflict exists, {@code null} otherwise.
     */
    private String checkMutualExclusivity(List<Building> existingBuildings, String newType) {
        String newLower = newType.toLowerCase();

        for (Building building : existingBuildings) {
            String existing = building.getBuildingType().toLowerCase();
            if (!existing.equals(newLower) &&
                    existing.contains("hotel") &&
                    newLower.contains("hotel")) {
                return existing;
            }
        }
        return null;
    }
}
