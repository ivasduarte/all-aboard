package dto;

import domain.Map;

import java.util.Objects;

/**
 * A Data Transfer Object (DTO) representing a map along with its properties such as name, dimensions,
 * and cell size in kilometers.
 *
 * This class provides methods to access the map's metadata, calculate total dimensions in kilometers,
 * and compute the total area of the map.
 */
public class MapDTO {
    private String name;
    private int width;
    private int height;
    private int cellSizeKm;

    /**
     * Constructs a MapDTO object with the specified name, cell size, height, and width.
     *
     * @param name The name of the map.
     * @param cellSizeInKm The cell size in kilometers for the map grid.
     * @param height The height of the map in grid cells.
     * @param width The width of the map in grid cells.
     */
    public MapDTO(String name, int cellSizeInKm, int height, int width) {
        this.name = name;
        this.cellSizeKm = cellSizeInKm;
        this.height = height;
        this.width = width;
    }

    /**
     * Converts a Map entity to its corresponding MapDTO.
     *
     * @param map the Map entity to convert
     * @return a new MapDTO with the same property values
     */
    public static MapDTO toDTO(Map map) {
        return new MapDTO(
                map.getName(),
                map.getCellSizeKm(),
                map.getHeight(),
                map.getWidth()
        );
    }

    /**
     * Retrieves the name of the map.
     *
     * @return The name of the map as a string.
     */
    public String getName() {
        return name;
    }

    /**
     * Retrieves the width of the map in grid cells.
     *
     * @return The width of the map as an integer.
     */
    public int getWidth() {
        return width;
    }

    /**
     * Retrieves the height of the map in grid cells.
     *
     * @return The height of the map as an integer.
     */
    public int getHeight() {
        return height;
    }

    /**
     * Retrieves the size of a map grid cell in kilometers.
     *
     * @return The size of a single map grid cell in kilometers as an integer.
     */
    public int getCellSizeKm() {
        return cellSizeKm;
    }

    /**
     * Calculates the total width of the map in kilometers by multiplying the grid width by the size of each cell.
     *
     * @return The total width of the map in kilometers as a double.
     */
    public double getTotalWidthKm() {
        return width * cellSizeKm;
    }

    /**
     * Calculates the total height of the map in kilometers by multiplying the grid height by the size of each cell.
     *
     * @return The total height of the map in kilometers as a double.
     */
    public double getTotalHeightKm() {
        return height * cellSizeKm;
    }

    /**
     * Calculates the total area of the map in square kilometers by multiplying its total width and height in kilometers.
     *
     * @return The total area of the map in square kilometers as a double.
     */
    public double getAreaKm() {
        return getTotalWidthKm() * getTotalHeightKm();
    }

    /**
     * Compares this MapDTO object to another object for equality.
     * Determines equality based on the name, width, height, and cell size of the map.
     *
     * @param o The object to compare with this MapDTO.
     * @return true if the specified object is equal to this MapDTO, otherwise false.
     */
    @Override
    public boolean equals(Object o) {
        if (!(o instanceof MapDTO)) return false;
        MapDTO mapDTO = (MapDTO) o;
        return width == mapDTO.width && height == mapDTO.height && cellSizeKm == mapDTO.cellSizeKm && Objects.equals(name, mapDTO.name);
    }

    /**
     * Generates a hash code value for the MapDTO object.
     * The hash code is computed based on the name, width, height, and cellSizeKm fields.
     *
     * @return An integer that represents the hash code of this MapDTO object.
     */
    @Override
    public int hashCode() {
        return Objects.hash(name, width, height, cellSizeKm);
    }

    /**
     * Provides a string representation of the MapDTO object, including its name, dimensions, cell size,
     * and total area in square kilometers.
     *
     * @return A string representation of the map with its name, dimensions (width x height),
     *         cell size in kilometers, and the calculated area in square kilometers.
     */
    @Override
    public String toString() {
        return "Map '" + name + "' (" + width + "x" + height + "), Cell size: " + cellSizeKm + " km, Area: " + getAreaKm() + " km²";
    }
}
