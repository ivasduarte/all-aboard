package repositories;

import domain.Industry;
import domain.Map;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class IndustryRepository implements Serializable {
    private List<Industry> industries = new ArrayList<>();

    /**
     * This method will be responsible to add the industry to the industry list.
     *
     * @param industry industry that is being created
     */
    public void save(Industry industry) {
        industries.add(industry);
    }

    /**
     * This method will be responsible to add a list of industries to the industry list.
     *
     * @param industries industries that are being created
     */
    public void save(List<Industry> industries) {
        for(Industry industry : industries)
            save(industry);
    }

    /**
     * This method allows other classes to get the list with all the industries.
     *
     * @return industry list
     */
    public List<Industry> getIndustryList() {
        return industries;
    }

    /**
     * This method allows other classes to get the list with all the industries from a specific map.
     *
     * @param map The desired map.
     * @return Industry list of the desired map.
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
