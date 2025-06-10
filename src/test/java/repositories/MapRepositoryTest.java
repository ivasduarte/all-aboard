package repositories;

import domain.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class MapRepositoryTest {
    private MapRepository repository;
    private Map map1;
    private Map map2;

    @BeforeEach
    void setUp() {
        repository = new MapRepository();
        map1 = new Map("MapOne", 10, 10);
        map2 = new Map("MapTwo", 15, 15);
    }

    @Test
    void testAddNewMapSuccess() {
        repository.add(map1);
        assertTrue(repository.getMapList().contains(map1));
    }

    @Test
    void testAddDuplicateMapFails() {
        repository.add(map1);
        repository.add(map1); // Duplicate
        assertEquals(1, repository.getMapList().size());
    }

    @Test
    void testAddNullMapThrowsException() {
        assertThrows(IllegalArgumentException.class, () -> repository.add(null));
    }

    @Test
    void testExistsByNameTrue() {
        repository.add(map1);
        assertTrue(repository.existsByName("mapone"));
        assertTrue(repository.existsByName("MAPONE"));
        assertTrue(repository.existsByName("MapOne"));
    }

    @Test
    void testExistsByNameFalse() {
        assertFalse(repository.existsByName("nonexistent"));
    }

    @Test
    void testExistsByNameWithNullThrowsException() {
        assertThrows(IllegalArgumentException.class, () -> repository.existsByName(null));
    }

    @Test
    void testGetMapList() {
        repository.add(map1);
        var mapList = repository.getMapList();
        assertEquals(1, mapList.size());
        assertThrows(UnsupportedOperationException.class, () -> mapList.add(map2));
    }

    @Test
    void testGetMapListWithMultipleMaps() {
        repository.add(map1);
        repository.add(map2);
        var mapList = repository.getMapList();
        assertEquals(2, mapList.size());
        assertTrue(mapList.contains(map1));
        assertTrue(mapList.contains(map2));
    }

    @Test
    void testGetMap() {
        repository.add(map1);
        Optional<Map> result = repository.getMap("MapOne");
        assertTrue(result.isPresent());
        assertEquals(map1, result.get());
    }

    @Test
    void testGetMapNotExists() {
        Optional<Map> result = repository.getMap("NonExistent");
        assertTrue(result.isEmpty());
    }

    @Test
    void testGetMapWithNullThrowsException() {
        assertThrows(IllegalArgumentException.class, () -> repository.getMap(null));
    }

    @Test
    void testGetMapCaseInsensitive() {
        repository.add(map1);
        Optional<Map> result = repository.getMap("mapone");
        assertTrue(result.isPresent());
        assertEquals(map1, result.get());
    }
}