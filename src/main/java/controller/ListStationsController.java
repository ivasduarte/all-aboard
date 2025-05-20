package controller;

import domain.Station;
import repositories.Repositories;
import repositories.StationRepository;

import java.util.List;

/**
 * Controller responsible for handling operations related to listing stations.
 */
public class ListStationsController {

    private final StationRepository StationRepo;

    /**
     * Constructs a ListStationsController and initializes the station repository.
     */
    public ListStationsController() {
        this.StationRepo = Repositories.getInstance().getStationRepository();
    }

    /**
     * Retrieves all stations from the repository.
     *
     * @return a list of all stations
     */
    public List<Station> getAllStations() {
        return StationRepo.getStations();
    }

    /**
     * Retrieves all stations from a specific map that still have available building slots.
     *
     * @param mapName the name of the map
     * @return a list of stations with available slots in the specified map
     */
    public List<Station> getStationsByMapName(String mapName) {
        return Repositories.getInstance().getStationRepository().getAvailableStationsByMapName(mapName);
    }

}
