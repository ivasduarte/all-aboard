package domain;

import java.io.Serializable;

public class Train implements Serializable {
    private final Locomotive locomotive;
    private final Station origin;
    private final Station destination;
    private final int departureYear;
    private double arrivalTime;
    private final int numberOfCarriages;
    private final boolean isDoubleTrack;

    public Train(Locomotive locomotive, Station origin, Station destination, int departureYear, int numberOfCarriages, boolean isDoubleTrack) {
        this.locomotive = locomotive;
        this.origin = origin;
        this.destination = destination;
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

        double distance = origin.distanceTo(destination);
        double travelTime = distance / effectiveSpeed;

        return departureYear + travelTime;
    }

    public boolean hasArrived(int currentYear) {
        return currentYear >= arrivalTime;
    }

    public double getArrivalTime() {
        return arrivalTime;
    }

    public Station getOrigin() {
        return origin;
    }

    public Station getDestination() {
        return destination;
    }

    public Locomotive getLocomotive() {
        return locomotive;
    }

    public int getNumberOfCarriages() {
        return numberOfCarriages;
    }
}