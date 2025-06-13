package domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.util.ArrayList;
import java.util.List;

class TrainTest {
    private Train train;
    private Locomotive locomotive;

    @BeforeEach
    void setUp() {
        locomotive = new Locomotive("Test Locomotive", "Diesel", 2000, 2030, 1000000, 50000, 30000, 100);
        train = new Train(locomotive, 2023);
    }

    @Test
    void testSetNumberOfCarriages_Increase() {
        assertEquals(0, train.getNumberOfCarriages());
        train.setNumberOfCarriages(5);
        assertEquals(5, train.getNumberOfCarriages());
    }

    @Test
    void testSetNumberOfCarriages_Decrease() {
        train.setNumberOfCarriages(10);
        assertEquals(10, train.getNumberOfCarriages());
        train.setNumberOfCarriages(3);
        assertEquals(3, train.getNumberOfCarriages());
    }

    @Test
    void testSetNumberOfCarriages_SameNumber() {
        train.setNumberOfCarriages(5);
        assertEquals(5, train.getNumberOfCarriages());
        train.setNumberOfCarriages(5);
        assertEquals(5, train.getNumberOfCarriages());
    }

    @Test
    void testSetNumberOfCarriages_NegativeNumber() {
        assertThrows(IllegalArgumentException.class, () -> train.setNumberOfCarriages(-1));
    }

    @Test
    void testCreateTrain() {
        Locomotive locomotive = new Locomotive("Test Locomotive", "STEAM", 1900, 1950, 10000, 1000, 500, 100);
        int departureYear = 1900;
        
        Train train = new Train(locomotive, departureYear);
        
        assertNotNull(train);
        assertEquals(locomotive, train.getLocomotive());
        assertTrue(train.getRoute().isEmpty());
        assertTrue(train.getCarriages().isEmpty());
    }
    
    @Test
    void testAssignRouteToTrain() {
        Locomotive locomotive = new Locomotive("Test Locomotive", "STEAM", 1900, 1950, 10000, 1000, 500, 100);
        Train train = new Train(locomotive, 1900);
        
        Station station1 = new Station("Test Map", 1, 100, 100, 
            Station.StationType.STATION, Station.Orientation.NE, "Station 1", "Test Scenario");
        Station station2 = new Station("Test Map", 1, 200, 200, 
            Station.StationType.STATION, Station.Orientation.NE, "Station 2", "Test Scenario");
        
        List<Station> stations = new ArrayList<>();
        stations.add(station1);
        stations.add(station2);
        
        train.setRouteWithStations(stations);
        
        assertEquals(2, train.getRoute().size());
        assertEquals(station1, train.getRoute().get(0).getStation());
        assertEquals(station2, train.getRoute().get(1).getStation());
    }
    
    @Test
    void testAddCarriageToTrain() {
        Locomotive locomotive = new Locomotive("Test Locomotive", "STEAM", 1900, 1950, 10000, 1000, 500, 100);
        Train train = new Train(locomotive, 1900);
        Cargo cargo = new Cargo(Cargo.Type.PASSENGERS);
        Carriage carriage = new Carriage(cargo, 100, 0);
        
        train.getCarriages().add(carriage);
        
        assertEquals(1, train.getNumberOfCarriages());
        assertTrue(train.getCarriages().contains(carriage));
    }
    
    @Test
    void testCalculateRouteDistance() {
        Locomotive locomotive = new Locomotive("Test Locomotive", "STEAM", 1900, 1950, 10000, 1000, 500, 100);
        Train train = new Train(locomotive, 1900);
        
        Station station1 = new Station("Test Map", 1, 0, 0, 
            Station.StationType.STATION, Station.Orientation.NE, "Station 1", "Test Scenario");
        Station station2 = new Station("Test Map", 1, 100, 100, 
            Station.StationType.STATION, Station.Orientation.NE, "Station 2", "Test Scenario");
        
        List<Station> stations = new ArrayList<>();
        stations.add(station1);
        stations.add(station2);
        
        train.setRouteWithStations(stations);
        double distance = train.calculateRouteDistance();
        
        assertTrue(distance > 0);
        assertEquals(141.42, distance, 0.01);
    }

    @Test
    void testGetLocomotive() {
        assertEquals(locomotive, train.getLocomotive());
    }

    @Test
    void testGetRoute() {
        assertTrue(train.getRoute().isEmpty());
        Station station = new Station("Test Map", 1, 100, 100, 
            Station.StationType.STATION, Station.Orientation.NE, "Station 1", "Test Scenario");
        List<Station> stations = new ArrayList<>();
        stations.add(station);
        train.setRouteWithStations(stations);
        assertEquals(1, train.getRoute().size());
    }

    @Test
    void testGetCarriages() {
        assertTrue(train.getCarriages().isEmpty());
        Cargo cargo = new Cargo(Cargo.Type.PASSENGERS);
        Carriage carriage = new Carriage(cargo, 100, 0);
        train.getCarriages().add(carriage);
        assertEquals(1, train.getCarriages().size());
    }
}
