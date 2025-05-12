package presentation;

import controller.CreateCityController;
import controller.DisplayMapController;
import domain.City;
import domain.HouseBlock;
import controller.CreateCityController.NameMapError;
import controller.CreateCityController.CenterError;
import controller.CreateCityController.BlockError;

import java.util.Scanner;

/**
 * The AddCityUI class is responsible for handling the user interface to add a new city
 * into the system. It retrieves user inputs, validates data, and coordinates with
 * controllers to create cities and assign house blocks.
 *
 * This class provides a command-line interface (CLI) for the user to select a map,
 * define a city, and configure its house blocks either manually or automatically.
 * It ensures that input validation is performed and displays appropriate error messages
 * when necessary.
 *
 * Implements the {@link Runnable} interface to run the user interface process.
 */

public class AddCityUI implements Runnable {
    private final CreateCityController ctrl = new CreateCityController();
    private final DisplayMapController controller = new DisplayMapController();
    private String mapName;
    private String cityName;
    private int centerX;
    private int centerY;
    private char blockMode;
    private int houseBlocks;


    /**
     * Handles the process of gathering user input, validating input data,
     * and creating a new city along with its associated blocks on a specific map.
     *
     * This method interacts with the user via the console to perform the following steps:
     * 1. Displays the list of available maps and prompts the user to select one.
     * 2. Prompts the user to provide a name for the city and validates it for duplicate names or map existence.
     * 3. Requests coordinates for the city's center and validates their position on the map.
     * 4. Creates the city on the selected map once the center coordinates are valid.
     * 5. Allows the user to choose between manual or automatic block assignment modes:
     *    - In manual mode, the user specifies block coordinates individually, which are validated and added accordingly.
     *    - In automatic mode, blocks are assigned using a specified count and spread (sigma) parameter.
     *
     * Appropriate error messages are displayed for any validation failures during the process, and input is re-requested
     * until valid data is provided.
     */
    public void getInput() {
        CreateCityController.NameMapError nameError;
        CreateCityController.CenterError centerError;
        Scanner sc = new Scanner(System.in);

        do {
            System.out.print(controller.getMapList());
            System.out.print("\nSelect a map from the list: ");
            mapName = sc.nextLine().trim();

            System.out.print("Enter the name of the city: ");
            cityName = sc.nextLine().trim();

            nameError = ctrl.validateNameMap(cityName, mapName);

            if (nameError == NameMapError.MAP_NOT_FOUND) {
                System.out.print("Error: Map '" + mapName + "' does not exist.");
            } else if (nameError == NameMapError.NAME_DUPLICATE) {
                System.out.print("Error: City name '" + cityName + "' already used on map '" + mapName + "'.");
            }
        } while (nameError != NameMapError.OK);


        do {
            System.out.print("Enter the center X of the city: ");
            centerX = Integer.parseInt(sc.nextLine());

            System.out.print("Enter the center Y of the city: ");
            centerY = Integer.parseInt(sc.nextLine());

            centerError = ctrl.validateCenter(cityName, mapName, centerX, centerY);

            if (centerError == CenterError.OUT_OF_BOUNDS) {
                System.out.println("Error: Center (" + centerX + "," + centerY + ") outside map bounds.");
            } else if (centerError == CenterError.POSITION_TAKEN) {
                System.out.println("Error: Another city occupies (" + centerX + "," + centerY + ").");
            } else if (centerError == CenterError.BLOCK_PRESENT)
                System.out.println("Error: A house block exists at (" + centerX + "," + centerY + ").");
        } while (centerError != CenterError.OK);

        City city = ctrl.createCity(cityName, mapName, centerX, centerY);
        System.out.println("City '" + city.getName() + "' successfully created on map '" + city.getMapName() + "'.");

        do {
            System.out.print("\nChoose block mode (M=Manual, A=Auto): ");
            String line = sc.nextLine().trim().toUpperCase();
            blockMode = line.isEmpty() ? ' ' : line.charAt(0);
        } while (blockMode != 'M' && blockMode != 'A');

        if (blockMode == 'M') {
            System.out.print("Enter the number of blocks to add: ");
            houseBlocks = Integer.parseInt(sc.nextLine());

            for (int i = 0; i < houseBlocks; i++) {
                CreateCityController.BlockError blockError;
                HouseBlock block;

                do {
                    System.out.println("Block " + (i + 1) + " X: ");
                    int blockX = Integer.parseInt(sc.nextLine());

                    System.out.println("Block " + (i + 1) + " Y: ");
                    int blockY = Integer.parseInt(sc.nextLine());

                    block = new HouseBlock(blockX, blockY);
                    blockError = ctrl.validateBlock(city.getMapName(), block);

                    if (blockError == BlockError.OUT_OF_BOUNDS) {
                        System.out.println("Error: Block (" + blockX + "," + blockY + ") outside map bounds.");
                    } else if (blockError == BlockError.COLLIDES_CITY_CENTER) {
                        System.out.println("Error: Block collides with a city center at (" + blockX + "," + blockY + ").");
                    } else if (blockError == BlockError.COLLIDES_BLOCK)
                        System.out.println("Error: Block conflicts with existing block at (" + blockX + "," + blockY + ").");
                } while (blockError != BlockError.OK);

                ctrl.addBlockManual(city, block);
            }
            System.out.println("Blocks assigned manually.");
        } else {
            System.out.print("Number of blocks: ");
            int n = Integer.parseInt(sc.nextLine());

            System.out.print("Sigma (spread): ");
            double sigma = Double.parseDouble(sc.nextLine());

            ctrl.assignBlocksAuto(city, n, sigma);
            System.out.println("Blocks assigned automatically.");
        }
        System.out.println("City '" + city.getName() + "' successfully created on map '" + city.getMapName() + "'.");
    }


    /**
     * Executes the main function of adding a city by displaying the user interface
     * and initiating the input collection process.
     *
     * This method provides a user-facing console interaction for city creation.
     * It displays a banner indicating the start of the "Add City" process,
     * and delegates the task of gathering required input data, validation,
     * and city creation steps to the `getInput` method.
     */
    @Override
    public void run() {
        System.out.println("\n\n------------------------ Add City ------------------------");
        getInput();
    }
}
