package domain;


import java.io.Serializable;

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
        this.name = name;
        this.restrictions = restrictions;
        this.map = map;
        this.budget = budget;

        this.currentYear = restrictions.getStartDate().getYear();
        this.finalYear = restrictions.getEndDate().getYear();
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
     * Retrives the current year of the scenario, changed by the simulation.
     *
     * @return The current year of the scenario.
     */
    public int getCurrentYear() { return currentYear;}

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
        if(name == null || name.isEmpty()){
            return false;
        } else if(name.length() > 15){
            return false;
        } else if(!name.matches("[A-Za-z0-9_]+")){
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

    public void setMap(Map map) { this.map = map; }

    /**
     * Validates the state of the current scenario.
     * This method checks the validity of the scenario's name, its restrictions,
     * and the associated map by invoking respective validation methods.
     *
     * @return true if the scenario's name, restrictions, and map are all valid; false otherwise.
     */
    public boolean isValid(){
        return validateName() && validateRestrictions() && hasValidMap();
    }

    public Budget getBudget() {
        return budget;
    }

    public void setBudget(Budget budget) {
        this.budget = budget;
    }

    public int subtractBudget(int amount) {
        budget.setBudget(budget.getBudget() - amount);
        return budget.getBudget();
    }

    public int addBudget(int amount) {
        budget.setBudget(budget.getBudget() + amount);
        return budget.getBudget();
    }

    @Override
    public String toString() {
        return name;
    }

    /**
     * Returns a string representation of the Scenario object.
     * The representation includes the scenario name, its restrictions,
     * and the name of the associated map.
     *
     * @return A string describing the scenario, formatted as "name (restrictions, belongs to map 'mapName')".
     */



    public double getModificationRateFor() {
        return modificationRateFor;
    }

    public void setModificationRateFor(double modificationRateFor) {
        this.modificationRateFor = modificationRateFor;
    }


}