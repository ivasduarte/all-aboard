package domain;

import java.io.Serializable;
import java.util.Objects;

public class Simulator implements Serializable {
    private final User user;
    private final Map selectedMap;
    private final Scenario selectedScenario;

    private Budget budget;
    private int currentYear;

    public Simulator(User user, Map selectedMap, Scenario selectedScenario, Budget budget, int currentYear) {
        this.user = user;
        this.selectedMap = selectedMap;
        this.selectedScenario = selectedScenario;
        this.budget = budget;
        this.currentYear = currentYear;
    }

    public User getUser() {
        return user;
    }

    public Map getSelectedMap() {
        return selectedMap;
    }

    public Scenario getSelectedScenario() {
        return selectedScenario;
    }

    public Budget getBudget() {
        return budget;
    }

    public void setBudget(Budget budget) {
        this.budget = budget;
    }

    public int getCurrentYear() {
        return currentYear;
    }

    public void setCurrentYear(int currentYear) {
        this.currentYear = currentYear;
    }

    @Override
    public String toString() {
        return "Simulator{" +
                "user=" + user +
                ", selectedMap=" + selectedMap +
                ", selectedScenario=" + selectedScenario +
                ", budget=" + budget +
                ", currentYear=" + currentYear +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Simulator)) return false;
        Simulator simulator = (Simulator) o;
        return Objects.equals(user, simulator.user) && Objects.equals(selectedMap, simulator.selectedMap) && Objects.equals(selectedScenario, simulator.selectedScenario);
    }

    @Override
    public int hashCode() {
        return Objects.hash(user, selectedMap, selectedScenario);
    }
}