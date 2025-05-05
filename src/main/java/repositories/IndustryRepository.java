package repositories;

import domain.Industry;
import domain.Map;

import java.util.ArrayList;
import java.util.List;

public class IndustryRepository {
    private List<Industry> industries = new ArrayList<>();

    public void save(Industry industry) {
        industries.add(industry);
    }

    public List<Industry> getIndustryList() {
        return industries;
    }

    public void displayIndustryList() {
        for (int i = 0; i < industries.size(); i++) {
            System.out.println((i+1) + ". " + industries.get(i));
        }
    }

    private boolean validateIndustry(Industry industry) {
        boolean isValid = !industries.contains(industry);
        return isValid;
    }
}
