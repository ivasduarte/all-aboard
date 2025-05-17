# US004 - Create A Scenario

## 4. Tests

**Test 1:** Validates addition of scenario

```java
void testAdd() {
    LocalDate startDate = LocalDate.of(1855, 1, 1);
    LocalDate endDate = LocalDate.of(1867, 12, 31);
    List<String> locomotiveTypes = Collections.singletonList("steam");
    List<String> historicalEvents = Collections.singletonList("none");
    Restrictions restrictions = new Restrictions(startDate, endDate, locomotiveTypes, historicalEvents);
    Budget budget = new Budget(15000);
    Scenario newScenario = new Scenario("TestScenario3", restrictions, testMap, budget);

    repo.add(newScenario);

    assertEquals(3, repo.getScenarioList().size());
}
```

**Test 2:** Validates addition of duplicate scenario

```java
void testAddDuplicateScenario() {
    LocalDate startDate = LocalDate.of(1870, 1, 1);
    LocalDate endDate = LocalDate.of(1880, 12, 31);
    List<String> locomotiveTypes = Collections.singletonList("steam");
    List<String> historicalEvents = Collections.singletonList("none");
    Restrictions restrictions = new Restrictions(startDate, endDate, locomotiveTypes, historicalEvents);
    Budget budget = new Budget(10000);
    Scenario duplicateScenario = new Scenario("TestScenario1", restrictions, testMap, budget);

    int initialSize = repo.getScenarioList().size();
    repo.add(duplicateScenario);

    assertEquals(initialSize, repo.getScenarioList().size());
}
```

**Test 3:** Validates of name

```java
void testExistsByName() {
    assertTrue(repo.existsByName("TestScenario1"));
    assertTrue(repo.existsByName("testscenario1")); // Should be case insensitive
    assertFalse(repo.existsByName("NonExistentScenario"));
}
```

**Test 4:** Validates getScenarioList

```java
void testGetScenarioList() {
    List<Scenario> scenarios = repo.getScenarioList();
    assertEquals(2, scenarios.size());
    assertTrue(scenarios.contains(testScenario1));
    assertTrue(scenarios.contains(testScenario2));

    assertThrows(UnsupportedOperationException.class, () -> scenarios.add(null));
}
```

**Test 5:** Validates getScenarios()

```java
void testGetScenarios() {
    Optional<Scenario> foundScenario = repo.getScenarios("TestScenario1");
    assertTrue(foundScenario.isPresent());
    assertEquals("TestScenario1", foundScenario.get().getScenarioName());

    Optional<Scenario> notFoundScenario = repo.getScenarios("NonExistentScenario");
    assertFalse(notFoundScenario.isPresent());
}
```

**Test 6:** Validates getScenarios with same name

```java
void testGetScenariosWithExactNameMatch() {
    Optional<Scenario> foundScenario = repo.getScenarios("TestScenario1");
    assertTrue(foundScenario.isPresent());

    Optional<Scenario> notFoundScenario = repo.getScenarios("testscenario1");
    assertFalse(notFoundScenario.isPresent());
}
```

**Test 7:** Validates getStartDate

```java
    void testGetStartDate() {
    assertEquals(startDate, validRestrictions.getStartDate());
}
```

**Test 8:** Validates getEndDate()

```java
    void testGetEndDate() {
    assertEquals(endDate, validRestrictions.getEndDate());
}
```

**Test 9:** Validates getLocomotiveTypes

```java
    void testGetLocomotiveTypes() {
    assertEquals(validLocomotiveTypes, validRestrictions.getLocomotiveTypes());
}
```

**Test 10:** Validates getHistorialEvents()

```java
    void testGetHistoricalEvents() {
    assertEquals(validHistoricalEvents, validRestrictions.getHistoricalEvents());
}
```

**Test 11:** Validates with valid restrictions

```java
    void testValidateWithValidRestrictions() {
    assertTrue(validRestrictions.validate());
}
```

**Test 12:** Validates with null start date

```java
    void testValidateWithNullStartDate() {
    Restrictions restrictions = new Restrictions(null, endDate, validLocomotiveTypes, validHistoricalEvents);
    assertFalse(restrictions.validate());
}
```

