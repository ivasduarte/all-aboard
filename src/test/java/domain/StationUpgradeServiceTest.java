package domain;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class StationUpgradeServiceTest {

    @Test
    void testUpgradeStationSuccess() {
        Station station = new Station("Map", 1, 0, 0, Station.StationType.STATION, Station.Orientation.NE, "S", "Sc");
        BuildingType bt = new BuildingType("Large Hotel", 1000, "Increases passengers", 1900);
        Building b = new Building(bt);

        StationUpgradeService service = new StationUpgradeService();

        boolean result = service.upgradeStation(station, b, 1950);

        assertTrue(result);
        assertEquals(1, station.getBuildings().size());
        assertEquals("Large Hotel", station.getBuildings().get(0).getBuildingType());
    }


    @Test
    void constructorRejectsNull() {
        IllegalArgumentException ex =
                assertThrows(IllegalArgumentException.class,
                        () -> new Building(null));

        assertEquals("Invalid building type", ex.getMessage());
    }


    @Test
    void testUpgradeStationFailsDueToInvalidYear() {
        Station s = new Station("Map", 1, 0, 0, Station.StationType.STATION, Station.Orientation.NE, "S", "Sc");
        Building b = new Building(new BuildingType("Telephone", 500, "Communication", 1920));

        StationUpgradeService service = new StationUpgradeService();
        boolean result = service.upgradeStation(s, b, 1900);

        assertFalse(result);
        assertTrue(s.getBuildings().isEmpty());
    }


    @Test
    void testUpgradeStationFailsDueToMutualExclusivity() {
        Station s = new Station("Map", 1, 0, 0, Station.StationType.STATION, Station.Orientation.NE, "S", "Sc");
        s.addBuilding(new Building(new BuildingType("Small Hotel", 800, "Provides lodging", 1900)));

        Building b = new Building(new BuildingType("Large Hotel", 1500, "More lodging", 1900));

        StationUpgradeService service = new StationUpgradeService();
        boolean result = service.upgradeStation(s, b, 1950);

        assertFalse(result);
        assertEquals(1, s.getBuildings().size());
    }


    @Test
    void testUpgradeStationFailsDueToCafeConflict() {
        Station s = new Station("Map", 1, 0, 0, Station.StationType.STATION, Station.Orientation.NE, "S", "Sc");
        s.addBuilding(new Building(new BuildingType("Small Cafe", 600, "Provides food", 1900)));

        Building b = new Building(new BuildingType("Large Cafe", 700, "Fancier cafe", 1900));

        StationUpgradeService service = new StationUpgradeService();
        boolean result = service.upgradeStation(s, b, 1950);

        assertFalse(result);
        assertEquals(1, s.getBuildings().size());
    }


}