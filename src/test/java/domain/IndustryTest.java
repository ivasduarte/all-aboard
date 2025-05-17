package domain;

import controller.DisplayMapController;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import repositories.CityRepository;
import repositories.MapRepository;

import static org.junit.jupiter.api.Assertions.*;

class IndustryTest {
    private MapRepository mapRepo = new MapRepository();

    private Industry industry;

    @BeforeEach
    void setUp() {
        Map map = new Map("TestMap", 100, 100);
        mapRepo.add(map);
        industry = new Industry(0, 0, 25, 75, "Mixed industry");
    }

    @Test
    void getMapID() {
        assertEquals(0, industry.getMapID());
    }

    @Test
    void getScenarioID() {
        assertEquals(0, industry.getScenarioID());
    }

    @Test
    void getPositionX() {
        assertEquals(25, industry.getPositionX());
    }

    @Test
    void getPositionY() {
        assertEquals(75, industry.getPositionY());
    }

    @Test
    void getIndustryType() {
        assertEquals("Mixed industry", industry.getIndustryType());
    }

    @Test
    void validatePosition_Valid() {
    }

    @Test
    void checkOverbuilding() {
    }
}