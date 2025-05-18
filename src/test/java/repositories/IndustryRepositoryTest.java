package repositories;

import domain.Industry;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class IndustryRepositoryTest {
    private IndustryRepository indRepo;
    private Industry testIndustry;

    @BeforeEach
    void setUp() {
        indRepo = new IndustryRepository();
        testIndustry = new Industry(0, 0, 50, 50, "Mixed Industry", 50);
    }

    @Test
    void save() {
        indRepo.save(testIndustry);
        List<Industry> industries = indRepo.getIndustryList();

        assertEquals(1, industries.size());
        assertTrue(industries.contains(testIndustry));
    }

    @Test
    void getIndustryList() {
        Industry testIndustry2 = new Industry(0, 0, 25, 25, "Transforming industry", 50);
        indRepo.save(testIndustry);
        indRepo.save(testIndustry2);

        List<Industry> industries = indRepo.getIndustryList();

        assertEquals(2, industries.size());
        assertTrue(industries.contains(testIndustry));
        assertTrue(industries.contains(testIndustry2));
    }
}