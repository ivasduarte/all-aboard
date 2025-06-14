package dto;

/**
 * A Data Transfer Object (DTO) representing a locomotive along with its properties such as model, fuel type, max speed,
 * start and end year of operations, price, fuel cost per 100km and maintenance cost per year.
 *
 * This class provides methods to access the locomotive's metadata.
 */
public class LocomotiveDTO {
    private String model;
    private String fuelType;
    private int speed;
    private int startYear;
    private int endYear;
    private int price;
    private int fuelCost;
    private int maintenanceCost;

    /**
     * Constructs a LocomotiveDTO object with a specified model, fuel type, max speed, the start and end year of
     * operations, price, the cost of fuel per 100km and the maintenance cost per year.
     *
     * @param model The locomotive model
     * @param fuelType The locomotive fuel type
     * @param speed The locomotive max speed
     * @param startYear The locomotive start year of operation
     * @param endYear The locomotive end year of operation
     * @param price The locomotive price
     * @param fuelCost The locomotive fuel cost per 100km
     * @param maintenanceCost The locomotive maintenance cost per year
     */
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

    /**
     * Retrieves the locomotive model.
     *
     * @return The locomotive model
     */
    public String getModel() {
        return model;
    }

    /**
     * Retrieves the locomotive fuel type.
     *
     * @return The locomotive fuel type
     */
    public String getFuelType() {
        return fuelType;
    }

    /**
     * Retrieves the locomotive max speed.
     *
     * @return The locomotive max speed
     */
    public int getSpeed() {
        return speed;
    }

    /**
     * Retrieves the locomotive start year of operation.
     *
     * @return The locomotive start year of operation
     */
    public int getStartYear() {
        return startYear;
    }

    /**
     * Retrieves the locomotive end year of operation.
     *
     * @return The locomotive end year of operation
     */
    public int getEndYear() {
        return endYear;
    }

    /**
     * Retrieves the locomotive price.
     *
     * @return The locomotive price
     */
    public int getPrice() {
        return price;
    }

    /**
     * Retrieves the locomotive fuel cost, per 100km.
     *
     * @return The locomotive fuel cost
     */
    public int getFuelCost() {
        return fuelCost;
    }

    /**
     * Retrieves the locomotive maintenance cost, per year.
     *
     * @return The locomotive maintenance cost
     */
    public int getMaintenanceCost() {
        return maintenanceCost;
    }

    /**
     * Provides a string representation of the LocomotiveDTO object, including its model.
     *
     * @return A string representation of the map with its model
     */
    @Override
    public String toString() {
        return model;
    }
}
