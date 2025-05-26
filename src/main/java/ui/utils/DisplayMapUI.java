package ui.utils;

import controller.DisplayMapController;
import controller.ListStationsController;
import domain.City;
import domain.HouseBlock;
import domain.Map;
import domain.Station;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Scanner;

/**
 * The DisplayMapUI class is responsible for managing the user interaction
 * related to displaying and selecting maps. It provides functionality to retrieve
 * a list of maps, select a specific map by name, and render the selected map
 * visually with its associated cities and house blocks.
 *
 * This class also implements the Runnable interface, allowing it to be executed
 * in a separate thread as necessary.
 */
public class DisplayMapUI implements Runnable{
    private final DisplayMapController ctrl = new DisplayMapController();
    private String mapName;
    private String[][] mapArray;

    /**
     * Executes the main process of the DisplayMapUI class by orchestrating the following actions:
     *
     * 1. Displays a list of available maps.
     * 2. Requests the user to select a map.
     * 3. Initializes the data for the selected map, including setting up the map structure and its contents.
     * 4. Displays the final visual representation of the map in the console output.
     *
     * This method provides a sequential execution of operations to display and populate a map based on user input.
     */
    public void run(){
        System.out.println("\n\n------------------------ Display Map ------------------------");
        displayMapList();
        selectData();
        submitData();
        System.out.println();
        displayMap();
    }

    /**
     * Displays the map data contained in the `mapArray` field to the console in a grid-like format.
     *
     * Each element of the `mapArray` is printed row by row. Elements within a row are separated by a
     * space, except for the last element in the row, which is printed without a trailing space. After
     * completing each row, a newline character is printed to allow for proper row separation in the output.
     *
     * This method is designed to provide a visual representation of the map's content by formatting
     * the `mapArray` data into a human-readable layout.
     */
    private void displayMap() {
        for (int i = 0; i < mapArray.length; i++) {
            for (int j = 0; j < mapArray[0].length; j++) {
                if (j == mapArray[0].length - 1) {
                    System.out.print(mapArray[i][j]); // no space after the last one
                } else {
                    System.out.print(mapArray[i][j] + " ");
                }
            }
            System.out.println();
        }
    }


    /**
     * Displays the list of available maps by retrieving the map data through the {@code ctrl.getMapList()} method
     * and outputting it to the console.
     *
     * This method serves as a utility for presenting all existing maps managed by the system, enabling
     * the user to view them in a textual format.
     */
    private void displayMapList() {
        System.out.println(ctrl.getMapList());
    }


    /**
     * Requests the user to select a map by invoking the {@code requestMapName()} method
     * and assigns the result to the {@code mapName} field.
     *
     * This method prompts user interaction for map selection and updates the internal
     * state of the class with the selected map's name.
     */
    private void selectData(){
        mapName = requestMapName();
    }


    /**
     * Prompts the user to input a map name by displaying a message and reading the user's input.
     * The input is trimmed to remove leading and trailing whitespace before being returned.
     *
     * @return  The trimmed map name entered by the user.
     */
    private String requestMapName(){
        Scanner sc = new Scanner(System.in);
        System.out.print("Select a map: ");
        return sc.nextLine().trim();
    }


    /**
     * Submits and initializes map data based on the selected map name.
     *
     * This method performs the following tasks:
     * 1. Retrieves the list of available maps and identifies the map matching the specified {@code mapName}.
     * 2. Verifies if the selected map exists.
     * 3. Initializes a 2D array representation of the map with specified width and height.
     * 4. Fills the map with preset data, including the addition of cities and house blocks.
     *
     * @return A two-dimensional String array representing the initialized map data.
     */
    private String[][] submitData(){
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

        if(ctrl.verifyIfMapExists(mapName)){
            width = matchingMap.get().getWidth();
            height = matchingMap.get().getHeight();
        }else{
            System.out.println("\nMap does not exist!");
            return null;
        }

        mapArray = new String[height][width];

        for(int i = 0; i < mapArray.length; i++){
            for(int k = 0; k < mapArray[0].length; k++){
                mapArray[i][k] = "⬛";
            }
        }

        addStationsToMap();
        addCitiesToMap();
        addHouseBlocksToMap();

        return mapArray;
    }


