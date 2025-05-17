package controller;

import domain.*;
import repositories.LocomotiveRepository;
import repositories.Repositories;
import repositories.ScenarioRepository;



public class BuyLocomotiveController {
    private LocomotiveRepository locomotiveRepository;

    /**
     * This method will be responsible to add the bought locomotive to the repository.
     *
     * @param locomotiveModel locomotive model
     *
     * @return Will show a success message if it is possible to buy the locomotive. Otherwise, it will show a message related to the error that occurred.
     */
    public String buyLocomotive(String locomotiveModel, String mapName) {
        LocomotiveRepository locomotiveRepository = Repositories.getInstance().getLocomotiveRepository();
        ScenarioRepository scenarioRepository = Repositories.getInstance().getScenarioRepository();

        Scenario scenario = null;

        for (Scenario s : scenarioRepository.getScenarioList()) {
            if (s.getMap().getMapName().equalsIgnoreCase(mapName)) {
                scenario = s;
            }
        }

        if (scenario == null) {
            return "ERROR: No scenario found for the selected map.";
        }

        Locomotive locomotive = locomotiveRepository.getLocomotiveByModel(locomotiveModel);

        if (scenario.getBudget().getBudget() >= locomotive.getPrice()) {
            scenario.subtractBudget(locomotive.getPrice());
            locomotiveRepository.save(locomotive);
            return "Locomotive " + locomotiveModel + " bought successfully.";
        } else {
            return "ERROR: Player doesn't have enough budget.";
        }
    }

    /**
     * This method allows the controller to get the locomotive repository.
     *
     * @return locomotive repository
     */
    public LocomotiveRepository getLocomotiveRepository() {
        if (locomotiveRepository == null) {
            Repositories repositories = Repositories.getInstance();
            locomotiveRepository = repositories.getLocomotiveRepository();
        }
        return locomotiveRepository;
    }
}
