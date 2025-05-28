package presentation;

import controller.AddStationController;
import controller.DisplayMapController;
import controller.SimulatorController;
import domain.Budget;
import domain.Map;
import domain.Station;
import domain.Scenario;
import domain.UserSession;
import repositories.MapRepository;
import repositories.Repositories;

import java.util.Arrays;
import java.util.List;
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

        System.out.println("\n\nStation types:");
        for (int i = 0; i < Station.StationType.values().length; i++) {
            Station.StationType type = Station.StationType.values()[i];
            System.out.println((i + 1) + " | " + type.getDisplayName() + " | " + type.getPrice() + "€");
        }

        System.out.println("\nYour current budget is " + scenario.getBudget().getBudget() + "€");
        System.out.print("\nEnter the station type you want to add: ");
        String stationType = sc.nextLine();

        String stationRotation = "NE";

        if (stationType.equalsIgnoreCase("station")) {
            System.out.println("Enter the rotation for the station (NE, NW, SE, SW): ");
            stationRotation = sc.nextLine();
        }

        System.out.print("\nEnter the position X of the station: ");
        int positionX = sc.nextInt();

        System.out.print("Enter the position Y of the station: ");
        int positionY = sc.nextInt();

        String result = stationController.AddStation(map, scenario, positionX, positionY, stationType, stationRotation);

        System.out.println(result);
    }

    @Override
    public void run() {
        getInput();
    }
}