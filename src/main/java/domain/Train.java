package domain;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Represents a train in the simulation system.
 * Contains information about the locomotive, carriages, route, and cargo operations.
 * Implements Serializable to support object persistence.
 */
public class Train implements Serializable {
    private final Locomotive locomotive;
    private List<PointOfRoute> route;
    private final int departureYear;
    private double arrivalTime;
    private List<Carriage> carriages;
    private boolean isDoubleTrack;

    private int currentCargo = 0;

    private Station waitingAtStation;
    private int waitingAtRouteIndex;
    private boolean isWaiting;

    /**
     * Creates a new Train instance with the specified locomotive and departure year.
     *
     * @param locomotive The locomotive for this train
     * @param departureYear The year when the train will depart
     */
    public Train(Locomotive locomotive, int departureYear) {
        this.locomotive = locomotive;
        this.route = new ArrayList<>();
        this.carriages = new ArrayList<>();
        this.departureYear = departureYear;
    }

    /**
     * Calculates the arrival time based on the route distance, locomotive speed, and number of carriages.
     *
     * @return The calculated arrival time
     */
    private double calculateArrivalTime() {
        double maxSpeed = locomotive.getMaxSpeed();

        double speedPenalty = Math.min(0.05 * carriages.size(), 0.30);
        double effectiveSpeed = maxSpeed * (1 - speedPenalty);

        if (!isDoubleTrack) {
            effectiveSpeed *= 0.8;
        }

        double distance = 0;
        for(int i = 0; i < route.size() - 1; i++) {
            distance += route.get(i).getStation().distanceTo(route.get(i+1).getStation());
        }

        return distance / effectiveSpeed;
    }

    /**
     * Gets the route of this train.
     *
     * @return The list of route points
     */
    public List<PointOfRoute> getRoute() {
        return route;
    }

    /**
     * Sets the route for this train and recalculates the arrival time.
     *
     * @param pointOfRoutes The list of route points to set
     */
    public void setRoute(List<PointOfRoute> pointOfRoutes) {
        this.route = new ArrayList<>(pointOfRoutes);
        this.arrivalTime = calculateArrivalTime();
    }

    /**
     * Sets the route using a list of stations, creating route points for each station.
     *
     * @param stations The list of stations to create the route from
     */
    public void setRouteWithStations(List<Station> stations) {
        this.route = new ArrayList<>();

        for(Station station : stations)
            this.route.add(new PointOfRoute(station));

        this.arrivalTime = calculateArrivalTime();
    }

    /**
     * Gets the locomotive of this train.
     *
     * @return The locomotive object
     */
    public Locomotive getLocomotive() {
        return locomotive;
    }

    /**
     * Gets the number of carriages in this train.
     *
     * @return The number of carriages
     */
    public int getNumberOfCarriages() {
        return carriages.size();
    }

    /**
     * Gets the list of carriages in this train.
     *
     * @return The list of carriages
     */
    public List<Carriage> getCarriages() {
        return carriages;
    }

    /**
     * Sets the number of carriages for this train.
     * If the new number is greater than the current number, adds empty carriages.
     * If the new number is lower than the current number, removes carriages from the end.
     *
     * @param numberOfCarriages the new number of carriages
     * @throws IllegalArgumentException if the number of carriages is negative
     */
    public void setNumberOfCarriages(int numberOfCarriages) {
        if (numberOfCarriages < 0) {
            throw new IllegalArgumentException("Number of carriages cannot be negative.");
        }

        int currentSize = carriages.size();

        if (numberOfCarriages > currentSize) {
            // add new carriages
            for (int i = 0; i < numberOfCarriages - currentSize; i++) {
                carriages.add(new Carriage(new Cargo(Cargo.Type.AUTO), 10, 0)); // default example
            }
        } else if (numberOfCarriages < currentSize) {
            // remove carriages from the end
            for (int i = 0; i < currentSize - numberOfCarriages; i++) {
                carriages.remove(carriages.size() - 1);
            }
        }

        // recalculate arrival time as the number of carriages affects speed
        if (route != null && !route.isEmpty()) {
            this.arrivalTime = calculateArrivalTime();
        }
    }

