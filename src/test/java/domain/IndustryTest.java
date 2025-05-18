package domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class IndustryTest {
    private Industry industry;

    @BeforeEach
    void setUp() {
        industry = new Industry(0, 0, 25, 75, "Mixed industry", 50) {
            @Override
            public boolean validatePosition() {
                int width = 100;
                int height = 100;

                return industry.getPositionX() >= 0 && industry.getPositionX() < width && industry.getPositionY() >= 0 && industry.getPositionY() < height;
            }
        };
    }

    @Test
    void getMapID() {
        assertEquals(0, industry.getMapID());
    }

    @Test
    void getScenarioID() {
        assertEquals(0, industry.getScenarioID());
    }

    @Test
    void getPositionX() {
        assertEquals(25, industry.getPositionX());
    }

    @Test
    void getPositionY() {
        assertEquals(75, industry.getPositionY());
    }

    @Test
    void getIndustryType() {
        assertEquals("Mixed industry", industry.getIndustryType());
    }

    @Test
    void validatePosition_Valid() {
        assertTrue(industry.validatePosition());
    }

    @Test
    void validatePosition_Invalid() {
        Industry invalidIndustry = new Industry(0, 0, 150, 75, "Invalid industry" ,50) {
            @Override
            public boolean validatePosition() {
                int width = 100;
                int height = 100;

                return getPositionX() >= 0 && getPositionX() <= width &&
                        getPositionY() >= 0 && getPositionY() <= height;
            }
        };

        assertFalse(invalidIndustry.validatePosition());
    }

    @Test
    void checkOverbuilding() {
        Industry conflictIndustry = new Industry(0, 0, 25, 75, "Primary sector industry", 50) {
            @Override
            public boolean checkOverbuilding() {
                int otherX = 25;
                int otherY = 75;

                return !(getPositionX() == otherX && getPositionY() == otherY);
            }
        };

        assertFalse(conflictIndustry.checkOverbuilding());
    }
}