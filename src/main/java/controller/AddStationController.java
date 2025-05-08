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

        if(!newStation.validatePosition(stationMap))
        {
            return false;
        }

        stationRepository.save(newStation);
        return true;
    }

}
