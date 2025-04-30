package repositories;

import domain.Industry;

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

    // verify if exists
}
