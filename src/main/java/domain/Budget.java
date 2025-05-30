package domain;

/**
 * Represents a budget with basic functionalities to retrieve and modify its value.
 */
public class Budget {
    int budget;

    /**
     * Constructs a Budget object with a specified initial budget value.
     *
     * @param budget    The initial value of the budget.
     */
    public Budget(int budget) {
        this.budget = budget;
    }

    /**
     * Retrieves the current budget value.
     *
     * @return  The current budget as an integer.
     */
    public int getBudget() {
        return budget;
    }

    /**
     * Sets the budget value.
     *
     * @param budget    The new budget value to be assigned.
     */
    public void setBudget(int budget) {
        this.budget = budget;
    }

}