package controller;

import domain.Station;

public class ChangeStationTypeController {

    /**
     * Change the type of the station into a higher one
     *
     * @param station the station which type is changing
     * @param newType the new type for the station
     * @return true if the change was successful, false otherwise
     */
    public boolean ChangeStationType(Station station, String newType) {
        station.setStationType(Station.StationType.valueOf(newType));
        return true;
    }
}
