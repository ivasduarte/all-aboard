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

        System.out.print("\nEnter the position X of the station: ");
        int positionX = sc.nextInt();

        System.out.print("Enter the position Y of the station: ");
        int positionY = sc.nextInt();

        int budget = scenario.getBudget().getBudget();
        sc.nextLine();

        System.out.println("\n\nStation types:");
        for (int i = 0; i < Station.stationTypes.length; i++) {
            System.out.println(Station.stationTypes[i] + " | " + Station.typesPrices[i] + "€");
        }

        System.out.println("\nYour current budget is " + scenario.getBudget().getBudget() + "€");
        System.out.print("\nEnter the station type you want to add: ");
        String stationType = sc.nextLine();

        boolean typeExists = false;
        int stationTypeID = -1;
        for (int i = 0; i < Station.stationTypes.length; i++) {
            if (stationType.trim().equalsIgnoreCase(Station.stationTypes[i])) {
                typeExists = true;
                stationTypeID = i;
            }
        }

        if (!typeExists) {
            System.out.println("Station type not valid");
            return;
        } else if (budget < Station.typesPrices[stationTypeID]) {
            System.out.println("User does not have sufficient budget!");
            return;
        }

        boolean result = stationController.AddStation(map, scenario, positionX, positionY, stationType);

        if (!result) {
            System.out.println("Station not valid");
        } else {
            System.out.println("Station added successfully");
        }
    }

    @Override
    public void run() {
        getInput();
    }
}
