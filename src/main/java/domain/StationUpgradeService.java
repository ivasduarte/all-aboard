package domain;

import java.util.List;

public class StationUpgradeService {

    public boolean upgradeStation(Station station, Building building, int currentYear) {
        if (station == null || building == null) {
            throw new IllegalArgumentException("Invalid station or building");
        }

        String buildingType = building.getBuildingType();

        if (!validateData(buildingType, currentYear)) {

            System.out.println("The building type: " + buildingType + " is not available in the current year.");

            return false;

        }

        List<Building> existingBuildings = station.getBuildings();

        if (checkMutualExclusivity(existingBuildings, buildingType) != null) {

            System.out.println("ERROR: Cannot add building " + buildingType + " because it conflicts with existing building " + checkMutualExclusivity(existingBuildings, buildingType) + ".");

            return false;

        }


        handleReplacement(existingBuildings, buildingType);

        return station.addBuilding(building, currentYear);


    }

    private boolean validateData(String type, int year) {
        if (type.equalsIgnoreCase("Telephone")) {

            return year >= 1920;

        }

        return true;

    }

    private String checkMutualExclusivity(List<Building> existingBuildings, String newType) {

        String newLower = newType.toLowerCase();

        for (Building building : existingBuildings) {

            String existingBuilding = building.getBuildingType().toLowerCase();

            if (!existingBuilding.equals(newLower) && (existingBuilding.contains("hotel") && newLower.contains("hotel"))) {

                return existingBuilding;

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

