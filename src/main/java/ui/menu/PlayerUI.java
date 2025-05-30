package ui.menu;

import controller.SimulatorController;
import domain.*;
import presentation.*;

import repositories.MapRepository;
import repositories.Repositories;
import presentation.DisplayMapUI;
import ui.utils.Utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * The PlayerUI class represents the main user interface for a player
 * to interact with the game. It allows players to select a map, choose
 * a scenario, and access various game functionalities such as building
 * stations, upgrading stations, listing stations, purchasing locomotives,
 * and assigning routes to trains.
 * <p>
 * This class implements the {@code Runnable} interface,
 */
public class PlayerUI implements Runnable {
    /**
     * Constructs an instance of the PlayerUI class, initializing the main console-based interface
     * for player interactions with the game.
     * <p>
     * This constructor creates an object responsible for providing a structured and interactive
     * interface for players, aligning with the game's core functionalities.
     */
    public PlayerUI() {
    }

    /**
     * This method performs the following operations:
     * 1. Retrieves the list of available maps from the map repository. If no maps are available, a message is
     * shown and the method exits.
     * 2. Prompts the user to select a map based on its name. If the entered map name is invalid, the user will be
     * prompted repeatedly.
     * 3. Retrieves and displays the list of scenarios associated with the selected map. If no scenarios are available
     * for the chosen map, a message is shown and the method exits.
     * 4. Prompts the user to select a scenario. If the entered scenario name is invalid, the user will be prompted
     * repeatedly.
     * 5. Displays the current game details such as the selected map, scenario, time period, and the user's current
     * budget.
     * 6. Provides the user with a menu containing various management options, such as adding stations, upgrading
     * stations, listing stations, buying locomotives, and assigning trains to routes. Users can select these options
     * repeatedly until they choose to exit the menu.
     */
    public void run() {
        Scanner sc = new Scanner(System.in);

        MapRepository mapRepository = Repositories.getInstance().getMapRepository();
        List<Map> maps = mapRepository.getMapList();

        if (maps == null || maps.isEmpty()) {
            System.out.println("\n\nIt seems that there are no maps available! You must have at least 1 map to play!");
            return;
        }

        System.out.println("\n------------------------------------------------------------");
        System.out.println("\nWelcome!");


        System.out.println("\n\nChoose a map to play from the list below:");
        for (int i = 0; i < maps.size(); i++) {
            System.out.println((i + 1) + ". " + maps.get(i).getName() + " | " + maps.get(i).getWidth() + "x" + maps.get(i).getHeight());
        }

        Map currentMap = null;

        do {
            System.out.print("\nEnter the number of the map you wish to play: ");
            try{
                int mapChoice = Integer.parseInt(sc.nextLine());

                if (mapChoice <= maps.size() && mapChoice >= 1) {
                    currentMap = maps.get(mapChoice - 1);
                } else {
                    System.out.println("\nSelected map does not exist, try a new one!");
                }
            } catch(NumberFormatException e){
                System.out.println("\nPlease enter a valid number.");
            }
        } while (currentMap == null);

        List<Scenario> scenarios = currentMap.getScenarios();

        if (scenarios == null || scenarios.isEmpty()) {
            System.out.println("\nIt seems that there are no scenarios available! You must have at least 1 scenario to play!");
            return;
        }

        System.out.println("\nChoose a scenario for the map " + currentMap.getName() + " from the list below:");
        for (int i = 0; i < scenarios.size(); i++) {
            Restrictions restrictions = scenarios.get(i).getRestrictions();
            System.out.println((i + 1) + ". " + scenarios.get(i).getScenarioName() + " | From " + restrictions.getStartDate() + " to " + restrictions.getEndDate() + "");
        }

        Scenario currentScenario = null;
        do {
            System.out.print("\nEnter the number of the scenario you wish to play: ");
            try {
                int scenarioChoice = Integer.parseInt(sc.nextLine());

                if (scenarioChoice >= 1 && scenarioChoice <= scenarios.size()) {
                    currentScenario = scenarios.get(scenarioChoice - 1);
                } else {
                    System.out.println("\nInvalid number! Please choose a number between 1 and " + scenarios.size());
                }
            } catch (NumberFormatException e) {
                System.out.println("\nPlease enter a valid number!");
            }
        } while (currentScenario == null);

        Budget budget = currentScenario.getBudget();

        if (currentScenario.getCurrentYear() > currentScenario.getRestrictions().getEndDate().getYear()) {
            System.out.println("This scenario has ended!");
            return;
        }

        String mapSaveFilePath = currentMap.getName() + ".map";

        List<MenuItem> options = new ArrayList<>();
        options.add(new MenuItem("Build a Station", new AddStationUI(currentMap, currentScenario)));
        options.add(new MenuItem("Upgrade a Station", new UpgradeAStationUI(currentMap, currentScenario)));
        options.add(new MenuItem("Upgrade Type of Station", new ChangeStationTypeUI(currentMap, currentScenario)));
        options.add(new MenuItem("List All Stations", new ListStationsUI(currentMap)));
        options.add(new MenuItem("Buy a Locomotive", new BuyLocomotiveUI(currentMap, currentScenario)));
        options.add(new MenuItem("Assign a Train to a Route", new AssignRouteUI(currentMap, currentScenario)));
        options.add(new MenuItem("Play Simulation", new SimulatorController(currentMap, currentScenario)));
        options.add(new MenuItem("Check Financial Results", new ShowFinancialResultsUI(currentScenario)));
        options.add(new MenuItem("Save current Map", new MapSave(mapSaveFilePath, currentMap)));


        int option;
        do {
            System.out.println("\n------------------------- PLAYER INFORMATION -------------------------");
            System.out.println("\nCurrent map: " + currentMap.getName());
            System.out.println("Current scenario: " + currentScenario.getScenarioName());
            System.out.println("Time period: " + currentScenario.getRestrictions().getStartDate() + " to " + currentScenario.getRestrictions().getEndDate());
            System.out.println("The current year is " + currentScenario.getCurrentYear());
            System.out.println("Your current budget: " + budget.getBudget() + "€");

            option = Utils.showAndSelectIndex(options, "\n\n------------------------- PLAYER MENU -------------------------");

            if ((option >= 0) && (option < options.size())) {
                options.get(option).run();
            }
        } while (option != -1);
    }
}
