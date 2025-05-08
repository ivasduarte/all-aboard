package repositories;

import domain.Station;

import java.util.ArrayList;
import java.util.List;

public class StationRepository {

    private List<Station> stations;

    public StationRepository() { stations = new ArrayList<>(); }

    public void save(Station station){ stations.add(station); }

    public List<Station> getStations() { return stations; }

    public void displayAllStations(){
        for (int i = 0; i < stations.size(); i++) {
            System.out.println(stations.get(i));
        }
    }

    public List<Station> getAvailableStations() {
        List<Station> available = new ArrayList<>();
        for (Station station : stations) {
            if (station.isAvailable()) {
                available.add(station);
            }
        }
        return available;
    }




}
