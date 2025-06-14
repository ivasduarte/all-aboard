package controller;

import domain.Map;
import domain.Scenario;
import repositories.MapRepository;
import repositories.Repositories;
import repositories.ScenarioRepository;

import java.io.FileInputStream;
import java.io.ObjectInputStream;
import java.util.List;

/**
 * Controller class responsible for managing the saving and loading of scenarios to/from files.
 * Provides functionality to save scenarios to files and load them back, as well as
 * retrieving lists of available maps and scenarios.
 */
public class SaveScenarioController {

    private final ScenarioRepository scenarioRepository;

    private final MapRepository mapRepository;

    /**
     * Constructs a new instance of SaveScenarioController.
     * Initializes the scenario and map repositories by obtaining them from the Repositories singleton.
     */
    public SaveScenarioController() {
        this.scenarioRepository = Repositories.getInstance().getScenarioRepository();
        this.mapRepository = Repositories.getInstance().getMapRepository();
    }

    /**
     * Saves a scenario to a file.
     * The scenario is serialized and stored in a file named after the scenario.
     * If a file with the same name already exists, the user will be prompted to confirm overwriting.
     *
     * @param scenario The scenario to be saved. Cannot be null.
     * @throws IllegalArgumentException if the scenario is null.
     */
    public void saveScenarioInFile(Scenario scenario) {
        if (scenario == null) {
            throw new IllegalArgumentException("Scenario is null");
        }
        scenarioRepository.saveScenarioToFile(scenario);
    }

    /**
     * Retrieves a list of all available maps.
     *
     * @return A list containing all maps in the system.
     */
    public List<Map> getMapList() {
        return mapRepository.getMapList();
    }

    /**
     * Retrieves a list of all available scenarios.
     *
     * @return A list containing all scenarios in the system.
     */
    public List<Scenario> getScenarioList() {
        return scenarioRepository.getScenarioList();
    }

    /**
     * Loads a scenario from a file.
     * The scenario is deserialized from the specified file and associated with the given map.
     *
     * @param fileName The name of the file to load the scenario from.
     * @param selectedMap The map to associate with the loaded scenario.
     * @return The loaded scenario, or null if loading fails.
     */
    public Scenario loadScenarioFromFile(String fileName, Map selectedMap) {
        return scenarioRepository.loadScenarioFromFile(fileName, selectedMap);
    }
}