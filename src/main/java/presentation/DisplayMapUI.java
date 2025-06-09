package presentation;

import controller.DisplayMapController;
import domain.*;
import repositories.*;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Scanner;
import java.util.stream.Collectors;

/**
 * The DisplayMapUI class is responsible for managing the user interaction
 * related to displaying and selecting maps. It provides functionality to retrieve
 * a list of maps, select a specific map by name, and render the selected map
 * visually with its associated cities and house blocks.
 * <p>
 * This class also implements the Runnable interface, allowing it to be executed
 * in a separate thread as necessary.
 */
public class DisplayMapUI implements Runnable {
    private final User currentUser = UserSession.getInstance().getUser();
    private final StationRepository stRep = Repositories.getInstance().getStationRepository();
    private final CityRepository cityRep = Repositories.getInstance().getCityRepository();
    private final IndustryRepository indRep = Repositories.getInstance().getIndustryRepository();

    private final Map map;
    private final Scenario scenario;

    public DisplayMapUI(Map map, Scenario scenario) {
        this.map = map;
        this.scenario = scenario;
    }

    /**
     * Executes the main process of the DisplayMapUI class by orchestrating the following actions:
     * <p>
     * 1. Displays a list of available maps.
     * 2. Requests the user to select a map.
     * 3. Initializes the data for the selected map, including setting up the map structure and its contents.
     * 4. Displays the final visual representation of the map in the console output.
     * <p>
     * This method provides a sequential execution of operations to display and populate a map based on user input.
     */
    public void run() {
        System.out.println("\n\n------------------------ MAP DISPLAY ------------------------");
        System.out.println();
        displayMap();
        System.out.println();
    }

    /**
     * Displays the map data contained in the `mapArray` field to the console in a grid-like format.
     * <p>
     * Each element of the `mapArray` is printed row by row. Elements within a row are separated by a
     * space, except for the last element in the row, which is printed without a trailing space. After
     * completing each row, a newline character is printed to allow for proper row separation in the output.
     * <p>
     * This method is designed to provide a visual representation of the map's content by formatting
     * the `mapArray` data into a human-readable layout.
     */
    private void displayMap() {
        int width = map.getWidth();
        int height = map.getHeight();
        String[][] grid = new String[height + 1][width + 1];
        for (int y = 1; y <= height; y++) {
            for (int x = 1; x <= width; x++) {
                grid[y][x] = "⬛";
            }
        }
        addCitiesToMap(grid, map);
        addIndustriesToMap(grid, map);
        addStationsToMap(grid, map, scenario, width, height);

        for (int y = 1; y <= height; y++) {
            for (int x = 1; x <= width; x++) {
                System.out.print(grid[y][x]);
            }
            System.out.println();
        }
    }

    /**
     * Populates a grid representation of a map with city icons and house block icons
     * based on the cities associated with the specified map.
     *
     * This method retrieves all cities linked to the given map, places a city marker
     * ("🌆") at the center of each city within the grid, and populates the surrounding
     * region of each city with house block markers ("🏠") based on the coordinates of
     * its blocks.
     *
     * @param grid The grid representation of the map, where each cell corresponds to a specific location.
     *             The grid is modified to include city and house block markers.
     * @param map  The map corresponding to the grid, specifying which cities are to be added.
     */
    private void addCitiesToMap(String[][] grid, Map map) {
        List<City> cities = cityRep.getCities().stream()
                .filter(c -> c.getMapName().equals(map.getName()))
                .collect(Collectors.toList());
        for (City city : cities) {
            grid[city.getCenterY()][city.getCenterX()] = "🌆";
            for (HouseBlock hb : city.getBlocks()) {
                grid[hb.getY()][hb.getX()] = "🏠";
            }
        }
    }

