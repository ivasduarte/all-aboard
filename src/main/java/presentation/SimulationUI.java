package presentation;

import controller.SimulatorController;
import domain.Map;
import domain.Scenario;
import domain.Simulator;
import ui.menu.MenuItem;
import ui.utils.Utils;

import java.util.ArrayList;
import java.util.List;

public class SimulationUI implements Runnable {
    private final SimulatorController controller;
    private final Map selectedMap;
    private final Scenario selectedScenario;

    public SimulationUI(Map map, Scenario scenario) {
        this.selectedMap = map;
        this.selectedScenario = scenario;
        this.controller = new SimulatorController(map, scenario);
    }

    @Override
    public void run() {
        System.out.println("\n----------------- SIMULATION START -----------------");

        if (selectedMap == null || selectedScenario == null) {
            System.out.println("ERROR: Map or scenario not selected.");
            return;
        }

        Simulator simulator = controller.startOrResumeSimulation();

        List<MenuItem> options = new ArrayList<>();
        options.add(0, new MenuItem("Start Simulation", controller::run));

        boolean exit = false;

        while (!exit) {
            int option = Utils.showAndSelectIndex(options, "\n--- SIMULATION MENU ---");
            if (option >= 0 && option < options.size()) {
                options.get(option).run();
            } else {
                exit = true;
                controller.saveSimulation(simulator);
                System.out.println("Simulation ended and saved.");
            }
        }
    }
}