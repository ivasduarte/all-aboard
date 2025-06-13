package domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Collections;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

public class ScenarioTest {
    private Scenario validScenario;
    private Map testMap;
    private Restrictions validRestrictions;
    private Budget testBudget;

    @BeforeEach
    void setUp(){
        //create test objetcts
        LocalDate startDate = LocalDate.of(1870, 1, 1);
        LocalDate endDate = LocalDate.of(1880, 12, 31);
        List<String> locomotiveTypes = Arrays.asList("steam", "diesel");
        List<String> historicalEvents = Collections.singletonList("none");
        List<PortBehavior> portBehaviors = new ArrayList<>();

        validRestrictions = new Restrictions(startDate, endDate, locomotiveTypes, historicalEvents, portBehaviors);
        testMap = new Map("TestMap", 100, 100, 1);
        testBudget = new Budget(10000);
        validScenario = new Scenario("TestScenario", validRestrictions, testMap, testBudget);
    }

    @Test
    void testGetScenarioName(){
        assertEquals("TestScenario", validScenario.getScenarioName());
    }

    @Test
    void testGetRestrictions(){
        assertEquals(validRestrictions, validScenario.getRestrictions());
    }

    @Test
    void testGetMap(){
        assertEquals(testMap, validScenario.getMap());
    }

    @Test
    void testGetBudget(){
        assertEquals(testBudget, validScenario.getBudget());
    }

    @Test
    void testSetBudget(){
        Budget newBudget = new Budget(20000);
        validScenario.setBudget(newBudget);
        assertEquals(newBudget, validScenario.getBudget());
    }

    @Test
    void testSetBudgetWithNull_ThrowsException() {
        assertThrows(IllegalArgumentException.class, () -> validScenario.setBudget(null));
    }

    @Test
    void testSubtractBudget(){
        int remainingBudget = validScenario.subtractBudget(2000);
        assertEquals(8000, remainingBudget);
        assertEquals(8000, validScenario.getBudget().getBudget());
    }

    @Test
    void testSubtractBudgetWithNegativeAmount_ThrowsException() {
        assertThrows(IllegalArgumentException.class, () -> validScenario.subtractBudget(-1000));
    }

    @Test
    void testSubtractBudgetWithAmountGreaterThanBudget_ThrowsException() {
        assertThrows(IllegalArgumentException.class, () -> validScenario.subtractBudget(15000));
    }

    @Test
    void testAddBudget(){
        int newBudget = validScenario.addBudget(5000);
        assertEquals(15000, newBudget);
        assertEquals(15000, validScenario.getBudget().getBudget());
    }

    @Test
    void testAddBudgetWithNegativeAmount_ThrowsException() {
        assertThrows(IllegalArgumentException.class, () -> validScenario.addBudget(-1000));
    }

    @Test
    void testAddBudgetWithZeroAmount_DoesNotChangeBudget() {
        int originalBudget = validScenario.getBudget().getBudget();
        int newBudget = validScenario.addBudget(0);
        assertEquals(originalBudget, newBudget);
        assertEquals(originalBudget, validScenario.getBudget().getBudget());
    }

    @Test
    void testValidateNameWithValidName(){
        assertTrue(validScenario.validateName());
    }

    @Test
    void testValidateNameWithNullName(){
        Scenario scenario = new Scenario(null, validRestrictions, testMap, testBudget);
        assertFalse(scenario.validateName());
    }

    @Test
    void testValidateNameWithoutUppercase() {
        Scenario scenario = new Scenario("testscenario", validRestrictions, testMap, testBudget);
        assertTrue(scenario.validateName());
    }

    @Test
    void testValidateNameWithOnlyNumbers() {
        Scenario scenario = new Scenario("12345", validRestrictions, testMap, testBudget);
        assertTrue(scenario.validateName());
    }

    @Test
    void testValidateNameWithNonAlphanumeric() {
        Scenario scenario = new Scenario("Test-Scenario!", validRestrictions, testMap, testBudget);
        assertFalse(scenario.validateName());
    }

    @Test
    void testValidateNameWithEmptyString() {
        Scenario scenario = new Scenario("", validRestrictions, testMap, testBudget);
        assertFalse(scenario.validateName());
    }

    @Test
    void testValidateNameWithOnlySpaces() {
        Scenario scenario = new Scenario("   ", validRestrictions, testMap, testBudget);
        assertFalse(scenario.validateName());
    }

    @Test
    void testValidateRestrictions() {
        assertTrue(validScenario.validateRestrictions());
    }

    @Test
    void testValidateRestrictionsWithNull() {
        Scenario scenario = new Scenario("TestScenario", null, testMap, testBudget);
        assertFalse(scenario.validateRestrictions());
    }

    @Test
    void testHasValidMap() {
        assertTrue(validScenario.hasValidMap());
    }

    @Test
    void testHasValidMapWithNull() {
        Scenario scenario = new Scenario("TestScenario", validRestrictions, null, testBudget);
        assertFalse(scenario.hasValidMap());
    }

    @Test
    void testToString() {
        String expectedString = "TestScenario - TestMap";
        assertEquals(expectedString, validScenario.toString());
    }

    @Test
    void testConstructorWithNullBudget_ThrowsException() {
        assertThrows(IllegalArgumentException.class, () -> 
            new Scenario("TestScenario", validRestrictions, testMap, null));
    }

    @Test
    void testGetPortBehaviors() {
        List<PortBehavior> portBehaviors = validScenario.getRestrictions().getPorts();
        assertNotNull(portBehaviors);
        assertTrue(portBehaviors.isEmpty());
    }

    @Test
    void testPortBehaviorWithValidPorts() {
        List<PortBehavior> portBehaviors = Arrays.asList(PortBehavior.PORT_ALPHA, PortBehavior.PORT_BRAVO);
        Restrictions restrictions = new Restrictions(
            LocalDate.of(1870, 1, 1),
            LocalDate.of(1880, 12, 31),
            Arrays.asList("steam", "diesel"),
            Collections.singletonList("none"),
            portBehaviors
        );
        Scenario scenario = new Scenario("TestScenario", restrictions, testMap, testBudget);

        List<PortBehavior> retrievedPorts = scenario.getRestrictions().getPorts();
        assertEquals(2, retrievedPorts.size());
        assertTrue(retrievedPorts.contains(PortBehavior.PORT_ALPHA));
        assertTrue(retrievedPorts.contains(PortBehavior.PORT_BRAVO));
    }

    @Test
    void testPortBehaviorWithNullPorts() {
        Restrictions restrictions = new Restrictions(
            LocalDate.of(1870, 1, 1),
            LocalDate.of(1880, 12, 31),
            Arrays.asList("steam", "diesel"),
            Collections.singletonList("none"),
            null
        );
        Scenario scenario = new Scenario("TestScenario", restrictions, testMap, testBudget);

        List<PortBehavior> retrievedPorts = scenario.getRestrictions().getPorts();
        assertNull(retrievedPorts);
    }

    @Test
    void testPortBehaviorWithEmptyPorts() {
        List<PortBehavior> emptyPorts = new ArrayList<>();
        Restrictions restrictions = new Restrictions(
            LocalDate.of(1870, 1, 1),
            LocalDate.of(1880, 12, 31),
            Arrays.asList("steam", "diesel"),
            Collections.singletonList("none"),
            emptyPorts
        );
        Scenario scenario = new Scenario("TestScenario", restrictions, testMap, testBudget);

        List<PortBehavior> retrievedPorts = scenario.getRestrictions().getPorts();
        assertNotNull(retrievedPorts);
        assertTrue(retrievedPorts.isEmpty());
    }
}