package domain;

import java.util.List;

public class StationUpgradeService {

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

        handleReplacement(existingBuildings, newBuildingType);

        return station.addBuilding(building);
    }

    private boolean validateYear(String type, int year) {
        return !type.equalsIgnoreCase("Telephone") || year >= 1920;
    }

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

    private void handleReplacement(List<Building> existingBuildings, String newType) {
        if (newType.equalsIgnoreCase("Telephone")) {
            existingBuildings.removeIf(b -> b.getBuildingType().equalsIgnoreCase("Telegraph"));
        }
    }
}
