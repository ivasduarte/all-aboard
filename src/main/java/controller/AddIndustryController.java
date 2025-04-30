package controller;

import domain.ApplicationSession;
import domain.Editor;
import domain.Industry;
import repositories.IndustryRepository;
import repositories.Repositories;

public class AddIndustryController {
    public String addIndustry(int mapID, int scenarioID, int positionX, int positionY, String industryType) {
        IndustryRepository industryRepository = Repositories.getInstance().getIndustryRepository();
        Editor editor = ApplicationSession.getInstance().getCurrentSession().getCurrentEditor();

        Industry industry = new Industry(mapID, scenarioID, positionX, positionY, industryType);

        if (!industry.validatePosition()) {
            return "ERROR: Invalid position";
        }

        industryRepository.save(industry);
        return "Industry [" + positionX + ", " + positionY + "] saved successfully.";
    }
}
