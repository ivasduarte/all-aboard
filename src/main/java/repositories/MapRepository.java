package repositories;

import domain.Map;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class MapRepository{
    private List<Map> maps;

    public MapRepository() {
        maps = new ArrayList<>();
    }

    public void add(Map map){
        if(validateMap(map)){
            maps.add(map);
        }
    }

    private boolean validateMap(Map map){
        System.out.println(!maps.contains(map));
        return !maps.contains(map);
    }

    public boolean existsByName(String name) {
        for (int i = 0; i < maps.size(); i++) {
            if (maps.get(i).getMapName().equalsIgnoreCase(name)) {
                return true;
            }
        }
        return false;
    }

    public List<Map> getMapList() {
        return List.copyOf(maps);
    }

    public Map getMapById(int mapID) {
        if (mapID >= 0 && mapID < maps.size()) {
            return maps.get(mapID);
        }
        return null;
    }

    public void displayMapList() {
        for (int i = 0; i < maps.size(); i++) {
            System.out.println((i+1) + ". " + maps.get(i).getMapName());
        }
    }

}