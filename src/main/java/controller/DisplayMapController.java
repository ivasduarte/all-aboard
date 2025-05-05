package controller;

import domain.Map;
import repositories.MapRepository;
import repositories.Repositories;

import java.util.ArrayList;
import java.util.List;

public class DisplayMapController {
    private MapRepository mapRepository;
    private List<Map> maps = new ArrayList<>();

    public DisplayMapController(){
        getMapRepository();
    }

    public DisplayMapController(MapRepository mapRepository){
        this.mapRepository = mapRepository;
    }

    private MapRepository getMapRepository(){
        if(mapRepository == null){
            Repositories repo = Repositories.getInstance();
            mapRepository = repo.getMapRepository();
        }
        return mapRepository;
    }

    public List<Map> getMapList() {
        return getMapRepository().getMapList();
    }

    public boolean verifyIfMapExists(String mapName){
        return getMapRepository().existsByName(mapName);
    }
}
