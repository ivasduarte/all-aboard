package repositories;

import domain.Industry;
import domain.Map;

import java.util.ArrayList;
import java.util.List;

public class IndustryRepository {
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
    public List<Industry> getIndustryByMap(Map map)
    {
        List<Industry> filteredIndustries = new ArrayList<>();

        MapRepository mapRepository = Repositories.getInstance().getMapRepository();
        List<Map> maps = mapRepository.getMapList();

        int mapID = -1;

        for(int i = 0; i < maps.size(); i++)
        {
            if(maps.get(i).equals(map))
            {
                mapID = i;
            }
        }

        for(Industry industry : industries)
        {
            if(industry.getMapID() == mapID)
                filteredIndustries.add(industry);
        }

        return filteredIndustries;
    }

    /**
     * This method is responsible to show the industry list in the form of a list.
     */
    public void displayIndustryList() {
        for (int i = 0; i < industries.size(); i++) {
            System.out.println((i+1) + ". " + industries.get(i));
        }
    }

    /**
     * This method is responsible to verify if the industry that is trying to be created already exists.
     *
     * @param industry industry that is being created
     *
     * @return True if the industry doesn't exist yet
     */
    private boolean validateIndustry(Industry industry) {
        boolean isValid = !industries.contains(industry);
        return isValid;
    }
}
