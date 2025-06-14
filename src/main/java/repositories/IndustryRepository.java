package repositories;

import domain.Industry;
import domain.Map;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Repository class responsible for managing and persisting Industry objects.
 * This class provides methods to save and retrieve industries from the system,
 * as well as filter industries by map.
 */
public class IndustryRepository implements Serializable {
    private List<Industry> industries = new ArrayList<>();

    /**
     * Saves a single industry to the repository.
     *
     * @param industry The industry to be saved
     */
    public void save(Industry industry) {
        industries.add(industry);
    }

    /**
     * Saves a list of industries to the repository.
     *
     * @param industries The list of industries to be saved
     */
    public void save(List<Industry> industries) {
        for(Industry industry : industries)
            save(industry);
    }

    /**
     * Retrieves a copy of the list containing all industries in the repository.
     *
     * @return An unmodifiable list containing all industries
     */
    public List<Industry> getIndustryList() {
        return List.copyOf(industries);
    }

    /**
     * Retrieves a list of industries that belong to a specific map.
     * The method filters industries based on the map's ID.
     *
     * @param map The map to filter industries by
     * @return A list of industries belonging to the specified map
     */
    public List<Industry> getIndustriesByMap(Map map) {
        List<Industry> filteredIndustries = new ArrayList<>();

        MapRepository mapRepository = Repositories.getInstance().getMapRepository();
        List<Map> maps = mapRepository.getMapList();

        int mapID = -1;

        for (int i = 0; i < maps.size(); i++) {
            if (maps.get(i).getName().equalsIgnoreCase(map.getName())) {
                mapID = i;
                break;
            }
        }

        for (Industry industry : industries) {
            if (industry.getMapID() == mapID) {
                filteredIndustries.add(industry);
            }
        }

        return filteredIndustries;
    }
}
