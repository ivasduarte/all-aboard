package presentation;

import controller.UpgradeAStationController;
import domain.BuildingType;
import domain.Station;

import java.time.LocalDate;
import java.util.List;
import java.util.Scanner;

public class UpgradeAStationUI implements Runnable {
    private final UpgradeAStationController controller = new UpgradeAStationController();
    private final Scanner ler = new Scanner(System.in);

    @Override
    public void run() {
        System.out.println("\n\n------------------------ Upgrade Station ----------------------");

        List<Station> stations = controller.getAvailableStations();
        if (stations.isEmpty()) {
            System.out.println("There are no available stations to upgrade.");
            return;
        }

        System.out.println("Available Stations:");
        for (int i = 0; i < stations.size(); i++) {
            Station s = stations.get(i);
            System.out.println((i + 1) + ". Station ID: " + s.getStationID() + ", Type: " + s.getStationType() +
                    ", Position: (" + s.getPositionX() + ", " + s.getPositionY() + ")");
        }

        System.out.print("\nSelect a station to upgrade (enter number): ");
        int selectedIndex = Integer.parseInt(ler.nextLine()) - 1;

        if (selectedIndex < 0 || selectedIndex >= stations.size()) {
            System.out.println("Invalid station selection.");
            return;
        }

        Station selectedStation = stations.get(selectedIndex);

        System.out.println("\nAvailable Building Types:");
        for (BuildingType bt : controller.getAvailableBuildingTypes()) {
            System.out.println("- " + bt.getName() + " (Cost: " + bt.getCost() + ", Effect: " + bt.getEffect() + ")");
        }

        System.out.print("\nEnter the type of building to add: ");
        String buildingTypeName = ler.nextLine().trim();

        int currentYear = LocalDate.now().getYear();
        boolean success = controller.upgradeStation(selectedStation, buildingTypeName, currentYear);

        if (success) {
            System.out.println("SUCCESS: Building '" + buildingTypeName + "' added to station.");
        } else {
            System.out.println("ERROR: Failed to upgrade the station with building '" + buildingTypeName + "'.");
        }
    }
}