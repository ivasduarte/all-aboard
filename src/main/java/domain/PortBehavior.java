package domain;

import java.util.Arrays;
import java.util.List;

/**
 * Enum representing different types of port behaviors in the simulation.
 * Each port type has specific characteristics regarding what it imports,
 * exports, and what transformations it can perform.
 */
public enum PortBehavior {
    PORT_ALPHA(
            Arrays.asList("coal", "oil"),
            Arrays.asList("steel"),
            Arrays.asList("refining")
    ),
    PORT_BRAVO(
            Arrays.asList("grain"),
            Arrays.asList("flour"),
            Arrays.asList("milling")
    ),
    PORT_CHARLIE(
            Arrays.asList("iron", "copper"),
            Arrays.asList("tools"),
            Arrays.asList("smelting", "assembly")
    );

    private final List<String> imports;
    private final List<String> exports;
    private final List<String> transformations;

    /**
     * Constructs a new PortBehavior with specified import, export, and transformation capabilities.
     *
     * @param imports List of resources that this port can import
     * @param exports List of resources that this port can export
     * @param transformations List of transformations this port can perform
     */
    PortBehavior(List<String> imports, List<String> exports, List<String> transformations) {
        this.imports = imports;
        this.exports = exports;
        this.transformations = transformations;
    }

    /**
     * Gets the list of resources that this port can import.
     *
     * @return List of importable resources
     */
    public List<String> getImports() {
        return imports;
    }

    /**
     * Gets the list of resources that this port can export.
     *
     * @return List of exportable resources
     */
    public List<String> getExports() {
        return exports;
    }

    /**
     * Gets the list of transformations this port can perform.
     *
     * @return List of possible transformations
     */
    public List<String> getTransformations() {
        return transformations;
    }

    /**
     * Returns a string representation of the port behavior.
     * Includes information about imports, exports, and transformations.
     *
     * @return String representation of the port behavior
     */
    @Override
    public String toString() {
        return name() + " (Imports: " + imports + ", Exports: " + exports + ", Transforms: " + transformations + ")";
    }
}