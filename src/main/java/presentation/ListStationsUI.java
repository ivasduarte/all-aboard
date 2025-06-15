package presentation;

import controller.ListStationsController;
import domain.Map;
import domain.Scenario;
import dto.StationDTO;

import java.util.List;
import java.util.Scanner;

public class ListStationsUI implements Runnable {

    private final ListStationsController controller;
    private final Scanner sc = new Scanner(System.in);
    private Scenario scenario;

    public ListStationsUI(Scenario scenario) {
        this.controller = new ListStationsController();
        this.scenario = scenario;
    }

    private void listStations() {

        controller.setData(scenario);

        List<StationDTO> stations = controller.getStationsDTOList();

        if (stations.isEmpty()) {
            System.out.println("No stations found.");
            return;
        }

        System.out.println("\n\n------------------------- List of Stations -------------------------");
        for (int i = 0; i < stations.size(); i++) {
            StationDTO dto = stations.get(i);
            System.out.printf("%d. %s | ID: %d | Type: %s | Position: (%d,%d)%n",
                    i + 1, dto.getName(), dto.getStationID(), dto.getStationType(), dto.getX(), dto.getY());
        }

        System.out.print("\nSelect a station by number: ");
        int idx;
        try {
            idx = Integer.parseInt(sc.nextLine()) - 1;
            if (idx < 0 || idx >= stations.size()) {
                System.out.println("Invalid selection.");
                return;
            }
        } catch (NumberFormatException e) {
            System.out.println("Invalid input.");
            return;
        }

        StationDTO sel = stations.get(idx);

        System.out.println("\n--- Station Details ---");
        System.out.printf("Name: %s%n", sel.getName());
        System.out.printf("ID: %d%n",   sel.getStationID());
        System.out.printf("Type: %s%n", sel.getStationType());
        System.out.printf("Position: (%d,%d)%n", sel.getX(), sel.getY());
        System.out.println("Cargo stored: " + sel.getCargo());

        System.out.println("\nExisting buildings:");
        if (sel.getBuildings().isEmpty()) {
            System.out.println("No buildings found");
        } else {
            sel.getBuildings().forEach(b -> System.out.println("- " + b));
        }
    }

    @Override
    public void run() {
        listStations();
    }
}
