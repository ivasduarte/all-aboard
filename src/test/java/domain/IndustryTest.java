package domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
class IndustryTest {

    private Industry exampleIndustry;

    @BeforeEach
    void setUp() {
        exampleIndustry = new Industry(2, 49, 51, Industry.IndustryInfo.COAL_MINE, 10);
    }

    @Test
    void getMapID() {
        assertEquals(2, exampleIndustry.getMapID());
    }

    @Test
    void getPositionX() {
        assertEquals(49, exampleIndustry.getPositionX());
    }

    @Test
    void getPositionY() {
        assertEquals(51, exampleIndustry.getPositionY());
    }

    @Test
    void getIndustryType() {
        assertEquals(Industry.IndustryInfo.COAL_MINE, exampleIndustry.getIndustryType());
    }

    @Test
    void getCargoAmount() {
        assertEquals(10, exampleIndustry.getCargoAmount());
    }
}