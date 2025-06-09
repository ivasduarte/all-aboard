package domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class TrainTest {
    private Train train;
    private Locomotive locomotive;

    @BeforeEach
    void setUp() {
        locomotive = new Locomotive("Test Locomotive", "Diesel", 2000, 2030, 1000000, 50000, 30000, 100);
        train = new Train(locomotive, 2023);
    }

    @Test
    void testSetNumberOfCarriages_Increase() {
        // Initial number of carriages is 0
        assertEquals(0, train.getNumberOfCarriages());

        // Set number of carriages to 5
        train.setNumberOfCarriages(5);

        // Verify number of carriages is now 5
        assertEquals(5, train.getNumberOfCarriages());
    }

    @Test
    void testSetNumberOfCarriages_Decrease() {
        // First add 10 carriages
        train.setNumberOfCarriages(10);
        assertEquals(10, train.getNumberOfCarriages());

        // Now decrease to 3 carriages
        train.setNumberOfCarriages(3);

        // Verify number of carriages is now 3
        assertEquals(3, train.getNumberOfCarriages());
    }

    @Test
    void testSetNumberOfCarriages_SameNumber() {
        // First add 5 carriages
        train.setNumberOfCarriages(5);
        assertEquals(5, train.getNumberOfCarriages());

        // Set to the same number
        train.setNumberOfCarriages(5);

        // Verify number of carriages is still 5
        assertEquals(5, train.getNumberOfCarriages());
    }

    @Test
    void testSetNumberOfCarriages_NegativeNumber() {
        // Verify that an exception is thrown for negative number
        assertThrows(IllegalArgumentException.class, () -> {
            train.setNumberOfCarriages(-1);
        });
    }
}
