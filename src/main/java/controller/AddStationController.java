package controller;

import domain.*;
import repositories.MapRepository;
import repositories.Repositories;
import repositories.ScenarioRepository;
import repositories.StationRepository;

import java.util.List;

public class AddStationController {

    /**
     * Add a station in a map
     * @param map map of the station
     * @param scenario scenario in which the budget will be subtracted from
     * @param positionX station's X position
     * @param positionY station's Y position
     * @param stationType station type
     * @return return true if successfully added station, false otherwise
     */
    public boolean AddStation(Map map, Scenario scenario, int positionX, int positionY, String stationType, String rotation)
    {
        int mapID = -1;

        MapRepository mapRepository = Repositories.getInstance().getMapRepository();
        List<Map> maps = mapRepository.getMapList();

        for(int i = 0; i < maps.size(); i++)
        {
            if(maps.get(i).equals(map))
                mapID = i;
        }

        StationRepository stationRepository = Repositories.getInstance().getStationRepository();
        Station newStation = new Station(mapID, positionX, positionY, stationType, rotation);
        Map stationMap = newStation.getMap();

        if(scenario == null){
            System.out.println("No scenario found for the selected map.");
            return false;
        }

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

        if(!newStation.validatePosition(stationMap, size))
            return false;

        if(!newStation.checkOverbuilding())
            return false;

        stationRepository.save(newStation);
        Station.increaseTotalStations();

        scenario.subtractBudget(Station.typesPrices[typeID]);
        return true;
    }
}
