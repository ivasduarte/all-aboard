package repositories;

import domain.Building;
import domain.BuildingType;

import java.util.ArrayList;
import java.util.List;

public class BuildingRepository {

    private final List<BuildingType> buildingTypes = new ArrayList<>();

    public BuildingRepository() {
        buildingTypes.add(new BuildingType("Telegraph", 5000, "Improves communication"));
        buildingTypes.add(new BuildingType("Telephone", 8000, "Replaces Telegraph"));
        buildingTypes.add(new BuildingType("Cafe", 3000, "Improves satisfaction"));
        buildingTypes.add(new BuildingType("PostOffice", 6000, "Handles mail"));
        buildingTypes.add(new BuildingType("SmallHotel", 10000, "Short-term cargo"));
        buildingTypes.add(new BuildingType("LargeHotel", 15000, "Long-term cargo"));
        buildingTypes.add(new BuildingType("Silo", 4000, "Stores grain"));
        buildingTypes.add(new BuildingType("LiquidStorage", 7000, "Stores liquids"));
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
