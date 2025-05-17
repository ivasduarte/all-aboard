package presentation;

import controller.DisplayMapController;
import controller.ListStationsController;
import domain.Building;
import domain.Map;
import domain.Station;
import java.util.List;
import java.util.Scanner;

public class ListStationsUI implements Runnable{

    Scanner sc = new Scanner(System.in);

    @Override
    public void run() {

        ListStationsController controller = new ListStationsController();

        DisplayMapController mapController = new DisplayMapController();

        List<Map> mapList = mapController.getMapList();

        for (int i = 0; i < mapList.size(); i++) {
            Map map = mapList.get(i);
            System.out.println("[" + map.getMapName() + " | " + map.getWidth() + "x" + map.getWidth() + "]");
        }

        System.out.print("Select a map from the list: ");
        String mapName = sc.nextLine();
        int mapID = -1;

        for (int i = 0; i < mapList.size(); i++) {
            if(mapList.get(i).getMapName().equalsIgnoreCase(mapName))
                mapID = i;
        }

        if(mapID == -1)
        {
            System.out.println("Map does not exist");
            return;
        }

        List<Station> stations = controller.getStationsByMapName(mapName);


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
}
