package controller;

import domain.City;
import domain.Map;
import repositories.CityRepository;
import repositories.MapRepository;
import repositories.Repositories;

import java.util.ArrayList;
import java.util.List;

/**
 * The DisplayMapController class serves as a manager for handling operations related to maps and cities stored in the
 * MapRepository and CityRepository. It provides methods to retrieve lists of maps and cities, validate the existence
 * of maps by their name, and ensure the initialization of required repositories.
 */
public class DisplayMapController {
    private MapRepository mapRepository;
    private CityRepository cityRepository;
    private List<Map> maps = new ArrayList<>();

    /**
     * Default constructor for the DisplayMapController class.
     */
    public DisplayMapController(){
        getMapRepository();
        getCityRepository();
    }

    /**
     * Constructs a new instance of the {@code DisplayMapController} class using the specified
     * {@code MapRepository} and {@code CityRepository} instances for map and city data management.
     *
     * @param mapRepository     The {@code MapRepository} instance used to manage map data.
     * @param cityRepository    The {@code CityRepository} instance used to manage city data.
     */
    public DisplayMapController(MapRepository mapRepository, CityRepository cityRepository){
        this.mapRepository = mapRepository;
        this.cityRepository = cityRepository;
    }

    /**
     * Retrieves the instance of {@code MapRepository} used to manage map data.
     * If the repository is not already initialized, it initializes the
     * {@code MapRepository} by obtaining it from the singleton instance
     * of the {@code Repositories} class.
     *
     * @return      The instance of {@code MapRepository}.
     */
    private MapRepository getMapRepository(){
        if(mapRepository == null){
            Repositories repo = Repositories.getInstance();
            mapRepository = repo.getMapRepository();
        }
        return mapRepository;
    }

    /**
     * Retrieves the instance of {@code CityRepository} used to manage city data.
     * If the repository is not already initialized, it initializes the
     * {@code CityRepository} by obtaining it from the singleton instance
     * of the {@code Repositories} class.
     *
     * @return      The instance of {@code CityRepository}.
     */
     private CityRepository getCityRepository(){
        if(cityRepository == null){
            Repositories repo = Repositories.getInstance();
            cityRepository = repo.getCityRepository();
        }
        return cityRepository;
    }

    /**
     * Retrieves a list of all maps managed by the {@code MapRepository}.
     *
     * @return      An unmodifiable list of {@code Map} objects representing the maps stored in the repository.
     */
    public List<Map> getMapList() {
        return getMapRepository().getMapList();
    }

    /**
     * Retrieves a list of all cities managed by the {@code CityRepository}.
     *
     * @return      An unmodifiable list of {@code City} objects representing the cities stored in the repository.
     */
    public List<City> getCityList(){
        return getCityRepository().getCities();
    }

    /**
     * Verifies if a map with the specified name exists in the map repository.
     *
     * @param mapName       The name of the map to check for existence. Must not be null.
     * @return              {@code true} if a map with the given name exists in the repository;
     *                      {@code false} otherwise.
     */
    public boolean verifyIfMapExists(String mapName){
        return getMapRepository().existsByName(mapName);
    }
}
