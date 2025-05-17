package domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class RestrictionsTest {
    private Restrictions validRestrictions;
    private LocalDate startDate;
    private LocalDate endDate;
    private List<String> validLocomotiveTypes;
    private List<String> validHistoricalEvents;

    @BeforeEach
    void setUp() {
        startDate = LocalDate.of(1870, 1, 1);
        endDate = LocalDate.of(1880, 12, 31);
        validLocomotiveTypes = Arrays.asList("steam", "diesel");
        validHistoricalEvents = Collections.singletonList("Industrial Revolution");
        validRestrictions = new Restrictions(startDate, endDate, validLocomotiveTypes, validHistoricalEvents);
    }

    @Test
    void testGetStartDate() {
        assertEquals(startDate, validRestrictions.getStartDate());
    }

    @Test
    void testGetEndDate() {
        assertEquals(endDate, validRestrictions.getEndDate());
    }

    @Test
    void testGetLocomotiveTypes() {
        assertEquals(validLocomotiveTypes, validRestrictions.getLocomotiveTypes());
    }

    @Test
    void testGetHistoricalEvents() {
        assertEquals(validHistoricalEvents, validRestrictions.getHistoricalEvents());
    }

    @Test
    void testValidateWithValidRestrictions() {
        assertTrue(validRestrictions.validate());
    }

    @Test
    void testValidateWithNullStartDate() {
        Restrictions restrictions = new Restrictions(null, endDate, validLocomotiveTypes, validHistoricalEvents);
        assertFalse(restrictions.validate());
    }

    @Test
    void testValidateWithNullEndDate() {
        Restrictions restrictions = new Restrictions(startDate, null, validLocomotiveTypes, validHistoricalEvents);
        assertFalse(restrictions.validate());
    }

    @Test
    void testValidateWithStartDateAfterEndDate() {
        LocalDate laterDate = LocalDate.of(1890, 1, 1);
        Restrictions restrictions = new Restrictions(laterDate, endDate, validLocomotiveTypes, validHistoricalEvents);
        assertFalse(restrictions.validate());
    }

    @Test
    void testValidateWithNullLocomotiveTypes() {
        Restrictions restrictions = new Restrictions(startDate, endDate, null, validHistoricalEvents);
        assertFalse(restrictions.validate());
    }

    @Test
    void testValidateWithInvalidLocomotiveType() {
        List<String> invalidTypes = Arrays.asList("steam", "nuclear"); // "nuclear" is not valid
        Restrictions restrictions = new Restrictions(startDate, endDate, invalidTypes, validHistoricalEvents);
        assertFalse(restrictions.validate());
    }

    @Test
    void testValidateWithAllValidLocomotiveTypes() {
        List<String> allValidTypes = Arrays.asList("steam", "diesel", "electric");
        Restrictions restrictions = new Restrictions(startDate, endDate, allValidTypes, validHistoricalEvents);
        assertTrue(restrictions.validate());
    }

    @Test
    void testValidateWithNullHistoricalEvents() {
        Restrictions restrictions = new Restrictions(startDate, endDate, validLocomotiveTypes, null);
        assertFalse(restrictions.validate());
    }

    @Test
    void testCaseInsensitivityForLocomotiveTypes() {
        List<String> mixedCaseTypes = Arrays.asList("Steam", "DiEsEl");
        Restrictions restrictions = new Restrictions(startDate, endDate, mixedCaseTypes, validHistoricalEvents);
        assertTrue(restrictions.validate());
    }
}