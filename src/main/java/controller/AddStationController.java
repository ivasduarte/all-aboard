package controller;

import domain.Map;
import domain.Station;
import repositories.StationRepository;

public class AddStationController {

    public boolean AddStation(int mapID, int scenarioID, int positionX, int positionY, String stationType)
    {
        Station newStation = new Station(mapID, scenarioID, positionX, positionY, stationType);
        Map stationMap = newStation.getMap();

        if(!newStation.validatePosition(stationMap))
        {
            return false;
        }

        StationRepository.save(newStation);
        return true;
    }

}
