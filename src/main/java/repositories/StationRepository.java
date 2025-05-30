package repositories;

import domain.Station;

import java.util.ArrayList;
import java.util.List;

public class StationRepository {

    private List<Station> stations;

    public StationRepository() { stations = new ArrayList<>(); }

    /**
     * Saves a new station
     * @param station new station
     */
    public void save(Station station){ stations.add(station); }

    /**
     * Saves a new list of stations
     * @param stations new stations
     */
    public void save(List<Station> stations){
        for(Station station : stations)
            save(station);
    }

    /**
     * Get all the stations
     * @return all stations
     */
    public List<Station> getStations() { return stations; }

    /**
     * Show all the stations
     */
    public void displayAllStations(){
        for (int i = 0; i < stations.size(); i++) {
            System.out.println(stations.get(i));
        }
    }

    /**
     * Get all the stations with available building slots
     * @return all stations with available building slots
     */
    public List<Station> getAvailableStationsByMapName(String mapName) {
        List<Station> result = new ArrayList<>();
        for (Station station : stations) {
            if (station.getMap().getName().equalsIgnoreCase(mapName)) {
                result.add(station);
            }
        }
        return result;
    }

    public void copyFrom(StationRepository st2){
        this.stations.clear();
        this.stations.addAll(st2.getStations());
    }
}
