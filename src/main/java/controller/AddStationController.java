package controller;

import domain.*;
import repositories.Repositories;
import repositories.ScenarioRepository;
import repositories.StationRepository;

public class AddStationController {

    /**
     * Add a station in a map
     * @param mapID id of the map
     * @param positionX station's X position
     * @param positionY station's Y position
     * @param stationType station type
     * @return return true if successfully added station, false otherwise
     */
    public boolean AddStation(int mapID, int positionX, int positionY, String stationType)
    {
        StationRepository stationRepository = Repositories.getInstance().getStationRepository();
        Station newStation = new Station(mapID, positionX, positionY, stationType);
        Map stationMap = newStation.getMap();

        Scenario scenario = getScenarioByMapID(mapID);

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

        if(!newStation.ValidatePosition(stationMap, size))
            return false;

        if(!newStation.checkOverbuilding())
            return false;

        stationRepository.save(newStation);
        Station.IncreaseTotalStations();

        scenario.subtractBudget(Station.typesPrices[typeID]);
        return true;
    }

    public Scenario getScenarioByMapID(int mapID) {
        ScenarioRepository scenarioRepository = Repositories.getInstance().getScenarioRepository();
        Map targetMap = Repositories.getInstance().getMapRepository().getMapList().get(mapID);

        for (Scenario scenario : scenarioRepository.getScenarioList()) {
            Map scenarioMap = scenario.getMap();
            if (scenarioMap != null && scenarioMap.equals(targetMap)) {
                return scenario;
            }
        }
        return null;
    }


}
