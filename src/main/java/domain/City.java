package domain;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Represents a City consisting of a name, map details, a center position,
 * and a collection of house blocks.
 */
public class City implements Serializable {
    private final String name;
    private final String mapName;
    private final int centerX;
    private final int centerY;
    private final List<HouseBlock> blocks = new ArrayList<>();

    /**
     * Constructs an instance of the City class, representing a city with a name, a map identifier,
     * and its center coordinates on the map.
     *
     * @param name          Name of the city.
     * @param mapName       Identifier or name of the map associated with the city.
     * @param centerX       X-coordinate of the city's center position on the map.
     * @param centerY       Y-coordinate of the city's center position on the map.
     */
    public City(String name, String mapName, int centerX, int centerY) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("City name cannot be null or empty");
        }
        if (mapName == null || mapName.trim().isEmpty()) {
            throw new IllegalArgumentException("Map name cannot be null or empty");
        }
        if (centerX < 0 || centerY < 0) {
            throw new IllegalArgumentException("City coordinates cannot be negative");
        }
        this.name = name;
        this.mapName = mapName;
        this.centerX = centerX;
        this.centerY = centerY;
    }

    /**
     * Retrieves the name of the city.
     *
     * @return      Name of the city.
     */
    public String getName() { return name; }

    /**
     * Retrieves the name of the map associated with the city.
     *
     * @return      Map name.
     */
    public String getMapName() { return mapName; }

    /**
     * Retrieves the X-coordinate of the city's center position on the map.
     *
     * @return      X-coordinate of the city's center position.
     */
    public int getCenterX() { return centerX; }

    /**
     * Retrieves the Y-coordinate of the city's center position on the map.
     *
     * @return      Y-coordinate of the city's center position.
     */
    public int getCenterY() { return centerY; }

    /**
     * Retrieves an unmodifiable list of house blocks associated with the city.
     *
     * @return      An unmodifiable list of {@code HouseBlock} objects representing the house blocks of the city.
     */
    public List<HouseBlock> getBlocks() { return Collections.unmodifiableList(blocks); }

    /**
     * Adds a house block manually to the city's collection of blocks.
     * This method allows for direct addition of a house block without any validation.
     * Use with caution as it bypasses any automatic validation that might be present
     * in other block addition methods.
     *
     * @param b The HouseBlock to be added to the city's collection
     */
    public void addBlockManual(HouseBlock b) {
        if (b == null) {
            throw new IllegalArgumentException("Block cannot be null");
        }
        if (Math.abs(b.getX() - centerX) > 100 || Math.abs(b.getY() - centerY) > 100) {
            throw new IllegalArgumentException("Block is outside city bounds");
        }
        blocks.add(b);
    }

    /**
     * Clears all house blocks from the city's collection of blocks.
     *
     * This method removes all {@code HouseBlock} objects stored in the list of
     * house blocks associated with the city.
     */
    public void clearBlocks() { blocks.clear(); }

    @Override
    public String toString() {
        return name + " (" + centerX + "," + centerY + ")";
    }
}
