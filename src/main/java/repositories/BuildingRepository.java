package repositories;

import domain.Building;
import domain.BuildingType;

import java.util.ArrayList;
import java.util.List;

public class BuildingRepository {

    private final List<BuildingType> buildingTypes = new ArrayList<>();

    public BuildingRepository() {
        buildingTypes.add(new BuildingType("Telegraph", 5000, "Improves communication", 1850));
        buildingTypes.add(new BuildingType("Telephone", 8000, "Replaces Telegraph", 1920));
        buildingTypes.add(new BuildingType("Cafe", 3000, "Improves satisfaction", 1880));
        buildingTypes.add(new BuildingType("PostOffice", 6000, "Handles mail", 1870));
        buildingTypes.add(new BuildingType("SmallHotel", 10000, "Short-term cargo", 1860));
        buildingTypes.add(new BuildingType("LargeHotel", 15000, "Long-term cargo", 1890));
        buildingTypes.add(new BuildingType("Silo", 4000, "Stores grain", 1855));
        buildingTypes.add(new BuildingType("LiquidStorage", 7000, "Stores liquids", 1900));
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
