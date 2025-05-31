package domain;

import repositories.SimulatorRepository;
import repositories.BuildingRepository;
import repositories.StationRepository;

import java.io.*;

public class SimulationSave implements Runnable, Serializable {
    private final String filePath;
    private final SimulatorRepository simRep;
    private final StationRepository stRep;
    private final BuildingRepository bdRep;

    public SimulationSave(String filePath, SimulatorRepository simRep, StationRepository stRep, BuildingRepository bdRep) {
        this.filePath = filePath;
        this.simRep = simRep;
        this.stRep = stRep;
        this.bdRep = bdRep;
    }

    @Override
    public void run() {
        try {
            save(filePath);
        } catch (IOException e) {
            System.out.println("Error saving simulation: " + e.getMessage());
        }
    }

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