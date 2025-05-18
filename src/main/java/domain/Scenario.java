package domain;

/**
 * Represents a scenario with specific attributes such as a name, restrictions, map, and budget.
 * A scenario is defined by its restrictions, environment, and available resources, and includes
 * methods for validation and budget management.
 */
public class Scenario {
    private String name;
    private Restrictions restrictions;
    private Map map;
    private Budget budget;

    /**
     * Constructs a Scenario with a specified name, restrictions, map, and budget.
     * This constructor initializes the scenario with the provided attributes and
     * validates the name upon creation.
     *
     * @param name          The name of the scenario. Must be non-null, non-empty,
     *                      and meet specific criteria such as character constraints.
     * @param restrictions  The restrictions applicable to this scenario, defining
     *                      constraints based on time, locomotive types, and historical events.
     *                      Must be non-null and valid.
     * @param map           The map on which the scenario will take place. Must be non-null.
     * @param budget        The budget allocated for the scenario. Represents the available resources.
     */
    public Scenario(String name, Restrictions restrictions, Map map, Budget budget){
        this.name = name;
        this.restrictions = restrictions;
        this.map = map;
        this.budget = budget;
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
     * @return The restrictions as a {@code Restrictions} object. The returned object
     *         contains constraints based on time, locomotive types, and historical events.
     *         It reflects the restrictions defined for the current scenario instance.
     */
    public Restrictions getRestrictions() {
        return restrictions;
    }

    /**
     * Retrieves the map associated with the scenario.
     *
     * @return  The map as a {@code Map} object. The returned object represents the
     *          two-dimensional layout or configuration applicable to the scenario.
     */
    public Map getMap() {
        return map;
    }

    /**
     * Validates the name of the scenario based on several criteria.
     *
     * This method ensures the name is:
     * 1. Not null or empty.
     * 2. Not exceeding 15 characters in length.
     * 3. Containing at least one uppercase letter.
     * 4. Consisting only of alphanumeric characters.
     *
     * @return  true if the name meets all the criteria, false otherwise.
     */
    public boolean validateName(){
        if(name == null || name.isEmpty()){
            return false;
        } else if(name.length() > 15){
            return false;
        } else if(!name.matches(".*[A-Z].*")){
            return false;
        } else if(!name.matches("[A-Za-z0-9]+")){
            return false;
        }
        return true;
    }

    /**
     * Validates the restrictions associated with the scenario.
     *
     * This method checks whether the restrictions object is non-null and valid by delegating
     * to the {@code validate} method of the {@code Restrictions} class.
     * It ensures that the scenario's restrictions comply with predefined rules,
     * including constraints on time periods, locomotive types, and historical events.
     *
     * @return  true if the restrictions are non-null and valid, false otherwise.
     */
    public boolean validateRestrictions(){
        return restrictions != null && restrictions.validate();
    }

    /**
     * Checks if the map associated with the scenario is valid.
     *
     * This method determines the validity of the map by checking whether
     * the {@code map} field is not null.
     *
     * @return  true if the map is not null, false otherwise.
     */
    public boolean hasValidMap(){
        return map != null;
    }

    /**
     * Validates the overall state of the scenario by checking its individual components.
     *
     * This method ensures the validity of the scenario:
     * - Validates the name using the {@code validateName()} method.
     * - Validates the restrictions using the {@code validateRestrictions()} method.
     * - Verifies the validity of the map using the {@code hasValidMap()} method.
     *
     * @return  true if all components of the scenario are valid, false otherwise.
     */
    public boolean isValid(){
        return validateName() && validateRestrictions() && hasValidMap();
    }

    /**
     * Retrieves the budget associated with the scenario.
     *
     * @return  The budget as a {@code Budget} object. The returned object represents the
     *          allocated resources for the scenario.
     */
    public Budget getBudget() {
        return budget;
    }

    /**
     * Sets the budget for the scenario.
     *
     * @param budget    The {@code Budget} object to be assigned as the new budget for this scenario.
     *                  Represents the allocated resources for the scenario. Must be non-null.
     */
    public void setBudget(Budget budget) {
        this.budget = budget;
    }

    /**
     * Subtracts a specified amount from the current budget and updates the budget value.
     *
     * @param amount    The amount to be subtracted from the budget. Must be a non-negative integer.
     * @return          The updated budget value after subtraction as an integer.
     */
    public int subtractBudget(int amount) {
        budget.setBudget(budget.getBudget() - amount);
        return budget.getBudget();
    }

    /**
     * Adds a specified amount to the current budget and updates the budget value.
     *
     * @param amount    The amount to be added to the budget. Must be a non-negative integer.
     * @return          The updated budget value after addition as an integer.
     */
    public int addBudget(int amount) {
        budget.setBudget(budget.getBudget() + amount);
        return budget.getBudget();
    }

    /**
     * Returns a string representation of the scenario object.
     * The string includes the name, restrictions, and the name of the associated map.
     *
     * @return  A string representation of the scenario in the format:
     *          "{name} ({restrictions}, belongs to map '{mapName}')".
     */
    @Override
    public String toString() {
        return "" + name + " (" + restrictions + ", belongs to map '" + map.getMapName() + "')";
    }
}
