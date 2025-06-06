package domain;

import java.io.Serializable;
import java.util.List;

public class Train implements Serializable {
    private final Locomotive locomotive;
    private List<Station> route;
    private final int departureYear;
    private double arrivalTime;
    private final int numberOfCarriages;
    private final boolean isDoubleTrack;

    public Train(Locomotive locomotive, List<Station> route, int departureYear, int numberOfCarriages, boolean isDoubleTrack) {
        this.locomotive = locomotive;
        this.route = route;
        this.departureYear = departureYear;
        this.numberOfCarriages = numberOfCarriages;
        this.isDoubleTrack = isDoubleTrack;
        this.arrivalTime = calculateArrivalTime();
    }

    private double calculateArrivalTime() {
        double maxSpeed = locomotive.getMaxSpeed();

        double speedPenalty = Math.min(0.05 * numberOfCarriages, 0.30);
        double effectiveSpeed = maxSpeed * (1 - speedPenalty);

        if (!isDoubleTrack) {
            effectiveSpeed *= 0.8;
        }

        double distance = 0;
        for(int i = 0; i < route.size() - 1; i++)
        {
            distance += route.get(i).distanceTo(route.get(i+1));
        }

        double travelTime = distance / effectiveSpeed;

        return departureYear + travelTime;
    }

    public boolean hasArrived(int currentYear) {
        return currentYear >= arrivalTime;
    }

    public double getArrivalTime() {
        return arrivalTime;
    }

    public List<Station> getRoute() { return route; }

    public Locomotive getLocomotive() {
        return locomotive;
    }

    public int getNumberOfCarriages() {
        return numberOfCarriages;
    }
}