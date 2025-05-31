package repositories;

import domain.*;

import java.io.Serializable;
import java.util.List;
import java.util.Optional;

public class SimulatorRepository implements Serializable {
    private final List<Simulator> simulations;

    public SimulatorRepository(){
        simulations = new java.util.ArrayList<>();
    }

    public Simulator add(Simulator sim){
        Optional<Simulator> exist = findBy(sim.getSelectedMap(), sim.getSelectedScenario(), sim.getUser());

        if(exist.isPresent()){
            Simulator existSimulator = exist.get();
            existSimulator.setCurrentYear(sim.getSelectedScenario().getCurrentYear());
            existSimulator.setBudget(sim.getBudget());
            return existSimulator;
        }

        simulations.add(sim);
        return sim;
    }

    public Optional<Simulator> findBy(Map map, Scenario scenario, User user){
        System.out.println("\n\n----------------- LOADING SIMULATION -----------------");
        System.out.println("Map: " + map.getName());
        System.out.println("Scenario: " + scenario.getScenarioName());

        String userEmail;
        if (user != null) {
            userEmail = user.getEmail();
        } else {
            userEmail = "null";
        }

        System.out.println("User: " + userEmail);

        for(Simulator sim : simulations){
            if(sim.getSelectedMap().equals(map) && sim.getSelectedScenario().getScenarioName().equals(scenario) && sim.getUser().equals(user)){
                return Optional.of(sim);
            }
        }
        System.out.println("No simulation found.");
        System.out.println();
        return Optional.empty();
    }


    public void update(Map map, Scenario scenario, User user, int year, Budget b){
        for(Simulator sim : simulations){
            if(sim.getSelectedMap().equals(map) && sim.getSelectedScenario().getScenarioName().equals(scenario) && sim.getUser().equals(user)){
                sim.setCurrentYear(year);
                sim.setBudget(b);
            }
        }
    }

    public List<Simulator> getSimulations(){
        return List.copyOf(simulations);
    }

    public void copyFrom(SimulatorRepository simulator2){
        this.simulations.clear();
        this.simulations.addAll(simulator2.getSimulations());
    }
}