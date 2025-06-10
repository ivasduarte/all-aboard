package domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class CityTest {
    private City city;

    @BeforeEach
    void setUp() {
        city = new City("CidadeTeste", "MapaTeste", 3, 4);
    }

    @Test
    void getName_ReturnsCorrectValue() {
        assertEquals("CidadeTeste", city.getName());
    }

    @Test
    void getMapName_ReturnsCorrectValue() {
        assertEquals("MapaTeste", city.getMapName());
    }

    @Test
    void getCenterX_ReturnsCorrectValue() {
        assertEquals(3, city.getCenterX());
    }

    @Test
    void getCenterY_ReturnsCorrectValue() {
        assertEquals(4, city.getCenterY());
    }

    @Test
    void constructor_WithInvalidName_ThrowsException() {
        assertThrows(IllegalArgumentException.class, () -> new City("", "MapaTeste", 3, 4));
        assertThrows(IllegalArgumentException.class, () -> new City(null, "MapaTeste", 3, 4));
        assertThrows(IllegalArgumentException.class, () -> new City("   ", "MapaTeste", 3, 4));
    }

    @Test
    void constructor_WithInvalidMapName_ThrowsException() {
        assertThrows(IllegalArgumentException.class, () -> new City("CidadeTeste", "", 3, 4));
        assertThrows(IllegalArgumentException.class, () -> new City("CidadeTeste", null, 3, 4));
        assertThrows(IllegalArgumentException.class, () -> new City("CidadeTeste", "   ", 3, 4));
    }

    @Test
    void constructor_WithInvalidCoordinates_ThrowsException() {
        assertThrows(IllegalArgumentException.class, () -> new City("CidadeTeste", "MapaTeste", -1, 4));
        assertThrows(IllegalArgumentException.class, () -> new City("CidadeTeste", "MapaTeste", 3, -1));
    }

    @Test
    void addBlockManual_AddsBlockSuccessfully() {
        HouseBlock block = new HouseBlock(1, 2);
        city.addBlockManual(block);
        List<HouseBlock> blocks = city.getBlocks();
        assertEquals(1, blocks.size());
        assertTrue(blocks.contains(block));
    }

    @Test
    void addBlockManual_WithNullBlock_ThrowsException() {
        assertThrows(IllegalArgumentException.class, () -> city.addBlockManual(null));
    }

    @Test
    void addBlockManual_WithDuplicateBlock_DoesNotAddDuplicate() {
        HouseBlock block = new HouseBlock(1, 2);
        city.addBlockManual(block);
        city.addBlockManual(block);
        assertEquals(1, city.getBlocks().size());
    }

    @Test
    void addBlockManual_WithMultipleBlocks_AddsAllBlocks() {
        HouseBlock block1 = new HouseBlock(1, 2);
        HouseBlock block2 = new HouseBlock(3, 4);
        HouseBlock block3 = new HouseBlock(5, 6);

        city.addBlockManual(block1);
        city.addBlockManual(block2);
        city.addBlockManual(block3);

        List<HouseBlock> blocks = city.getBlocks();
        assertEquals(3, blocks.size());
        assertTrue(blocks.contains(block1));
        assertTrue(blocks.contains(block2));
        assertTrue(blocks.contains(block3));
    }

    @Test
    void getBlocks_ReturnsUnmodifiableList() {
        List<HouseBlock> blocks = city.getBlocks();
        assertThrows(UnsupportedOperationException.class, () -> blocks.add(new HouseBlock(0, 0)));
    }

    @Test
    void testToString() {
        String expected = "CidadeTeste (MapaTeste) at (3,4)";
        assertEquals(expected, city.toString());
    }
}