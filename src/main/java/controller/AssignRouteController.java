package controller;

import domain.CargoMode;
import domain.Station;
import domain.Train;
import domain.PointOfRoute;

import java.util.List;

/**
 * Provides functionality to assign routes, configure cargo modes, execute routes, and display route information for trains.
 */
public class AssignRouteController {

    /**
     * Assigns a specified route to a train and prints the details of the assigned route,
     * including cargo availability and demand at each station.
     *
     * @param train    the train to which the route will be assigned
     * @param stations the list of stations that define the route for the train
     */
    public void assignRoute(Train train, List<Station> stations) {
        train.setRoute(stations);
        System.out.println("\n\n[ASSIGNED ROUTE] Train '" + train.getLocomotive().getLocomotiveModel() +
                "' received route with " + stations.size() + " stations:");

        for (int i = 0; i < stations.size(); i++) {
            Station station = stations.get(i);
            System.out.println("\n  " + (i + 1) + ". " + station.getName() +
                    " (" + station.getPositionX() + "," + station.getPositionY() + ")" +
                    " - Cargo: " + station.getCargo() +
                    " - Demand: " + station.getDemand());
        }
    }

    /**
     * Sets the cargo mode for a specific point in the train's route and provides
     * a visual sign of the configuration and behavior based on the mode.
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
}