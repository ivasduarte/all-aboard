package repositories;

import domain.Train;
import domain.Locomotive;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

class TrainRepositoryTest {
    private TrainRepository repository;
    private Train train1;
    private Train train2;

    @BeforeEach
    void setUp() {
        repository = new TrainRepository();
        Locomotive locomotive1 = new Locomotive("Steam", "coal", 1900, 1950, 10000, 1000, 500, 100);
        Locomotive locomotive2 = new Locomotive("Diesel", "diesel", 1951, 2000, 15000, 1200, 600, 120);
        train1 = new Train(locomotive1, 1900);
        train2 = new Train(locomotive2, 1951);
    }

    @Test
    void testSaveSingleTrain() {
        repository.saveTrain(train1);
        List<Train> trains = repository.getAllTrains();
        assertEquals(1, trains.size());
        assertTrue(trains.contains(train1));
    }

    @Test
    void testSaveMultipleTrains() {
        repository.saveTrain(train1);
        repository.saveTrain(train2);
        List<Train> trains = repository.getAllTrains();
        assertEquals(2, trains.size());
        assertTrue(trains.contains(train1));
        assertTrue(trains.contains(train2));
    }

    @Test
    void testGetTrainsEmpty() {
        List<Train> trains = repository.getAllTrains();
        assertTrue(trains.isEmpty());
    }

    @Test
    void testGetTrainsReturnsUnmodifiableList() {
        List<Train> trains = repository.getAllTrains();
        assertThrows(UnsupportedOperationException.class, () -> trains.add(train1));
    }

    @Test
    void testFindTrainByName() {
        repository.saveTrain(train1);
        repository.saveTrain(train2);
        var found = repository.getTrainByName("Steam");
        assertTrue(found.isPresent());
        assertEquals(train1, found.get());
    }

    @Test
    void testFindTrainByNameNotFound() {
        repository.saveTrain(train1);
        var found = repository.getTrainByName("Non-existent Train");
        assertTrue(found.isEmpty());
    }

    @Test
    void testFindTrainByNameWithNullName() {
        assertThrows(IllegalArgumentException.class, () -> repository.getTrainByName(null));
    }

    @Test
    void testFindTrainByNameWithEmptyName() {
        assertThrows(IllegalArgumentException.class, () -> repository.getTrainByName(""));
    }
} 