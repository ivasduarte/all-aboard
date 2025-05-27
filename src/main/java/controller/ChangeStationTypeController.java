package controller;

import domain.Station;

public class ChangeStationTypeController {

    /**
     * Change the type of the station into a higher one
     * @param station the station which type is changing
     * @param newType the new type for the station
     * @return true if the change was successful, false otherwise
     */
    public boolean ChangeStationType(Station station, String newType)
    {
        int typeID = 0;
        for(int i = 0; i < Station.stationTypes.length; i++)
        {
            if(newType.equalsIgnoreCase(Station.stationTypes[i]))
            {
                typeID = i;
            }
        }

        int size = Station.typesSizes[typeID] / 2;

        if(!station.validatePosition(station.getMap(), size))
            return false;

        station.setStationType(newType);
        return true;
    }

}
