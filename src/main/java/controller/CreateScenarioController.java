package controller;

import domain.*;
import repositories.Repositories;
import repositories.ScenarioRepository;

public class CreateScenarioController {

    public String createScenario(String name, Restrictions restrictions, Map map) {
        try {
            UserSession userSession = ApplicationSession.getInstance().getCurrentSession();
            ScenarioRepository scenarioRepository = Repositories.getInstance().getScenarioRepository();

            if (scenarioRepository.existsByName(name)) {
                return "ERROR: A scenario with this name already exists.";
            }

            Scenario scenario = new Scenario(name, restrictions, map);
            scenarioRepository.add(scenario);

            return "Scenario '" + name + "' created successfully.";
        } catch (IllegalArgumentException e){
            return "ERROR: " + e.getMessage();
        } catch (Exception e){
            return "ERROR: Unexpected error occurred.";
        }
    }
}
