package repositories;

import domain.Station;

import java.util.ArrayList;
import java.util.List;

public class StationRepository {

    private static List<Station> stations;

    public StationRepository() { stations = new ArrayList<>(); }

    public static void save(Station station){ stations.add(station); }

    public static List<Station> getStations() { return stations; }


}
