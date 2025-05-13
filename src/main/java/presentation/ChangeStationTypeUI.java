package presentation;

import controller.ChangeStationTypeController;
import controller.DisplayMapController;
import domain.Map;
import domain.Station;
import repositories.Repositories;
import repositories.StationRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class ChangeStationTypeUI implements Runnable {

    public void getInput()
    {
        Scanner sc = new Scanner(System.in);

        ChangeStationTypeController changeStationTypeController = new ChangeStationTypeController();
        StationRepository stationRepository = Repositories.getInstance().getStationRepository();

        stationRepository.displayAllStations();
        System.out.println("Choose one of the available stations: ");
        int stationID = sc.nextInt();

        if(stationID < 0 || stationID >= stationRepository.getStations().size())
        {
            System.out.println("Station does not exist!");
            return;
        }

        Station station = stationRepository.getStations().get(stationID);

        if(station.getStationTypeID() >= Station.stationTypes.length-1)
        {
            System.out.println("Station is already a " + station.getStationType() + ". Cannot be further upgraded");
            return;
        }

        System.out.println("Station type is: " + station.getStationType());
        System.out.println("Station can be upgraded into: ");

        List<String> availableStationTypes = new ArrayList<>();

        for(int i = 0; i < Station.stationTypes.length; i++)
        {
            if(i > station.getStationTypeID()) {
                System.out.println(Station.stationTypes[i]);
                availableStationTypes.add(Station.stationTypes[i]);
            }
        }

        System.out.println("Select the new station type: ");
        String newType = sc.nextLine();

        boolean validType = false;
        for(int i = 0; i < availableStationTypes.size(); i++)
        {
            if(availableStationTypes.get(i).equalsIgnoreCase(newType))
            {
                validType = true;
            }
        }

        if(!validType)
        {
            System.out.println("New type isn't valid!");
            return;
        }

        boolean result = changeStationTypeController.ChangeStationType(station, newType);

        if(!result)
            System.out.println("Station type change failed! Station does not fit!");
        else
            System.out.println("Station type change successful!");
    }

    @Override
    public void run()
    {
        getInput();
    }
}