**Test 13:** Validates with null end date

```java
    void testValidateWithNullEndDate() {
    Restrictions restrictions = new Restrictions(startDate, null, validLocomotiveTypes, validHistoricalEvents);
    assertFalse(restrictions.validate());
}
```

**Test 14:** Validates with start date after end date

```java
    void testValidateWithStartDateAfterEndDate() {
    LocalDate laterDate = LocalDate.of(1890, 1, 1);
    Restrictions restrictions = new Restrictions(laterDate, endDate, validLocomotiveTypes, validHistoricalEvents);
    assertFalse(restrictions.validate());
}
```

**Test 15:** Validates with null locomotive types

```java
    void testValidateWithNullLocomotiveTypes() {
    Restrictions restrictions = new Restrictions(startDate, endDate, null, validHistoricalEvents);
    assertFalse(restrictions.validate());
}
```

**Test 16:** Validates with invalid locomotive types

```java
    void testValidateWithInvalidLocomotiveType() {
    List<String> invalidTypes = Arrays.asList("steam", "nuclear"); // "nuclear" is not valid
    Restrictions restrictions = new Restrictions(startDate, endDate, invalidTypes, validHistoricalEvents);
    assertFalse(restrictions.validate());
}
```

**Test 17:** Validates with valid locomotive types

```java
    void testValidateWithAllValidLocomotiveTypes() {
    List<String> allValidTypes = Arrays.asList("steam", "diesel", "electric");
    Restrictions restrictions = new Restrictions(startDate, endDate, allValidTypes, validHistoricalEvents);
    assertTrue(restrictions.validate());
}
```

**Test 18:** Validates with null historical events

```java
    void testValidateWithNullHistoricalEvents() {
    Restrictions restrictions = new Restrictions(startDate, endDate, validLocomotiveTypes, null);
    assertFalse(restrictions.validate());
}
```

**Test 19:** Validates insensitivity for locomotive types

```java
    void testCaseInsensitivityForLocomotiveTypes() {
    List<String> mixedCaseTypes = Arrays.asList("Steam", "DiEsEl");
    Restrictions restrictions = new Restrictions(startDate, endDate, mixedCaseTypes, validHistoricalEvents);
    assertTrue(restrictions.validate());
}
```

**Test 20:** Validates getScenarioName

```java
    void testGetScenarioName() {
    assertEquals("Test Scenario", validScenario.getScenarioName());
}
```

**Test 21:** Validates getRestrictions

```java
    void testGetRestrictions() {
    assertEquals(validRestrictions, validScenario.getRestrictions());
}
```


**Test 22:** Validates getMap

```java
    void testGetMap() {
    assertEquals(testMap, validScenario.getMap());
}
```

**Test 23:** Validates getBudget

```java
    void testGetBudget() {
    assertEquals(testBudget, validScenario.getBudget());
}
```

**Test 24:** Validates setBudget

```java
    void testSetBudget() {
    Budget newBudget = new Budget(20000);
    validScenario.setBudget(newBudget);
    assertEquals(newBudget, validScenario.getBudget());
}
```

**Test 25:** Validates subtractBudget

```java
    void testSubtractBudget() {
    int remainingBudget = validScenario.subtractBudget(2000);
    assertEquals(8000, remainingBudget);
    assertEquals(8000, validScenario.getBudget().getBudget());
}
```

**Test 26:** Validates addBudget

```java
    void testAddBudget() {
    int newBudget = validScenario.addBudget(5000);
    assertEquals(15000, newBudget);
    assertEquals(15000, validScenario.getBudget().getBudget());
}
```

**Test 27:** Validates name with valid name

```java
    void testValidateNameWithValidName() {
    assertTrue(validScenario.validateName());
}
```

**Test 28:** Validates name with null name

```java
    void testValidateNameWithNullName() {
    Scenario scenario = new Scenario(null, validRestrictions, testMap, testBudget);
    assertFalse(scenario.validateName());
}
```

**Test 29:** Validates name without uppercase

