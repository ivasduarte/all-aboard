package repositories;

import domain.*;
import presentation.SimulationUI;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Repository class for managing simulator instances.
 * Implements Serializable to support object persistence.
 */
public class SimulatorRepository implements Serializable {
    private final List<Simulator> simulations;
    private SimulationUI ui;

    /**
     * Creates a new SimulatorRepository instance.
     * Initializes an empty list to store simulations.
     */
    public SimulatorRepository(){
        this.simulations = new ArrayList<>();
    }

    public void setUI(SimulationUI ui) {
        this.ui = ui;
    }

    /**
     * Adds a new simulator to the repository or updates an existing one.
     * If a simulator with the same map, scenario, and user exists, updates its year and budget.
     *
     * @param sim The simulator to add or update
     * @return The added or updated simulator
     */
    public Simulator add(Simulator sim){
        simulations.add(sim);
        return sim;
    }

    /**
     * Finds a simulator by its map, scenario, and user.
     * Prints information about the search parameters.
     *
     * @param map The map to search for
     * @param scenario The scenario to search for
     * @param user The user to search for
     * @return An Optional containing the found simulator, or empty if not found
     */
    public Optional<Simulator> findBy(Map map, Scenario scenario, User user){
        if (ui != null) {
            ui.appendLog("\n\n----------------- LOADING SIMULATION -----------------");
            ui.appendLog("Map: " + map.getName());
            ui.appendLog("Scenario: " + scenario.getScenarioName());

            String userEmail;
            if (user != null) {
                userEmail = user.getEmail();
            } else {
                userEmail = "null";
            }

            ui.appendLog("User: " + userEmail);
        }

        for(Simulator sim : simulations){
            if(sim.getSelectedMap().equals(map) && sim.getSelectedScenario().getScenarioName().equals(scenario) && sim.getUser().equals(user)){
                return Optional.of(sim);
            }
        }

        if (ui != null) {
            ui.appendLog("No simulation found.");
            ui.appendLog("");
        }
        return Optional.empty();
    }

    /**
     * Updates an existing simulator's year and budget.
     *
     * @param map The map of the simulator to update
     * @param scenario The scenario of the simulator to update
     * @param user The user of the simulator to update
     * @param year The new year to set
     * @param b The new budget to set
     */
    public void update(Map map, Scenario scenario, User user, int year, Budget b){
        for(Simulator sim : simulations){
            if(sim.getSelectedMap().equals(map) && sim.getSelectedScenario().getScenarioName().equals(scenario) && sim.getUser().equals(user)){
                sim.setCurrentYear(year);
                sim.setBudget(b);
            }
        }
    }

    /**
     * Gets an unmodifiable list of all simulations in the repository.
     *
     * @return An unmodifiable list of all simulations
     */
    public List<Simulator> getSimulations(){
        return List.copyOf(simulations);
    }

    /**
     * Copies all simulations from another repository.
     * Clears the current repository and adds all simulations from the source repository.
     *
     * @param simulator2 The source repository to copy from
     */
    public void copyFrom(SimulatorRepository simulator2){
        this.simulations.clear();
        this.simulations.addAll(simulator2.getSimulations());
    }
}