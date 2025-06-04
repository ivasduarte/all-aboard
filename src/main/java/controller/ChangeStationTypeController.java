package controller;

import domain.Station;

public class ChangeStationTypeController {

    /**
     * Change the type of the station into a higher one
     *
     * @param station the station which type is changing
     * @param typeID the new type for the station
     * @return true if the change was successful, false otherwise
     */
    public boolean ChangeStationType(Station station, int typeID) {
        station.setStationType(Station.StationType.values()[typeID]);
        return true;
    }
}
