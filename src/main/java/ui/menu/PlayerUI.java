package ui.menu;

import presentation.*;

import ui.utils.Utils;

import java.util.ArrayList;
import java.util.List;

public class PlayerUI implements Runnable{

    public PlayerUI(){}

    public void run(){
        List<MenuItem> options = new ArrayList<MenuItem>();
        options.add(new MenuItem("Build a Station", new AddStationUI()));
        options.add(new MenuItem("Upgrade a Station", new UpgradeAStationUI()));
        options.add(new MenuItem("Check Station Details", new ListStationsUI()));
        options.add(new MenuItem("Buy a Locomotive", new BuyLocomotiveUI()));
        options.add(new MenuItem("Assign a Train to a Route", new AssignRouteUI()));

        int option = 0;
        do{
            option = Utils.showAndSelectIndex(options, "\n\n------------------------- PLAYER MENU -------------------------");

            if((option >= 0) && (option < options.size())){
                options.get(option).run();
            }
        }while(option != -1);
    }
}
