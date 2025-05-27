package domain;

import repositories.Repositories;
import repositories.BuildingRepository;
import repositories.StationRepository;
import repositories.SimulatorRepository;

import java.io.*;

public class SimulationFile {
    public static final String FILE = "simulator.sim";

    public boolean load(SimulatorRepository simRep, StationRepository stRep, BuildingRepository bdRep) {
        return load(FILE, simRep, stRep, bdRep);
    }

    public boolean load(String fileName, SimulatorRepository simRepo, StationRepository stationRepo, BuildingRepository buildingRepo) {
        return load(new File(fileName), simRepo, stationRepo, buildingRepo);
    }

    public boolean load(File file, SimulatorRepository simRep, StationRepository stRep, BuildingRepository bdRep) {
        if (!file.exists()) {
            System.out.println("File not found.");
            return true;
        }

        try (ObjectInputStream input = new ObjectInputStream(new FileInputStream(file))) {
            SimulatorRepository repo = (SimulatorRepository) input.readObject();
            StationRepository rep2 = (StationRepository) input.readObject();
            BuildingRepository rep3 = (BuildingRepository) input.readObject();
            System.out.println("File loaded successfully.");

            simRep.copyFrom(repo);
            stRep.copyFrom(rep2);
            bdRep.copyFrom(rep3);
            return true;
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Error loading file: " + e.getMessage());
            return false;
        }
    }

    public boolean save() {
        return save(FILE);
    }

    public boolean save(String fileName) {
        return save(new File(fileName));
    }

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