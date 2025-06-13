package repositories;

import domain.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class MapRepositoryTest {

    private MapRepository mapRepository;

    @BeforeEach
    void setUp() {
        mapRepository = new MapRepository();
    }

    @Test
    void add_validMap_addsToRepository() {
        Map map = new Map("TestMap", 10, 10, 1);
        mapRepository.add(map);

        assertTrue(mapRepository.existsByName("TestMap"));
        assertEquals(1, mapRepository.getMapList().size());
    }

    @Test
    void add_nullMap_throwsException() {
        assertThrows(IllegalArgumentException.class, () -> mapRepository.add(null));
    }

    @Test
    void add_duplicateMap_doesNotAddAgain() {
        Map map1 = new Map("Duplicate", 5, 5, 1);
        Map map2 = new Map("Duplicate", 8, 8, 2); // Mesmo nome, será considerado igual

        mapRepository.add(map1);
        mapRepository.add(map2);

        assertEquals(1, mapRepository.getMapList().size());
    }

    @Test
    void existsByName_caseInsensitiveMatch_returnsTrue() {
        Map map = new Map("Capital", 10, 10, 1);
        mapRepository.add(map);

        assertTrue(mapRepository.existsByName("capital"));
        assertTrue(mapRepository.existsByName("CAPITAL"));
    }

    @Test
    void existsByName_nullName_throwsException() {
        assertThrows(IllegalArgumentException.class, () -> mapRepository.existsByName(null));
    }

    @Test
    void existsByName_emptyName_returnsFalse() {
        assertFalse(mapRepository.existsByName(""));
    }

    @Test
    void existsByName_mapDoesNotExist_returnsFalse() {
        assertFalse(mapRepository.existsByName("Unknown"));
    }

    @Test
    void getMap_existingMap_returnsOptionalWithMap() {
        Map map = new Map("KnownMap", 10, 10, 1);
        mapRepository.add(map);

        Optional<Map> result = mapRepository.getMap("KnownMap");

        assertTrue(result.isPresent());
        assertEquals("KnownMap", result.get().getName());
    }

    @Test
    void getMap_caseInsensitiveMatch_returnsOptionalWithMap() {
        Map map = new Map("KnownMap", 10, 10, 1);
        mapRepository.add(map);

        Optional<Map> result = mapRepository.getMap("knownmap");

        assertTrue(result.isPresent());
        assertEquals("KnownMap", result.get().getName());
    }

    @Test
    void getMap_nullName_throwsException() {
        assertThrows(IllegalArgumentException.class, () -> mapRepository.getMap(null));
    }

    @Test
    void getMap_emptyName_returnsEmptyOptional() {
        Optional<Map> result = mapRepository.getMap("");
        assertFalse(result.isPresent());
    }

    @Test
    void getMap_mapDoesNotExist_returnsEmptyOptional() {
        Optional<Map> result = mapRepository.getMap("Missing");

        assertFalse(result.isPresent());
    }

    @Test
    void getMapList_returnsUnmodifiableList() {
        Map map = new Map("LockedMap", 10, 10, 1);
        mapRepository.add(map);

        List<Map> maps = mapRepository.getMapList();

        assertThrows(UnsupportedOperationException.class, () -> maps.add(new Map("Another", 5, 5, 1)));
    }

    @Test
    void getMapList_returnsCorrectSize() {
        Map map1 = new Map("Map1", 10, 10, 1);
        Map map2 = new Map("Map2", 20, 20, 2);
        Map map3 = new Map("Map3", 30, 30, 3);

        mapRepository.add(map1);
        mapRepository.add(map2);
        mapRepository.add(map3);

        List<Map> maps = mapRepository.getMapList();
        assertEquals(3, maps.size());
        assertTrue(maps.contains(map1));
        assertTrue(maps.contains(map2));
        assertTrue(maps.contains(map3));
    }

    @Test
    void getMapList_emptyRepository_returnsEmptyList() {
        List<Map> maps = mapRepository.getMapList();
        assertTrue(maps.isEmpty());
    }

    @Test
    void getMapList_returnsCopyOfInternalList() {
        Map map = new Map("TestMap", 10, 10, 1);
        mapRepository.add(map);

        List<Map> maps1 = mapRepository.getMapList();
        List<Map> maps2 = mapRepository.getMapList();

        assertNotSame(maps1, maps2);
        assertEquals(maps1, maps2);
    }
}