    /**
     * Calculates the total distance of the train's route.
     *
     * @return The total route distance
     */
    public double calculateRouteDistance() {
        double distance = 0;
        for(int i = 0; i < route.size() - 1; i++) {
            distance += route.get(i).getStation().distanceTo(route.get(i+1).getStation());
        }
        return distance;
    }

    /**
     * Checks if the train is running on a double track.
     *
     * @return true if the train is on a double track, false otherwise
     */
    public boolean isOnDoubleTrack() {
        if (route == null || route.size() < 2) return true;

        for (int i = 0; i < route.size() - 1; i++) {
            Station from = route.get(i).getStation();
            Station to = route.get(i + 1).getStation();
            if (!isDoubleTrackBetween(from, to)) {
                return false;
            }
        }
        return true;
    }

    /**
     * Checks if there is a double track between two stations.
     *
     * @param from The starting station
     * @param to The destination station
     * @return true if there is a double track between the stations, false otherwise
     */
    private boolean isDoubleTrackBetween(Station from, Station to) {
        int dx = from.getPositionX() - to.getPositionX();
        int dy = from.getPositionY() - to.getPositionY();
        double distance = Math.sqrt(dx * dx + dy * dy);

        return distance < 15;
    }

    /**
     * Sets the cargo mode for a specific point in the route.
     *
     * @param pointIndex The index of the route point
     * @param mode The cargo mode to set
     */
    public void setCargoMode(int pointIndex, CargoMode mode) {
        if (pointIndex >= 0 && pointIndex < route.size()) {
            route.get(pointIndex).setCargoMode(mode);
        }
    }

    /**
     * Checks if the train is ready to depart from a specific point in the route.
     *
     * @param pointIndex The index of the route point
     * @param currentLoad The current cargo load
     * @return true if the train is ready to depart, false otherwise
     */
    public boolean isReadyToDepart(int pointIndex, int currentLoad) {
        if (pointIndex >= 0 && pointIndex < route.size()) {
            return route.get(pointIndex).isReadyToDepart(currentLoad, (carriages.size() * carriages.get(0).getMaxCargo()));
        }
        return false;
    }

    /**
     * Collects cargo from a station based on the cargo mode of the route point.
     *
     * @param station The station to collect cargo from
     * @param point The route point with cargo mode settings
     */
    public void collectCargoFromStation(Station station, PointOfRoute point) {
        int availableCargo = station.getCargo();
        int trainCapacity = this.getRemainingCapacity();

        if (trainCapacity <= 0) {
            System.out.println("[INFO] Train is full, cannot collect more cargo");
            return;
        }

        if (availableCargo <= 0) {
            System.out.println("[INFO] No cargo available at station " + station.getName());
            return;
        }

        switch (point.getCargoMode()) {
            case FULL:
                if (availableCargo >= trainCapacity) {
                    station.removeCargo(trainCapacity);
                    this.addCargo(trainCapacity);
                    System.out.println("[FULL MODE] Collected " + trainCapacity + " cargo units");
                } else {
                    System.out.println("[FULL MODE] Not enough cargo. Need " + trainCapacity +
                            ", available " + availableCargo);
                }
                break;

            case HALF:
                int halfCapacity = trainCapacity / 2;
                if (halfCapacity <= 0) halfCapacity = 1; // minimum = 1

                if (availableCargo >= halfCapacity) {
                    int toCollect = Math.min(availableCargo, halfCapacity);
                    station.removeCargo(toCollect);
                    this.addCargo(toCollect);
                    System.out.println("[HALF MODE] Collected " + toCollect + " cargo units");
                } else {
                    System.out.println("[HALF MODE] Not enough cargo. Need " + halfCapacity +
                            ", available " + availableCargo);
                }
                break;

            case AVAILABLE:
                int toCollect = Math.min(availableCargo, trainCapacity);
                station.removeCargo(toCollect);
                this.addCargo(toCollect);
                System.out.println("[AVAILABLE MODE] Collected " + toCollect + " cargo units");
                break;
        }
    }

