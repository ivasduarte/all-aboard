package domain;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class PointOfRoute implements Serializable {
    private final Station station;
    private final List<Cargo> cargoesToPickup;
    private CargoMode cargoMode;

    public PointOfRoute(Station station) {
        this.station = station;
        this.cargoesToPickup = new ArrayList<>();
        this.cargoMode = CargoMode.AVAILABLE; // Default mode
    }

    public Station getStation() {
        return station;
    }

    public List<Cargo> getCargoesToPickup() {
        return cargoesToPickup;
    }

    public CargoMode getCargoMode() {
        return cargoMode;
    }

    public void setCargoMode(CargoMode cargoMode) {
        this.cargoMode = cargoMode;
    }

    public void addCargo(Cargo cargo) {
        cargoesToPickup.add(cargo);
    }

    public void removeCargo(Cargo cargo) {
        cargoesToPickup.remove(cargo);
    }

    public boolean isReadyToDepart(int currentLoad, int maxLoad) {
        switch (cargoMode) {
            case FULL:
                return currentLoad >= maxLoad;
            case HALF:
                return currentLoad >= maxLoad / 2;
            case AVAILABLE:
            default:
                return true;
        }
    }
}