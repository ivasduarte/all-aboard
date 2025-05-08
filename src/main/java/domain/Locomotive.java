package domain;

import java.util.List;

public class Locomotive {
    /**
     * The locomotive serial number.
     */
    private int serialNumber;

    /**
     * The locomotive type.
     */
    private LocomotiveType locomotiveType;

    /**
     * The locomotive speed.
     */
    private int speed;

    /**
     * The locomotive start year of production.
     */
    private int startYear;

    /**
     * The acquisition price of the locomotive.
     */
    private double price;

    /**
     * The fuel cost the locomotive needs, per year.
     */
    private double fuelCost;

    /**
     * The locomotive maintenance cost, per year.
     */
    private double maintenanceCost;

    /**
     * List of stations that create the locomotive's route
     */
    private List<Station> route;

    /**
     * This method is the constructor of the locomotive.
     *
     * @param serialNumber The serial number of the locomotive.
     * @param locomotiveType The locomotive type of the locomotive.
     * @param speed The speed of the locomotive.
     * @param startYear The start year of production of the locomotive.
     * @param price The acquisition price of the locomotive.
     * @param fuelCost The cost spent on fuel, per year.
     * @param maintenanceCost The cost for the locomotive maintenance, per year.
     */
    public Locomotive(int serialNumber, LocomotiveType locomotiveType, int speed, int startYear, double price, double fuelCost, double maintenanceCost) {
        this.serialNumber = serialNumber;
        this.locomotiveType = locomotiveType;
        this.speed = speed;
        this.startYear = startYear;
        this.price = price;
        this.fuelCost = fuelCost;
        this.maintenanceCost = maintenanceCost;
    }

    /**
     * This method returns the locomotive serial number.
     *
     * @return The serial number
     */
    public int getSerialNumber() {
        return serialNumber;
    }

    /**
     * This method returns the locomotive type.
     *
     * @return The locomotive type
     */
    public LocomotiveType getLocomotiveType() {
        return locomotiveType;
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

    /**
     * This method sets the locomotive's route
     *
     * @param route The new route to be assigned
     */
    public void setRoute(List<Station> route) { this.route = route;}

    // possible validations

    public enum LocomotiveType {
        Steam, Diesel, Electricity
    }
}
