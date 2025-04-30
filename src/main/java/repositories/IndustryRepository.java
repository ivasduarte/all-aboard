package repositories;

import domain.Industry;
import domain.Map;

import java.util.ArrayList;
import java.util.List;

public class IndustryRepository {
    private List<Industry> industries = new ArrayList<>();

    Map mp = new Map();

    public void save(Industry industry) {
        industries.add(industry);
    }
    public List<Industry> getIndustryList() {
        return industries;
    }

    public boolean validatePosition() {
        if (Industry.get >= 0 && posX <= mp.width){
            if (posY >= 0 && posY <= mp.height){
                return true;
            }
        }
        return false;
    }
}
