package domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class FinancialResultsTest {

    private FinancialResults financialResultsExample;

    @BeforeEach
    void setUp() {
        financialResultsExample = new FinancialResults(10000, 10000, 10000, 100);
    }

    @Test
    void getTrackMaintenance() {
        assertEquals(10000, financialResultsExample.getTrackMaintenance());
    }

    @Test
    void getTrainMaintenance() {
        assertEquals(10000, financialResultsExample.getTrainMaintenance());
    }

    @Test
    void getFuelCost() {
        assertEquals(10000, financialResultsExample.getFuelCost());
    }

    @Test
    void getCargoDeliveries() {
        assertEquals(100, financialResultsExample.getCargoDeliveries());
    }
}