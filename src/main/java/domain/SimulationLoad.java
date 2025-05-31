package domain;

import repositories.Repositories;
import repositories.SimulatorRepository;
import repositories.StationRepository;
import repositories.BuildingRepository;

import java.io.*;

public class SimulationLoad implements Runnable, Serializable {
    private final String filePath;

    public SimulationLoad(String filePath) {
        this.filePath = filePath;
    }

    @Override
    public void run() {
        load(Repositories.getInstance().getSimRep(), Repositories.getInstance().getStationRepository(), Repositories.getInstance().getBuildingRepository());
    }

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