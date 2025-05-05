package repositories;

import domain.Station;

import java.util.ArrayList;
import java.util.List;

public class StationRepository {

    private List<Station> stations;

    public StationRepository() { this.stations = new ArrayList<>(); }

    public void save(Station station){ stations.add(station); }

    public List<Station> getStations() { return stations; }


}
