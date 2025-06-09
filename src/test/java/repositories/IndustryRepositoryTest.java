package repositories;

import domain.Industry;
import domain.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class IndustryRepositoryTest {
    private IndustryRepository indRep;
    private Industry ind1;
    private Industry ind2;
    private Industry ind3;
    private Industry ind4;

    @BeforeEach
    void setUp() {
        indRep = new IndustryRepository();

        ind1 = new Industry(2, 10, 10, Industry.IndustryInfo.COAL_MINE, 10);
        ind2 = new Industry(2, 20, 20, Industry.IndustryInfo.IRON_MINE, 10);
        ind3 = new Industry(2, 30, 30, Industry.IndustryInfo.HOUSING, 10);
        ind4 = new Industry(0, 10, 10, Industry.IndustryInfo.TOOL_AND_DIE_FACTORY, 10);
    }

    @Test
    void saveSingle() {
        indRep.save(ind1);

        List<Industry> results = indRep.getIndustryList();

        assertEquals(1, results.size());
        assertEquals(ind1, results.get(0));
    }

    @Test
    void saveList() {
        List<Industry> industries = new ArrayList<>();
        industries.add(ind1);
        industries.add(ind2);
        industries.add(ind3);

        indRep.save(industries);

        List<Industry> results = indRep.getIndustryList();

        assertEquals(3, results.size());
        assertEquals(ind1, results.get(0));
        assertEquals(ind2, results.get(1));
        assertEquals(ind3, results.get(2));
    }

    @Test
    void getIndustryList() {
        indRep.save(ind1);
        indRep.save(ind2);

        List<Industry> results = indRep.getIndustryList();

        assertEquals(2, results.size());
        assertEquals(ind1, results.get(0));
        assertEquals(ind2, results.get(1));
        assertTrue(results.contains(ind1));
        assertTrue(results.contains(ind2));
    }

    @Test
    void getIndustryListEmpty() {
        List<Industry> results = indRep.getIndustryList();

        assertEquals(0, results.size());
        assertTrue(results.isEmpty());
    }

    @Test
    void getIndustriesByMap_EMPTY() {
        Map map = new Map("mapa", 50, 50, 15);

        List<Industry> results = indRep.getIndustriesByMap(map);

        assertNotNull(results);
        assertEquals(0, results.size());
    }

    @Test
    void getIndustriesByMap_CORRECT_RETURN() {
        MapRepository mapRepository = Repositories.getInstance().getMapRepository();
        List<Map> maps = mapRepository.getMapList();

        if (!maps.isEmpty()) {
            Map firstMap = maps.get(0);

            List<Industry> result = indRep.getIndustriesByMap(firstMap);
            
            for (Industry industry : result) {
                assertEquals(0, industry.getMapID());
            }
        }
    }
}