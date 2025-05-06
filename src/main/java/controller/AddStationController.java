package controller;

import domain.Map;
import domain.Station;
import repositories.StationRepository;

public class AddStationController {

    public static String AddStation(int mapID, int scenarioID, int positionX, int positionY, String stationName, String stationType)
    {
        Station newStation = new Station(mapID, scenarioID, positionX, positionY, stationName, stationType);
        Map stationMap = newStation.getMap();

        if(!newStation.validatePosition(stationMap))
        {
            return "ERROR: Invalid position";
        }

        StationRepository.save(newStation);
        return "Station added successfully";
    }

}