    /**
     * Gets the total cargo capacity of the train.
     *
     * @return The total capacity
     */
    public int getTotalCapacity(){
        int totalCapacity = 0;
        for(Carriage carriage : carriages) {
            totalCapacity  += carriage.getMaxCargo();
        }
        return totalCapacity;
    }

    /**
     * Gets the remaining cargo capacity of the train.
     *
     * @return The remaining capacity
     */
    public int getRemainingCapacity() {
        int totalCapacity = 0;
        for(Carriage carriage : carriages) {
            totalCapacity  += carriage.getMaxCargo();
        }
        return totalCapacity - currentCargo;
    }

    /**
     * Adds cargo to the train.
     *
     * @param amount The amount of cargo to add
     */
    public void addCargo(int amount){
        if(amount > 0){
            int remainingCapacity = getRemainingCapacity();
            int toAdd = Math.min(remainingCapacity, amount);
            this.currentCargo += toAdd;

            System.out.println("\n[CARGO] Added " + toAdd + "cargo units to train.\nCurrent load: " + this.currentCargo + "/" + this.getTotalCapacity());
        }
    }

    /**
     * Gets the current cargo load of the train.
     *
     * @return The current cargo amount
     */
    public int getCurrentCargo() {
        return currentCargo;
    }

    /**
     * Removes cargo from the train.
     *
     * @param amount The amount of cargo to remove
     */
    public void removeCargo(int amount) {
        if (amount > 0) {
            this.currentCargo = Math.max(0, this.currentCargo - amount);
            System.out.println("[DELIVERY] Delivered " + amount + " cargo units. " +
                    "Remaining load: " + currentCargo);
        }
    }

    /**
     * Unloads cargo at a station based on the station's demand.
     *
     * @param station The station where cargo will be unloaded
     * @param amount The maximum amount of cargo that can be unloaded
     * @return The actual amount of cargo unloaded
     */
    public int unloadCargoAtStation(Station station, int amount) {
        if (currentCargo <= 0) {
            System.out.println("[INFO] No cargo to unload at station " + station.getName());
            return 0;
        }

        int stationDemand = station.getDemand();
        if (stationDemand <= 0) {
            System.out.println("[INFO] Station " + station.getName() + " has no demand for cargo");
            return 0;
        }

        int toUnload = Math.min(Math.min(currentCargo, amount), stationDemand);
        if (toUnload > 0) {
            removeCargo(toUnload);
            station.addDeliveredCargo(toUnload);
            System.out.println("[DELIVERY] Unloaded " + toUnload + " cargo units at station " + station.getName());
        }
        return toUnload;
    }

    /**
     * Sets the train to wait at a specific station and route index
     */
    public void setWaitingAtStation(Station station, int routeIndex) {
        this.waitingAtStation = station;
        this.waitingAtRouteIndex = routeIndex;
        this.isWaiting = true;
    }

    /**
     * Clears the waiting state when train is ready to proceed
     */
    public void clearWaitingState() {
        this.waitingAtStation = null;
        this.waitingAtRouteIndex = -1;
        this.isWaiting = false;
    }

    /**
     * Checks if train is currently waiting at a station
     */
    public boolean isWaitingAtStation() {
        return isWaiting;
    }

    /**
     * Gets the station where train is waiting
     */
    public Station getWaitingStation() {
        return waitingAtStation;
    }

    /**
     * Gets the route index where train is waiting
     */
    public int getWaitingRouteIndex() {
        return waitingAtRouteIndex;
    }

    @Override
    public String toString()
    {
        return locomotive.getLocomotiveModel() + " | " + locomotive.getLocomotiveFuelType();
    }
}