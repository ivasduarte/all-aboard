package repositories;

import java.util.ArrayList;

public class ScenarioRepository {
    private List<Scenario> scenarios;

    public ScenarioRepository() {
        scenarios = new ArrayList<>();
    }

    public void add(Scenario scenario){
        if(validateScenario(scenario)){
            scenarios.add(scenario);
        }
    }

    private boolean validateScenario(Scenario scenario){
        System.out.println(!scenarios.contains(scenario));
        return !scenarios.contains(scenario);
    }

    public boolean existsByName(String name) {
        for (int i = 0; i < scenarios.size(); i++){
            if(scenarios.get(i).getScenarioName().equalsIgnoreCase(name)){
                return true;
            }
        }
        return false;
    }

    public List<Scenario> getScenarioList(){
        return List.cofyOf(scenarios);
    }

    public Optional<Scenario> getScenario(String name){
        return scenarios.stream().filter(s -> s.getScenarioName().equals(name)).findFirst();
    }

    public void displayScenarioList() {
        for (int i = 0; i < scenarios.size(); i++) {
            System.out.println((i+1) + ". " + scenarios.get(i));
        }
    }
}
