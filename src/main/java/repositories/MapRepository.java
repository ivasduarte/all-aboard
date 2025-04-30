package repositories;

import domain.Map;

import java.util.ArrayList;
import java.util.List;

public class MapRepository{
    private List<Map> maps = new ArrayList<>();

    public void save(Map map){
        maps.add(map);
    }

    public List<Map> getMapList(){
        return maps;
    }

    public boolean existsByName(String name) {
        return maps.stream().anyMatch(m -> m.getMapName().equals(name));
    }
    public void displayMapList() {
        for (int i = 0; i < maps.size(); i++) {
            System.out.println(i + ". " + maps.get(i));
        }
    }
}