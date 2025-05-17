package controller;

import domain.*;
import repositories.MapRepository;
import repositories.Repositories;
import repositories.ScenarioRepository;

import java.util.List;

public class CreateScenarioController {
    private ScenarioRepository scenarioRepository;

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

            return "Scenario '" + name + "' created successfully.";
        } catch (IllegalArgumentException e){
            return "ERROR: " + e.getMessage();
        } catch (Exception e){
            return "ERROR: Unexpected error occurred.";
        }
    }

    private ScenarioRepository getScenarioRepository(){
        if(scenarioRepository == null){
            Repositories repo = Repositories.getInstance();
            scenarioRepository = repo.getScenarioRepository();
        }
        return scenarioRepository;
    }

    public List<Scenario> getScenarioList() {
        return getScenarioRepository().getScenarioList();
    }
}
