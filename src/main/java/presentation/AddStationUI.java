package presentation;

import controller.AddStationController;
import controller.DisplayMapController;
import domain.Map;
import domain.Station;
import repositories.MapRepository;

import java.util.List;
import java.util.Scanner;

public class AddStationUI implements Runnable {

    public void getInput()
    {
        Scanner sc = new Scanner(System.in);

        AddStationController stationController = new AddStationController();
        DisplayMapController mapController = new DisplayMapController();

        List<Map> mapList = mapController.getMapList();

        for (int i = 0; i < mapList.size(); i++) {
            Map map = mapList.get(i);
            System.out.println((i + 1) + ". " + map.getMapName());
            System.out.println();
        }

        System.out.println("Select a map from the list: ");
        int mapID = sc.nextInt() - 1;

        System.out.println("\nEnter the scenario ID: ");
        int scenarioID = sc.nextInt() - 1;

        System.out.println("\nEnter the position X of the station: ");
        int positionX = sc.nextInt();

        System.out.println("\nEnter the position Y of the station: ");
        int positionY = sc.nextInt();

        System.out.println("\nStation types:");
        for(int i = 0; i < Station.stationTypes.length; i++)
        {
            System.out.println((i+1) + " - " + Station.stationTypes[i]);
        }
        System.out.println("Enter the station type: ");
        String stationType = sc.nextLine();

        boolean result = stationController.AddStation(mapID, scenarioID, positionX, positionY, stationType);

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
