package presentation;

import controller.DisplayMapController;
import controller.UpgradeAStationController;
import domain.*;

import domain.Map;
import java.time.LocalDate;
import java.util.List;
import java.util.Scanner;

public class UpgradeAStationUI implements Runnable {

    private Map map;
    private Scenario scenario;

    public UpgradeAStationUI(Map map, Scenario scenario)
    {
        this.map = map;
        this.scenario = scenario;
    }

    public void getInput() {
        System.out.println("\n\n------------------------ Upgrade Station ----------------------");
        Scanner sc = new Scanner(System.in);

        UpgradeAStationController controller = new UpgradeAStationController();

        int budget = scenario.getBudget().getBudget();

        List<Station> stations = controller.getAvailableStations(map.getMapName());

        if (stations.isEmpty()) {
            System.out.println("There are no available stations to upgrade.");
            return;
        }

        System.out.println("Available Stations:");
        for (int i = 0; i < stations.size(); i++) {
            Station s = stations.get(i);
            System.out.println((i + 1) + ". Station ID: " + s.getStationID() + " | Type: " + s.getStationType() +
                    " | Position: (" + s.getPositionX() + ", " + s.getPositionY() + ")");
        }

        System.out.print("\nSelect a station to upgrade (enter number): ");
        int selectedIndex = Integer.parseInt(sc.nextLine()) - 1;

        if (selectedIndex < 0 || selectedIndex >= stations.size()) {
            System.out.println("Invalid station selection.");
            return;
        }

        Station selectedStation = stations.get(selectedIndex);

        System.out.println();

        System.out.println("Your current budget is " + budget + "€");

        System.out.println("\nAvailable Building Types:");
        for (BuildingType bt : controller.getAvailableBuildingTypes()) {
            System.out.println(bt.getName() + " | " + bt.getCost() + "€ | Effect: " + bt.getEffect());
        }

        System.out.print("\nEnter the type of building to add: ");
        String buildingTypeName = sc.nextLine().trim();

        BuildingType selectedBuildingType = controller.getBuildingTypeByName(buildingTypeName);
        if (selectedBuildingType == null) {
            System.out.println("ERROR: Invalid building type.");
            return;
        }

        if (budget < selectedBuildingType.getCost()) {
            System.out.println("ERROR: Not enough budget to build " + buildingTypeName + ".");
            return;
        }

        int currentYear = LocalDate.now().getYear();
        boolean success = controller.upgradeStation(selectedStation, buildingTypeName, currentYear);

        if (success) {
            scenario.subtractBudget(selectedBuildingType.getCost());
            System.out.println("SUCCESS: Building '" + buildingTypeName + "' added to station.");
        } else {
            System.out.println("ERROR: Failed to upgrade the station with building '" + buildingTypeName + "'.");
        }
    }

    @Override
    public void run() {
        getInput();
    }
}