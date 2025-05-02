package repositories;

import domain.Map;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class MapRepository{
    private List<Map> maps = new ArrayList<>();
    private static final String MAP_FILE = "maps.txt";

    public MapRepository() {
        loadFromFile();
    }

    public List<Map> getMapList() {
        return maps;
    }

    public Map getMapById(int mapID) {
        if (mapID >= 0 && mapID < maps.size()) {
            return maps.get(mapID);
        }
        return null;
    }

    public void displayMapList() {
        for (int i = 0; i < maps.size(); i++) {
            System.out.println(i + ". " + maps.get(i).getMapName());
        }
    }


    public void save(Map map) {
        if (!existsByName(map.getMapName())) {
            maps.add(map);
            saveToFile();
        } else {
            System.out.println("Name already exists.");
        }
    }

    public boolean existsByName(String name) {
        for (int i = 0; i < maps.size(); i++) {
            if (maps.get(i).getMapName().equalsIgnoreCase(name)) {
                return true;
            }
        }
        return false;
    }

    public void saveToFile() {
        try (PrintWriter writer = new PrintWriter(new File("maps.txt"))) {
            maps.forEach(map -> writer.println(map.getMapName()));
        } catch (FileNotFoundException e) {
            System.out.println("Failed saving map: " + e.getMessage());
        }
    }

    private void loadFromFile() {
        try (BufferedReader reader = new BufferedReader(new FileReader("maps.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                maps.add(new Map(line, 5, 5));  // Inicializa com uma matriz 5x5 de exemplo
            }
        } catch (IOException e) {
            System.out.println("Failed loading archive: " + e.getMessage());
        }
    }

}