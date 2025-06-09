package controller;

import domain.CargoMode;
import domain.Station;
import domain.Train;
import domain.PointOfRoute;

import java.util.List;

/**
 * Provides functionality to assign routes, configure cargo modes, and display route information for trains.
 */
public class AssignRouteController {
    /**
     * Assigns a specified route to a train and prints the details of the assigned route.
     *
     * @param train    the train to which the route will be assigned
     * @param stations the list of stations that define the route for the train
     */
    public void assignRoute(Train train, List<Station> stations) {
        train.setRoute(stations);
        System.out.println("\n\n[ASSIGNED ROUTE] Train '" + train.getLocomotive().getLocomotiveModel() +
                "' received route with " + stations.size() + " stations:");

        for (int i = 0; i < stations.size(); i++) {
            System.out.println("\n  " + (i + 1) + ". " + stations.get(i).getName() +
                    " (" + stations.get(i).getPositionX() + "," + stations.get(i).getPositionY() + ")");
        }
    }

    /**
     * Sets the cargo mode for a specific point in the train's route and provides
     * a visual indication of the configuration and behavior based on the mode.
     * If the specified point index corresponds to a valid station in the train's route,
     * additional details about the cargo mode configuration at the station are displayed.
     *
     * @param train      the train whose cargo mode is being configured
     * @param pointIndex the index of the point in the train's route where the cargo mode is to be set
     * @param mode       the cargo mode to be set, determining the departure conditions from the station
     */
    public void setCargoMode(Train train, int pointIndex, CargoMode mode) {
        train.setCargoMode(pointIndex, mode);

        if (train.getRoute() != null && pointIndex < train.getRoute().size()) {
            PointOfRoute point = train.getRoute().get(pointIndex);
            System.out.println("\n[CARGO MODE] Station '" + point.getStation().getName() +
                    "' set with mode: " + mode.toString());

            switch (mode) {
                case FULL:
                    System.out.println("  → Train only departs when full loaded");
                    break;
                case HALF:
                    System.out.println("  → Train only departs when half loaded");
                    break;
                case AVAILABLE:
                    System.out.println("  → Train only departs with whatever cargo is available");
                    break;
            }
        }
    }

    /**
     * Displays detailed information about the train's route, including each station
     * in the route, the cargo mode at each point, the cargo associated with each station,
     * and the total distance of the route. If no route is assigned, a message is displayed
     * indicating that the train does not have an assigned route.
     *
     * @param train the train whose route information is to be displayed
     */
    public void displayRouteInfo(Train train) {
        if (train.getRoute() == null || train.getRoute().isEmpty()) {
            System.out.println("\n[ROUTE INFO] Train '" + train.getLocomotive().getLocomotiveModel() +
                    "' doesn't have an assigned route.");
            return;
        }

        System.out.println("\n[ROUTE INFO] Train '" + train.getLocomotive().getLocomotiveModel() + "':");
        List<PointOfRoute> route = train.getRoute();

        for (int i = 0; i < route.size(); i++) {
            PointOfRoute point = route.get(i);
            System.out.println("  " + (i + 1) + ". " + point.getStation().getName() +
                    " (Mode: " + point.getCargoMode() + ", Cargo: " + point.getStation().getCargo() + ")");
        }

        double totalDistance = train.calculateRouteDistance();
        System.out.println("  ROUTE'S TOTAL DISTANCE: " + (int) totalDistance + " km.");
    }
}