package domain;


import java.util.Objects;
import java.util.Optional;

/**
 * Represents an Editor capable of creating and managing scenarios.
 * This class provides functionality for retrieving editor details
 * and supporting the creation of scenarios under defined constraints.
 */
public class User {
    private String name;
    private String email;
    private String roleId;
    private String password;
    private Budget budget;


    public User(String name, String email, String roleId, String password, Budget budget) {
        this.name = name;
        this.email = email;
        this.roleId = roleId;
        this.password = password;
        this.budget = budget;
    }

    public String getRoleId() {
        return roleId;
    }

    public void setRoleId(String roleId) {
        this.roleId = roleId;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Budget getBudget() {
        return budget;
    }

    public int getBudget1() {
        return budget.getBudget();
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
}
