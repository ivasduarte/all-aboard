package presentation;

import controller.ListStationsController;
import domain.Building;
import domain.Map;
import domain.Station;

import java.util.List;
import java.util.Scanner;

/**
 * User Interface class responsible for listing all stations of a given map,
 * showing their type, position, buildings, and stored cargo.
 */
public class ListStationsUI implements Runnable {

    private Map map;

    static Scanner ler = new Scanner(System.in);


    /**
     * Constructs the UI with the specified map.
     *
     * @param map the map to filter stations by
     */
    public ListStationsUI(Map map) {
        this.map = map;
    }

    /**
     * Gets input and displays all available stations on the specified map,
     * along with their buildings and cargo stored.
     */
    public void getInput() {
        List<Station> stations = map.getStations();

        if (stations.isEmpty()) {
            System.out.println("No stations found");
            return;
        }

        System.out.println("\n\n------------------------- List of Stations -------------------------");

        for (int i = 0; i < stations.size(); i++) {
            Station station = stations.get(i);
            System.out.println((i + 1) + ". " + station.getName() + " | ID: " + station.getStationID()
                    + " | Type: " + station.getStationType().getDisplayName()
                    + " | Position: (" + station.getPositionX() + ", " + station.getPositionY() + ")");
        }

        System.out.print("\nSelect a station by number: ");
        int index;
        try {
            index = Integer.parseInt(ler.nextLine()) - 1;
            if (index < 0 || index >= stations.size()) {
                System.out.println("Invalid selection.");
                return;
            }
        } catch (NumberFormatException e) {
            System.out.println("Invalid input.");
            return;
        }

        Station selectedStation = stations.get(index);

        System.out.println("\n--- Station Details ---");
        System.out.println("Name: " + selectedStation.getName());
        System.out.println("ID: " + selectedStation.getStationID());
        System.out.println("Type: " + selectedStation.getStationType());
        System.out.println("Position: (" + selectedStation.getPositionX() + ", " + selectedStation.getPositionY() + ")");

        System.out.println("\nExisting buildings of this station: ");
        List<Building> buildings = selectedStation.getBuildings();
        if (buildings.isEmpty()) {
            System.out.println("No buildings found");
        } else {
            for (Building building : buildings) {
                System.out.println("- " + building.getBuildingType());
            }
        }

        System.out.println("\nCargo stored: " + selectedStation.getCargo());
    }

    /**
     * Starts the execution of the UI for listing stations.
     */
    @Override
    public void run() {
        getInput();
    }
}