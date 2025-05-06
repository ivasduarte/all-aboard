package controller;

import domain.*;
import repositories.*;

public class CreateScenarioController {

    public String createScenario(String name, Restrictions restrictions, int mapID) {
        try {
            UserSession userSession = ApplicationSession.getInstance().getCurrentSession();

            if (!(userSession.getUser() instanceof Editor)) {
                return "ERROR: Only Editors can create scenarios.";
            }

            Editor editor = (Editor) userSession.getUser();

            Map selectedMap = findMapById(mapID);
            if (selectedMap == null) {
                return "ERROR. Must select a map for scenario.";
            }

            ScenarioRepository scenarioRepository = Repositories.getInstance().getScenarioRepository();

            if (!scenarioRepository.existsByName(name)) {
                return "ERROR: Scenario Name already exists.";
            }

            Scenario scenario = editor.createScenario(name, restrictions, selectedMap);
            scenarioRepository.save(scenario);

            return "Scenario '" + name + "' created successfully.";

        } catch (IllegalArgumentException e) {
            return "ERROR: " + e.getMessage();
        } catch (Exception e) {
            return "ERROR: Unexpected error occurred.";
        }
    }
}
