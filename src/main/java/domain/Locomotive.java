package domain;

import java.io.Serializable;
import java.util.List;

public class Locomotive implements Serializable {

    private String locomotiveModel;
    private String fuelType;
    private int maxSpeed;
    private int startYear;
    private int endYear;
    private int price;
    private int fuelCost;
    private int maintenanceCost;

    public Locomotive(String locomotiveModel, String fuelType, int startYear, int endYear, int price, int maintenanceCost, int fuelCost, int maxSpeed) {
        this.locomotiveModel = locomotiveModel;
        this.fuelType = fuelType;
        this.startYear = startYear;
        this.endYear = endYear;
        this.price = price;
        this.maintenanceCost = maintenanceCost;
        this.fuelCost = fuelCost;
        this.maxSpeed = maxSpeed;
    }

    public String getLocomotiveModel() {
        return locomotiveModel;
    }

    public String getLocomotiveFuelType() {
        return fuelType;
    }

    public int getPrice() {
        return price;
    }

    public int getStartYear() {
        return startYear;
    }

    public int getEndYear() {
        return endYear;
    }

    public int getFuelCost() {
        return fuelCost;
    }

    public int getMaintenanceCost() {
        return maintenanceCost;
    }

    public int getMaxSpeed() {
        return maxSpeed;
    }

    @Override
    public String toString() {
        return locomotiveModel;
    }
}