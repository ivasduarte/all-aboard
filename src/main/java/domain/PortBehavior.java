package domain;

import java.util.Arrays;
import java.util.List;

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

    PortBehavior(List<String> imports, List<String> exports, List<String> transformations) {
        this.imports = imports;
        this.exports = exports;
        this.transformations = transformations;
    }

    public List<String> getImports() {
        return imports;
    }

    public List<String> getExports() {
        return exports;
    }

    public List<String> getTransformations() {
        return transformations;
    }

    @Override
    public String toString() {
        return name() + " (Imports: " + imports + ", Exports: " + exports + ", Transforms: " + transformations + ")";
    }
}