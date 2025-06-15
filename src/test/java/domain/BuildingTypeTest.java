package domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class BuildingTypeTest {

    private BuildingType sampleType() {
        return new BuildingType(
                "Post Office",
                500,
                "Handles mail",
                1875
        );
    }

    @Test
    void gettersReturnValuesFromType() {
        BuildingType type = sampleType();
        Building building = new Building(type);

        assertEquals(type.getName(),   building.getBuildingType());
        assertEquals(type.getCost(),   building.getCost());
        assertEquals(type.getEffect(), building.getEffect());
    }

    @Test

    void constructorRejectsNull() {
        IllegalArgumentException ex =
                assertThrows(IllegalArgumentException.class,
                        () -> new Building(null));

        assertEquals("Invalid building type", ex.getMessage());
    }
}
