package repositories;

import domain.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

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
        Scenario newScenario = new Scenario( "TestScenario3", restrictions, testMap, budget);

        repo.add(newScenario);

        assertEquals(3, repo.getScenarioList().size());
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
    void testGetScenarioList() {
        List<Scenario> scenarios = repo.getScenarioList();
        assertEquals(2, scenarios.size());
        assertTrue(scenarios.contains(testScenario1));
        assertTrue(scenarios.contains(testScenario2));

        assertThrows(UnsupportedOperationException.class, () -> scenarios.add(null));
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
    void testGetScenariosWithExactNameMatch() {
        Optional<Scenario> foundScenario = repo.getScenarios("TestScenario1");
        assertTrue(foundScenario.isPresent());

        Optional<Scenario> notFoundScenario = repo.getScenarios("testscenario1");
        assertFalse(notFoundScenario.isPresent());
    }
}