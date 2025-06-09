package domain;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Train implements Serializable {
    private final Locomotive locomotive;
    private List<PointOfRoute> route;
    private final int departureYear;
    private double arrivalTime;
    private List<Carriage> carriages;
    private boolean isDoubleTrack;

    private int currentCargo = 0;

    public Train(Locomotive locomotive, int departureYear) {
        this.locomotive = locomotive;
        this.route = new ArrayList<>();
        this.carriages = new ArrayList<>();
        this.departureYear = departureYear;
    }

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

    public List<PointOfRoute> getRoute() {
        return route;
    }

    public void setRoute(List<Station> stations) {
        this.route = new ArrayList<>();
        for (Station station : stations) {
            this.route.add(new PointOfRoute(station));
        }
        this.arrivalTime = calculateArrivalTime();
    }

    public Locomotive getLocomotive() {
        return locomotive;
    }

    public int getNumberOfCarriages() {
        return carriages.size();
    }

    /**
     * Sets the number of carriages for this train.
     * If the new number is greater than the current number, adds empty carriages.
     * If the new number is lower than the current number, removes carriages from the end.
     * 
     * @param numberOfCarriages the new number of carriages
     */
    public void setNumberOfCarriages(int numberOfCarriages) {
        if (numberOfCarriages < 0) {
            throw new IllegalArgumentException("Number of carriages cannot be negative.");
        }

        int currentSize = carriages.size();

        if (numberOfCarriages > currentSize) {
            // add new carriages
            for (int i = 0; i < numberOfCarriages - currentSize; i++) {
                carriages.add(new Carriage(new Cargo(Cargo.Type.AUTO), 9999, 0)); // default example
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


    public double calculateRouteDistance() {
        double distance = 0;
        for(int i = 0; i < route.size() - 1; i++) {
            distance += route.get(i).getStation().distanceTo(route.get(i+1).getStation());
        }
        return distance;
    }

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

    private boolean isDoubleTrackBetween(Station from, Station to) {
        int dx = from.getPositionX() - to.getPositionX();
        int dy = from.getPositionY() - to.getPositionY();
        double distance = Math.sqrt(dx * dx + dy * dy);

        return distance < 15;
    }

    public void setCargoMode(int pointIndex, CargoMode mode) {
        if (pointIndex >= 0 && pointIndex < route.size()) {
            route.get(pointIndex).setCargoMode(mode);
        }
    }

    public boolean isReadyToDepart(int pointIndex, int currentLoad) {
        if (pointIndex >= 0 && pointIndex < route.size()) {
            return route.get(pointIndex).isReadyToDepart(currentLoad, carriages.size());
        }
        return false;
    }

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

    public int getTotalCapacity(){
        int totalCapacity = 0;
        for(Carriage carriage : carriages) {
            totalCapacity  += carriage.getMaxCargo();
        }
        return totalCapacity;
    }

    public int getRemainingCapacity() {
        int totalCapacity = 0;
        for(Carriage carriage : carriages) {
            totalCapacity  += carriage.getMaxCargo();
        }
        return totalCapacity - currentCargo;
    }

    public void addCargo(int amount){
        if(amount > 0){
            int remainingCapacity = getRemainingCapacity();
            int toAdd = Math.min(remainingCapacity, amount);
            this.currentCargo += toAdd;

            System.out.println("\n[CARGO] Added " + toAdd + "cargo units to train.\nCurrent load: " + this.currentCargo + "/" + this.getTotalCapacity());
        }
    }

    public int getCurrentCargo() {
        return currentCargo;
    }

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
}
