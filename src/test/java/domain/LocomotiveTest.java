package domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class LocomotiveTest {
    private Locomotive testLocomotive;

    @BeforeEach
    void setUp() {
        testLocomotive = new Locomotive("USRA", "Steam", 1918, 1942, 90, 13, 30, 40);
    }

    @Test
    void getLocomotiveModel() {
        assertEquals("USRA", testLocomotive.getLocomotiveModel());
    }

    @Test
    void getLocomotiveFuelType() {
        assertEquals("Steam", testLocomotive.getLocomotiveFuelType());
    }

    @Test
    void getPrice() {
        assertEquals(90, testLocomotive.getPrice());
    }

    @Test
    void getStartYear() {
        assertEquals(1918, testLocomotive.getStartYear());
    }

    @Test
    void getEndYear() {
        assertEquals(1942, testLocomotive.getEndYear());
    }

    @Test
    void getFuelCost() {
        assertEquals(30, testLocomotive.getFuelCost());
    }

    @Test
    void getMaintenanceCost() {
        assertEquals(13, testLocomotive.getMaintenanceCost());
    }

    @Test
    void getMaxSpeed() {
        assertEquals(40, testLocomotive.getMaxSpeed());
    }
}