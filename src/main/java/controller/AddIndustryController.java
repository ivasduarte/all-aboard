package controller;

import domain.Industry;
import domain.Map;
import repositories.IndustryRepository;
import repositories.Repositories;

public class AddIndustryController {
    private IndustryRepository industryRepository;

    /**
     * This method will be responsible to add the created industry to the repository.
     *
     * @param mapID        id of the map in which the industry is being added
     * @param positionX    position X the industry will be placed
     * @param positionY    position Y the industry will be placed
     * @param industryType type of industry
     * @return Will show a success message if it is possible to create the industry. Otherwise, it will show a message related to the error that occurred.
     */
    public String addIndustry(int mapID, int positionX, int positionY, Industry.IndustryInfo industryType, int cargoAmount) {
        IndustryRepository industryRepository = Repositories.getInstance().getIndustryRepository();

        Industry industry = new Industry(mapID, positionX, positionY, industryType, cargoAmount);

        industryRepository.save(industry);
        Map map = Repositories.getInstance().getMapRepository().getMapList().get(mapID);

        map.addNewIndustry(industry);

        return "\n"+ industryType.getName() + " addedd successfully at (" + positionX + ", " + positionY + ").\nIndustry Type: " + industryType.getType().getName();
    }

    /**
     * This method allows the industry controller to get the industry repository.
     *
     * @return industry repository
     */
    public IndustryRepository getIndustryRepository() {
        if (industryRepository == null) {
            Repositories repositories = Repositories.getInstance();
            industryRepository = repositories.getIndustryRepository();
        }
        return industryRepository;
    }
}
