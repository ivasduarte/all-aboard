package domain;

import repositories.SimulatorRepository;
import repositories.BuildingRepository;
import repositories.StationRepository;

import java.io.*;

/**
 * Class responsible for saving simulation data to files.
 * Implements Runnable and Serializable interfaces to support concurrent execution and object persistence.
 */
public class SimulationSave implements Runnable, Serializable {
    private final String filePath;
    private final SimulatorRepository simRep;
    private final StationRepository stRep;
    private final BuildingRepository bdRep;

    /**
     * Creates a new SimulationSave instance with the specified file path and repositories.
     *
     * @param filePath The path where the simulation will be saved
     * @param simRep The simulator repository to save
     * @param stRep The station repository to save
     * @param bdRep The building repository to save
     */
    public SimulationSave(String filePath, SimulatorRepository simRep, StationRepository stRep, BuildingRepository bdRep) {
        this.filePath = filePath;
        this.simRep = simRep;
        this.stRep = stRep;
        this.bdRep = bdRep;
    }

    /**
     * Implementation of the Runnable interface.
     * Executes the save operation and handles any exceptions that may occur.
     */
    @Override
    public void run() {
        try {
            save(filePath);
        } catch (IOException e) {
            System.out.println("Error saving simulation: " + e.getMessage());
        }
    }

    /**
     * Saves the simulation data to the specified file path.
     * Serializes and writes the repositories to the file.
     *
     * @param filePath The path where the simulation will be saved
     * @throws IOException if there are issues with file operations
     */
    public void save(String filePath) throws IOException {
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(filePath))) {
            out.writeObject(simRep);
            out.writeObject(stRep);
            out.writeObject(bdRep);
            System.out.println("\nSimulation data has been saved to " + filePath);
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
}