package domain;

import java.util.List;

public class Locomotive {
    private String locomotiveModel;
    private String fuelType;
    private int maxSpeed; // Changed from `speed`
    private int startYear;
    private int endYear;
    private int price;
    private int fuelCost;
    private int maintenanceCost;
    private List<Station> route;

    public Locomotive(String locomotiveModel, String fuelType, int startYear, int endYear,
                      int price, int maintenanceCost, int fuelCost, int maxSpeed) {
        this.locomotiveModel = locomotiveModel;
        this.fuelType = fuelType;
        this.startYear = startYear;
        this.endYear = endYear;
        this.price = price;
        this.maintenanceCost = maintenanceCost;
        this.fuelCost = fuelCost;
        this.maxSpeed = maxSpeed;
    }

    public String getLocomotiveModel() {
        return locomotiveModel;
    }

    public String getLocomotiveFuelType() {
        return fuelType;
    }

    public int getPrice() {
        return price;
    }

    public int getStartYear() {
        return startYear;
    }

    public int getEndYear() {
        return endYear;
    }

    public int getFuelCost() {
        return fuelCost;
    }

    public int getMaintenanceCost() {
        return maintenanceCost;
    }

    public List<Station> getRoute() {
        return route;
    }

    public void setRoute(List<Station> route) {
        this.route = route;
    }

    public int getMaxSpeed() {
        return maxSpeed;
    }

    public boolean isOnDoubleTrack() {
        if (route == null || route.size() < 2) return true;

        for (int i = 0; i < route.size() - 1; i++) {
            Station from = route.get(i);
            Station to = route.get(i + 1);
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

}