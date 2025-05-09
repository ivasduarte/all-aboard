package presentation;

import controller.ListStationsController;
import domain.Building;
import domain.Station;
import java.util.List;

public class ListStationsUI implements Runnable{
    @Override
    public void run() {

        ListStationsController controller = new ListStationsController();


        List<Station> stations = controller.getAllStations();

        if(stations.isEmpty()){

            System.out.println("No stations found");

            return;
        }

        System.out.println("\n--- List of Stations ---");

        for(Station station : stations){
            System.out.println("Station ID:" + station.getStationID() + ", Type: " + station.getStationType() + ", Position: (" + station.getPositionX() +", "+station.getPositionY()+")");

        System.out.println(" Buildings : ");

        if(station.getBuildings().isEmpty()){

            System.out.println("No buildings found");

        } else {

            for(Building building : station.getBuildings()){

                System.out.println(building.getBuildingType());

            }
            System.out.println();
        }
        }
    }
}