    /**
     * Adds cities to the map array based on the city data managed by the controller.
     *
     * This method iterates through the list of cities, checks if any city is associated
     * with the specified map name, and updates the map array to mark the position of each
     * city's center. The center position is denoted on the map using the "🌆" symbol.
     *
     * The method performs the following steps:
     * 1. Retrieves the list of cities using the controller.
     * 2. Verifies if each city in the list is associated with the current map name.
     * 3. Determines the center X and Y coordinates of qualifying cities.
     * 4. Updates the map array at the corresponding position with the "🌆" symbol.
     *
     * This method assumes that the mapArray is correctly initialized and that the
     * center coordinates are valid within the array's bounds.
     */
    private void addCitiesToMap() {
        List<City> citiesList = ctrl.getCityList();
        for (int i = 0; i < ctrl.getCityList().size(); i++) {
            if(citiesList.stream().anyMatch(city -> city.getMapName().equals(mapName))) {
                int centerX = citiesList.get(i).getCenterX();
                int centerY = citiesList.get(i).getCenterY();
                mapArray[centerY - 1][centerX - 1] = "🌆";
            }
        }
    }


    /**
     * Updates the map array to represent the positions of house blocks associated
     * with cities that match the specified map name. Each house block is denoted
     * by the "🌆" symbol on the map.
     *
     * The method performs the following steps:
     * 1. Retrieves the list of cities through the controller.
     * 2. Identifies cities associated with the current map name by checking the
     *    map name of each city.
     * 3. For qualifying cities, retrieves the list of house blocks.
     * 4. Determines the X and Y coordinates of each house block and updates
     *    the map array at the corresponding position with the "🌆" symbol.
     *
     * Assumptions:
     * - The `mapArray` is correctly initialized and large enough to accommodate
     *   all house block positions.
     * - The X and Y coordinates of the house blocks are within the array's bounds.
     */
    private void addHouseBlocksToMap() {
        List<City> citiesList = ctrl.getCityList();
        for (int i = 0; i < citiesList.size(); i++) {
            if (citiesList.stream().anyMatch(city -> city.getMapName().equals(mapName))){
                List<HouseBlock> houseBlockList = citiesList.get(i).getBlocks();
                for (int j = 0; j < houseBlockList.size(); j++) {
                    int posX = houseBlockList.get(j).getX();
                    int posY = houseBlockList.get(j).getY();
                    mapArray[posY-1][posX-1] = "🌆";
                }
            }
        }
    }

    /**
     * Updates the `mapArray` field to represent the positions of stations on the map
     * based on the data retrieved from the `ListStationsController`.
     *
     * This method performs the following steps:
     * 1. Retrieves the list of stations associated with the current `mapName`.
     * 2. Updates the `mapArray` to show the range of each station, according to its size.
     * 3. Updates the `mapArray` at the corresponding station positions with the
     *    "🚉" symbol to denote the presence of a station.
     *
     * Assumptions:
     * - The `mapArray` is correctly initialized and matches the dimensions of the map.
     * - The coordinates of each station are valid and within the bounds of `mapArray`.
     *
     * This method ensures that all stations related to the current map are visually
     * represented on `mapArray`, enhancing the visualization of transit features.
     */
    private void addStationsToMap()
    {
        ListStationsController listStationsController = new ListStationsController();
        List<Station> stations =  listStationsController.getStationsByMapName(mapName);

        for(Station station : stations)
        {
            int Y = station.getPositionY();
            int X = station.getPositionX();

            int typeID = station.getStationTypeID();
            int size = Station.typesSizes[typeID] / 2;

            if(Station.typesSizes[typeID] % 2 != 0)
            {
                for(int i = X-size; i <= X+size; i++)
                {
                    for(int j = X-size; j <= X+size; j++)
                    {
                        if(!(i == X && j == X))
                        {
                            mapArray[i][j] = "🛤️";
                        }
                    }
                }
            }else
            {
                String rotation = station.getRotation();

                int offsetRow = 0;
                int offsetCol = 0;

                switch (rotation) {
                    case "NW":
                        offsetRow = size - 1;
                        offsetCol = size - 1;
                        break;
                    case "NE":
                        offsetRow = size - 1;
                        offsetCol = size;
                        break;
                    case "SW":
                        offsetRow = size;
                        offsetCol = size - 1;
                        break;
                    case "SE":
                        offsetRow = size;
                        offsetCol = size;
                        break;
                }

                int startRow = Y - offsetRow;
                int startCol = X - offsetCol;

                for (int i = 0; i < Station.typesSizes[typeID]; i++) {
                    for (int j = 0; j < Station.typesSizes[typeID]; j++) {
                        mapArray[startRow + i][startCol + j] = "🛤️";
                    }
                }
            }

            mapArray[station.getPositionY()][station.getPositionX()] = "🚉";
        }
    }
}
