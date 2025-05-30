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

    @BeforeEach
    void setUp() {
        repo = new CityRepository();
        repo.save(new City("A","Map1",1,1));
        repo.save(new City("B","Map1",2,2));
    }

    @Test
    void add_IncreasesSize() {
        repo.save(new City("C","Map1",3,3));
        assertEquals(3, repo.getCities().size());
    }

    @Test
    void getCities_Unmodifiable() {
        List<City> cities = repo.getCities();
        assertThrows(UnsupportedOperationException.class, () -> cities.add(null));
    }

    @Test
    void isNameUnique_WorksCorrectly() {
        assertTrue(repo.isNameUnique("C","Map1"));
        assertFalse(repo.isNameUnique("A","Map1"));
    }

    @Test
    void existsAtPosition_WorksCorrectly() {
        assertTrue(repo.existsAtPosition("Map1",1,1));
        assertFalse(repo.existsAtPosition("Map1",9,9));
    }

    @Test
    void isBlockPositionUnique_WorksCorrectly() {
        City city = new City("X","Map1",5,5);
        HouseBlock hb = new HouseBlock(7,7);
        city.addBlockManual(hb);
        repo.save(city);
        assertFalse(repo.isBlockPositionUnique("Map1", new HouseBlock(7,7)));
    }

    @Test
    void validateBlock_AlwaysOK() {
        assertEquals(CreateCityController.BlockError.OK,
                repo.validateBlock("Map1", new HouseBlock(0,0)));
    }
}