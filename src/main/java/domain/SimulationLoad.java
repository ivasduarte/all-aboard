package domain;

import repositories.Repositories;
import repositories.SimulatorRepository;
import repositories.StationRepository;
import repositories.BuildingRepository;

import java.io.*;

/**
 * Class responsible for loading simulation data from saved files.
 * Implements Runnable and Serializable interfaces to support concurrent execution and object persistence.
 */
public class SimulationLoad implements Runnable, Serializable {
    private final String filePath;

    /**
     * Creates a new SimulationLoad instance with the specified file path.
     *
     * @param filePath The path to the simulation save file
     */
    public SimulationLoad(String filePath) {
        this.filePath = filePath;
    }

    /**
     * Implementation of the Runnable interface.
     * Loads simulation data using the repositories from the Repositories singleton.
     */
    @Override
    public void run() {
        load(Repositories.getInstance().getSimRep(), Repositories.getInstance().getStationRepository(), Repositories.getInstance().getBuildingRepository());
    }

    /**
     * Loads simulation data from a file into the specified repositories.
     *
     * @param simRep The simulator repository to load data into
     * @param stRep The station repository to load data into
     * @param bdRep The building repository to load data into
     * @return true if the load operation was successful, false otherwise
     */
    public boolean load(SimulatorRepository simRep, StationRepository stRep, BuildingRepository bdRep) {
        File file = new File(filePath);

        if (!file.exists()) {
            System.out.println("Error: File '" + filePath + "' does not exist.");
            return false;
        }

        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(file))) {
            SimulatorRepository loadedSimRep = (SimulatorRepository) in.readObject();
            StationRepository loadedStRep = (StationRepository) in.readObject();
            BuildingRepository loadedBdRep = (BuildingRepository) in.readObject();

            simRep.copyFrom(loadedSimRep);
            stRep.copyFrom(loadedStRep);
            bdRep.copyFrom(loadedBdRep);

            System.out.println("Simulation data loaded successfully from " + filePath);
            return true;
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Error loading file: " + e.getMessage());
            return false;
        }
    }
}