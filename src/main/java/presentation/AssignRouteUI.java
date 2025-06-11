package presentation;

import controller.AssignRouteController;
import domain.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class AssignRouteUI implements Runnable {
    private Map map;
    private Scenario scenario;

    public AssignRouteUI(Map map, Scenario scenario) {
        this.map = map;
        this.scenario = scenario;
    }

    @Override
    public void run() {
        getInput();
    }

    public void getInput() {
        Scanner sc = new Scanner(System.in);

        List<Train> trains = scenario.getTrains();

        if (trains.isEmpty()) {
            System.out.println("No available trains!");
            return;
        }

        List<Station> stations = map.getStations();

        if (stations.size() < 2) {
            System.out.println("There's not enough stations to create a route!");
            return;
        }

        System.out.println("\n------------------------ Assign Route ------------------------");
        for (int i = 0; i < trains.size(); i++) {
            System.out.println((i + 1) + ". " + trains.get(i).getLocomotive().getLocomotiveModel() + " - " + trains.get(i).getLocomotive().getLocomotiveFuelType());
        }
        System.out.print("Select the number of a train to assign the route: ");
        int trainID = sc.nextInt() - 1;
        Train train = trains.get(trainID);

        List<Station> stationsToAdd = new ArrayList<>();

        int value;
        do {
            System.out.println("\n");

            int counter = 0;
            List<Station> availableStations = new ArrayList<>();
            for (Station station : stations) {

                boolean added = false;

                for (Station stationToAdd : stationsToAdd) {
                    if (station.equals(stationToAdd))
                        added = true;
                }

                if (!added) {
                    System.out.println((counter + 1) + ". " + station.getName() + " - " + station.getStationType() + " (" + station.getPositionX() + "," + station.getPositionY() + ")");
                    counter++;
                    availableStations.add(station);
                }
            }

            if (!availableStations.isEmpty()) {
                System.out.print("\nEnter the number of the station you wish to add (enter a negative number or zero to exit): ");
                value = sc.nextInt() - 1;

                if (value >= availableStations.size() || value < 0) {
                    System.out.println("Number isn't valid, try again!");
                } else{
                    stationsToAdd.add(availableStations.get(value));
                    System.out.println("Station added to the route!");
                }
            } else {
                System.out.println("No available stations to add, assigning route!");
                value = -1;
            }
        } while (value >= 0);

        AssignRouteController routeController = new AssignRouteController();
        routeController.assignRoute(train, stationsToAdd);

        for (int i = 0; i < train.getRoute().size(); i++) {
            System.out.println("\nConfigure cargo mode for station: " + train.getRoute().get(i).getStation().getName());
            System.out.println("1. FULL - Train departs only when fully loaded");
            System.out.println("2. HALF - Train departs when half loaded");
            System.out.println("3. AVAILABLE - Train departs with available cargo");
            System.out.print("Select cargo mode (1-3): ");

            int modeChoice = sc.nextInt();
            CargoMode mode;
            switch (modeChoice) {
                case 1:
                    mode = CargoMode.FULL;
                    break;
                case 2:
                    mode = CargoMode.HALF;
                    break;
                case 3:
                default:
                    mode = CargoMode.AVAILABLE;
                    break;
            }
            routeController.setCargoMode(train, i, mode);
        }
        System.out.println("\nRoute assigned successfully!");
    }
}