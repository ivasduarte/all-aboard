package repositories;

import domain.Scenario;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


/**
 * Provides management and storage of scenarios within a repository.
 * This class allows adding, retrieving, and validating scenarios while ensuring unique entries by name.
 */
public class ScenarioRepository {
    private List<Scenario> scenarios;


    /**
     * Constructs a new instance of the {@code ScenarioRepository} class.
     * This initializes an empty collection to store scenarios.
     */
    public ScenarioRepository() {
        scenarios = new ArrayList<>();
    }


    /**
     * Adds a given scenario to the repository if it is valid and not already present.
     * The scenario is considered valid if it passes validation checks
     * and does not already exist in the repository.
     *
     * @param scenario The scenario to be added. Must not be null and should pass validation.
     */
    public void add(Scenario scenario){
        if(validateScenario(scenario)){
            scenarios.add(scenario);
        }
    }


    /**
     * Validates whether the specified scenario is not already present in the repository.
     * This method checks the list of existing scenarios and determines if the given scenario
     * can be considered valid for addition.
     *
     * @param scenario  The scenario to validate. Must not be null.
     * @return          True if the scenario does not exist in the repository and is valid; false otherwise.
     */
    private boolean validateScenario(Scenario scenario){
        System.out.println(!scenarios.contains(scenario));
        return !scenarios.contains(scenario);
    }


    /**
     * Checks if a scenario with the specified name exists in the repository.
     * This method iterates through the list of stored scenarios and compares their names
     * case-insensitively with the provided name to determine if a match exists.
     *
     * @param name  The name of the scenario to check for existence. Must not be null or empty.
     * @return      True if a scenario with the given name exists in the repository; false otherwise.
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
     * Retrieves an immutable list of all scenarios stored in the repository.
     * The returned list reflects the current state of the repository, ensuring
     * the caller cannot modify the underlying scenario storage.
     *
     * @return An unmodifiable {@code List} containing all {@code Scenario} objects in the repository.
     */
    public List<Scenario> getScenarioList(){
        return List.copyOf(scenarios);
    }


    /**
     * Retrieves a scenario from the repository based on its name.
     * This method searches the stored list of scenarios for one that matches the specified name.
     *
     * @param name  The name of the scenario to retrieve. Must not be null.
     * @return      An {@code Optional} containing the {@code Scenario} with the specified name if found,
     *              or an empty {@code Optional} if no matching scenario exists.
     */
    public Optional<Scenario> getScenario(String name){
        return scenarios.stream().filter(s -> s.getScenarioName().equals(name)).findFirst();
    }
}
