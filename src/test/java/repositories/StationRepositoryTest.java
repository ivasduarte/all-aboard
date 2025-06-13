package repositories;

import domain.Station;
import domain.Station.StationType;
import domain.Station.Orientation;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class StationRepositoryTest {

    private StationRepository repo;
    private Station station1;
    private Station station2;
    private Station station3;

    @BeforeEach
    void setUp() {
        repo = new StationRepository();

        // Parâmetros fictícios para construir uma estação válida
        station1 = new Station("Station 1", 10, 20, 5, StationType.DEPOT, Orientation.SW, "mapA", "extra1");
        station2 = new Station("Station 2", 30, 40, 5, StationType.STATION, Orientation.SE, "mapA", "extra2");
        station3 = new Station("Station 3", 50, 60, 5, StationType.TERMINAL, Orientation.NW, "mapB", "extra3");
    }

    @Test
    void testSaveSingleStation() {
        repo.save(station1);
        List<Station> stations = repo.getStations();
        assertEquals(1, stations.size());
        assertTrue(stations.contains(station1));
    }

    @Test
    void testSaveListOfStations() {
        List<Station> list = new ArrayList<>();
        list.add(station1);
        list.add(station2);

        repo.save(list);

        List<Station> stations = repo.getStations();
        assertEquals(2, stations.size());
        assertTrue(stations.contains(station1));
        assertTrue(stations.contains(station2));
    }

    @Test
    void testGetStationsEmpty() {
        List<Station> stations = repo.getStations();
        assertNotNull(stations);
        assertTrue(stations.isEmpty());
    }

    @Test
    void testCopyFrom() {
        repo.save(station1);

        StationRepository otherRepo = new StationRepository();
        otherRepo.save(station2);
        otherRepo.save(station3);

        repo.copyFrom(otherRepo);

        List<Station> stations = repo.getStations();
        assertEquals(2, stations.size());
        assertTrue(stations.contains(station2));
        assertTrue(stations.contains(station3));
        assertFalse(stations.contains(station1));
    }

    @Test
    void testDisplayAllStationsOutput() {
        repo.save(station1);
        repo.save(station2);
        repo.displayAllStations();
    }
}
