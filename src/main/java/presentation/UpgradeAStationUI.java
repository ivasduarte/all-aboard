package presentation;

import controller.DisplayMapController;
import controller.UpgradeAStationController;
import domain.*;

import domain.Map;
import java.time.LocalDate;
import java.util.List;
import java.util.Scanner;

public class UpgradeAStationUI implements Runnable {
    private final UpgradeAStationController controller = new UpgradeAStationController();
    private final Scanner ler = new Scanner(System.in);

    static Scanner sc = new Scanner(System.in);

    @Override
    public void run() {
        System.out.println("\n\n------------------------ Upgrade Station ----------------------");

        DisplayMapController mapController = new DisplayMapController();

        List<Map> mapList = mapController.getMapList();

        if(mapList.isEmpty())
        {
            System.out.println("There are no maps.");
            return;
        }

        for (int i = 0; i < mapList.size(); i++) {
            Map map = mapList.get(i);
            System.out.println("[" + map.getMapName() + " | " + map.getWidth() + "x" + map.getWidth() + "]");
        }

        System.out.print("Select a map from the list: ");
        String mapName = sc.nextLine();
        int mapID = -1;

        for (int i = 0; i < mapList.size(); i++) {
            if(mapList.get(i).getMapName().equalsIgnoreCase(mapName))
                mapID = i;
        }

        if(mapID == -1)
        {
            System.out.println("Map does not exist");
            return;
        }

        Scenario activeScenario = controller.getScenarioByMapName(mapName);

        if (activeScenario == null) {
            System.out.println("No scenario found for the selected map.");
            return;
        }

        int budget = activeScenario.getBudget().getBudget();

        List<Station> stations = controller.getAvailableStations(mapName);

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
        int selectedIndex = Integer.parseInt(ler.nextLine()) - 1;

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
        String buildingTypeName = ler.nextLine().trim();

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
            activeScenario.subtractBudget(selectedBuildingType.getCost());
            System.out.println("SUCCESS: Building '" + buildingTypeName + "' added to station.");
            System.out.println("Remaining budget: " + activeScenario.getBudget().getBudget() + "€");
        } else {
            System.out.println("ERROR: Failed to upgrade the station with building '" + buildingTypeName + "'.");
        }
    }
}