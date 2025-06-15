package domain;


import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Represents a scenario with a specific name, associated restrictions, and map details.
 * This class includes functionality to validate its properties to ensure they meet specified requirements.
 */
public class Scenario implements Serializable {
    private String name;
    private Restrictions restrictions;
    private Map map;
    private Budget budget;

    private int currentYear;
    private int finalYear;
    private double modificationRateFor;
    private double productionRate;

    private List<Train> trains;
    /**
     * Creates a new Scenario instance with the specified name, restrictions, and map.
     * The constructor initializes the scenario's attributes and performs a validation
     * check on the name to ensure it meets required constraints.
     *
     * @param name         The name of the scenario. Must not be null or empty,
     *                     must have a maximum length of 15 characters, contain at least
     *                     one uppercase letter, and consist only of alphanumeric characters.
     * @param restrictions The restrictions associated with the scenario. Cannot be null.
     * @param map          The map linked to the scenario. Cannot be null.
     */
    public Scenario(String name, Restrictions restrictions, Map map, Budget budget){
        if (budget == null) {
            throw new IllegalArgumentException("Budget cannot be null");
        }
        this.name = name;
        this.restrictions = restrictions;
        this.map = map;
        this.budget = budget;
        this.productionRate = productionRate;

        trains = new ArrayList<>();
        if (restrictions != null) {
            this.currentYear = restrictions.getStartDate().getYear();
            this.finalYear = restrictions.getEndDate().getYear();
        }

        validateName();
    }


    /**
     * Retrieves the name of the scenario.
     *
     * @return The name of the scenario as a String.
     */
    public String getScenarioName() {
        return name;
    }


    /**
     * Retrieves the restrictions associated with the scenario.
     *
     * @return The restrictions linked to the scenario as a {@code Restrictions} object.
     */
    public Restrictions getRestrictions() {
        return restrictions;
    }


    /**
     * Retrieves the map associated with the scenario.
     *
     * @return The map linked to the scenario as a {@code Map} object.
     */
    public Map getMap() {
        return map;
    }


    /**
     * Retrieves the current year of the scenario, changed by the simulation.
     *
     * @return The current year of the scenario.
     */
    public int getCurrentYear() { return currentYear;}

    /**
     * Retrieves the production rate.
     *
     * @return The scenario's production rate
     */
    public double getProductionRate() {
        return productionRate;
    }

    /**
     * Retrieves the current trains of the scenario.
     *
     * @return the current trains of the scenario.
     */
    public List<Train> getTrains() {return trains;}

    /**
     * Change the current year of the scenario.
     *
     * @param currentYear The new year when changing.
     */
    public void setCurrentYear(int currentYear) { this.currentYear = currentYear; }

    /**
     * Validates the name of the scenario against specific constraints.
     * The name must not be null or empty, must have a maximum length of 15 characters,
     * must consist only of alphanumeric characters.
     *
     * @return true if the name satisfies all constraints; false otherwise.
     */
    public boolean validateName(){
        if(name == null || name.trim().isEmpty()) {
            return false;
        } else if(name.length() > 15) {
            return false;
        } else if(!name.matches("[A-Za-z0-9_]+")) {
            return false;
        }
        return true;
    }


    /**
     * Validates the restrictions associated with the scenario.
     * This method checks if the restrictions object is not null and if it passes its own validation.
     *
     * @return true if the restrictions exist and are valid; false otherwise.
     */
    public boolean validateRestrictions(){
        return restrictions != null && restrictions.validate();
    }


    /**
     * Checks if the map associated with the scenario is valid.
     * A map is considered valid if it is not null.
     *
     * @return true if the map is not null; false otherwise.
     */
    public boolean hasValidMap(){
        return map != null;
    }

    /**
     * Sets the map associated with this scenario.
     *
     * @param map The map to be associated with this scenario
     */
    public void setMap(Map map) { this.map = map; }

    /**
     * Retrieves the budget associated with this scenario.
     *
     * @return The budget object containing the scenario's financial information
     */
    public Budget getBudget() {
        return budget;
    }

    /**
     * Sets the budget for this scenario.
     *
     * @param budget The new budget to be associated with this scenario
     */
    public void setBudget(Budget budget) {
        if (budget == null) {
            throw new IllegalArgumentException("Budget cannot be null");
        }
        this.budget = budget;
    }

    /**
     * Subtracts a specified amount from the scenario's budget.
     *
     * @param amount The amount to subtract from the budget
     * @return The remaining budget after subtraction
     */
    public int subtractBudget(int amount) {
        if (amount < 0) {
            throw new IllegalArgumentException("Amount cannot be negative");
        }
        if (amount > budget.getBudget()) {
            throw new IllegalArgumentException("Amount cannot be greater than current budget");
        }
        budget.setBudget(budget.getBudget() - amount);
        return budget.getBudget();
    }

    /**
     * Adds a specified amount to the scenario's budget.
     *
     * @param amount The amount to add to the budget
     * @return The new budget total after addition
     */
    public int addBudget(int amount) {
        if (amount < 0) {
            throw new IllegalArgumentException("Amount cannot be negative");
        }
        budget.setBudget(budget.getBudget() + amount);
        return budget.getBudget();
    }

    /**
     * Adds a new train to the scenario's collection of trains.
     *
     * @param train The train to be added to the scenario
     */
    public void addNewTrain(Train train) {
        trains.add(train);
    }

    /**
     * Returns a string representation of the Scenario object.
     * The representation includes the scenario name, its restrictions,
     * and the name of the associated map.
     *
     * @return A string describing the scenario, formatted as "name (restrictions, belongs to map 'mapName')".
     */
    @Override
    public String toString() {
        return name + " - " + map.getName();
    }

    /**
     * Retrieves the modification rate for this scenario.
     * The modification rate represents the rate at which changes can be made
     * to the scenario's elements.
     *
     * @return The modification rate as a double value
     */
    public double getModificationRateFor() {
        return modificationRateFor;
    }

    /**
     * Sets the modification rate for this scenario.
     * The modification rate represents the rate at which changes can be made
     * to the scenario's elements.
     *
     * @param modificationRateFor The new modification rate to be set
     * @throws IllegalArgumentException if the modification rate is negative
     */
    public void setModificationRateFor(double modificationRateFor) {
        if (modificationRateFor < 0) {
            throw new IllegalArgumentException("Modification rate cannot be negative");
        }
        this.modificationRateFor = modificationRateFor;
    }

    /**
     * Compares this scenario with the specified object for equality.
     * Two scenarios are considered equal if they have the same name,
     * as scenario names should be unique identifiers within the system.
     *
     * @param obj the object to be compared for equality with this scenario
     * @return {@code true} if the specified object is equal to this scenario;
     *         {@code false} otherwise
     * @see #hashCode()
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Scenario scenario = (Scenario) obj;
        return Objects.equals(name, scenario.name);
    }

    /**
     * Returns the hash code value for this scenario.
     * The hash code is computed based on the scenario's name to ensure
     * that scenarios with the same name have the same hash code,
     * maintaining the general contract of hashCode in relation to equals.
     *
     * @return a hash code value for this scenario
     * @see #equals(Object)
     */
    @Override
    public int hashCode() {
        return Objects.hash(name);
    }
}