package ui.utils;

import controller.DisplayMapController;
import controller.ListStationsController;
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
    private final DisplayMapController ctrl = new DisplayMapController();
    private String mapName;
    private String[][] mapArray;

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
        System.out.println("\n\n------------------------ Display Map ------------------------");
        displayMapList();
        selectData();
        submitData();
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
        addStationsToMap(grid, map, scenario, width, height);
        addIndustriesToMap(grid, map);

        for (int y = 1; y <= height; y++) {
            for (int x = 1; x <= width; x++) {
                System.out.print(grid[y][x]);
            }
            System.out.println();
        }
    }


    /**
     * Displays the list of available maps by retrieving the map data through the {@code ctrl.getMapList()} method
     * and outputting it to the console.
     * <p>
     * This method serves as a utility for presenting all existing maps managed by the system, enabling
     * the user to view them in a textual format.
     */
    private void displayMapList() {
        System.out.println(ctrl.getMapList());
    }


    /**
     * Requests the user to select a map by invoking the {@code requestMapName()} method
     * and assigns the result to the {@code mapName} field.
     * <p>
     * This method prompts user interaction for map selection and updates the internal
     * state of the class with the selected map's name.
     */
    private void selectData() {
        mapName = requestMapName();
    }


    /**
     * Prompts the user to input a map name by displaying a message and reading the user's input.
     * The input is trimmed to remove leading and trailing whitespace before being returned.
     *
     * @return The trimmed map name entered by the user.
     */
    private String requestMapName() {
        Scanner sc = new Scanner(System.in);
        System.out.print("Select a map: ");
        return sc.nextLine().trim();
    }


    /**
     * Submits and initializes map data based on the selected map name.
     * <p>
     * This method performs the following tasks:
     * 1. Retrieves the list of available maps and identifies the map matching the specified {@code mapName}.
     * 2. Verifies if the selected map exists.
     * 3. Initializes a 2D array representation of the map with specified width and height.
     * 4. Fills the map with preset data, including the addition of cities and house blocks.
     *
     * @return A two-dimensional String array representing the initialized map data.
     */
    private String[][] submitData() {
        int width = 0, height = 0;

        Optional<Map> matchingMap = Optional.empty();
        List<Map> mapList = ctrl.getMapList();
        for (int i = 0; i < mapList.size(); i++) {
            Map map = mapList.get(i);
            if (map.getName().equalsIgnoreCase(mapName)) {
                matchingMap = Optional.of(map);
                mapName = map.getName();
                break;
            }
        }

        if (ctrl.verifyIfMapExists(mapName)) {
            width = matchingMap.get().getWidth();
            height = matchingMap.get().getHeight();
        } else {
            System.out.println("\nMap does not exist!");
            return null;
        }

        displayMap();
        return mapArray;
    }

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

    private void addIndustriesToMap(String[][] grid, Map map) {
        List<Industry> ind = indRep.getIndustriesByMap(map);
        for (Industry industry : ind) {
            grid[industry.getPositionY()][industry.getPositionX()] = "🏭";
        }
    }

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
