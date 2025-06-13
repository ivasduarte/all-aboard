package domain;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class StationTest {
    
    @Test
    void testCreateStation() {
        String mapName = "Test Map";
        int mapID = 1;
        int posX = 100;
        int posY = 100;
        Station.StationType stationType = Station.StationType.STATION;
        Station.Orientation orientation = Station.Orientation.NE;
        String name = "Test Station";
        String scenarioName = "Test Scenario";
        
        Station station = new Station(mapName, mapID, posX, posY, stationType, orientation, name, scenarioName);
        
        assertNotNull(station);
        assertEquals(name, station.getName());
        assertEquals(mapName, station.getMapName());
        assertEquals(posX, station.getPositionX());
        assertEquals(posY, station.getPositionY());
        assertEquals(stationType, station.getStationType());
        assertEquals(orientation, station.getOrientation());
        assertEquals(0, station.getCargo());
        assertEquals(1000, station.getMaxCargo());
        assertTrue(station.getBuildings().isEmpty());
    }
    
    @Test
    void testAddBuildingToStation() {
        Station station = new Station("Test Map", 1, 100, 100, 
            Station.StationType.STATION, Station.Orientation.NE, "Test Station", "Test Scenario");
        BuildingType buildingType = new BuildingType("PassengerTerminal", 1000, "Handles passengers", 1900);
        Building building = new Building(buildingType);
        
        boolean result = station.addBuilding(building);
        
        assertTrue(result);
        assertEquals(1, station.getBuildings().size());
        assertTrue(station.getBuildings().contains(building));
    }
    
    @Test
    void testUpdateStationCargo() {
        Station station = new Station("Test Map", 1, 100, 100, 
            Station.StationType.STATION, Station.Orientation.NE, "Test Station", "Test Scenario");
        int initialCargo = 100;
        int additionalCargo = 50;
        
        station.addCargo(initialCargo);
        station.addCargo(additionalCargo);
        
        assertEquals(initialCargo + additionalCargo, station.getCargo());
    }
    
    @Test
    void testUpdateStationDemand() {
        Station station = new Station("Test Map", 1, 100, 100, 
            Station.StationType.STATION, Station.Orientation.NE, "Test Station", "Test Scenario");
        int demand = 200;
        
        station.setDemand(demand);
        
        assertEquals(demand, station.getDemand());
    }
    
    @Test
    void testCreateStationWithInvalidParameters() {
        assertThrows(IllegalArgumentException.class, () -> {
            new Station(null, 1, 100, 100, 
                Station.StationType.STATION, Station.Orientation.NE, "Test Station", "Test Scenario");
        });
        
        assertThrows(IllegalArgumentException.class, () -> {
            new Station("Test Map", 1, -1, 100, 
                Station.StationType.STATION, Station.Orientation.NE, "Test Station", "Test Scenario");
        });
        
        assertThrows(IllegalArgumentException.class, () -> {
            new Station("Test Map", 1, 100, -1, 
                Station.StationType.STATION, Station.Orientation.NE, "Test Station", "Test Scenario");
        });
        
        assertThrows(IllegalArgumentException.class, () -> {
            new Station("Test Map", 1, 100, 100, 
                null, Station.Orientation.NE, "Test Station", "Test Scenario");
        });
        
        assertThrows(IllegalArgumentException.class, () -> {
            new Station("Test Map", 1, 100, 100, 
                Station.StationType.STATION, null, "Test Station", "Test Scenario");
        });
    }

    @Test
    void testAddNullBuilding() {
        Station station = new Station("Test Map", 1, 100, 100, 
            Station.StationType.STATION, Station.Orientation.NE, "Test Station", "Test Scenario");
        assertThrows(IllegalArgumentException.class, () -> station.addBuilding(null));
    }

    @Test
    void testAddCargoExceedsMaxCargo() {
        Station station = new Station("Test Map", 1, 100, 100, 
            Station.StationType.STATION, Station.Orientation.NE, "Test Station", "Test Scenario");
        assertThrows(IllegalArgumentException.class, () -> station.addCargo(2000));
    }

    @Test
    void testAddNegativeCargo() {
        Station station = new Station("Test Map", 1, 100, 100, 
            Station.StationType.STATION, Station.Orientation.NE, "Test Station", "Test Scenario");
        assertThrows(IllegalArgumentException.class, () -> station.addCargo(-100));
    }

    @Test
    void testSetNegativeDemand() {
        Station station = new Station("Test Map", 1, 100, 100, 
            Station.StationType.STATION, Station.Orientation.NE, "Test Station", "Test Scenario");
        assertThrows(IllegalArgumentException.class, () -> station.setDemand(-100));
    }

    @Test
    void testGetBuildingsReturnsUnmodifiableList() {
        Station station = new Station("Test Map", 1, 100, 100, 
            Station.StationType.STATION, Station.Orientation.NE, "Test Station", "Test Scenario");
        assertThrows(UnsupportedOperationException.class, () -> station.getBuildings().add(new Building(new BuildingType("Test", 1000, "Test", 1900))));
    }

    @Test
    void testToString() {
        Station station = new Station("Test Map", 1, 100, 100, 
            Station.StationType.STATION, Station.Orientation.NE, "Test Station", "Test Scenario");
        String expected = "Test Station (STATION) at (100,100)";
        assertEquals(expected, station.toString());
    }
} 