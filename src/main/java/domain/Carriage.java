package domain;

public class Carriage {
    private String id;
    private CargoType cargoType;

    public Carriage(String id, CargoType cargoType) {
        this.id = id;
        this.cargoType = cargoType;
    }

    // getters
    public String getId() {
        return id;
    }
    public CargoType getCargoType() {
        return cargoType;
    }

    // setters
    public void setId(String id) {
        this.id = id;
    }
    public void setCargoType(CargoType cargoType) {
        this.cargoType = cargoType;
    }

    // possible validations

    public enum CargoType {
        Passengers, Mail, Coal, IronOre, Steel, Cars;
    }
}
