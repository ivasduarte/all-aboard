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

        UpgradeAStationController controller = new UpgradeAStationController();

        List<Station> stations = controller.getAvailableStations(map.getName());

        System.out.println("\nAVAILABLE STATIONS TYPES TO UPGRADE:");
        for (int i = 0; i < stations.size(); i++) {
            Station s = stations.get(i);
            System.out.println((i + 1) + ". Station ID: " + s.getStationID() + " | Type: " + s.getStationType() +
                    " | Position: (" + s.getPositionX() + ", " + s.getPositionY() + ")");
        }

        System.out.println("\nSELECT STATION'S NUMBER: ");
        int stationID = Integer.parseInt(sc.nextLine()) - 1;

        if (stationID < 0 || stationID >= stations.size()) {
            System.out.println("ERROR: Invalid station selection.");
            return;
        }

        Station station = stations.get(stationID);

        if (station.getStationTypeID() >= Station.StationType.values().length - 1) {
            System.out.println("WARNING:");
            System.out.println("Chosen station is already a " + station.getStationType() + ". Cannot be further upgraded.");
            return;
        }

        System.out.println("\nSTATION TYPE: " + station.getStationType());

        int StationInitialPrice = station.getStationPrice();

        System.out.println("\nAVAILABLE UPGRADES: ");

        List<String> availableStationTypes = new ArrayList<>();

        for (int i = 0; i < Station.StationType.values().length; i++) {
            if (i > station.getStationTypeID()) {
                System.out.println(Station.StationType.values()[i]);
                availableStationTypes.add(String.valueOf(Station.StationType.values()[i]));
            }
        }

        System.out.print("\nSELECT NEW STATION: ");
        String newType = sc.nextLine();

        boolean validType = false;
        int typeID = 0;
        for (int i = 0; i < Station.StationType.values().length; i++) {
            if (Station.StationType.values()[i].getDisplayName().equalsIgnoreCase(newType)) {
                validType = true;
                typeID = i;
            }
        }

        if (!validType) {
            System.out.println("ERROR: New type isn't valid!");
            return;
        }

        boolean result = changeStationTypeController.ChangeStationType(station, typeID);

        if (!result)
            System.out.println("Station type change failed! Station does not fit!");
        else
            System.out.println("\nType of station successfully changed.");

        int stationFinalPrice = station.getStationPrice();

        int DecreasedBudget = stationFinalPrice - StationInitialPrice;

        scenario.subtractBudget(DecreasedBudget);

        System.out.println("\nTotal Remaining Budget: " + scenario.getBudget().getBudget());


    }

    @Override
    public void run() {
        getInput();
    }
}
