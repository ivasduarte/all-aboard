package controller;

import domain.Budget;
import domain.Map;
import domain.Station;
import domain.UserSession;
import repositories.Repositories;
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
        Budget playerBudget = UserSession.getInstance().getUser().getBudget();

        StationRepository stationRepository = Repositories.getInstance().getStationRepository();
        Station newStation = new Station(mapID, positionX, positionY, stationType);
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

        if(!newStation.ValidatePosition(stationMap, size))
            return false;

        if(!newStation.checkOverbuilding())
            return false;

        stationRepository.save(newStation);
        Station.IncreaseTotalStations();

        playerBudget.setBudget(playerBudget.getBudget() - Station.typesPrices[typeID]);
        return true;
    }
}
