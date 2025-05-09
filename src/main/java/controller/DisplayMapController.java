package controller;

import domain.City;
import domain.Map;
import repositories.CityRepository;
import repositories.MapRepository;
import repositories.Repositories;

import java.util.ArrayList;
import java.util.List;

/**
 * This class handles the interaction with map and city repositories.
 * It is responsible for retrieving data such as lists of maps and cities,
 * as well as verifying the existence of maps based on their names.
 */
public class DisplayMapController {
    private MapRepository mapRepository;
    private CityRepository cityRepository;
    private List<Map> maps = new ArrayList<>();


    /**
     * Initializes an instance of the DisplayMapController class by ensuring that
     * the required MapRepository and CityRepository are properly retrieved.
     * If these repositories are not already initialized, they are acquired from
     * the singleton instance of the Repositories class.
     */
    public DisplayMapController(){
        getMapRepository();
        getCityRepository();
    }


    /**
     * Constructor for the DisplayMapController class that initializes the repositories.
     * This controller is used to interact with the map and city repositories for
     * retrieving or managing data related to maps and cities.
     *
     * @param mapRepository     An instance of MapRepository used to manage map data
     * @param cityRepository    An instance of CityRepository used to manage city data
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
     * @return The instance of {@code MapRepository}.
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
     * @return The instance of {@code CityRepository}.
     */
     private CityRepository getCityRepository(){
        if(cityRepository == null){
            Repositories repo = Repositories.getInstance();
            cityRepository = repo.getCityRepository();
        }
        return cityRepository;
    }


    /**
     * Retrieves a list of all existing maps currently managed by the {@code MapRepository}.
     *
     * @return A list of {@code Map} objects representing the existing maps.
     */
    public List<Map> getMapList() {
        return getMapRepository().getMapList();
    }


    /**
     * Retrieves a list of all cities managed by the {@code CityRepository}.
     *
     * @return  Unmodifiable list of {@code City} objects representing the cities stored in the repository.
     */
    public List<City> getCityList(){
        return getCityRepository().getCities();
    }


    /**
     * Verifies if a map with the specified name exists in the repository.
     *
     * @param mapName   The name of the map to verify. Cannot be null or empty.
     * @return          {@code true} if a map with the specified name exists, {@code false} otherwise.
     */
    public boolean verifyIfMapExists(String mapName){
        return getMapRepository().existsByName(mapName);
    }
}
