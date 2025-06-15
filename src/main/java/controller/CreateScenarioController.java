package controller;

import domain.*;
import dto.MapDTO;
import dto.MapMapper;
import repositories.Repositories;
import repositories.ScenarioRepository;
import repositories.MapRepository;

import java.util.List;

/**
 * This class handles operations related to creating and managing scenarios.
 * It interacts with the ScenarioRepository to perform operations such as scenario creation
 * and retrieving a list of all scenarios.
 */
public class CreateScenarioController {
    private ScenarioRepository scenarioRepository;
    private MapRepository mapRepository;

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
            ScenarioRepository scenarioRepository = Repositories.getInstance().getScenarioRepository();
            List<Scenario> scenarios = map.getScenarios();

            for(Scenario scenario : scenarios)
            {
                if(scenario.getScenarioName().equalsIgnoreCase(name))
                    return "ERROR: A scenario with this name already exists.";
            }

            Budget defaultBudget = new Budget(1000);
            Scenario scenario = new Scenario(name, restrictions, map, defaultBudget);
            scenarioRepository.add(scenario);
            map.addNewScenario(scenario);

            return "\nScenario '" + name + "' created successfully.";
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
     * Retrieves the {@code MapRepository} associated with this controller.
     * If the repository has not been instantiated, it initializes it using the
     * {@code Repositories} singleton, ensuring a single shared instance.
     *
     * @return The {@code MapRepository} instance used for managing maps.
     */
    private MapRepository getMapRepository() {
        if(mapRepository == null) {
            Repositories repo = Repositories.getInstance();
            mapRepository = repo.getMapRepository();
        }
        return mapRepository;
    }

    /**
     * Retrieves a list of all available maps as DTOs.
     * This method converts the domain Map objects to MapDTO objects for presentation purposes.
     *
     * @return A list of MapDTO objects representing all available maps.
     */
    public List<MapDTO> getMapListDTO() {
        return MapMapper.toDTO(getMapRepository().getMapList());
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

    /**
     * Retrieves a list of scenarios associated with a specific map.
     * This method filters the scenarios in the repository to return only those
     * that are linked to the provided map.
     *
     * @param map The map for which to retrieve associated scenarios
     * @return A list of Scenario objects that are associated with the specified map
     */
    public List<Scenario> getScenarioListByMap(Map map) {
        return getScenarioRepository().getScenarioListByMap(map);
    }
}
