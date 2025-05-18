package controller;

import domain.Industry;
import repositories.IndustryRepository;
import repositories.Repositories;

public class AddIndustryController {
    private IndustryRepository industryRepository;

    /**
     * This method will be responsible to add the created industry to the repository.
     *
     * @param mapID id of the map in which the industry is being added
     * @param scenarioID id of the scenario in which the industry is being added
     * @param positionX position X the industry will be placed
     * @param positionY position Y the industry will be placed
     * @param industryType type of industry
     *
     * @return Will show a success message if it is possible to create the industry. Otherwise, it will show a message related to the error that occurred.
     */
    public String addIndustry(int mapID, int scenarioID, int positionX, int positionY, String industryType, int cargoAmount) {
        IndustryRepository industryRepository = Repositories.getInstance().getIndustryRepository();

        Industry industry = new Industry(mapID, scenarioID, positionX, positionY, industryType, cargoAmount);

        if (!industry.validatePosition()) {
            return "ERROR: Invalid position";
        } else if (!industry.checkOverbuilding()) {
            return "ERROR: Position is already occupied.";
        }

        industryRepository.save(industry);
        return "Industry [" + positionX + ", " + positionY + "] saved successfully.";
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
