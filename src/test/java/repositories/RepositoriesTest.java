package repositories;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class RepositoriesTest {

    @Test
    void testGetInstance() {
        Repositories instance1 = Repositories.getInstance();
        Repositories instance2 = Repositories.getInstance();

        assertNotNull(instance1);
        assertNotNull(instance2);
        assertSame(instance1, instance2);
    }

    @Test
    void testGetMapRepository() {
        MapRepository repository = Repositories.getInstance().getMapRepository();
        assertNotNull(repository);
        assertTrue(repository instanceof MapRepository);
    }

    @Test
    void testGetIndustryRepository() {
        IndustryRepository repository = Repositories.getInstance().getIndustryRepository();
        assertNotNull(repository);
        assertTrue(repository instanceof IndustryRepository);
    }

    @Test
    void testGetLocomotiveRepository() {
        LocomotiveRepository repository = Repositories.getInstance().getLocomotiveRepository();
        assertNotNull(repository);
        assertTrue(repository instanceof LocomotiveRepository);
    }

    @Test
    void testGetTrainRepository() {
        TrainRepository repository = Repositories.getInstance().getTrainRepository();
        assertNotNull(repository);
        assertTrue(repository instanceof TrainRepository);
    }

    @Test
    void testGetAuthenticationRepository() {
        AuthenticationRepository repository = Repositories.getInstance().getAuthenticationRepository();
        assertNotNull(repository);
        assertTrue(repository instanceof AuthenticationRepository);
    }

    @Test
    void testGetScenarioRepository() {
        ScenarioRepository repository = Repositories.getInstance().getScenarioRepository();
        assertNotNull(repository);
        assertTrue(repository instanceof ScenarioRepository);
    }

    @Test
    void testGetSimulatorRepository() {
        SimulatorRepository repository = Repositories.getInstance().getSimRep();
        assertNotNull(repository);
        assertTrue(repository instanceof SimulatorRepository);
    }

    @Test
    void testSetScenarioRepository() {
        Repositories repositories = Repositories.getInstance();
        ScenarioRepository newRepository = new ScenarioRepository();
        repositories.setScenarioRepository(newRepository);
        assertSame(newRepository, repositories.getScenarioRepository());
    }

    @Test
    void testSetSimulatorRepository() {
        Repositories repositories = Repositories.getInstance();
        SimulatorRepository newRepository = new SimulatorRepository();
        repositories.setSimRep(newRepository);
        assertSame(newRepository, repositories.getSimRep());
    }
} 