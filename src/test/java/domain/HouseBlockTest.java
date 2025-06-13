package domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class HouseBlockTest {
    private HouseBlock block;

    @BeforeEach
    void setUp() {
        block = new HouseBlock(3, 4);
    }

    @Test
    void testGetX() {
        assertEquals(3, block.getX());
    }

    @Test
    void testGetY() {
        assertEquals(4, block.getY());
    }

    @Test
    void testEquals_SameCoordinates() {
        HouseBlock otherBlock = new HouseBlock(3, 4);
        assertEquals(block, otherBlock);
    }

    @Test
    void testEquals_DifferentCoordinates() {
        HouseBlock otherBlock = new HouseBlock(5, 6);
        assertNotEquals(block, otherBlock);
    }

    @Test
    void testEquals_SameObject() {
        assertEquals(block, block);
    }

    @Test
    void testEquals_Null() {
        assertNotEquals(null, block);
    }

    @Test
    void testEquals_DifferentType() {
        assertNotEquals(block, "not a block");
    }

    @Test
    void testHashCode_EqualsObjects() {
        HouseBlock otherBlock = new HouseBlock(3, 4);
        assertEquals(block.hashCode(), otherBlock.hashCode());
    }

    @Test
    void testHashCode_DifferentObjects() {
        HouseBlock otherBlock = new HouseBlock(5, 6);
        assertNotEquals(block.hashCode(), otherBlock.hashCode());
    }

    @Test
    void testToString() {
        String expected = "HouseBlock at (3,4)";
        assertEquals(expected, block.toString());
    }
}