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
    void testExistsByNameTrue() {
        repository.add(map1);
        assertTrue(repository.existsByName("mapone"));
    }

    @Test
    void testExistsByNameFalse() {
        assertFalse(repository.existsByName("nonexistent"));
    }

    @Test
    void testGetMapList() {
        repository.add(map1);
        var mapList = repository.getMapList();
        assertEquals(1, mapList.size());
        assertThrows(UnsupportedOperationException.class, () -> mapList.add(map2));
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
}