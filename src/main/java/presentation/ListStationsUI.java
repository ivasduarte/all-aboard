package presentation;

import controller.DisplayMapController;
import controller.ListStationsController;
import domain.Building;
import domain.Map;
import domain.Scenario;
import domain.Station;
import java.util.List;
import java.util.Scanner;

public class ListStationsUI implements Runnable{

    private Map map;

    public ListStationsUI(Map map)
    {
        this.map = map;
    }

    public void getInput() {
        ListStationsController controller = new ListStationsController();

        List<Station> stations = controller.getStationsByMapName(map.getMapName());

        if(stations.isEmpty()){
            System.out.println("No stations found");
            return;
        }

        System.out.println("\n\n------------------------- List of Stations -------------------------");

        for(Station station : stations){
            System.out.println("Station ID: " + station.getStationID() + " | Type: " + station.getStationType() + " | Position: (" + station.getPositionX() +", "+station.getPositionY()+")");
            System.out.println("Buildings: ");

            if(station.getBuildings().isEmpty()){
                System.out.println("No buildings found");
                System.out.println();
            } else {
                for(Building building : station.getBuildings()){
                    System.out.print(building.getBuildingType());
                    System.out.println();
                }
            }
        }
    }

    @Override
    public void run() {
        getInput();
    }
}
