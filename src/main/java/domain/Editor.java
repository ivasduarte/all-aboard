package domain;

import repositories.Repositories;
import repositories.ScenarioRepository;

public class Editor {
    private String name;
    private String ID;
    private String password;

    public Editor(String name, String ID, String password) {
        this.name = name;
        this.ID = ID;
        this.password = password;
    }

    //getters
    public String getEditorName() {
        return name;
    }

    public String getPassword() {
        return password;
    }

    public String getID() {
        return ID;
    }

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
