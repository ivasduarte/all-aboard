package repositories;

import domain.BuildingType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

class BuildingRepositoryTest {
    private BuildingRepository repository;

    @BeforeEach
    void setUp() {
        repository = new BuildingRepository();
    }

    @Test
    void testGetBuildingTypes() {
        List<BuildingType> buildingTypes = repository.getBuildingTypes();
        assertEquals(10, buildingTypes.size());
        assertTrue(buildingTypes.stream().anyMatch(bt -> bt.getName().equals("Telegraph")));
        assertTrue(buildingTypes.stream().anyMatch(bt -> bt.getName().equals("Telephone")));
        assertTrue(buildingTypes.stream().anyMatch(bt -> bt.getName().equals("Small Cafe")));
        assertTrue(buildingTypes.stream().anyMatch(bt -> bt.getName().equals("Large Cafe")));
        assertTrue(buildingTypes.stream().anyMatch(bt -> bt.getName().equals("Post Office")));
        assertTrue(buildingTypes.stream().anyMatch(bt -> bt.getName().equals("Small Hotel")));
        assertTrue(buildingTypes.stream().anyMatch(bt -> bt.getName().equals("Large Hotel")));
        assertTrue(buildingTypes.stream().anyMatch(bt -> bt.getName().equals("Silo")));
        assertTrue(buildingTypes.stream().anyMatch(bt -> bt.getName().equals("Liquid Storage")));
        assertTrue(buildingTypes.stream().anyMatch(bt -> bt.getName().equals("Customs")));
    }

    @Test
    void testGetBuildingTypeExisting() {
        BuildingType telegraph = repository.getBuildingType("Telegraph");
        BuildingType telephone = repository.getBuildingType("Telephone");
        BuildingType smallCafe = repository.getBuildingType("Small Cafe");

        assertNotNull(telegraph);
        assertEquals("Telegraph", telegraph.getName());
        assertEquals(50, telegraph.getCost());
        assertEquals("Improves communication", telegraph.getEffect());
        assertEquals(1850, telegraph.getAvailableYear());

        assertNotNull(telephone);
        assertEquals("Telephone", telephone.getName());
        assertEquals(80, telephone.getCost());
        assertEquals("Replaces Telegraph", telephone.getEffect());
        assertEquals(1920, telephone.getAvailableYear());

        assertNotNull(smallCafe);
        assertEquals("Small Cafe", smallCafe.getName());
        assertEquals(30, smallCafe.getCost());
        assertEquals("Slightly improves satisfaction", smallCafe.getEffect());
        assertEquals(1880, smallCafe.getAvailableYear());
    }

    @Test
    void testGetBuildingTypeCaseInsensitive() {
        BuildingType telegraph = repository.getBuildingType("telegraph");
        BuildingType telephone = repository.getBuildingType("TELEPHONE");
        BuildingType smallCafe = repository.getBuildingType("small CAFE");

        assertNotNull(telegraph);
        assertEquals("Telegraph", telegraph.getName());

        assertNotNull(telephone);
        assertEquals("Telephone", telephone.getName());

        assertNotNull(smallCafe);
        assertEquals("Small Cafe", smallCafe.getName());
    }

    @Test
    void testGetBuildingTypeNonExistent() {
        BuildingType nonExistent = repository.getBuildingType("Non Existent Building");
        assertNull(nonExistent);
    }

    @Test
    void testCopyFrom() {
        BuildingRepository sourceRepository = new BuildingRepository();
        BuildingRepository targetRepository = new BuildingRepository();
        targetRepository.copyFrom(sourceRepository);

        List<BuildingType> sourceTypes = sourceRepository.getBuildingTypes();
        List<BuildingType> targetTypes = targetRepository.getBuildingTypes();
        assertEquals(sourceTypes.size(), targetTypes.size());
        for (int i = 0; i < sourceTypes.size(); i++) {
            BuildingType sourceType = sourceTypes.get(i);
            BuildingType targetType = targetTypes.get(i);
            assertEquals(sourceType.getName(), targetType.getName());
            assertEquals(sourceType.getCost(), targetType.getCost());
            assertEquals(sourceType.getEffect(), targetType.getEffect());
            assertEquals(sourceType.getAvailableYear(), targetType.getAvailableYear());
        }
    }
} 