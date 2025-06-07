package repositories;

import domain.FinancialResults;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class FinancialResultsRepositoryTest {
    private FinancialResultsRepository exampleRepository;
    private FinancialResults exampleFinancialResults1;
    private FinancialResults exampleFinancialResults2;

    @BeforeEach
    void setUp() {
        exampleRepository = new FinancialResultsRepository();

        exampleFinancialResults1 = new FinancialResults(100, 100, 100, 100);
        exampleFinancialResults2 = new FinancialResults(10, 10, 10, 10);
    }

    @Test
    void saveSingleFinancialResult() {
        exampleRepository.save(exampleFinancialResults1);

        List<FinancialResults> results = exampleRepository.getFinancialResultsList();

        assertEquals(1, results.size());
        assertEquals(exampleFinancialResults1, results.get(0));
    }

    @Test
    void saveMultipleFinancialResults() {
        exampleRepository.save(exampleFinancialResults1);
        exampleRepository.save(exampleFinancialResults2);

        List<FinancialResults> results = exampleRepository.getFinancialResultsList();

        assertEquals(2, results.size());
        assertEquals(exampleFinancialResults1, results.get(0));
        assertEquals(exampleFinancialResults2, results.get(1));
    }

    @Test
    void testEmptyList() {
        List<FinancialResults> results = exampleRepository.getFinancialResultsList();

        assertEquals(0, results.size());
        assertTrue(results.isEmpty());
    }

    @Test
    void getFinancialResultsList() {
        exampleRepository.save(exampleFinancialResults1);
        exampleRepository.save(exampleFinancialResults2);

        List<FinancialResults> results = exampleRepository.getFinancialResultsList();

        assertEquals(2, results.size());
        assertTrue(results.contains(exampleFinancialResults1));
        assertTrue(results.contains(exampleFinancialResults2));
    }
}