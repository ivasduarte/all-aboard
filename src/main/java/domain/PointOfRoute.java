package domain;

import javafx.fxml.FXML;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Represents a point in a train route, containing information about the station and cargo operations.
 * Implements Serializable to support object persistence.
 */
public class PointOfRoute implements Serializable {
    private final Station station;
    private final List<Cargo> cargoesToPickup;
    private CargoMode cargoMode;

    /**
     * Creates a new point of route with the specified station and default cargo mode (AVAILABLE).
     *
     * @param station The station associated with this route point
     */
    public PointOfRoute(Station station) {
        this.station = station;
        this.cargoesToPickup = new ArrayList<>();
        this.cargoMode = CargoMode.AVAILABLE; // Default mode
    }

    /**
     * Creates a new point of route with the specified station and cargo mode.
     *
     * @param station The station associated with this route point
     * @param cargoMode The cargo mode for this route point
     */
    public PointOfRoute(Station station, CargoMode cargoMode) {
        this.station = station;
        this.cargoesToPickup = new ArrayList<>();
        this.cargoMode = cargoMode;
    }

    /**
     * Gets the station associated with this route point.
     *
     * @return The station object
     */
    public Station getStation() {
        return station;
    }

    /**
     * Gets the list of cargoes to be picked up at this point.
     *
     * @return List of cargoes to be picked up
     */
    public List<Cargo> getCargoesToPickup() {
        return cargoesToPickup;
    }

    /**
     * Gets the current cargo mode for this route point.
     *
     * @return The current cargo mode
     */
    public CargoMode getCargoMode() {
        return cargoMode;
    }

    /**
     * Sets the cargo mode for this route point.
     *
     * @param cargoMode The new cargo mode to set
     */
    public void setCargoMode(CargoMode cargoMode) {
        this.cargoMode = cargoMode;
    }

    /**
     * Adds a cargo to the pickup list.
     *
     * @param cargo The cargo to be added
     */
    public void addCargo(Cargo cargo) {
        cargoesToPickup.add(cargo);
    }

    /**
     * Removes a cargo from the pickup list.
     *
     * @param cargo The cargo to be removed
     */
    public void removeCargo(Cargo cargo) {
        cargoesToPickup.remove(cargo);
    }

    /**
     * Checks if the train is ready to depart based on current load and cargo mode.
     *
     * @param currentLoad The current load of the train
     * @param maxLoad The maximum load capacity of the train
     * @return true if the train is ready to depart, false otherwise
     */
    public boolean isReadyToDepart(int currentLoad, int maxLoad) {
        switch (cargoMode) {
            case FULL:
                return currentLoad >= maxLoad;
            case HALF:
                return currentLoad >= maxLoad / 2;
            case AVAILABLE:
                return currentLoad >= 0;
            default:
                return true;
        }
    }

    /**
     * Returns a string representation of the route point.
     *
     * @return String containing station name and cargo mode
     */
    @Override
    public String toString()
    {
        return station.getName() + " | " + cargoMode;
    }
}