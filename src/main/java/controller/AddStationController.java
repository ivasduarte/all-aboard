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
    public String AddStation(Map map, Scenario scenario, int positionX, int positionY, String stationType, String rotation)
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

        int typeID = Station.checkStationType(stationType);
        if(typeID == -1)
            return "Station type is not valid!";

        int size = Station.typesSizes[typeID] / 2;

        stationType = Station.stationTypes[typeID];

        int budget = scenario.getBudget().getBudget();

        if(Station.typesPrices[typeID] > budget)
            return "Not enough money to purchase the station!";

        Station newStation = new Station(mapID, positionX, positionY, stationType, rotation);

        if(!newStation.validatePosition(map, size))
            return "Station position is not valid!";

        if(!newStation.checkOverbuilding())
            return "Station is overbuilding other objects!";

        stationRepository.save(newStation);
        Station.increaseTotalStations();

        scenario.subtractBudget(Station.typesPrices[typeID]);
        return "Station added successfully!";
    }
}
