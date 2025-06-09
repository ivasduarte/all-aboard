package repositories;

import domain.Locomotive;
import domain.Restrictions;
import domain.Scenario;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class LocomotiveRepositoryTest {
    private LocomotiveRepository locoRepo;
    private Locomotive loco1;
    private Locomotive loco2;

    @BeforeEach
    void setUp() {
        locoRepo = new LocomotiveRepository();

        loco1 = new Locomotive("model", "steam", 1900, 2000, 100, 50, 25, 70);
        loco2 = new Locomotive("loco", "diesel", 1950, 2020, 200, 100, 50, 75);
    }

    @Test
    void saveLocomotive() {
        locoRepo.saveLocomotive(loco1);

        List<Locomotive> results = locoRepo.getAllLocomotives();

        assertEquals(1, results.size());
        assertTrue(results.contains(loco1));
    }

    @Test
    void save() {
        locoRepo.save(loco2);

        List<Locomotive> results = locoRepo.getPlayerLocomotives();

        assertEquals(1, results.size());
        assertTrue(results.contains(loco2));
    }

    @Test
    void getAllLocomotives() {
        locoRepo.saveLocomotive(loco1);
        locoRepo.saveLocomotive(loco2);

        List<Locomotive> results = locoRepo.getAllLocomotives();

        assertEquals(2, results.size());
        assertEquals(loco1, results.get(0));
        assertEquals(loco2, results.get(1));
        assertTrue(results.contains(loco1));
        assertTrue(results.contains(loco2));
    }

    @Test
    void getAvailableLocomotives() {
        locoRepo.saveLocomotive(loco1);
        locoRepo.saveLocomotive(loco2);

        ScenarioRepository scenarioRepo = Repositories.getInstance().getScenarioRepository();
        List<Scenario> scenarios = scenarioRepo.getScenarioList();

        if (!scenarios.isEmpty()) {
            List<Locomotive> availableLocomotives = locoRepo.getAvailableLocomotives(0);

            assertNotNull(availableLocomotives);

            Scenario scenario = scenarios.get(0);
            for (Locomotive locomotive : availableLocomotives) {
                assertTrue(locomotive.getStartYear() <= scenario.getCurrentYear());
                assertTrue(scenario.getCurrentYear() <= locomotive.getEndYear());
            }
        }
    }

    @Test
    void getPlayerLocomotives() {
        locoRepo.save(loco1);
        locoRepo.save(loco2);

        List<Locomotive> playerLocomotives = locoRepo.getPlayerLocomotives();

        assertEquals(2, playerLocomotives.size());
        assertTrue(playerLocomotives.contains(loco1));
        assertTrue(playerLocomotives.contains(loco2));
    }

    @Test
    void getLocomotiveByModel() {
        locoRepo.saveLocomotive(loco1);
        locoRepo.unlockForPlayer(loco1);

        Locomotive result = locoRepo.getLocomotiveByModel("model");

        assertEquals("model", result.getLocomotiveModel());
        assertEquals(loco1, result);
    }

    @Test
    void isAvailableToPlayer() {
        locoRepo.saveLocomotive(loco1);
        locoRepo.unlockForPlayer(loco1);

        boolean isAvailable = locoRepo.isAvailableToPlayer(loco1);

        assertTrue(isAvailable);
    }
}