package presentation;

import controller.ListStationsController;
import domain.Building;
import domain.Map;
import domain.Station;

import java.util.List;

/**
 * User Interface class responsible for listing all stations of a given map,
 * showing their type, position, buildings, and stored cargo.
 */
public class ListStationsUI implements Runnable {

    private Map map;

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

        for (Station station : stations) {
            System.out.println(station.getName() + " | ID: " + station.getStationID()
                    + " | Type: " + station.getStationType()
                    + " | Position: (" + station.getPositionX() + ", " + station.getPositionY() + ")");
            System.out.println("\nExisting buildings of this station: ");

            if (station.getBuildings().isEmpty()) {
                System.out.println("No buildings found");
            } else {
                for (Building building : station.getBuildings()) {
                    System.out.print(building.getBuildingType());
                    System.out.println();
                }
            }

            System.out.println("\nCargo stored: " + station.getCargo());
        }
    }

    /**
     * Starts the execution of the UI for listing stations.
     */
    @Override
    public void run() {
        getInput();
    }
}
