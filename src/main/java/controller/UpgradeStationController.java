package controller;

import domain.Station;

public class UpgradeStationController {

    public boolean UpgradeStation(Station station, String newType)
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

        if(!station.ValidatePosition(station.getMap(), station.getPositionX(), station.getPositionY(), size))
            return false;

        station.setStationType(newType);
        return true;
    }

}
