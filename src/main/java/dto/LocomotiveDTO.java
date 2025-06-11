package dto;

public class LocomotiveDTO {
    private String model;
    private String fuelType;
    private int speed;
    private int startYear;
    private int endYear;
    private int price;
    private int fuelCost;
    private int maintenanceCost;

    public LocomotiveDTO(String model, String fuelType, int speed, int startYear, int endYear, int price, int fuelCost, int maintenanceCost) {
        this.model = model;
        this.fuelType = fuelType;
        this.speed = speed;
        this.startYear = startYear;
        this.endYear = endYear;
        this.price = price;
        this.fuelCost = fuelCost;
        this.maintenanceCost = maintenanceCost;
    }

    public String getModel() {
        return model;
    }

    public String getFuelType() {
        return fuelType;
    }

    public int getSpeed() {
        return speed;
    }

    public int getStartYear() {
        return startYear;
    }

    public int getEndYear() {
        return endYear;
    }

    public int getPrice() {
        return price;
    }

    public int getFuelCost() {
        return fuelCost;
    }

    public int getMaintenanceCost() {
        return maintenanceCost;
    }

    @Override
    public String toString() {
        return model;
    }
}
