package repositories;

import domain.Locomotive;
import domain.Restrictions;
import domain.Scenario;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class LocomotiveRepository implements Serializable {
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
     * This method is responsible to give the list of all locomotives in the game.
     *
     * @return List of all locomotives
     */
    public List<Locomotive> getAllLocomotives() {
        return locomotives;
    }

    /**
     * This method is responsible to get a list of the available locomotives based on the scenario restrictions.
     *
     * @param scenarioId The id of the scenario that is being played.
     *
     * @return list of available locomotives
     */
    public List<Locomotive> getAvailableLocomotives(int scenarioId) {
        availableLocomotives.clear();
        List<Scenario> scenarios = Repositories.getInstance().getScenarioRepository().getScenarioList();
        Scenario scenario = scenarios.get(scenarioId);
        Restrictions scenarioRestrictions = scenario.getRestrictions();

        for (int i = 0; i < locomotives.size(); i++) {

            boolean alreadyAdded = false;

            if (locomotives.get(i).getStartYear() <= scenario.getCurrentYear() && scenario.getCurrentYear() <= locomotives.get(i).getEndYear()) {
                for (String techRestrictions : scenarioRestrictions.getLocomotiveTypes()) {
                    if (!alreadyAdded && techRestrictions.equalsIgnoreCase(locomotives.get(i).getLocomotiveFuelType())) {
                        availableLocomotives.add(locomotives.get(i));
                        alreadyAdded = true;
                    }
                }
            }
        }

        return availableLocomotives;
    }

    /**
     * This method returns a list of the locomotives the player has already bought.
     *
     * @return List of player owned locomotives
     */
    public List<Locomotive> getPlayerLocomotives() {
        return playerLocomotives;
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
     * This method returns whether or not the a specific locomotive is available to the player.
     *
     * @param locomotiveModel the specific locomotive model
     *
     * @return True if the locomotive is available. If not it returns False.
     */
    public boolean isAvailableToPlayer(Locomotive locomotiveModel) {
        for (Locomotive locomotive : availableLocomotives) {
            if (locomotive.getLocomotiveModel() != null) {
                return true;
            }
        }
        return false;
    }

    /**
     * This method verifies what locomotives become available for the player.
     *
     * @param locomotiveModel the model of a locomotive
     */
    public void unlockForPlayer(Locomotive locomotiveModel) {
        for (Locomotive locomotive : locomotives) {
            if (locomotive.getLocomotiveModel() != null) {
                if (!availableLocomotives.contains(locomotive)) {
                    availableLocomotives.add(locomotive);
                }
                return;
            }
        }
        throw new IllegalArgumentException("Locomotive with model " + locomotiveModel + " not found");
    }
}
