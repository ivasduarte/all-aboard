package ui.menu;

import domain.Map;
import domain.Scenario;
import presentation.*;

import repositories.MapRepository;
import repositories.Repositories;
import repositories.ScenarioRepository;
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

        if(maps.isEmpty())
        {
            System.out.println("\nIt seems that there are no maps available! You must have at least 1 map to play!");
            return;
        }

        System.out.println("\nWelcome! Choose a map to play from the list below!");
        for(Map map : maps)
        {
            System.out.println("["+ map.getMapName() + " | " + map.getWidth() + "x" + map.getHeight() + "]");
        }

        boolean validMap;
        Map currentMap = null;
        do {
            validMap = false;
            System.out.print("Enter the name of the map you wish to play: ");
            String mapName = sc.nextLine();

            for(Map map : maps)
            {
                if(map.getMapName().equalsIgnoreCase(mapName))
                {
                    validMap = true;
                    currentMap = map;
                }
            }

            if(!validMap)
                System.out.println("Selected map does not exist, try a new one!");
        }while (!validMap);



        //System.out.println("\nCurrent map: " + currentMap.getMapName() + ", " + currentMap.getWidth() + "x" + currentMap.getHeight());

        List<MenuItem> options = new ArrayList<>();
        options.add(new MenuItem("Build a Station", new AddStationUI()));
        options.add(new MenuItem("Upgrade a Station", new UpgradeAStationUI()));
        options.add(new MenuItem("List all Stations", new ListStationsUI()));
        options.add(new MenuItem("Buy a Locomotive", new BuyLocomotiveUI()));
        options.add(new MenuItem("Assign a Train to a Route", new AssignRouteUI()));

        int option;
        do{
            option = Utils.showAndSelectIndex(options, "\n\n------------------------- PLAYER MENU -------------------------");

            if((option >= 0) && (option < options.size())){
                options.get(option).run();
            }
        }while(option != -1);
    }
}
