package repositories;

import domain.Map;
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

    public List<Scenario> getScenarioListByMap(Map map){
        List<Scenario> filteredScenarios = new ArrayList<>();

        for (Scenario scenario : scenarios) {
            if(scenario.getMap().equals(map)){
                filteredScenarios.add(scenario);
            }
        }

        return List.copyOf(filteredScenarios);
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
