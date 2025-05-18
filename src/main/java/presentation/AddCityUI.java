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
 * The AddCityUI class provides a user-facing interface for creating a new city
 * on an existing map. It implements the Runnable interface to enable execution
 * within a sequential or parallel execution context.
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
     * Collects and processes user input to create a new city on a selected map.
     *
     * Upon successful execution, a new city is created, added to the selected
     * map, and reported to the user. If errors occur during the input process,
     * appropriate error messages are displayed to guide the user.
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
     * Executes the AddCityUI workflow by displaying initial prompts and delegating
     * user input collection and processing to the `getInput` method.
     *
     * The method is intended as the main execution point for the Add City functionality.
     * It begins by displaying a header to indicate the start of the process and then
     * calls `getInput` to handle user interactions and city creation logic.
     */
    @Override
    public void run() {
        System.out.println("\n\n------------------------ Add City ------------------------");
        getInput();
    }
}
