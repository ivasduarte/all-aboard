package domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class CityTest {
    private City city;

    @BeforeEach
    void setUp() {
        city = new City("Test City", "Test Map", 100, 100);
    }

    @Test
    void testCreateCity() {
        String name = "Test City";
        String mapName = "Test Map";
        int centerX = 100;
        int centerY = 100;
        
        City city = new City(name, mapName, centerX, centerY);
        
        assertNotNull(city);
        assertEquals(name, city.getName());
        assertEquals(mapName, city.getMapName());
        assertEquals(centerX, city.getCenterX());
        assertEquals(centerY, city.getCenterY());
        assertTrue(city.getBlocks().isEmpty());
    }
    
    @Test
    void testAddHouseBlock() {
        HouseBlock block = new HouseBlock(50, 50);
        city.addBlockManual(block);
        assertEquals(1, city.getBlocks().size());
        assertTrue(city.getBlocks().contains(block));
    }
    
    @Test
    void testClearBlocks() {
        HouseBlock block1 = new HouseBlock(50, 50);
        HouseBlock block2 = new HouseBlock(60, 60);
        city.addBlockManual(block1);
        city.addBlockManual(block2);
        
        city.clearBlocks();
        assertTrue(city.getBlocks().isEmpty());
    }
    
    @Test
    void testCreateCityWithInvalidParameters() {
        assertThrows(IllegalArgumentException.class, () -> new City(null, "Test Map", 100, 100));
        assertThrows(IllegalArgumentException.class, () -> new City("Test City", null, 100, 100));
        assertThrows(IllegalArgumentException.class, () -> new City("Test City", "Test Map", -1, 100));
        assertThrows(IllegalArgumentException.class, () -> new City("Test City", "Test Map", 100, -1));
    }
    
    @Test
    void testAddCityToMap() {
        Map map = new Map("TestMap", 1000, 1000, 1);
        map.addNewCity(city);
        assertTrue(map.getCities().contains(city));
        assertEquals(1, map.getCities().size());
    }
    
    @Test
    void testAddCityOutsideMapBounds() {
        Map map = new Map("TestMap", 1000, 1000, 1);
        City city = new City("TestCity", "TestMap", 2000, 2000);
        assertThrows(IllegalArgumentException.class, () -> map.addNewCity(city));
    }

    @Test
    void testGetBlocksReturnsUnmodifiableList() {
        List<HouseBlock> blocks = city.getBlocks();
        assertThrows(UnsupportedOperationException.class, () -> blocks.add(new HouseBlock(50, 50)));
    }

    @Test
    void testAddBlockOutsideCityBounds() {
        HouseBlock block = new HouseBlock(201, 100);
        assertThrows(IllegalArgumentException.class, () -> city.addBlockManual(block));
    }


    @Test
    void testAddNullBlock() {
        assertThrows(IllegalArgumentException.class, () -> city.addBlockManual(null));
    }

    @Test
    void testToString() {
        String expected = "Test City (100,100)";
        assertEquals(expected, city.toString());
    }
}