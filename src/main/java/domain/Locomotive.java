package domain;

public class Locomotive {
    private LocomotiveType locomotiveType;
    private Date purchaseDate;
    private double price;

    public Locomotive(LocomotiveType locomotiveType, Date purchaseDate, double price) {
        this.locomotiveType = locomotiveType;
        this.purchaseDate = purchaseDate;
        this.price = price;
    }

    // getters
    public LocomotiveType getLocomotiveType() {
        return locomotiveType;
    }
    public Date getPurchaseDate() {
        return purchaseDate;
    }
    public double getPrice() {
        return price;
    }

    // setters
    public void setLocomotiveType(LocomotiveType locomotiveType) {
        this.locomotiveType = locomotiveType;
    }
    public void setPurchaseDate(Date purchaseDate) {
        this.purchaseDate = purchaseDate;
    }
    public void setPrice(double price) {
        this.price = price;
    }

    // possible validations

    public enum LocomotiveType {
        Steam, Diesel, Electricity;
    }
}
