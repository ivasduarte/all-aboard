package domain;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Represents a map with a name, dimensions (width and height), and a list of associated scenarios.
 * This class provides methods to access and manipulate the map's properties and scenarios.
 */
public class Map implements Serializable {
    private String name;
    private int width;
    private int height;

    private List<Scenario> scenarios;

    /**
     * Constructs a new Map instance with the specified name, width, and height.
     * <p>
     * This constructor initializes the map's attributes, including its name, dimensions,
     * and an empty list of scenarios. It also validates the name and size of the map
     * during initialization.
     *
     * @param name   The name of the map. Must not be null or empty.
     * @param width  The width of the map. Must be a positive integer.
     * @param height The height of the map. Must be a positive integer.
     * @throws IllegalArgumentException if the name is null or empty, or if the width or height is non-positive.
     */
    public Map(String name, int width, int height) {
        this.name = name;
        this.width = width;
        this.height = height;
        scenarios = new ArrayList<>();
        validateName(name);
        validateSize(width, height);
    }

    /**
     * Retrieves the name of the map associated with the instance.
     *
     * @return The name of the map as a String.
     */
    public String getName() {
        return name;
    }


    /**
     * Retrieves the width of the map associated with the instance.
     *
     * @return The width of the map as an integer.
     */
    public int getWidth() {
        return width;
    }


    /**
     * Retrieves the height of the map.
     *
     * @return The height of the map as an integer.
     */
    public int getHeight() {
        return height;
    }

    /**
     * Retrieves the list of scenarios of the map.
     *
     * @return All the scenarios of the map.
     */
    public List<Scenario> getScenarios() {
        return List.copyOf(scenarios);
    }


    /**
     * Add a new scenario to the list.
     *
     * @param scenario The new scenario to be added.
     */
    public void addNewScenario(Scenario scenario) {
        if (scenario == null) {
            throw new IllegalArgumentException("Scenario cannot be null.");
        }
        scenarios.add(scenario);
    }

    public boolean removeScenario(Scenario scenario) {
        return scenarios.remove(scenario);
    }

    public boolean hasScenario(Scenario scenario) {
        return scenarios.contains(scenario);
    }

    /**
     * Validates the name of the map.
     * <p>
     * This method ensures that the name of the map is not null or empty.
     * If the name is found to be null or empty, an {@code IllegalArgumentException}
     * is thrown to enforce the validity of the map's name.
     *
     * @throws IllegalArgumentException if the name is null or an empty string.
     */
    private void validateName(String name) {
        if (name == null || name.isEmpty()) {
            throw new IllegalArgumentException("Map name cannot be empty.");
        }
    }

    /**
     * Validates the dimensions of the map.
     * <p>
     * This method ensures that the width and height of the map are positive integers.
     * If either the width or height is less than or equal to zero, an {@code IllegalArgumentException}
     * is thrown to ensure valid map dimensions.
     *
     * @throws IllegalArgumentException if the width or height is non-positive.
     */
    private void validateSize(int width, int height) {
        if (width <= 0 || height <= 0) {
            throw new IllegalArgumentException("Map dimensions must be positive.");
        }
    }

    /**
     * Returns a string representation of the map, including its name and dimensions.
     * <p>
     * The string is formatted as "name (widthxheight)", where `name` is the
     * name of the map, and `width` and `height` represent the dimensions of the map.
     *
     * @return A string representation of the map's name and dimensions.
     */
    @Override
    public String toString() {
        return "" + name + " (" + width + "x" + height + ")";
    }
}