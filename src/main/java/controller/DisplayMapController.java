package controller;

import domain.Map;
import repositories.MapRepository;
import repositories.Repositories;

import java.util.ArrayList;
import java.util.List;

public class DisplayMapController {
    private MapRepository mapRepository;
    //private CityRepository cityRepository;
    private List<Map> maps = new ArrayList<>();

    public DisplayMapController(){
        getMapRepository();
        //getCityRepository();
    }

    public DisplayMapController(MapRepository mapRepository){ //add CityRepository cityRepository
        this.mapRepository = mapRepository;
        //this.cityRepository = cityRepository;
    }

    private MapRepository getMapRepository(){
        if(mapRepository == null){
            Repositories repo = Repositories.getInstance();
            mapRepository = repo.getMapRepository();
        }
        return mapRepository;
    }

    /* private CityRepository getCityRepository(){
        if(cityRepository == null){
            Repositories repo = repositories.getInstance();
            cityRepository = repo.getCityRepositories();
        }
        return cityRepository();
    }*/

    public List<Map> getMapList() {
        return getMapRepository().getMapList();
    }

/*    public List<City> getCityList(){
        return getCityRepository.getCities();
    }*/

    public boolean verifyIfMapExists(String mapName){
        return getMapRepository().existsByName(mapName);
    }
}
