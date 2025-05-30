package presentation;

import controller.AssignRouteController;
import controller.DisplayMapController;
import controller.ListStationsController;
import domain.Locomotive;
import domain.Map;
import domain.Scenario;
import domain.Station;
import repositories.LocomotiveRepository;
import repositories.Repositories;
import repositories.ScenarioRepository;
import repositories.StationRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class AssignRouteUI implements Runnable {

    private Map map;
    private Scenario scenario;

    public AssignRouteUI(Map map, Scenario scenario)
    {
        this.map = map;
        this.scenario = scenario;
    }

    @Override
    public void run()
    {
        getInput();
    }

    public void getInput()
    {
        Scanner sc = new Scanner(System.in);

        ScenarioRepository scenarioRepository = Repositories.getInstance().getScenarioRepository();
        List<Scenario> scenarios = scenarioRepository.getScenarioList();
        int scenarioID = -1;

        for(int i = 0; i < scenarios.size(); i++)
        {
            if(scenarios.get(i).equals(scenario))
                scenarioID = i;
        }

        LocomotiveRepository locomotiveRepository = Repositories.getInstance().getLocomotiveRepository();
        List<Locomotive> locomotives = locomotiveRepository.getPlayerLocomotives();

        if(locomotives.isEmpty())
        {
            System.out.println("No available locomotives!");
            return;
        }

        ListStationsController listStationsController = new ListStationsController();
        List<Station> stations = listStationsController.getStationsByMapName(map.getName());

        if(stations.size() < 2)
        {
            System.out.println("There's not enough stations to create a route!");
            return;
        }

        System.out.println("\n------------------------ Assign Route ------------------------");
        for(int i = 0; i < locomotives.size(); i++)
        {
            System.out.println((i+1) + ". " + locomotives.get(i).getLocomotiveModel() + " - " + locomotives.get(i).getLocomotiveFuelType());
        }
        System.out.println("Select the number of a train to assign the route:");
        int trainID = sc.nextInt();
        trainID -= 1;

        List<Integer> stationIDs = new ArrayList<>();

        int value;
        do {
            System.out.println();

            for(Station station : stations)
            {
                System.out.println(station.getStationID() + ". " + station.getStationType() + " (" + station.getPositionX() + "," + station.getPositionY() + ")");
            }

            System.out.print("Enter the ID of the station you wish to add (enter a negative number to exit): ");
            value = sc.nextInt();

            boolean exists = false;
            for(Station station : stations)
            {
                if(station.getStationID() == value)
                    exists = true;
            }

            if(value >= 0)
            {
                if(exists)
                {
                    boolean repeated = false;
                    for(Integer id : stationIDs)
                    {
                        if(id == value)
                            repeated = true;
                    }

                    if(!repeated)
                    {
                        stationIDs.add(value);
                        System.out.println("Added successfully!");
                    }else
                    {
                        System.out.println("Selected ID is already in list!");
                    }
                }else
                {
                    System.out.println("Selected ID does not exist!");
                }
            }

        }while(value >= 0);

        System.out.println("\nRoute assigned successfully!");

        Locomotive locomotive = locomotiveRepository.getAvailableLocomotives(scenarioID).get(trainID);

        List<Station> newStations = new ArrayList<>();

        for(Station station : stations)
        {
            if(stationIDs.contains(station.getStationID()))
                newStations.add(station);
        }

        AssignRouteController routeController = new AssignRouteController();
        routeController.AssignRoute(locomotive, newStations);
    }
}
