package presentation;

import controller.CreateMapController;
import domain.Map;
import repositories.MapRepository;

import java.util.Scanner;

/**
 * The CreateMapUI class represents a user interface for creating a map through
 * a console-based application. It facilitates interaction with the user to gather
 * required input data such as map name, width, and height, validates the inputs,
 * and delegates the map creation process to the CreateMapController.
 *
 * This class also handles errors during map creation and provides feedback to the user.
 * It is designed to be executed as a runnable process.
 */
public class CreateMapUI implements Runnable {
    private CreateMapController controller = new CreateMapController();
    private MapRepository mapRepository = new MapRepository();

    private String mapName;
    private int width = 0, height = 0;

    /**
     * Retrieves the instance of {@code CreateMapController} associated with this class.
     *
     * @return      The {@code CreateMapController} instance used for map-related operations.
     */
    private CreateMapController getController() {
        return controller;
    }

    /**
     * Executes the main workflow for creating a map in the user interface.
     *
     * This method serves as the entry point to the map creation process. It sequentially
     * performs the following actions:
     * 1. Displays a message indicating the start of the map creation process.
     * 2. Invokes {@code getInput()} to collect the map name, width, and height from the user.
     * 3. Calls {@code submitData()} to validate the input and create the map using the
     *    associated controller.
     */
    public void run() {
        System.out.println("\n\n------------------------ Create Map ------------------------");
        getInput();
        submitData();
    }

    /**
     * Collects and assigns user input for the map name, width, and height.
     *
     * This method sequentially performs the following operations:
     * 1. Prompts the user to input a unique map name by invoking {@code requestMapName()}.
     * 2. Requests the user to input a valid width for the map by calling {@code requestWidth()}.
     * 3. Requests the user to input a valid height for the map by calling {@code requestHeight()}.
     *
     * The inputs are assigned to the respective fields of the class:
     * - {@code mapName} is set to the user-provided unique map name.
     * - {@code width} is set to the validated positive integer for the width.
     * - {@code height} is set to the validated positive integer for the height.
     */
    public void getInput() {
        mapName = requestMapName();
        width = requestWidth();
        height = requestHeight();
    }

    /**
     * Prompts the user to input a name for the map and validates its uniqueness in the repository.
     *
     * The method interacts with the user through the console to collect the map name.
     * If the entered name already exists in the repository, the user is prompted to input
     * a different name. The method trims any leading or trailing whitespace from the input
     * before returning it.
     *
     * @return      The validated map name provided by the user as a trimmed string.
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
     * @return      The validated width of the map provided by the user.
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
     * @return      The validated height of the map provided by the user.
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
     * Validates user input data and creates a new map using the provided name, width, and height.
     *
     * The method performs the following operations:
     * - Calls the {@code createMap()} method of the associated controller to generate a new map instance.
     * - Logs a success message indicating the creation of the map, displaying the map's name.
     * - Handles exceptions to ensure proper error messaging for validation failures or unexpected errors:
     *    - {@code IllegalArgumentException} is caught to provide feedback on invalid or duplicate input.
     *    - Any other {@code Exception} is caught to log a generic error message for unforeseen issues.
     *
     * Exceptions:
     * - {@code IllegalArgumentException} if the map name is not unique or if dimensions are invalid.
     * - {@code Exception} for unexpected errors that occur during the map creation process.
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