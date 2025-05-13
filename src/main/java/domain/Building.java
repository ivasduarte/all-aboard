package domain;

public class Building {

    private final BuildingType type;

    public Building(BuildingType type) {
        if (type == null) throw new IllegalArgumentException("Invalid building type");
        this.type = type;
    }

    public String getBuildingType() {
        return type.getName();
    }

    public int getCost() {
        return type.getCost();
    }

    public String getEffect() {
        return type.getEffect();
    }


}
