package presentation;

import controller.AddStationController;
import controller.DisplayMapController;
import controller.SimulatorController;
import domain.Budget;
import domain.Map;
import domain.Station;
import domain.UserSession;
import repositories.MapRepository;
import repositories.Repositories;

import java.util.List;
import java.util.Scanner;

public class AddStationUI implements Runnable {

    public void getInput() {

        Scanner sc = new Scanner(System.in);

        AddStationController stationController = new AddStationController();
        DisplayMapController mapController = new DisplayMapController();

        List<Map> mapList = mapController.getMapList();

        Budget playerBudget = UserSession.getInstance().getUser().getBudget();
        int budget = playerBudget.getBudget();

        if(mapList.isEmpty())
        {
            System.out.println("There are no maps");
            return;
        }

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

        System.out.print("\nEnter the position X of the station: ");
        int positionX = sc.nextInt();

        System.out.print("\nEnter the position Y of the station: ");
        int positionY = sc.nextInt();

        sc.nextLine();
        System.out.println("Station types:");
        for(int i = 0; i < Station.stationTypes.length; i++)
        {
            System.out.println(Station.stationTypes[i] + " | " + Station.typesPrices[i] + "€");
        }
        System.out.println("Your current budget is " + budget + "€");
        System.out.print("Enter the station type you want to add: ");
        String stationType = sc.nextLine();

        boolean typeExists = false;
        int stationTypeID = -1;
        for(int i = 0; i < Station.stationTypes.length; i++)
        {
            if(stationType.trim().equalsIgnoreCase(Station.stationTypes[i]))
            {
                typeExists = true;
                stationTypeID = i;
            }
        }

        if (!typeExists) {
            System.out.println("Station type not valid");
            return;
        }else if(budget < Station.typesPrices[stationTypeID])
        {
            System.out.println("User does not have sufficient budget!");
            return;
        }

        boolean result = stationController.AddStation(mapID, positionX, positionY, stationType);

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
