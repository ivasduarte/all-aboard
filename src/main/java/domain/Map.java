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
    private int cellSizeKm;

    private List<Scenario> scenarios;
    private List<Station> stations;
    private List<City> cities;
    private List<Industry> industries;

    /**
     * Constructs a new Map instance with the specified name, width, and height.
     * <p>
     * This constructor initializes the map's attributes, including its name, dimensions,
     * and an empty list of scenarios. It also validates the name and cellSizeKm of the map
     * during initialization.
     *
     * @param name   The name of the map. Must not be null or empty.
     * @param width  The width of the map. Must be a positive integer.
     * @param height The height of the map. Must be a positive integer.
     * @throws IllegalArgumentException if the name is null or empty, or if the width or height is non-positive.
     */
    public Map(String name, int width, int height, int cellSizeKm) {
        this.name = name;
        this.width = width;
        this.height = height;
        this.cellSizeKm = cellSizeKm;

        scenarios = new ArrayList<>();
        stations = new ArrayList<>();
        cities = new ArrayList<>();
        industries = new ArrayList<>();

        validateName(name);
        validateSize(width, height);
        validateCellSize(cellSizeKm);
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
     * Retrieves the list of stations of the map.
     *
     * @return All the stations of the map.
     */
    public List<Station> getStations() {
        return List.copyOf(stations);
    }

    /**
     * Retrieves the list of cities of the map.
     *
     * @return All the cities of the map.
     */
    public List<City> getCities() {
        return List.copyOf(cities);
    }

    /**
     * Retrieves the list of industries of the map.
     *
     * @return All the industries of the map.
     */
    public List<Industry> getIndustries() {
        return List.copyOf(industries);
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

    /**
     * Add a new station to the list.
     *
     * @param station The new station to be added.
     */
    public void addNewStation(Station station) {
        if (station == null) {
            throw new IllegalArgumentException("Station cannot be null.");
        }
        stations.add(station);
    }

    /**
     * Add a new city to the list.
     *
     * @param city The new city to be added.
     */
    public void addNewCity(City city) {
        if (city == null) {
            throw new IllegalArgumentException("city cannot be null.");
        }
        cities.add(city);
    }

    /**
     * Add a new industry to the list.
     *
     * @param industry The new industry to be added.
     */
    public void addNewIndustry(Industry industry) {
        if (industry == null) {
            throw new IllegalArgumentException("Station cannot be null.");
        }
        industries.add(industry);
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
     * Validates the cellSizeKm of the map in kilometers.
     *
     * This method ensures that the provided map cellSizeKm is a positive value.
     * If the cellSizeKm is less than or equal to 0, an {@code IllegalArgumentException}
     * is thrown to enforce validity.
     *
     * @param cellSizeKm The cellSizeKm of the map in kilometers to be validated.
     *             Must be a positive integer.
     * @throws IllegalArgumentException if the cellSizeKm is less than or equal to 0.
     */
    public void validateCellSize(int cellSizeKm){
        if(cellSizeKm < 0){
            throw new IllegalArgumentException("Cell size must be positive.");
        }
    }

    public int getCellSizeKm() {
        return cellSizeKm;
    }

    public double getTotalWidthKm() {
        return width * cellSizeKm;
    }

    public double getTotalHeightKm() {
        return height * cellSizeKm;
    }

    public double getAreaKm() {
        return getTotalWidthKm() * getTotalHeightKm();
    }


    /**
     * Returns a string representation of the Map object.
     * The string representation includes the map's name,
     * dimensions (width and height), and the cellSizeKm of the space it covers in kilometers.
     *
     * @return A string describing the map's name, dimensions, and corresponding cellSizeKm in kilometers.
     */
    @Override
    public String toString() {
        return "Map '" + name + "' (" + width + "x" + height + "), Cell size: " + cellSizeKm + " km, Area: " + getAreaKm() + " km²";
    }
}