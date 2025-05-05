package domain;

public class Locomotive {
    private int serialNumber;
    private LocomotiveType locomotiveType;
    private int speed;
    private int startYear;
    private double price;
    private double fuelCost;
    private double maintenanceCost;

    public int getSerialNumber() {
        return serialNumber;
    }
    public LocomotiveType getLocomotiveType() {
        return locomotiveType;
    }
    public double getPrice() {
        return price;
    }

    // possible validations

    public enum LocomotiveType {
        Steam, Diesel, Electricity
    }
}
