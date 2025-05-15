package repositories;

import domain.Locomotive;
import domain.Restrictions;
import domain.Scenario;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class LocomotiveRepository {
    private List<Locomotive> locomotives = new ArrayList<>();
    private List<Locomotive> availableLocomotives = new ArrayList<>();
    private List<Locomotive> playerLocomotives = new ArrayList<>();

    /**
     * This method is responsible to add locomotives to the list of all locomotives available in the simulator.
     *
     * @param locomotive predefined locomotives
     */
    public void saveLocomotive(Locomotive locomotive) {
        locomotives.add(locomotive);
    }

    /**
     * This method saves the locomotives to the list of bought locomotives.
     *
     * @param locomotive bought locomotive
     */
    public void save(Locomotive locomotive) {
        playerLocomotives.add(locomotive);
    }

    /**
     * This method is responsible to get a list of the available locomotives based on the scenario restrictions.
     *
     * @param scenarioId The id of the scenario that is being played.
     *
     * @return list of available locomotives
     */
    public List<Locomotive> getAvailableLocomotives(int scenarioId) {
        List<Scenario> scenarios = Repositories.getInstance().getScenarioRepository().getScenarioList();
        Scenario scenario = scenarios.get(scenarioId);
        Restrictions scenarioRestrictions = scenario.getRestrictions();
        LocalDate scenarioStartDate = scenarioRestrictions.getStartDate();

        for (int i = 0; i < locomotives.size(); i++) {
            if (locomotives.get(i).getStartYear() <= scenarioStartDate.getYear() && locomotives.get(i).getEndYear() >= scenarioStartDate.getYear()) {
                for (String techRestrictions : scenarioRestrictions.getLocomotiveTypes()) {
                    if (techRestrictions.equalsIgnoreCase(locomotives.get(i).getLocomotiveFuelType())) {
                        availableLocomotives.add(locomotives.get(i));
                    }
                }
            }
        }

        return availableLocomotives;
    }

    /**
     * This method is responsible to get a specific locomotive by its model.
     *
     * @param locomotiveModel locomotive model
     *
     * @return corresponding locomotive
     */
    public Locomotive getLocomotiveByModel(String locomotiveModel) {
        for (Locomotive locomotive : availableLocomotives) {
            if (locomotive.getLocomotiveModel().equalsIgnoreCase(locomotiveModel)) {
                return locomotive;
            }
        }
        throw new IllegalArgumentException("Locomotive with model " + locomotiveModel + " doesn't exist.");
    }

    /**
     * This method is responsible to show the locomotive list in the form of a list.
     */
    public void displayAvailableLocomotives() {
        for (int i = 0; i < availableLocomotives.size(); i++) {
            System.out.println((i+1) + ". " + availableLocomotives.get(i).getLocomotiveModel() + ": " + availableLocomotives.get(i).getPrice() + "€");
        }
    }
}
