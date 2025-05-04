package domain;

public class Locomotive {
    private String serialNumber;
    LocomotiveType locomotiveType;
    private int power;
    private int acceleration;
    private int topSpeed;
    private int startYear;
    private String acquisitionType;

    public Locomotive(String serialNumber, LocomotiveType locomotiveType, int power, int acceleration, int topSpeed, int startYear, String acquisitionType) {
        this.serialNumber = serialNumber;
        this.locomotiveType = locomotiveType;
        this.power = power;
        this.acceleration = acceleration;
        this.topSpeed = topSpeed;
        this.startYear = startYear;
        this. acquisitionType = acquisitionType;
    }

    // getters
    public String getSerialNumber() {
        return serialNumber;
    }
    public LocomotiveType getLocomotiveType() {
        return locomotiveType;
    }
    public int getPower() {
        return power;
    }
    public int getAcceleration() {
        return acceleration;
    }
    public int getTopSpeed() {
        return topSpeed;
    }
    public int getStartYear() {
        return startYear;
    }
    public String getAcquisitionType() {
        return acquisitionType;
    }

    // setters
    public void setSerialNumber(String serialNumber) {
        this.serialNumber = serialNumber;
    }
    public void setLocomotiveType(LocomotiveType locomotiveType) {
        this.locomotiveType = locomotiveType;
    }
    public void setPower(int power) {
        this.power = power;
    }
    public void setAcceleration(int acceleration) {
        this.acceleration = acceleration;
    }
    public void setTopSpeed(int topSpeed) {
        this.topSpeed = topSpeed;
    }
    public void setStartYear(int startYear) {
        this.startYear = startYear;
    }
    public void setAcquisitionType(String acquisitionType) {
        this.acquisitionType = acquisitionType;
    }

    // possible validations

    private enum LocomotiveType {
        Steam, Diesel, Electricity;
    }
}
