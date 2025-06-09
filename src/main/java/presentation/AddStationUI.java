package presentation;

import controller.AddStationController;
import domain.Map;
import domain.Station;
import domain.Scenario;
import java.util.Scanner;

public class AddStationUI implements Runnable {
    private Map map;
    private Scenario scenario;

    public AddStationUI(Map map, Scenario scenario) {
        this.map = map;
        this.scenario = scenario;
    }

    public void getInput() {
        Scanner sc = new Scanner(System.in);

        AddStationController stationController = new AddStationController();

        System.out.println("\n\nSTATION TYPES:");
        for (int i = 0; i < Station.StationType.values().length; i++) {
            Station.StationType type = Station.StationType.values()[i];
            System.out.println((i + 1) + " | " + type.getDisplayName() + " | " + type.getPrice() + "€");
        }

        System.out.println("\n\nWARNING:");
        System.out.println("Current budget: " + scenario.getBudget().getBudget() + "€.");
        System.out.print("\nCHOOSE A STATION FROM LIST ABOVE: ");
        int stationType = sc.nextInt() - 1;

        String stationRotation = "NE";

        if (Station.StationType.values()[stationType].getSize() % 2 == 0) {
            System.out.print("\nINSERT STATION'S ORIENTATION (NE, NW, SE, SW): ");
            sc.nextLine();
            stationRotation = sc.nextLine();
        }

        System.out.print("\nSTATION'S X POSITION: ");
        int positionX = sc.nextInt();

        System.out.print("STATION'S Y POSITION: ");
        int positionY = sc.nextInt();

        String result = stationController.AddStation(map, scenario, positionX, positionY, stationType, stationRotation);

        System.out.println(result);
    }

    @Override
    public void run() {
        getInput();
    }
}