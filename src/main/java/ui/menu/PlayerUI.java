package ui.menu;

import controller.DisplayMapController;
import controller.SimulatorController;
import domain.Budget;
import domain.Map;
import domain.Restrictions;
import domain.Scenario;
import presentation.*;

import repositories.MapRepository;
import repositories.Repositories;
import repositories.ScenarioRepository;
import ui.utils.DisplayMapUI;
import ui.utils.Utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class PlayerUI implements Runnable{

    public PlayerUI(){}

    public void run(){
        Scanner sc = new Scanner(System.in);

        MapRepository mapRepository = Repositories.getInstance().getMapRepository();
        List<Map> maps = mapRepository.getMapList();

        if(maps == null || maps.isEmpty())
        {
            System.out.println("\nIt seems that there are no maps available! You must have at least 1 map to play!");
            return;
        }

        System.out.println("\nWelcome! Choose a map to play from the list below!");
        for(Map map : maps)
        {
            System.out.println("["+ map.getMapName() + " | " + map.getWidth() + "x" + map.getHeight() + "]");
        }

        Map currentMap = null;
        do {
            System.out.print("Enter the name of the map you wish to play: ");
            String mapName = sc.nextLine();

            for(Map map : maps)
            {
                if(map.getMapName().equalsIgnoreCase(mapName))
                {
                    currentMap = map;
                }
            }

            if(currentMap == null)
                System.out.println("Selected map does not exist, try a new one!");
        }while (currentMap == null);

        List<Scenario> scenarios = currentMap.getScenarios();

        if(scenarios == null || scenarios.isEmpty())
        {
            System.out.println("\nIt seems that there are no scenarios available! You must have at least 1 scenario to play!");
            return;
        }

        System.out.println("\nChoose a scenario for the map " + currentMap.getMapName());
        for(Scenario scenario : scenarios)
        {
            Restrictions restrictions = scenario.getRestrictions();
            System.out.println("[" + scenario.getScenarioName() + " | From " + restrictions.getStartDate() + " to " + restrictions.getEndDate() + "]");
        }

        Scenario currentScenario = null;
        do {
            System.out.print("Enter the name of the scenario you wish to play: ");
            String scenarioName = sc.nextLine();

            for(Scenario scenario : scenarios)
            {
                if(scenario.getScenarioName().equalsIgnoreCase(scenarioName))
                {
                    currentScenario = scenario;
                }
            }

            if(currentScenario == null)
                System.out.println("Selected scenario does not exist, try a new one!");
        }while (currentScenario == null);

        Budget budget = currentScenario.getBudget();

        List<MenuItem> options = new ArrayList<>();
        options.add(new MenuItem("Build a Station", new AddStationUI(currentMap, currentScenario)));
        options.add(new MenuItem("Upgrade a Station", new UpgradeAStationUI(currentMap, currentScenario)));
        options.add(new MenuItem("List all Stations", new ListStationsUI(currentMap)));
        options.add(new MenuItem("Buy a Locomotive", new BuyLocomotiveUI(currentMap, currentScenario)));
        options.add(new MenuItem("Assign a Train to a Route", new AssignRouteUI(currentMap, currentScenario)));
        options.add(new MenuItem("Start a simulation", new SimulatorController(currentMap, currentScenario)));


        int option;
        do{
            System.out.println("\nCurrent map: " + currentMap.getMapName());
            System.out.println("Current scenario: " + currentScenario.getScenarioName());
            System.out.println("Time period: " + currentScenario.getRestrictions().getStartDate() + " to " + currentScenario.getRestrictions().getEndDate());
            System.out.println("Your current budget: " + budget.getBudget() + "€");

            option = Utils.showAndSelectIndex(options, "\n\n------------------------- PLAYER MENU -------------------------");

            if((option >= 0) && (option < options.size())){
                options.get(option).run();
            }
        }while(option != -1);
    }
}
