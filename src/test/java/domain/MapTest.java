package domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
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
        testMap = new Map("TestMap", 100, 100);
        LocalDate startDate = LocalDate.of(1870, 1, 1);
        LocalDate endDate = LocalDate.of(1880, 12, 31);
        List<String> locomotiveTypes = Arrays.asList("steam", "diesel");
        List<String> historicalEvents = Collections.singletonList("Industrial Revolution");

        validRestrictions = new Restrictions(startDate, endDate, locomotiveTypes, historicalEvents);
        testMap = new Map("TestMap", 100, 100);
        testBudget = new Budget(10000);
    }

    @Test
    void testConstructorWithValidInput(){
        assertEquals("TestMap", testMap.getName());
        assertEquals(100, testMap.getWidth());
        assertEquals(100, testMap.getHeight());
        assertTrue(testMap.getScenarios().isEmpty());
    }

    @Test
    void testConstructorWithInvalidName() {
        assertThrows(IllegalArgumentException.class, () -> new Map("", 10, 10));
        assertThrows(IllegalArgumentException.class, () -> new Map(null, 10, 10));
        assertThrows(IllegalArgumentException.class, () -> new Map("   ", 10, 10));
    }

    @Test
    void testConstructorWithInvalidSize() {
        assertThrows(IllegalArgumentException.class, () -> new Map("Map", 0, 10));
        assertThrows(IllegalArgumentException.class, () -> new Map("Map", 10, -1));
        assertThrows(IllegalArgumentException.class, () -> new Map("Map", -1, 10));
        assertThrows(IllegalArgumentException.class, () -> new Map("Map", 10, 0));
    }

    @Test
    void testConstructorWithMaximumSize() {
        Map maxSizeMap = new Map("MaxMap", Integer.MAX_VALUE, Integer.MAX_VALUE);
        assertEquals(Integer.MAX_VALUE, maxSizeMap.getWidth());
        assertEquals(Integer.MAX_VALUE, maxSizeMap.getHeight());
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
}