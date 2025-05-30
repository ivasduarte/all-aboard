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

        // Auto-start the simulation
        new Thread(controller).start();
        System.out.println("Simulation started automatically.");

        List<MenuItem> options = new ArrayList<>();
        options.add(0, new MenuItem("Resume Simulation", () -> {
            controller.resumeSimulation();
            System.out.println("Simulation resumed.");
        }));
        options.add(1, new MenuItem("Pause Simulation", () -> {
            controller.pauseSimulation();
            System.out.println("Simulation paused. You can now navigate the menu.");
        }));
        options.add(2, new MenuItem("Stop and Save Simulation", () -> {
            controller.stopSimulation();
            controller.saveSimulation(simulator);
            System.out.println("Simulation stopped and saved.");
        }));

        boolean exit = false;

        while (!exit) {
            // Show current simulation status
            String status = controller.isPaused() ? "PAUSED" :
                    controller.isRunning() ? "RUNNING" : "STOPPED";
            System.out.println("\nCurrent Status: " + status);

            int option = Utils.showAndSelectIndex(options, "\n--- SIMULATION MENU ---");
            if (option >= 0 && option < options.size()) {
                options.get(option).run();
            } else {
                // User selected cancel/exit (option 0 or invalid)
                exit = true;
                if (controller.isRunning()) {
                    System.out.println("Stopping simulation...");
                    controller.stopSimulation();
                    // Give simulation thread a moment to stop gracefully
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    }
                    controller.saveSimulation(simulator);
                    System.out.println("Simulation ended and saved.");
                } else {
                    System.out.println("Returning to main menu...");
                }
            }
        }
    }
}