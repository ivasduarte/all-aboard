package presentation;

import controller.SimulatorController;
import domain.*;
import repositories.Repositories;
import ui.menu.MenuItem;
import ui.utils.Utils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class SimulationUI implements Runnable, Serializable {

    private final SimulatorController controller;
    private final Map selectedMap;
    private final Scenario selectedScenario;

    private static final String DEFAULT_SIMULATION_FILE = "simulations.sim";

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

        loadSimulation();

        Simulator simulator = controller.startOrResumeSimulation();

        // Auto-start the simulation
        new Thread(controller).start();
        System.out.println("Simulation started automatically.");

        List<MenuItem> options = new ArrayList<>();
        options.add(new MenuItem("Resume Simulation", () -> {
            controller.resumeSimulation();
            System.out.println("Simulation resumed.");
        }));
        options.add(new MenuItem("Pause Simulation", () -> {
            controller.pauseSimulation();
            System.out.println("Simulation paused. You can now navigate the menu.");
        }));
        options.add(new MenuItem("Stop and Save Simulation", () -> {
            controller.stopSimulation();
            saveSimulation();
        }));

        boolean exit = false;

        while (!exit) {
            String status = controller.isPaused() ? "PAUSED" :
                    controller.isRunning() ? "RUNNING" : "STOPPED";
            System.out.println("\nCurrent Status: " + status);

            int option = Utils.showAndSelectIndex(options, "\n--- SIMULATION MENU ---");
            if (option >= 0 && option < options.size()) {
                options.get(option).run();
            } else {
                exit = true;
                if (controller.isRunning()) {
                    System.out.println("Stopping simulation...");
                    controller.stopSimulation();
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    }
                    saveSimulation();
                    System.out.println("Simulation ended and saved.");
                } else {
                    System.out.println("Returning to main menu...");
                }
            }
        }
    }

    private void saveSimulation() {
        SimulationSave saver = new SimulationSave(DEFAULT_SIMULATION_FILE, Repositories.getInstance().getSimRep(), Repositories.getInstance().getStationRepository(), Repositories.getInstance().getBuildingRepository());
        new Thread(saver).start();
    }

    private boolean loadSimulation() {
        System.out.println("Attempting to load previous simulation...");
        boolean success = new SimulationLoad(DEFAULT_SIMULATION_FILE).load(Repositories.getInstance().getSimRep(), Repositories.getInstance().getStationRepository(), Repositories.getInstance().getBuildingRepository());

        if (success) {
            System.out.println("Previous simulation loaded.");
        } else {
            System.out.println("No previous simulation found or failed to load.");
        }
        return success;
    }
}