    /**
     * Populates a grid representation of a map with station markers based on the stations
     * associated with the given map, scenario, and current user.
     *
     * The method processes multiple types of stations (e.g., DEPOT, TERMINAL, STATION),
     * determines their layout and orientation, and marks their positions on the grid
     * accordingly. Each station type has specific dimensions and placement rules to
     * reflect its representation within the grid.
     *
     * @param grid   The grid representation of the map, where each cell corresponds to
     *               a specific location. The grid is modified to include station markers.
     * @param map    The map corresponding to the grid, specifying which stations are to
     *               be added.
     * @param scenario The scenario associated with the current map, used to filter the
     *                 stations based on their scenario name.
     * @param width  The width of the grid, representing the horizontal size of the map.
     * @param height The height of the grid, representing the vertical size of the map.
     */
    private void addStationsToMap(String[][] grid, Map map, Scenario scenario, int width, int height) {
        if (currentUser != null) {
            String owner = currentUser.getEmail();
            for (Station st : stRep.getStations()) {
                if (!st.getMapName().equals(map.getName()) ||
                        !Objects.equals(st.getOwnerPlayer(), owner) ||
                        !Objects.equals(st.getScenarioName(), scenario.getScenarioName())) {
                    continue;
                }

                int w, h, ox, oy;
                switch (st.getStationType()) {
                    case DEPOT:
                        w = h = 3;
                        ox = st.getPositionX() - 1;
                        oy = st.getPositionY() - 1;
                        paintArea(grid, width, height, st.getPositionX(), st.getPositionY(), ox, oy, w, h);
                        break;
                    case TERMINAL:
                        w = h = 5;
                        ox = st.getPositionX() - 2;
                        oy = st.getPositionY() - 2;
                        paintArea(grid, width, height, st.getPositionX(), st.getPositionY(), ox, oy, w, h);
                        break;
                    case STATION:
                        w = h = 4;
                        ox = st.getPositionX() - 1;
                        oy = st.getPositionY() - 1;
                        int sdx = 1, sdy = 1;
                        switch (st.getOrientation()) {
                            case NW:
                                break;
                            case NE:
                                sdx = 2;
                                break;
                            case SW:
                                sdy = 2;
                                break;
                            case SE:
                                sdx = 2;
                                sdy = 2;
                                break;
                        }
                        for (int dy = 0; dy < h; dy++) {
                            for (int dx = 0; dx < w; dx++) {
                                int gx = ox + dx;
                                int gy = oy + dy;
                                if (gx >= 1 && gx <= width && gy >= 1 && gy <= height) {
                                    grid[gy][gx] = (dx == sdx && dy == sdy) ? "🚉" : "🅿️";
                                }
                            }
                        }
                        break;
                    default:
                        grid[st.getPositionY()][st.getPositionX()] = "🚉";
                }
            }
        }
    }

    /**
     * Populates a grid representation of a map with industry markers based on
     * the industries associated with the specified map.
     *
     * This method retrieves all industries linked to the given map and places
     * an industry marker ("🏭") on the grid at the corresponding coordinates of
     * each industry.
     *
     * @param grid The grid representation of the map, where each cell corresponds
     *             to a specific location. The grid is modified to include industry
     *             markers.
     * @param map  The map corresponding to the grid, specifying which industries
     *             are to be added.
     */
    private void addIndustriesToMap(String[][] grid, Map map) {
        List<Industry> ind = indRep.getIndustriesByMap(map);
        for (Industry industry : ind) {
            grid[industry.getPositionY()][industry.getPositionX()] = "🏭";
        }
    }

    /**
     * Paints a specified rectangular area within the grid with station and parking markers.
     *
     * The method modifies the provided grid to mark a rectangular area based on the specified
     * dimensions and offset. Within this area, a station marker ("🚉") is placed at the center,
     * and parking markers ("🅿️") are placed in the remaining cells. The painting is constrained
     * by the limits of the grid's dimensions.
     *
     * @param grid The grid representation where the area is to be painted. Each cell
     *             corresponds to a specific location on the map.
     * @param maxW The maximum width of the grid, used to constrain the painting area.
     * @param maxH The maximum height of the grid, used to constrain the painting area.
     * @param cx   The x-coordinate of the center position for the station marker ("🚉").
     * @param cy   The y-coordinate of the center position for the station marker ("🚉").
     * @param ox   The x-coordinate of the offset where the rectangular area starts.
     * @param oy   The y-coordinate of the offset where the rectangular area starts.
     * @param w    The width of the rectangular area to be painted.
     * @param h    The height of the rectangular area to be painted.
     */
    private void paintArea(String[][] grid, int maxW, int maxH, int cx, int cy, int ox, int oy, int w, int h) {
        for (int dy = 0; dy < h; dy++) {
            for (int dx = 0; dx < w; dx++) {
                int x = ox + dx;
                int y = oy + dy;
                if (x >= 1 && x <= maxW && y >= 1 && y <= maxH) {
                    grid[y][x] = (x == cx && y == cy) ? "🚉" : "🅿️";
                }
            }
        }
    }
}
