package controller;

import domain.ApplicationSession;
import domain.Map;
import domain.UserSession;
import presentation.CreateMapUI;
import repositories.MapRepository;
import repositories.Repositories;

import java.util.List;

public class CreateMapController {

    public Map createMap(String name, int width, int height){
        if(Repositories.getInstance().getMapRepository().existsByName(name)){
            throw new IllegalArgumentException("Map with this name already exists.");
        }
        Map map = new Map(name, width, height);
        Repositories.getInstance().getMapRepository().add(map);
        return map;
    }
}