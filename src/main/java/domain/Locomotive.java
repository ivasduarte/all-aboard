package domain;

public class Locomotive {
    private int serialNumber;
    private LocomotiveType locomotiveType;
    private int speed;
    private int startYear;
    private double price;
    private double fuelCost;
    private double maintenanceCost;

    public Locomotive(int serialNumber, LocomotiveType locomotiveType, int speed, int startYear, double price, double fuelCost, double maintenanceCost) {
        this.serialNumber = serialNumber;
        this.locomotiveType = locomotiveType;
        this.speed = speed;
        this.startYear = startYear;
        this.price = price;
        this.fuelCost = fuelCost;
        this.maintenanceCost = maintenanceCost;
    }

    public int getSerialNumber() {
        return serialNumber;
    }
    public LocomotiveType getLocomotiveType() {
        return locomotiveType;
    }
    public double getPrice() {
        return price;
    }
    public int getStartYear() {
        return startYear;
    }

    // possible validations

    public enum LocomotiveType {
        Steam, Diesel, Electricity
    }
}
