package domain;

public class Building {

    private String buildingType;

    private final String[] buildingTypes = {"Telegraph", "Cafe", "PostOffice", "SmallHotel","LargeHotel", "Silo", "LiquidStorage", "Telephone"};


    private boolean isValidType(String build){
        for(int i = 0; i < buildingTypes.length; i++) {
            if (buildingTypes[i].equalsIgnoreCase(build)) {
                return true;
            }
        }
        return false;
    }

    public Building(String buildingType) {
        if(!isValidType(buildingType)) {
            throw new IllegalArgumentException("Invalid building type: " + buildingType);
        }
        this.buildingType = buildingType;

    }

    public String getBuildingType(){

        return buildingType;

    }

}
