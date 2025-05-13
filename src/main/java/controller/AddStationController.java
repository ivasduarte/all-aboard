package controller;

import domain.Map;
import domain.Station;
import repositories.Repositories;
import repositories.StationRepository;

public class AddStationController {

    public boolean AddStation(int mapID, int scenarioID, int positionX, int positionY, String stationType)
    {
        StationRepository stationRepository = Repositories.getInstance().getStationRepository();
        Station newStation = new Station(mapID, scenarioID, positionX, positionY, stationType);
        Map stationMap = newStation.getMap();

        boolean validStationType = false;
        int typeID = 0;
        for(int i = 0; i < Station.stationTypes.length; i++)
        {
            if(stationType.equalsIgnoreCase(Station.stationTypes[i]))
            {
                typeID = i;
                validStationType = true;
            }
        }

        if(!validStationType)
            return false;

        int size = Station.typesSizes[typeID] / 2;

        if(!newStation.ValidatePosition(stationMap, positionX, positionY, size))
            return false;


        stationRepository.save(newStation);
        return true;
    }
}
