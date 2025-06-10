package repositories;

import domain.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

public class ScenarioRepositoryTest {
    private ScenarioRepository repo;
    private Scenario testScenario1;
    private Scenario testScenario2;
    private Map testMap;

    @BeforeEach
    void setUp() {
        repo = new ScenarioRepository();
        testMap = new Map("TestMap", 100, 100);

        LocalDate startDate1 = LocalDate.of(1870, 1, 1);
        LocalDate endDate1 = LocalDate.of(1880, 12, 31);
        List<String> locomotiveTypes1 = Collections.singletonList("steam");
        List<String> historicalEvents1 = Collections.singletonList("none");
        Restrictions restrictions1 = new Restrictions(startDate1, endDate1, locomotiveTypes1, historicalEvents1);
        Budget budget1 = new Budget(10000);
        testScenario1 = new Scenario("TestScenario1", restrictions1, testMap, budget1);

        // Create second scenario
        LocalDate startDate2 = LocalDate.of(1970, 1, 1);
        LocalDate endDate2 = LocalDate.of(1980, 12, 31);
        List<String> locomotiveTypes2 = Arrays.asList("steam", "diesel", "electric");
        List<String> historicalEvents2 = Collections.singletonList("school program");
        Restrictions restrictions2 = new Restrictions(startDate2, endDate2, locomotiveTypes2, historicalEvents2);
        Budget budget2 = new Budget(20000);
        testScenario2 = new Scenario("TestScenario2", restrictions2, testMap, budget2);

        repo.add(testScenario1);
        repo.add(testScenario2);
    }

    @Test
    void testAdd() {
        LocalDate startDate = LocalDate.of(1855, 1, 1);
        LocalDate endDate = LocalDate.of(1867, 12, 31);
        List<String> locomotiveTypes = Collections.singletonList("steam");
        List<String> historicalEvents = Collections.singletonList("none");
        Restrictions restrictions = new Restrictions(startDate, endDate, locomotiveTypes, historicalEvents);
        Budget budget = new Budget(15000);
        Scenario newScenario = new Scenario("TestScenario3", restrictions, testMap, budget);

        repo.add(newScenario);

        assertEquals(3, repo.getScenarioList().size());
        assertTrue(repo.getScenarioList().contains(newScenario));
    }

    @Test
    void testAddNullScenarioThrowsException() {
        assertThrows(IllegalArgumentException.class, () -> repo.add(null));
    }

    @Test
    void testAddDuplicateScenario() {
        LocalDate startDate = LocalDate.of(1870, 1, 1);
        LocalDate endDate = LocalDate.of(1880, 12, 31);
        List<String> locomotiveTypes = Collections.singletonList("steam");
        List<String> historicalEvents = Collections.singletonList("none");
        Restrictions restrictions = new Restrictions(startDate, endDate, locomotiveTypes, historicalEvents);
        Budget budget = new Budget(10000);
        Scenario duplicateScenario = new Scenario("TestScenario1", restrictions, testMap, budget);

        int initialSize = repo.getScenarioList().size();
        repo.add(duplicateScenario);

        assertEquals(initialSize, repo.getScenarioList().size());
    }

    @Test
    void testExistsByName() {
        assertTrue(repo.existsByName("TestScenario1"));
        assertTrue(repo.existsByName("testscenario1")); // Should be case insensitive
        assertFalse(repo.existsByName("NonExistentScenario"));
    }

    @Test
    void testExistsByNameWithNullThrowsException() {
        assertThrows(IllegalArgumentException.class, () -> repo.existsByName(null));
    }

    @Test
    void testGetScenarioList() {
        List<Scenario> scenarios = repo.getScenarioList();
        assertEquals(2, scenarios.size());
        assertTrue(scenarios.contains(testScenario1));
        assertTrue(scenarios.contains(testScenario2));

        assertThrows(UnsupportedOperationException.class, () -> scenarios.add(null));
    }

    @Test
    void testGetScenarioListByMap() {
        List<Scenario> scenarios = repo.getScenarioListByMap(testMap);
        assertEquals(2, scenarios.size());
        assertTrue(scenarios.contains(testScenario1));
        assertTrue(scenarios.contains(testScenario2));

        Map otherMap = new Map("OtherMap", 100, 100);
        List<Scenario> otherScenarios = repo.getScenarioListByMap(otherMap);
        assertTrue(otherScenarios.isEmpty());
    }

    @Test
    void testGetScenarios() {
        Optional<Scenario> foundScenario = repo.getScenarios("TestScenario1");
        assertTrue(foundScenario.isPresent());
        assertEquals("TestScenario1", foundScenario.get().getScenarioName());

        Optional<Scenario> notFoundScenario = repo.getScenarios("NonExistentScenario");
        assertFalse(notFoundScenario.isPresent());
    }

    @Test
    void testGetScenariosWithNullThrowsException() {
        assertThrows(IllegalArgumentException.class, () -> repo.getScenarios(null));
    }

    @Test
    void testGetScenariosWithExactNameMatch() {
        Optional<Scenario> foundScenario = repo.getScenarios("TestScenario1");
        assertTrue(foundScenario.isPresent());

        Optional<Scenario> notFoundScenario = repo.getScenarios("testscenario1");
        assertFalse(notFoundScenario.isPresent());
    }

    @Test
    void testGetScenarioByName() {
        Scenario foundScenario = repo.getScenarioByName("TestScenario1");
        assertNotNull(foundScenario);
        assertEquals("TestScenario1", foundScenario.getScenarioName());

        Scenario notFoundScenario = repo.getScenarioByName("NonExistentScenario");
        assertNull(notFoundScenario);
    }

    @Test
    void testGetScenarioByNameCaseInsensitive() {
        Scenario foundScenario = repo.getScenarioByName("testscenario1");
        assertNotNull(foundScenario);
        assertEquals("TestScenario1", foundScenario.getScenarioName());
    }

    @Test
    void testSaveScenarioToFile() {
        String fileName = "test_scenario.ser";
        try {
            repo.saveScenarioToFile(testScenario1);
            File file = new File("scenarios/TestScenario1.ser");
            assertTrue(file.exists());
            file.delete();
        } catch (Exception e) {
            fail("Should not throw exception: " + e.getMessage());
        }
    }

    @Test
    void testSaveScenarioToFileWithNullScenarioThrowsException() {
        assertThrows(IllegalArgumentException.class, () -> repo.saveScenarioToFile(null));
    }

    @Test
    void testLoadScenarioFromFile() {
        String fileName = "test_scenario.ser";
        try {
            repo.saveScenarioToFile(testScenario1);
            Scenario loadedScenario = repo.loadScenarioFromFile("scenarios/TestScenario1.ser", testMap);
            assertNotNull(loadedScenario);
            assertEquals(testScenario1.getScenarioName(), loadedScenario.getScenarioName());
            new File("scenarios/TestScenario1.ser").delete();
        } catch (Exception e) {
            fail("Should not throw exception: " + e.getMessage());
        }
    }

    @Test
    void testLoadScenarioFromFileWithInvalidFileThrowsException() {
        assertThrows(RuntimeException.class, () -> 
            repo.loadScenarioFromFile("nonexistent.ser", testMap));
    }
}