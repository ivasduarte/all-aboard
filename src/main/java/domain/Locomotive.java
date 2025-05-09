package domain;

import java.util.List;

public class Locomotive {
    /**
     * The locomotive model.
     */
    private String locomotiveModel;

    /**
     * The type of fuel the locomotive consumes.
     */
    private String fuelType;

    /**
     * The locomotive speed.
     */
    private int speed;

    /**
     * The locomotive start year of production.
     */
    private int startYear;

    /**
     * The year the locomotive has stopped to be producted.
     */
    private int endYear;

    /**
     * The acquisition price of the locomotive.
     */
    private int price;

    /**
     * The fuel cost the locomotive needs, per year.
     */
    private int fuelCost;

    /**
     * The locomotive maintenance cost, per year.
     */
    private int maintenanceCost;

    /**
     * List of stations that create the locomotive's route
     */
    private List<Station> route;

    /**
     * This is the constructor of Locomotive.
     *
     * @param locomotiveModel The locomotive model
     * @param fuelType The type of fuel the locomotive consumes
     * @param startYear The start year of production of the locomotive
     * @param endYear The end year of production of the locomotive
     * @param price The acquisition price of the locomotive
     * @param maintenanceCost The maintenance cost of the locomotive, per year
     * @param fuelCost The fuel cost of the locomotive, per year
     * @param speed The speed of the locomotive
     */
    public Locomotive(String locomotiveModel, String fuelType, int startYear, int endYear, int price, int maintenanceCost, int fuelCost, int speed) {
        this.locomotiveModel = locomotiveModel;
        this.fuelType = fuelType;
        this.startYear = startYear;
        this.endYear = endYear;
        this.price = price;
        this.maintenanceCost = maintenanceCost;
        this.fuelCost = fuelCost;
        this.speed = speed;
    }

    /**
     * This method returns the locomotive model.
     *
     * @return The locomotive model
     */
    public String getLocomotiveModel() {
        return locomotiveModel;
    }

    /**
     * This method returns the locomotive fuel type.
     *
     * @return The locomotive fuel type
     */
    public String getLocomotiveFuelType() {
        return fuelType;
    }

    /**
     * This method returns the price of the locomotive.
     *
     * @return The locomotive price
     */
    public double getPrice() {
        return price;
    }

    /**
     * This method returns the start year of production of the locomotive.
     *
     * @return The start year of production
     */
    public int getStartYear() {
        return startYear;
    }

    public int getEndYear() {
        return endYear;
    }

    /**
     * This method sets the locomotive's route
     *
     * @param route The new route to be assigned
     */
    public void setRoute(List<Station> route) { this.route = route;}

    // possible validations
}
