package domain;

import repositories.Repositories;
import repositories.SimulatorRepository;
import repositories.StationRepository;
import repositories.BuildingRepository;
import presentation.SimulationUI;

import java.io.*;

/**
 * Class responsible for loading simulation data from files.
 * Implements Runnable and Serializable interfaces to support concurrent execution and object persistence.
 */
public class SimulationLoad implements Runnable, Serializable {
    private final String filePath;
    private SimulationUI ui;

    /**
     * Creates a new SimulationLoad instance with the specified file path.
     *
     * @param filePath The path from where the simulation will be loaded
     */
    public SimulationLoad(String filePath) {
        this.filePath = filePath;
    }

    public void setUI(SimulationUI ui) {
        this.ui = ui;
    }

    /**
     * Implementation of the Runnable interface.
     * Executes the load operation and handles any exceptions that may occur.
     */
    @Override
    public void run() {
        try {
            load(Repositories.getInstance().getSimRep(),
                 Repositories.getInstance().getStationRepository(),
                 Repositories.getInstance().getBuildingRepository());
        } catch (Exception e) {
            if (ui != null) {
                ui.appendLog("Error loading simulation: " + e.getMessage());
            } else {
                System.err.println("Error loading simulation: " + e.getMessage());
            }
        }
    }

    /**
     * Loads the simulation data from the specified file path.
     * Deserializes and reads the repositories from the file.
     *
     * @param simRep The simulator repository to load into
     * @param stRep The station repository to load into
     * @param bdRep The building repository to load into
     * @return true if the load was successful, false otherwise
     */
    public boolean load(SimulatorRepository simRep, StationRepository stRep, BuildingRepository bdRep) {
        File file = new File(filePath);
        if (!file.exists()) {
            if (ui != null) {
                ui.appendLog("Error: File '" + filePath + "' does not exist.");
            }
            return false;
        }

        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(file))) {
            SimulatorRepository loadedSimRep = (SimulatorRepository) in.readObject();
            StationRepository loadedStRep = (StationRepository) in.readObject();
            BuildingRepository loadedBdRep = (BuildingRepository) in.readObject();

            simRep.copyFrom(loadedSimRep);
            stRep.copyFrom(loadedStRep);
            bdRep.copyFrom(loadedBdRep);
            
            if (ui != null) {
                ui.appendLog("Simulation data loaded successfully from " + filePath);
            }
            return true;
        } catch (IOException | ClassNotFoundException e) {
            if (ui != null) {
                ui.appendLog("Error loading file: " + e.getMessage());
            }
            return false;
        }
    }
}