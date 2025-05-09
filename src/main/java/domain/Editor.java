package domain;

import repositories.Repositories;
import repositories.ScenarioRepository;


/**
 * Represents an Editor capable of creating and managing scenarios.
 * This class provides functionality for retrieving editor details
 * and supporting the creation of scenarios under defined constraints.
 */
public class Editor {
    private String name;
    private String ID;
    private String password;


    /**
     * Constructs an Editor instance with the specified name, ID, and password.
     *
     * @param name      The name of the editor. It cannot be null or empty.
     * @param ID        A unique identifier for the editor. It cannot be null or empty.
     * @param password  The password associated with the editor's account. It cannot be null or empty.
     */
    public Editor(String name, String ID, String password) {
        this.name = name;
        this.ID = ID;
        this.password = password;
    }

    /**
     * Retrieves the name of the editor.
     *
     * @return The name of the editor as a String.
     */
    public String getEditorName() {
        return name;
    }


    /**
     * Retrieves the password associated with the editor.
     *
     * @return The password of the editor as a String.
     */
    public String getPassword() {
        return password;
    }


    /**
     * Retrieves the unique identifier associated with the editor.
     *
     * @return The unique identifier (ID) of the editor as a String.
     */
    public String getID() {
        return ID;
    }


    /**
     * Creates a new scenario with the specified name, restrictions, and map.
     * The method performs validation on the name, restrictions, and map before creation.
     * It ensures the scenario does not already exist in the repository and then adds it.
     *
     * @param name          The name of the scenario. Must not be null, must have a maximum length of 15 characters,
     *                      must include at least one uppercase letter, and must consist only of alphanumeric characters.
     * @param restrictions  The restrictions associated with the scenario. Must not be null and must pass validation.
     * @param map           The map associated with the scenario. Must not be null.
     * @return              The created Scenario object.
     * @throws              IllegalArgumentException If the map is null, the name is invalid, the restrictions are invalid,
     *                      or if a scenario with the same name already exists.
     */
    public Scenario createScenario(String name, Restrictions restrictions, Map map){
        if(map == null){
            throw new IllegalArgumentException("ERROR. Must select a map for scenario.");
        }

        Scenario scenario = new Scenario(name, restrictions, map);

        if (!scenario.validateName()) {
            throw new IllegalArgumentException("ERROR: Invalid name. Must be under 15 characters, have at least one upper case character, and can't be null.");
        }

        if (!scenario.validateRestrictions()) {
            throw new IllegalArgumentException("ERROR: Time, historical, or technological restrictions are invalid. Please enter again.");
        }

        ScenarioRepository repository = Repositories.getInstance().getScenarioRepository();

        if (!repository.existsByName(name)) {
            throw new IllegalArgumentException("ERROR: Scenario Name already exists.");
        }

        repository.add(scenario);
        return scenario;
    }
}
