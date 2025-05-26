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
    }

    @Test
    void testConstructorWithInvalidSize() {
        assertThrows(IllegalArgumentException.class, () -> new Map("Map", 0, 10));
        assertThrows(IllegalArgumentException.class, () -> new Map("Map", 10, -1));
    }

    @Test
    void testAddNewScenario() {
        Scenario scenario = new Scenario("testScenario", validRestrictions, testMap, testBudget);
        testMap.addNewScenario(scenario);
        assertEquals(1, testMap.getScenarios().size());
        assertTrue(testMap.getScenarios().contains(scenario));
    }

    @Test
    void testToString() {
        assertEquals("TestMap (100x100)", testMap.toString());
    }
}