package domain;

import repositories.SimulatorRepository;
import repositories.BuildingRepository;
import repositories.Repositories;
import repositories.StationRepository;

import java.io.*;

public class SimulationFile {
    public static final String FILE = "simulations.sim";

    public boolean load(SimulatorRepository simRep, StationRepository stRep, BuildingRepository bdRep) {
        return load(FILE, simRep, stRep, bdRep);
    }

    public boolean load(String fileName, SimulatorRepository simRep, StationRepository stRep, BuildingRepository bdRep) {
        return load(new File(fileName), simRep, stRep, bdRep);
    }

    public boolean load(File file, SimulatorRepository simRep, StationRepository stRep, BuildingRepository bdRep) {
        if (!file.exists()) {
            System.out.println("Error: File does not exist.");
            return true;
        }

        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(file))) {
            SimulatorRepository rep = (SimulatorRepository) in.readObject();
            StationRepository stRepo = (StationRepository) in.readObject();
            BuildingRepository bdRepo = (BuildingRepository) in.readObject();
            System.out.println("SUCCESS: File load complete.");

            simRep.copyFrom(rep);
            stRep.copyFrom(stRepo);
            bdRep.copyFrom(bdRepo);
            return true;
        } catch (IOException | ClassNotFoundException ex) {
            System.out.println("Error loading file '" + ex.getMessage() + "'.");
            return false;
        }
    }

    public boolean save() { return save(FILE); }

    public boolean save(String fileName) { return save(new File(fileName)); }

    public boolean save(File file) {
        SimulatorRepository simulationRepo = Repositories.getInstance().getSimRep();
        StationRepository stationRepo = Repositories.getInstance().getStationRepository();
        BuildingRepository buildingRepo = Repositories.getInstance().getBuildingRepository();
        try {
            ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(file));

            try {
                out.writeObject(simulationRepo);
                out.writeObject(stationRepo);
                out.writeObject(buildingRepo);
            } finally {
                out.close();
            }
            System.out.println("File saved successfully");
            return true;
        } catch (IOException e) {
            System.out.println("Error saving file: " + e.getMessage());
            return false;
        }
    }
}