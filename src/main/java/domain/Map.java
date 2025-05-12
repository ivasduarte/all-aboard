package domain;


/**
 * Represents a two-dimensional map with a specific name, dimensions, and optional creator.
 * The map includes a 2D character matrix that can be used to represent various layouts or configurations.
 */
public class Map {
    private String name;
    private int width;
    private int height;


    /**
     * Constructs a new instance of the Map class, initializing it with a specified name,
     * width, and height, and creates a two-dimensional grid (matrix) based on these dimensions.
     *
     * @param name      The name of the map. It cannot be null or empty.
     * @param width     The width of the map. Must be a positive integer.
     * @param height    The height of the map. Must be a positive integer.
     * @throws          IllegalArgumentException if the name is null, empty, or if the dimensions are non-positive.
     */
    public Map(String name, int width, int height) {
        this.name = name;
        this.width = width;
        this.height = height;
        validateName();
        validateSize();
    }


    /**
     * Retrieves the name of the map associated with the instance.
     *
     * @return  The name of the map as a String.
     */
    public String getMapName() {
        return name;
    }


    /**
     * Retrieves the width of the map associated with the instance.
     *
     * @return  The width of the map as an integer.
     */
    public int getWidth() {
        return width;
    }


    /**
     * Retrieves the height of the map.
     *
     * @return  The height of the map as an integer.
     */
    public int getHeight() {
        return height;
    }


    /**
     * Validates the name of the map.
     *
     * This method ensures that the name of the map is not null or empty.
     * If the name is found to be null or empty, an {@code IllegalArgumentException}
     * is thrown to enforce the validity of the map's name.
     *
     * @throws IllegalArgumentException if the name is null or an empty string.
     */
    private void validateName() {
        if (name == null || name.isEmpty()) {
            throw new IllegalArgumentException("Map name cannot be empty.");
        }
    }


    /**
     * Validates the dimensions of the map.
     *
     * This method ensures that the width and height of the map are positive integers.
     * If either the width or height is less than or equal to zero, an {@code IllegalArgumentException}
     * is thrown to ensure valid map dimensions.
     *
     * @throws IllegalArgumentException if the width or height is non-positive.
     */
    private void validateSize() {
        if (width <= 0 || height <= 0) {
            throw new IllegalArgumentException("Map dimensions must be positive.");
        }
    }


    /**
     * Returns a string representation of the map object.
     * The string includes the name of the map and its dimensions
     * in the format "name (widthxheight)".
     *
     * @return A string representation of the map, including its name, width, and height.
     */
    @Override
    public String toString() {
        return "" + name + " (" + width + "x" + height + ")";
    }
}