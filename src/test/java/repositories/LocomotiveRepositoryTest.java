package repositories;

import domain.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class LocomotiveRepositoryTest {
    private LocomotiveRepository locomotiveRepository;

    @BeforeEach
    void setUp() {
        locomotiveRepository = new LocomotiveRepository();

        List<String> locomotiveTypes = new ArrayList<>();
        locomotiveTypes.add("Steam");
        locomotiveTypes.add("Diesel");
        List<String> historicalEvents = new ArrayList<>();

        Restrictions restrictions = new Restrictions(LocalDate.parse("1980-01-01"), LocalDate.parse("1990-12-31"), locomotiveTypes, historicalEvents);

        Scenario scenario = new Scenario("TestScenario", restrictions, new Map("TestMap", 100, 100), new Budget(1000));

        ScenarioRepository testScenarioRepository = new ScenarioRepository() {
            @Override
            public List<Scenario> getScenarioList() {
                return List.of(scenario);
            }
        };

        Repositories.getInstance().setScenarioRepository(testScenarioRepository);
    }

    @Test
    void saveLocomotive() {
        Locomotive locomotive = new Locomotive("USRA", "Steam", 1918, 1942, 90, 13, 30, 40);
        locomotiveRepository.saveLocomotive(locomotive);

        List<Locomotive> result = locomotiveRepository.getAllLocomotives();
        assertTrue(result.contains(locomotive));
    }

    @Test
    void save() {
        Locomotive locomotive = new Locomotive("USRA", "Steam", 1918, 1942, 90, 13, 30, 40);
        locomotiveRepository.save(locomotive);

        List<Locomotive> result = locomotiveRepository.getPlayerLocomotives();
        assertTrue(result.contains(locomotive));
    }

    @Test
    void getAvailableLocomotives() {
        Locomotive locomotive1 = new Locomotive("USRA", "Steam", 1918, 1942, 90, 13, 30, 40);
        Locomotive locomotive2 = new Locomotive("USRA II", "Steam", 1930, 1948, 98, 18, 32, 45);
        Locomotive locomotive3 = new Locomotive("V200", "Diesel", 1959, 1985, 160, 19, 54, 87);

        locomotiveRepository.saveLocomotive(locomotive1);
        locomotiveRepository.saveLocomotive(locomotive2);
        locomotiveRepository.saveLocomotive(locomotive3);

        List<Locomotive> available = locomotiveRepository.getAvailableLocomotives(0);

        assertTrue(available.contains(locomotive3));
        assertFalse(available.contains(locomotive1));
        assertFalse(available.contains(locomotive2));
    }

    @Test
    void getLocomotiveByModel() {
        Locomotive locomotive = new Locomotive("V200", "Diesel", 1959, 1985, 160, 19, 54, 87);
        locomotiveRepository.saveLocomotive(locomotive);

        locomotiveRepository.getAvailableLocomotives(0);

        Locomotive result = locomotiveRepository.getLocomotiveByModel("V200");
        assertEquals(locomotive, result);
    }
}