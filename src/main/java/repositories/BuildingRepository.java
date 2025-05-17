package repositories;

import domain.Building;
import domain.BuildingType;

import java.util.ArrayList;
import java.util.List;

public class BuildingRepository {

    private final List<BuildingType> buildingTypes = new ArrayList<>();

    public BuildingRepository() {
        buildingTypes.add(new BuildingType("Telegraph", 50, "Improves communication", 1850));
        buildingTypes.add(new BuildingType("Telephone", 80, "Replaces Telegraph", 1920));
        buildingTypes.add(new BuildingType("Cafe", 30, "Improves satisfaction", 1880));
        buildingTypes.add(new BuildingType("Post Office", 60, "Handles mail", 1870));
        buildingTypes.add(new BuildingType("Small Hotel", 100, "Short-term cargo", 1860));
        buildingTypes.add(new BuildingType("Large Hotel", 150, "Long-term cargo", 1890));
        buildingTypes.add(new BuildingType("Silo", 40, "Stores grain", 1855));
        buildingTypes.add(new BuildingType("Liquid Storage", 70, "Stores liquids", 1900));
    }

    public List<BuildingType> getBuildingTypes() {

        return buildingTypes;

    }

    public BuildingType getBuildingType(String name) {
        for (BuildingType bt : buildingTypes) {
            if (bt.getName().equalsIgnoreCase(name)) return bt;
        }
        return null;
        }
}
