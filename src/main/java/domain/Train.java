package domain;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Train implements Serializable {
    private static final int MAX_CARRIAGES = 8;

    private final Locomotive locomotive;
    private List<PointOfRoute> route;
    private final int departureYear;
    private double arrivalTime;
    private List<Carriage> carriages;
    private boolean isDoubleTrack;

    private int currentStop;

    public Train(Locomotive locomotive, int departureYear) {
        this.locomotive = locomotive;
        this.route = new ArrayList<>();
        this.carriages = new ArrayList<>();
        this.departureYear = departureYear;
        this.currentStop = 0;
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

    public boolean hasArrived(int currentYear) {
        return currentYear >= arrivalTime;
    }

    public double getArrivalTime() {
        return arrivalTime;
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

    public List<Carriage> getCarriages() {return carriages;}

    public int getCurrentStop() {return currentStop;}

    public static int getMaxCarriages() { return MAX_CARRIAGES; }

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

    public void setCarriages(int carriageAmount)
    {
        carriages = new ArrayList<>();
        for(int i = 0; i < carriageAmount && i < MAX_CARRIAGES; i++)
            carriages.add(new Carriage(new Cargo(Cargo.Type.NONE), 0));
    }

    public void updateStop() {
        if(currentStop < route.size()-1)
            currentStop++;
        else
            currentStop = 0;
    }

    public int getTotalCargo()
    {
        int totalCargo = 0;

        for(Carriage carriage : carriages)
        {
            totalCargo += carriage.getCurrentCargo();
        }

        return totalCargo;
    }
}