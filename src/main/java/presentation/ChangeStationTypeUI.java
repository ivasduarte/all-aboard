package presentation;

import controller.ChangeStationTypeController;
import controller.UpgradeAStationController;
import domain.Map;
import domain.Scenario;
import domain.Station;
import repositories.Repositories;
import repositories.StationRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class ChangeStationTypeUI implements Runnable {
    private Map map;
    private Scenario scenario;

    public ChangeStationTypeUI(Map map, Scenario scenario) {
        this.map = map;
        this.scenario = scenario;
    }

    public void getInput() {
        Scanner sc = new Scanner(System.in);

        ChangeStationTypeController changeStationTypeController = new ChangeStationTypeController();
        StationRepository stationRepository = Repositories.getInstance().getStationRepository();
        UpgradeAStationController controller = new UpgradeAStationController();

        List<Station> stations = controller.getAvailableStations(map.getMapName());

        System.out.println("\nAvailable Stations:");
        for (int i = 0; i < stations.size(); i++) {
            Station s = stations.get(i);
            System.out.println((i + 1) + ". Station ID: " + s.getStationID() + " | Type: " + s.getStationType() +
                    " | Position: (" + s.getPositionX() + ", " + s.getPositionY() + ")");
        }

        System.out.print("\nSelect a station to upgrade (enter number): ");
        int stationID = Integer.parseInt(sc.nextLine()) - 1;

        if (stationID < 0 || stationID >= stations.size()) {
            System.out.println("Invalid station selection.");
            return;
        }

        Station station = stationRepository.getStations().get(stationID);

        if (station.getStationTypeID() >= Station.stationTypes.length - 1) {
            System.out.println("Station is already a " + station.getStationType() + ". Cannot be further upgraded");
            return;
        }

        System.out.println("\nStation type is: " + station.getStationType());
        System.out.println("\nStation can be upgraded into: ");

        List<String> availableStationTypes = new ArrayList<>();

        for (int i = 0; i < Station.stationTypes.length; i++) {
            if (i > station.getStationTypeID()) {
                System.out.println(Station.stationTypes[i]);
                availableStationTypes.add(Station.stationTypes[i]);
            }
        }

        System.out.print("\nSelect the new station type: ");
        String newType = sc.nextLine();

        boolean validType = false;
        for (int i = 0; i < availableStationTypes.size(); i++) {
            if (availableStationTypes.get(i).equalsIgnoreCase(newType)) {
                validType = true;
            }
        }

        if (!validType) {
            System.out.println("New type isn't valid!");
            return;
        }

        boolean result = changeStationTypeController.ChangeStationType(station, newType);

        if (!result)
            System.out.println("Station type change failed! Station does not fit!");
        else
            System.out.println("\nType of station successfully changed.");
    }

    @Override
    public void run() {
        getInput();
    }
}
