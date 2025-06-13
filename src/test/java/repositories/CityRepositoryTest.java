package repositories;

import controller.CreateCityController;
import domain.City;
import domain.HouseBlock;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class CityRepositoryTest {

    private CityRepository repository;
    private City city1;
    private City city2;
    private City city3;

    @BeforeEach
    public void setUp() {
        repository = new CityRepository();
        city1 = new City("Coimbra", "MapA", 10, 15);
        city2 = new City("Lisboa", "MapA", 20, 25);
        city3 = new City("Porto", "MapB", 10, 15);
        repository.save(List.of(city1, city2, city3));
    }

    @Test
    public void testSaveAndGetCities() {
        List<City> allCities = repository.getCities();
        assertEquals(3, allCities.size());
        assertTrue(allCities.contains(city1));
        assertTrue(allCities.contains(city2));
        assertTrue(allCities.contains(city3));
    }

    @Test
    public void testGetCitiesByMap() {
        List<City> mapACities = repository.getCitiesByMap("MapA");
        assertEquals(2, mapACities.size());
        assertTrue(mapACities.contains(city1));
        assertTrue(mapACities.contains(city2));
    }

    @Test
    public void testIsNameUnique() {
        assertFalse(repository.isNameUnique("Lisboa", "MapA"));
        assertTrue(repository.isNameUnique("Guimarães", "MapB"));
    }

    @Test
    public void testExistsAtPosition() {
        assertTrue(repository.existsAtPosition("MapA", 10, 15));
        assertFalse(repository.existsAtPosition("MapA", 0, 0));
    }

    @Test
    public void testIsBlockPositionUniqueWhenEmpty() {
        HouseBlock block = new HouseBlock(5, 5);
        assertTrue(repository.isBlockPositionUnique("MapA", block));
    }

    @Test
    public void testIsBlockPositionNotUniqueWhenDuplicateBlockExists() {
        HouseBlock block = new HouseBlock(1, 1);
        city1.addBlockManual(block);

        assertFalse(repository.isBlockPositionUnique("MapA", new HouseBlock(1, 1)));
    }


    @Test
    public void testValidateBlockAlwaysReturnsOk() {
        HouseBlock block = new HouseBlock(0, 0);
        assertEquals(CreateCityController.BlockError.OK, repository.validateBlock("MapA", block));
    }
}