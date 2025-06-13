package domain;

import java.io.Serializable;
import java.util.List;

/**
 * Represents a locomotive in the railway system.
 * This class stores information about a locomotive's model, fuel type, operational years,
 * costs, and performance characteristics.
 */
public class Locomotive implements Serializable {

    private String locomotiveModel;
    private String fuelType;
    private int maxSpeed;
    private int startYear;
    private int endYear;
    private int price;
    private int fuelCost;
    private int maintenanceCost;

    /**
     * Creates a new Locomotive instance with the specified characteristics.
     *
     * @param locomotiveModel The model name of the locomotive
     * @param fuelType The type of fuel used by the locomotive
     * @param startYear The year when this locomotive model became available
     * @param endYear The year when this locomotive model was discontinued (use 99999 for still in production)
     * @param price The purchase price of the locomotive
     * @param maintenanceCost The annual maintenance cost
     * @param fuelCost The cost of fuel per unit
     * @param maxSpeed The maximum speed the locomotive can achieve
     */
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

    /**
     * Gets the model name of the locomotive.
     *
     * @return The locomotive model name
     */
    public String getLocomotiveModel() {
        return locomotiveModel;
    }

    /**
     * Gets the type of fuel used by the locomotive.
     *
     * @return The fuel type
     */
    public String getLocomotiveFuelType() {
        return fuelType;
    }

    /**
     * Gets the purchase price of the locomotive.
     *
     * @return The price in currency units
     */
    public int getPrice() {
        return price;
    }

    /**
     * Gets the year when this locomotive model became available.
     *
     * @return The start year
     */
    public int getStartYear() {
        return startYear;
    }

    /**
     * Gets the year when this locomotive model was discontinued.
     *
     * @return The end year (99999 if still in production)
     */
    public int getEndYear() {
        return endYear;
    }

    /**
     * Gets the cost of fuel per unit for this locomotive.
     *
     * @return The fuel cost
     */
    public int getFuelCost() {
        return fuelCost;
    }

    /**
     * Gets the annual maintenance cost for this locomotive.
     *
     * @return The maintenance cost
     */
    public int getMaintenanceCost() {
        return maintenanceCost;
    }

    /**
     * Gets the maximum speed this locomotive can achieve.
     *
     * @return The maximum speed
     */
    public int getMaxSpeed() {
        return maxSpeed;
    }

    /**
     * Returns a string representation of the locomotive.
     *
     * @return The locomotive model name
     */
    @Override
    public String toString() {
        return locomotiveModel;
    }
}