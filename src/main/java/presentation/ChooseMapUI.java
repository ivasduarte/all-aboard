package presentation;

import domain.Map;
import repositories.MapRepository;
import repositories.Repositories;
import ui.menu.MenuItem;
import ui.utils.Utils;

import java.util.ArrayList;
import java.util.List;

public class ChooseMapUI implements Runnable {
    MapRepository mapRepository = Repositories.getInstance().getMapRepository();
    List<Map> maps = mapRepository.getMapList();

    public ChooseMapUI(){}

    public void run(){
        Map selectedMap = getInput();
        List<MenuItem> options = new ArrayList<MenuItem>();
        options.add(new MenuItem("Create a Scenario", new CreateScenarioUI(selectedMap)));
        options.add(new MenuItem("Add a City", new AddCityUI(selectedMap)));
        options.add(new MenuItem("Add an Industry", new AddIndustryUI(selectedMap)));

        int option = 0;
        do {
            option = Utils.showAndSelectIndex(options, "\n\n------------------------- EDITOR MENU -------------------------");

            if ((option >= 0) && (option < options.size())) {
                options.get(option).run();
            }
        } while (option != -1);
    }

    public Map getInput(){
        System.out.println("\n\n------------------------ Choose Map ------------------------");
        for(int i = 0; i < maps.size(); i++){
            System.out.println((i+1) + ". " + maps.get(i).getName());
        }

        System.out.print("\nEnter the number of the map you want to play: ");
        int mapID = Integer.parseInt(System.console().readLine()) - 1;
        if(mapID < 0 || mapID >= maps.size()){
            System.out.println("Invalid map selection.");
            return null;
        }
        return maps.get(mapID);
    }
}
