package presentation;

import controller.CreateMapController;
import domain.Map;
import repositories.MapRepository;

import java.util.Scanner;


/**
 * The CreateMapUI class provides a user interface for creating maps.
 * This class implements the Runnable interface to facilitate the execution
 * of the map creation workflow in a separate thread.
 * It interacts with the CreateMapController and MapRepository
 * to collect input, validate data, and create a new map.
 */
public class CreateMapUI implements Runnable {
    private CreateMapController controller = new CreateMapController();
    private MapRepository mapRepository = new MapRepository();

    private String mapName;
    private int width = 0, height = 0;


    /**
     * Retrieves the instance of {@code CreateMapController} associated with this class.
     *
     * @return    An instance of {@code CreateMapController} used to handle operations
     *            related to map creation.
     */
    private CreateMapController getController() {
        return controller;
    }


    /**
     * Retrieves the instance of {@code MapRepository} associated with this class.
     *
     * @return  An instance of {@code MapRepository} used to manage and access the maps.
     */
    private MapRepository getMapRepository() {
        return mapRepository;
    }

    /**
     * Executes the main process of creating a map by interacting with the user interface.
     *
     * This method provides a command-line prompt to guide the user through the map creation process.
     * It displays a banner indicating the initialization of map creation, then proceeds to:
     * 1. Prompt and collect user input required for map creation via the `getInput` method.
     * 2. Submit the collected input data for map creation using the `submitData` method.
     *
     * Any errors encountered during data submission and map creation are displayed as error messages.
     */
    public void run() {
        System.out.println("\n\n------------------------ Create Map ------------------------");
        getInput();
        submitData();
    }


    /**
     * Collects input data for creating a map by prompting the user for map name, width, and height.
     *
     * This method uses helper methods to request each input:
     * - The map name is obtained through the {@code requestMapName} method, ensuring uniqueness by checking
     *   against existing map names in the repository.
     * - The map dimensions (width and height) are collected using the {@code requestWidth} and {@code requestHeight} methods,
     *   validating user input to ensure it falls within acceptable ranges.
     *
     * Once all inputs are provided and validated, they are assigned to the corresponding fields:
     * - {@code mapName}: The name of the map, provided by the user.
     * - {@code width}: The width of the map, constrained to valid dimensions.
     * - {@code height}: The height of the map, constrained to valid dimensions.
     */
    public void getInput() {
        mapName = requestMapName();
        width = requestWidth();
        height = requestHeight();
    }


    /**
     * Prompts the user to enter a name for the map and ensures the entered name is unique.
     * If the name entered already exists in the map repository, the user is prompted to enter
     * a different name. The method trims any leading or trailing spaces from the input.
     *
     * @return  A trimmed string representing the user-provided unique map name.
     */
    private String requestMapName() {
        Scanner sc = new Scanner(System.in);
        System.out.print("Enter the name of the map: ");
        String mapName = sc.nextLine();
        if (mapRepository.existsByName(mapName)) {
            System.out.print("\nName already exists. Please enter a different name: ");
        }
        return mapName.trim();
    }


    /**
     * Prompts the user to input the width of the map, validates the input,
     * and ensures it is a positive integer within the allowed range (1 to 256).
     *
     * @return  The validated width of the map provided by the user.
     */
    private int requestWidth() {
        Scanner sc = new Scanner(System.in);
        System.out.print("Enter the width of the map: ");
        int width;
        do {
            width = sc.nextInt();
            if (width <= 0 || width > 256) {
                System.out.println("Invalid width. Please enter a positive integer less than or equal to 256.");
            }
        } while (width <= 0 || width > 256);
        return width;
    }


    /**
     * Prompts the user to input the height of the map, validates the input,
     * and ensures it is a positive integer within the allowed range (1 to 256).
     *
     * @return  The validated height of the map provided by the user.
     */
    private int requestHeight() {
        Scanner sc = new Scanner(System.in);
        System.out.print("Enter the height of the map: ");
        int height;
        do {
            height = sc.nextInt();
            if (height <= 0 || height > 256) {
                System.out.println("Invalid height. Please enter a positive integer less than or equal to 256.");
            }
        } while (height <= 0 || height > 256);
        return height;
    }


    /**
     * Submits the data required to create a new map by invoking the map creation
     * method from the controller.
     *
     * This method attempts to create a new map with the specified name, width,
     * and height fields of the class. Upon successful creation, a confirmation
     * message is displayed, including the name of the successfully created map.
     *
     * If the map creation process encounters invalid input (e.g., duplicate map
     * name or invalid dimensions), an {@code IllegalArgumentException} is caught
     * and an error message is printed. For other unexpected exceptions, a generic
     * error message is displayed to inform the user.
     */
    private void submitData() {
        try {
            Map map = getController().createMap(mapName, width, height);
            System.out.println("\nMap '" + map.getMapName() + "' successfully created!");
        } catch (IllegalArgumentException e) {
            System.out.println("\nERROR: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("\nERROR: Unexpected error occurred. Please try again later.");
        }
    }
}