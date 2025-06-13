package domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class MapTest {
    private Map testMap;
    private Scenario validScenario;
    private Restrictions validRestrictions;
    private Budget testBudget;

    @BeforeEach
    void setUp() {
        testMap = new Map("TestMap", 100, 100, 10);
        LocalDate startDate = LocalDate.of(1870, 1, 1);
        LocalDate endDate = LocalDate.of(1880, 12, 31);
        List<String> locomotiveTypes = Arrays.asList("steam", "diesel");
        List<String> historicalEvents = Collections.singletonList("Industrial Revolution");
        List<PortBehavior> portBehaviors = new ArrayList<>();

        validRestrictions = new Restrictions(startDate, endDate, locomotiveTypes, historicalEvents, portBehaviors);
        testMap = new Map("TestMap", 100, 100, 10);
        testBudget = new Budget(10000);
    }

    @Test
    void testConstructorWithValidInput() {
        assertEquals("TestMap", testMap.getName());
        assertEquals(100, testMap.getWidth());
        assertEquals(100, testMap.getHeight());
        assertEquals(10, testMap.getCellSizeKm());
        assertTrue(testMap.getScenarios().isEmpty());
    }

    @Test
    void testConstructorWithInvalidName() {
        assertThrows(IllegalArgumentException.class, () -> new Map("", 10, 10, 1));
        assertThrows(IllegalArgumentException.class, () -> new Map(null, 10, 10, 1));
        assertThrows(IllegalArgumentException.class, () -> new Map("   ", 10, 10, 1));
    }

    @Test
    void testConstructorWithInvalidSize() {
        assertThrows(IllegalArgumentException.class, () -> new Map("Map", 0, 10, 1));
        assertThrows(IllegalArgumentException.class, () -> new Map("Map", 10, -1, 1));
        assertThrows(IllegalArgumentException.class, () -> new Map("Map", -1, 10, 1));
        assertThrows(IllegalArgumentException.class, () -> new Map("Map", 10, 0, 1));
    }

    @Test
    void testConstructorWithInvalidCellSize() {
        assertThrows(IllegalArgumentException.class, () -> new Map("Map", 10, 10, 0));
        assertThrows(IllegalArgumentException.class, () -> new Map("Map", 10, 10, -1));
    }

    @Test
    void testConstructorWithMaximumSize() {
        Map maxSizeMap = new Map("MaxMap", Integer.MAX_VALUE, Integer.MAX_VALUE, 1);
        assertEquals(Integer.MAX_VALUE, maxSizeMap.getWidth());
        assertEquals(Integer.MAX_VALUE, maxSizeMap.getHeight());
        assertEquals(1, maxSizeMap.getCellSizeKm());
    }

    @Test
    void testAddNewScenario() {
        Scenario scenario = new Scenario("testScenario", validRestrictions, testMap, testBudget);
        testMap.addNewScenario(scenario);
        assertEquals(1, testMap.getScenarios().size());
        assertTrue(testMap.getScenarios().contains(scenario));
    }

    @Test
    void testAddMultipleScenarios() {
        Scenario scenario1 = new Scenario("testScenario1", validRestrictions, testMap, testBudget);
        Scenario scenario2 = new Scenario("testScenario2", validRestrictions, testMap, testBudget);
        
        testMap.addNewScenario(scenario1);
        testMap.addNewScenario(scenario2);
        
        assertEquals(2, testMap.getScenarios().size());
        assertTrue(testMap.getScenarios().contains(scenario1));
        assertTrue(testMap.getScenarios().contains(scenario2));
    }

    @Test
    void testAddDuplicateScenario() {
        Scenario scenario = new Scenario("testScenario", validRestrictions, testMap, testBudget);
        testMap.addNewScenario(scenario);
        testMap.addNewScenario(scenario);
        assertEquals(1, testMap.getScenarios().size());
    }

    @Test
    void testAddNullScenario() {
        assertThrows(IllegalArgumentException.class, () -> testMap.addNewScenario(null));
    }

    @Test
    void testToString() {
        assertEquals("TestMap (100x100)", testMap.toString());
    }

    @Test
    void testGetScenariosReturnsUnmodifiableList() {
        List<Scenario> scenarios = testMap.getScenarios();
        assertThrows(UnsupportedOperationException.class, () -> scenarios.add(null));
    }

    @Test
    void testGetTotalWidthKm() {
        assertEquals(1000, testMap.getTotalWidthKm());
    }

    @Test
    void testGetTotalHeightKm() {
        assertEquals(1000, testMap.getTotalHeightKm());
    }

    @Test
    void testGetAreaKm() {
        assertEquals(1000000, testMap.getAreaKm());
    }

    @Test
    void testAddCity() {
        City city = new City("Test City", "TestMap", 50, 50);
        testMap.addNewCity(city);
        assertTrue(testMap.getCities().contains(city));
        assertEquals(1, testMap.getCities().size());
    }

    @Test
    void testAddCityOutsideBounds() {
        City city = new City("Test City", "TestMap", 200, 200);
        assertThrows(IllegalArgumentException.class, () -> testMap.addNewCity(city));
    }

    @Test
    void testAddNullCity() {
        assertThrows(IllegalArgumentException.class, () -> testMap.addNewCity(null));
    }

    @Test
    void testGetCitiesReturnsUnmodifiableList() {
        List<City> cities = testMap.getCities();
        assertThrows(UnsupportedOperationException.class, () -> cities.add(null));
    }
}