```java
    void testValidateNameWithoutUppercase() {
    Scenario scenario = new Scenario("testscenario", validRestrictions, testMap, testBudget);
    assertFalse(scenario.validateName());
}
```

**Test 30:** Validates name with non-aplhanumeric

```java
    void testValidateNameWithNonAlphanumeric() {
    Scenario scenario = new Scenario("Test-Scenario!", validRestrictions, testMap, testBudget);
    assertFalse(scenario.validateName());
}
```

**Test 31:** Validates restrictions

```java
    void testValidateRestrictions() {
    assertTrue(validScenario.validateRestrictions());
}
```

**Test 31:** Validates with null restrictions

```java
    void testValidateRestrictionsWithNull() {
    Scenario scenario = new Scenario("TestScenario", null, testMap, testBudget);
    assertFalse(scenario.validateRestrictions());
}
```

**Test 32:** Validates hasValidMap

```java
    void testHasValidMap() {
    assertTrue(validScenario.hasValidMap());
}
```

**Test 33:** Validates hasValidMap with null map

```java
    void testHasValidMapWithNull() {
    Scenario scenario = new Scenario("TestScenario", validRestrictions, null, testBudget);
    assertFalse(scenario.hasValidMap());
}
```

**Test 34:** Validates isValid

```java
    void testIsValid() {
    assertTrue(validScenario.isValid());
}
```

**Test 35:** Validates scenario with invalid components

```java
    void testIsValidWithInvalidComponents() {
    Scenario invalidNameScenario = new Scenario("", validRestrictions, testMap, testBudget);
    assertFalse(invalidNameScenario.isValid());

    Scenario invalidRestrictionsScenario = new Scenario("TestScenario", null, testMap, testBudget);
    assertFalse(invalidRestrictionsScenario.isValid());

    Scenario invalidMapScenario = new Scenario("TestScenario", validRestrictions, null, testBudget);
    assertFalse(invalidMapScenario.isValid());
}
```


**Test 36:** Validates toString

```java
    void testToString() {
    String expectedString = "TestScenario (" + validRestrictions + ", belongs to map 'TestMap')";
    assertEquals(expectedString, validScenario.toString());
}
```



## 5. Construction (Implementation)

### Class CreateScenarioController

```java
package controller;

import domain.*;
import repositories.Repositories;
import repositories.ScenarioRepository;

import java.util.List;

/**
 * This class handles operations related to creating and managing scenarios.
 * It interacts with the ScenarioRepository to perform operations such as scenario creation
 * and retrieving a list of all scenarios.
 */
public class CreateScenarioController {
    private ScenarioRepository scenarioRepository;

    /**
     * Creates a new scenario with the specified name, restrictions, and map.
     * If a scenario with the provided name already exists, it will return an error message.
     * In the case of successful creation, a default budget will be assigned to the scenario.
     *
     * @param name              The name of the new scenario. It must be unique.
     * @param restrictions      The restrictions to be applied to the scenario, such as time, locomotive types, and events.
     * @param map               The map object associated with the scenario.
     * @return                  A string message indicating the result of the operation. If successful, it confirms the scenario creation.
     *                          If an error occurs, an appropriate error message is returned.
     */
    public String createScenario(String name, Restrictions restrictions, Map map) {
        try {
            UserSession userSession = ApplicationSession.getInstance().getCurrentSession();
            ScenarioRepository scenarioRepository = Repositories.getInstance().getScenarioRepository();

            if (scenarioRepository.existsByName(name)) {
                return "ERROR: A scenario with this name already exists.";
            }

            Budget defaultBudget = new Budget(1000);
            Scenario scenario = new Scenario(name, restrictions, map, defaultBudget);
            scenarioRepository.add(scenario);

            return "Scenario '" + name + "' created successfully.";
        } catch (IllegalArgumentException e){
            return "ERROR: " + e.getMessage();
        } catch (Exception e){
            return "ERROR: Unexpected error occurred.";
        }
    }

    /**
     * Retrieves the {@code ScenarioRepository} associated with this controller.
     * If the repository has not been instantiated, it initializes it using the
     * {@code Repositories} singleton, ensuring a single shared instance.
     *
     * @return The {@code ScenarioRepository} instance used for managing scenarios.
     */
    private ScenarioRepository getScenarioRepository(){
        if(scenarioRepository == null){
            Repositories repo = Repositories.getInstance();
            scenarioRepository = repo.getScenarioRepository();
        }
        return scenarioRepository;
    }

    /**
     * Retrieves a list of all available scenarios from the repository.
     * The scenarios represent configurations defined for specific use cases
     * and include attributes such as name, restrictions, and associated map details.
     *
     * @return A {@code List} of {@code Scenario} objects representing all scenarios stored
     *         in the repository. The returned list is an immutable copy, ensuring
     *         that the underlying data remains unaltered.
     */
    public List<Scenario> getScenarioList() {
        return getScenarioRepository().getScenarioList();
    }
}
```


