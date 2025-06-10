package repositories;

import controller.CreateCityController;
import domain.City;
import domain.HouseBlock;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class CityRepositoryTest {
    private CityRepository repo;
    private City city1;
    private City city2;

    @BeforeEach
    void setUp() {
        repo = new CityRepository();
        city1 = new City("CidadeA", "Mapa1", 1, 1);
        city2 = new City("CidadeB", "Mapa1", 2, 2);
        repo.save(city1);
        repo.save(city2);
    }

    @Test
    void testSaveSingleCity() {
        City city3 = new City("CidadeC", "Mapa1", 3, 3);
        repo.save(city3);
        assertEquals(3, repo.getCities().size());
        assertTrue(repo.getCities().contains(city3));
    }

    @Test
    void testSaveNullCityThrowsException() {
        assertThrows(IllegalArgumentException.class, () -> repo.save((City) null));
    }

    @Test
    void testSaveListOfCities() {
        List<City> newCities = List.of(
            new City("CidadeC", "Mapa1", 3, 3),
            new City("CidadeD", "Mapa1", 4, 4)
        );
        repo.save(newCities);
        assertEquals(4, repo.getCities().size());
        assertTrue(repo.getCities().containsAll(newCities));
    }

    @Test
    void testSaveNullListThrowsException() {
        assertThrows(IllegalArgumentException.class, () -> repo.save((List<City>) null));
    }

    @Test
    void testGetCitiesReturnsUnmodifiableList() {
        List<City> cities = repo.getCities();
        assertThrows(UnsupportedOperationException.class, () -> cities.add(new City("CidadeC", "Mapa1", 3, 3)));
    }

    @Test
    void testGetCitiesByMap() {
        List<City> citiesInMap1 = repo.getCitiesByMap("Mapa1");
        assertEquals(2, citiesInMap1.size());
        assertTrue(citiesInMap1.contains(city1));
        assertTrue(citiesInMap1.contains(city2));

        List<City> citiesInMap2 = repo.getCitiesByMap("Mapa2");
        assertTrue(citiesInMap2.isEmpty());
    }

    @Test
    void testGetCitiesByMapCaseInsensitive() {
        List<City> citiesInMap1 = repo.getCitiesByMap("mapa1");
        assertEquals(2, citiesInMap1.size());
        assertTrue(citiesInMap1.contains(city1));
        assertTrue(citiesInMap1.contains(city2));
    }

    @Test
    void testIsNameUnique() {
        assertTrue(repo.isNameUnique("CidadeC", "Mapa1"));
        assertFalse(repo.isNameUnique("CidadeA", "Mapa1"));
        assertTrue(repo.isNameUnique("CidadeA", "Mapa2"));
    }

    @Test
    void testIsNameUniqueCaseInsensitive() {
        assertFalse(repo.isNameUnique("cidadea", "Mapa1"));
        assertFalse(repo.isNameUnique("CIDADEA", "Mapa1"));
    }

    @Test
    void testExistsAtPosition() {
        assertTrue(repo.existsAtPosition("Mapa1", 1, 1));
        assertTrue(repo.existsAtPosition("Mapa1", 2, 2));
        assertFalse(repo.existsAtPosition("Mapa1", 3, 3));
        assertFalse(repo.existsAtPosition("Mapa2", 1, 1));
    }

    @Test
    void testIsBlockPositionUnique() {
        HouseBlock block1 = new HouseBlock(1, 1);
        HouseBlock block2 = new HouseBlock(3, 3);
        
        city1.addBlockManual(block1);
        
        assertFalse(repo.isBlockPositionUnique("Mapa1", block1));
        assertTrue(repo.isBlockPositionUnique("Mapa1", block2));
        assertTrue(repo.isBlockPositionUnique("Mapa2", block1));
    }

    @Test
    void testValidateBlock() {
        HouseBlock block = new HouseBlock(1, 1);
        assertEquals(CreateCityController.BlockError.OK, repo.validateBlock("Mapa1", block));
    }

    @Test
    void testValidateBlockWithNullBlockThrowsException() {
        assertThrows(IllegalArgumentException.class, () -> repo.validateBlock("Mapa1", null));
    }

    @Test
    void testValidateBlockWithNullMapNameThrowsException() {
        assertThrows(IllegalArgumentException.class, () -> repo.validateBlock(null, new HouseBlock(1, 1)));
    }
}