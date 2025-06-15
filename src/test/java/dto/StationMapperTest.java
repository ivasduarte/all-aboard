package dto;

import domain.Building;
import domain.BuildingType;
import domain.Station;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class StationMapperTest {

    private static int stationIdCounter = 123;

    private Station createMockStation() {
        Station station = new Station(
                "MockMap",
                42,
                10,
                20,
                Station.StationType.STATION,
                Station.Orientation.SE,
                "Mock Station",
                "TestScenario"
        );

        // Add buildings
        BuildingType warehouseType = new BuildingType("Warehouse", 10000, "Stores cargo", 1850);
        BuildingType terminalType = new BuildingType("Terminal", 15000, "Handles passengers", 1840);
        station.addBuilding(new Building(warehouseType));
        station.addBuilding(new Building(terminalType));

        station.setCargo(300);

        return station;
    }

    private StationDTO createMockDTO() {
        return new StationDTO(
                123,
                "Mock Station",
                "STATION",  // must match Station.StationType.name()
                10,
                20,
                300,
                List.of("Warehouse", "Terminal"),
                "MockMap",
                42,
                "TestScenario",
                Station.Orientation.NE
        );
    }

    @Test
    void testToDTO_NullInput() {
        assertNull(StationMapper.toDTO((Station) null));
    }

    @Test
    void testToDTO_ValidStation() {
        Station station = createMockStation();
        StationDTO dto = StationMapper.toDTO(station);

        assertNotNull(dto);
        assertEquals("Mock Station", dto.getName());
        assertEquals("Station", dto.getStationType());
        assertEquals(10, dto.getX());
        assertEquals(20, dto.getY());
        assertEquals(300, dto.getCargo());
        assertEquals(List.of("Warehouse", "Terminal"), dto.getBuildings());
        assertEquals("MockMap", dto.getMapName());
        assertEquals(42, dto.getMapId());
        assertEquals("TestScenario", dto.getScenarioName());
        assertEquals(Station.Orientation.SE, dto.getOrientation());
    }

    @Test
    void testToDTO_ListConversion() {
        Station station1 = createMockStation();
        Station station2 = createMockStation();
        station2.setCargo(1000);
        station2.setName("Second Station");

        List<StationDTO> dtos = StationMapper.toDTO(List.of(station1, station2));

        assertEquals(2, dtos.size());
        assertEquals("Mock Station", dtos.get(0).getName());
        assertEquals("Second Station", dtos.get(1).getName());
        assertEquals(300, dtos.get(0).getCargo());
        assertEquals(1000, dtos.get(1).getCargo());
    }

    @Test
    void testToDomain_ValidDTO() {
        StationDTO dto = createMockDTO();
        Station station = StationMapper.toDomain(dto);

        assertNotNull(station);
        assertEquals("Mock Station", station.getName());
        assertEquals("MockMap", station.getMapName());
        assertEquals("TestScenario", station.getScenarioName());
        assertEquals(10, station.getPositionX());
        assertEquals(20, station.getPositionY());
        assertEquals(Station.StationType.STATION, station.getStationType());
        assertEquals(Station.Orientation.NE, station.getOrientation());
    }

    @Test
    void testToDomain_ListConversion() {
        StationDTO dto1 = createMockDTO();

        StationDTO dto2 = new StationDTO(
                456,
                "Second Station",
                "DEPOT", // Valid StationType
                30,
                40,
                900,
                List.of("Depot"),
                "SecondMap",
                99,
                "FallScenario",
                Station.Orientation.SW
        );

        List<Station> stations = StationMapper.toDomain(List.of(dto1, dto2));

        assertEquals(2, stations.size());
        assertEquals("Mock Station", stations.get(0).getName());
        assertEquals("Second Station", stations.get(1).getName());
        assertEquals("SecondMap", stations.get(1).getMapName());
        assertEquals(Station.Orientation.SW, stations.get(1).getOrientation());
        assertEquals(Station.StationType.DEPOT, stations.get(1).getStationType());
    }
}