### Class ScenarioRepository

```java
package repositories;

import domain.Scenario;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * The {@code ScenarioRepository} class provides functionality to manage a collection of {@code Scenario} objects.
 * It allows adding, validating, retrieving, and checking the existence of scenarios by their names.
 */
public class ScenarioRepository {
    private List<Scenario> scenarios;

    /**
     * Constructs a new instance of the {@code ScenarioRepository} class.
     * This constructor initializes an internal list for storing scenarios.
     *
     * The repository is designed to manage a collection of scenarios, providing
     * functionalities such as adding new scenarios, retrieving scenarios, validating
     * scenarios, and checking for the existence of a scenario by name.
     */
    public ScenarioRepository() {
        scenarios = new ArrayList<>();
    }

    /**
     * Adds a scenario to the repository if it passes validation.
     * This method validates the provided scenario using the {@code validateScenario} method
     * and, if valid, adds it to the internal list of scenarios.
     *
     * @param scenario  The scenario to be added to the repository. Must not be null and must pass validation.
     */
    public void add(Scenario scenario){
        if(validateScenario(scenario)){
            scenarios.add(scenario);
        }
    }

    /**
     * Validates if the provided scenario is not already present in the repository's list of scenarios.
     * This method checks the repository's internal collection to ensure the scenario is unique.
     *
     * @param scenario      The scenario to validate. Must not be null. The object's equality is checked 
     *                      against existing scenarios in the repository.
     * @return              true if the scenario does not already exist in the repository; false otherwise.
     */
    private boolean validateScenario(Scenario scenario){
        System.out.println(!scenarios.contains(scenario));
        return !scenarios.contains(scenario);
    }

    /**
     * Checks if a scenario with the specified name exists in the repository.
     * This method iterates through the stored scenarios and compares their names
     * (case-insensitively) with the provided name.
     *
     * @param name      The name of the scenario to check. Must not be null.
     * @return          true if a scenario with the specified name exists in the repository; false otherwise.
     */
    public boolean existsByName(String name) {
        for (int i = 0; i < scenarios.size(); i++){
            if(scenarios.get(i).getScenarioName().equalsIgnoreCase(name)){
                return true;
            }
        }
        return false;
    }

    /**
     * Retrieves an unmodifiable list of all stored scenarios in the repository.
     *
     * This method returns a copy of the internal collection of scenarios,
     * ensuring that the returned list cannot be modified by the caller.
     *
     * @return  An unmodifiable {@code List} of {@code Scenario} objects representing all scenarios
     *          currently stored in the repository.
     */
    public List<Scenario> getScenarioList(){
        return List.copyOf(scenarios);
    }

    /**
     * Retrieves an optional scenario from the repository based on its name.
     * This method filters the internal list of scenarios by comparing their names
     * to the specified name and returns the first match if available.
     *
     * @param name      The name of the scenario to retrieve. Must not be null.
     * @return          An {@code Optional} containing the matching {@code Scenario}
     *                  if a scenario with the specified name exists; otherwise, an 
     *                  empty {@code Optional}.
     */
    public Optional<Scenario> getScenarios(String name){
        return scenarios.stream().filter(s -> s.getScenarioName().equals(name)).findFirst();
    }
}
```