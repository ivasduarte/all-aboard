package domain;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.Arrays;
import java.util.List;

class PortBehaviorTest {

    @Test
    void testPortAlphaProperties() {
        List<String> expectedImports = Arrays.asList("coal", "oil");
        List<String> expectedExports = Arrays.asList("steel");
        List<String> expectedTransformations = Arrays.asList("refining");

        assertEquals(expectedImports, PortBehavior.PORT_ALPHA.getImports());
        assertEquals(expectedExports, PortBehavior.PORT_ALPHA.getExports());
        assertEquals(expectedTransformations, PortBehavior.PORT_ALPHA.getTransformations());
    }

    @Test
    void testPortBravoProperties() {
        List<String> expectedImports = Arrays.asList("grain");
        List<String> expectedExports = Arrays.asList("flour");
        List<String> expectedTransformations = Arrays.asList("milling");

        assertEquals(expectedImports, PortBehavior.PORT_BRAVO.getImports());
        assertEquals(expectedExports, PortBehavior.PORT_BRAVO.getExports());
        assertEquals(expectedTransformations, PortBehavior.PORT_BRAVO.getTransformations());
    }

    @Test
    void testPortCharlieProperties() {
        List<String> expectedImports = Arrays.asList("iron", "copper");
        List<String> expectedExports = Arrays.asList("tools");
        List<String> expectedTransformations = Arrays.asList("smelting", "assembly");

        assertEquals(expectedImports, PortBehavior.PORT_CHARLIE.getImports());
        assertEquals(expectedExports, PortBehavior.PORT_CHARLIE.getExports());
        assertEquals(expectedTransformations, PortBehavior.PORT_CHARLIE.getTransformations());
    }

    @Test
    void testToString() {
        String expectedAlpha = "PORT_ALPHA (Imports: [coal, oil], Exports: [steel], Transforms: [refining])";
        String expectedBravo = "PORT_BRAVO (Imports: [grain], Exports: [flour], Transforms: [milling])";
        String expectedCharlie = "PORT_CHARLIE (Imports: [iron, copper], Exports: [tools], Transforms: [smelting, assembly])";

        assertEquals(expectedAlpha, PortBehavior.PORT_ALPHA.toString());
        assertEquals(expectedBravo, PortBehavior.PORT_BRAVO.toString());
        assertEquals(expectedCharlie, PortBehavior.PORT_CHARLIE.toString());
    }

    @Test
    void testListsAreUnmodifiable() {
        assertThrows(UnsupportedOperationException.class, () -> PortBehavior.PORT_ALPHA.getImports().add("newImport"));
        assertThrows(UnsupportedOperationException.class, () -> PortBehavior.PORT_ALPHA.getExports().add("newExport"));
        assertThrows(UnsupportedOperationException.class, () -> PortBehavior.PORT_ALPHA.getTransformations().add("newTransformation"));
    }

    @Test
    void testValues() {
        PortBehavior[] values = PortBehavior.values();
        assertEquals(3, values.length);
        assertTrue(Arrays.asList(values).contains(PortBehavior.PORT_ALPHA));
        assertTrue(Arrays.asList(values).contains(PortBehavior.PORT_BRAVO));
        assertTrue(Arrays.asList(values).contains(PortBehavior.PORT_CHARLIE));
    }

    @Test
    void testValueOf() {
        assertEquals(PortBehavior.PORT_ALPHA, PortBehavior.valueOf("PORT_ALPHA"));
        assertEquals(PortBehavior.PORT_BRAVO, PortBehavior.valueOf("PORT_BRAVO"));
        assertEquals(PortBehavior.PORT_CHARLIE, PortBehavior.valueOf("PORT_CHARLIE"));
        assertThrows(IllegalArgumentException.class, () -> PortBehavior.valueOf("INVALID"));
    }
} 