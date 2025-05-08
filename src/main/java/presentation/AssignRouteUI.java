package presentation;

import controller.AssignRouteController;
import controller.DisplayMapController;
import domain.Locomotive;
import domain.Map;
import domain.Station;
import repositories.LocomotiveRepository;
import repositories.Repositories;
import repositories.StationRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class AssignRouteUI implements Runnable {

    @Override
    public void run()
    {
        getInput();
    }

    public void getInput()
    {
        Scanner sc = new Scanner(System.in);

        DisplayMapController mapController = new DisplayMapController();

        List<Map> mapList = mapController.getMapList();

        for (int i = 0; i < mapList.size(); i++) {
            Map map = mapList.get(i);
            System.out.println((i + 1) + ". " + map.getMapName());
            System.out.println();
        }

        System.out.println("\nEnter the scenario ID: ");
        int scenarioID = sc.nextInt() - 1;

        System.out.println("\nEnter the train's serial number: ");
        int serialNumber = sc.nextInt();

        int value;
        System.out.println("\nEnter the IDs of the stations you desire for the route. Enter a negative value when finished");
        List<Integer> stationIDs = new ArrayList<>();

        do {
            System.out.println("Enter a desired station: ");
            value = sc.nextInt();

            if(value >= 0)
            {
                boolean added = false;
                for(Integer id : stationIDs)
                {
                    if(value == id)
                    {
                        System.out.println("Station already in list!");
                        added = true;
                    }
                }

                if(!added)
                    stationIDs.add(value);
            }
        }while (value >= 0);

        LocomotiveRepository locomotiveRepository = Repositories.getInstance().getLocomotiveRepository();
        Locomotive locomotive = locomotiveRepository.getAvailableLocomotives(scenarioID).get(serialNumber);

        StationRepository stationRepository = Repositories.getInstance().getStationRepository();
        List<Station> stations = new ArrayList<>();

        for(Integer id : stationIDs)
        {
            Station station = stationRepository.getStations().get(id);
            stations.add(station);
        }

        AssignRouteController routeController = new AssignRouteController();
        routeController.AssignRoute(locomotive, stations);
    }
}
