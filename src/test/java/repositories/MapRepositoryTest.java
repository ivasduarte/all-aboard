package repositories;

import domain.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import java.util.Optional;

public class MapRepositoryTest {
    private MapRepository repository;
    private Map map1;
    private Map map2;

    @BeforeEach
    public void setUp() {
        repository = new MapRepository();
        map1 = new Map("Map 1", 1000, 1000, 10);
        map2 = new Map("Map 2", 2000, 2000, 20);
    }

    @Test
    public void testAddMap() {
        // Act
        repository.add(map1);

        // Assert
        assertTrue(repository.existsByName("Map 1"));
        assertEquals(1, repository.getMapList().size());
    }

    @Test
    public void testAddDuplicateMap() {
        // Arrange
        repository.add(map1);
        Map duplicateMap = new Map("Map 1", 1000, 1000, 10);

        // Act
        repository.add(duplicateMap);

        // Assert
        assertEquals(1, repository.getMapList().size());
    }

    @Test
    public void testGetMapList() {
        // Arrange
        repository.add(map1);
        repository.add(map2);

        // Act
        List<Map> maps = repository.getMapList();

        // Assert
        assertEquals(2, maps.size());
        assertTrue(maps.contains(map1));
        assertTrue(maps.contains(map2));
    }

    @Test
    public void testGetMapByName() {
        // Arrange
        repository.add(map1);
        repository.add(map2);

        // Act
        Optional<Map> foundMap = repository.getMap("Map 1");

        // Assert
        assertTrue(foundMap.isPresent());
        assertEquals(map1, foundMap.get());
    }

    @Test
    public void testGetMapByNameNotFound() {
        // Arrange
        repository.add(map1);

        // Act
        Optional<Map> foundMap = repository.getMap("NonExistentMap");

        // Assert
        assertFalse(foundMap.isPresent());
    }

    @Test
    public void testExistsByName() {
        // Arrange
        repository.add(map1);

        // Act & Assert
        assertTrue(repository.existsByName("Map 1"));
        assertFalse(repository.existsByName("NonExistentMap"));
    }

    @Test
    public void testGetMapListIsUnmodifiable() {
        // Arrange
        repository.add(map1);
        List<Map> maps = repository.getMapList();

        // Act & Assert
        assertThrows(UnsupportedOperationException.class, () -> {
            maps.add(map2);
        });
    }
}