package domain;

import java.io.Serializable;
import java.util.Objects;

/**
 * Represents a simulation instance in the system.
 * Contains information about the user, selected map, scenario, budget, and current year.
 * Implements Serializable to support object persistence.
 */
public class Simulator implements Serializable {
    private final User user;
    private final Map selectedMap;
    private final Scenario selectedScenario;

    private Budget budget;
    private int currentYear;

    /**
     * Creates a new Simulator instance with the specified parameters.
     *
     * @param user The user associated with this simulation
     * @param selectedMap The map selected for this simulation
     * @param selectedScenario The scenario selected for this simulation
     * @param budget The budget for this simulation
     * @param currentYear The current year of the simulation
     * @throws IllegalArgumentException if any required parameter is null or if the current year is outside the scenario's date range
     */
    public Simulator(User user, Map selectedMap, Scenario selectedScenario, Budget budget, int currentYear) {
        if (user == null) {
            throw new IllegalArgumentException("User cannot be null");
        }
        if (selectedMap == null) {
            throw new IllegalArgumentException("Map cannot be null");
        }
        if (selectedScenario == null) {
            throw new IllegalArgumentException("Scenario cannot be null");
        }
        if (currentYear < selectedScenario.getRestrictions().getStartDate().getYear() || 
            currentYear > selectedScenario.getRestrictions().getEndDate().getYear()) {
            throw new IllegalArgumentException("Current year must be between start and end dates");
        }
        this.user = user;
        this.selectedMap = selectedMap;
        this.selectedScenario = selectedScenario;
        this.budget = budget;
        this.currentYear = currentYear;
    }

    /**
     * Gets the user associated with this simulation.
     *
     * @return The user object
     */
    public User getUser() {
        return user;
    }

    /**
     * Gets the selected map for this simulation.
     *
     * @return The selected map
     */
    public Map getSelectedMap() {
        return selectedMap;
    }

    /**
     * Gets the selected scenario for this simulation.
     *
     * @return The selected scenario
     */
    public Scenario getSelectedScenario() {
        return selectedScenario;
    }

    /**
     * Gets the current budget for this simulation.
     *
     * @return The current budget
     */
    public Budget getBudget() {
        return budget;
    }

    /**
     * Sets the budget for this simulation.
     *
     * @param budget The new budget to set
     */
    public void setBudget(Budget budget) {
        this.budget = budget;
    }

    /**
     * Gets the current year of the simulation.
     *
     * @return The current year
     */
    public int getCurrentYear() {
        return currentYear;
    }

    /**
     * Sets the current year of the simulation.
     *
     * @param currentYear The new year to set
     * @throws IllegalArgumentException if the year is outside the scenario's date range
     */
    public void setCurrentYear(int currentYear) {
        if (currentYear < selectedScenario.getRestrictions().getStartDate().getYear() || 
            currentYear > selectedScenario.getRestrictions().getEndDate().getYear()) {
            throw new IllegalArgumentException("Current year must be between start and end dates");
        }
        this.currentYear = currentYear;
    }

    /**
     * Returns a string representation of the simulator.
     *
     * @return String containing user name and current year
     */
    @Override
    public String toString() {
        return "Simulator for " + user.getName() + " (Current Year: " + currentYear + ")";
    }

    /**
     * Compares this simulator with another object for equality.
     *
     * @param o The object to compare with
     * @return true if the objects are equal, false otherwise
     */
    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Simulator)) return false;
        Simulator simulator = (Simulator) o;
        return Objects.equals(user, simulator.user) && Objects.equals(selectedMap, simulator.selectedMap) && Objects.equals(selectedScenario, simulator.selectedScenario);
    }

    /**
     * Generates a hash code for this simulator.
     *
     * @return The hash code
     */
    @Override
    public int hashCode() {
        return Objects.hash(user, selectedMap, selectedScenario);
    }
}