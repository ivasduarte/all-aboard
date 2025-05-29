package presentation;

import controller.CreateCityController;
import controller.DisplayMapController;
import domain.City;
import domain.HouseBlock;
import controller.CreateCityController.NameMapError;
import controller.CreateCityController.CenterError;
import controller.CreateCityController.BlockError;
import domain.Map;
import domain.Scenario;
import repositories.MapRepository;
import repositories.Repositories;
import repositories.ScenarioRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;

/**
 * The AddCityUI class provides a user-facing interface for creating a new city
 * on an existing map. It implements the Runnable interface to enable execution
 * within a sequential or parallel execution context.
 */
public class AddCityUI implements Runnable {
    private final CreateCityController ctrl = new CreateCityController();

    private Map map;
    private String mapName;
    private String cityName;
    private int centerX;
    private int centerY;
    private char blockMode;
    private int houseBlocks;

    public AddCityUI(Map map) {
        this.map = map;
        this.mapName = map.getName();

    }

    /**
     * Handles the process of collecting and validating user input for creating a new city and its associated house blocks.
     * This includes:
     * <ul>
     * <li>Prompting the user for a city name and validating its uniqueness on a specified map.</li>
     * <li>Prompting for city center coordinates and ensuring they are valid and unoccupied.</li>
     * <li>Allowing the user to add house blocks either manually or automatically, with appropriate validation checks.</li>
     * <li>Integrating the city into the selected map and triggering UI updates for displaying results.</li>
     * </ul>
     *
     * The method also ensures that all inputs meet the necessary constraints, providing
     * error messages and re-prompting the user when invalid inputs are detected.
     * It connects and interacts with repositories and controllers to fetch data, execute
     * operations, and maintain consistency within the application's domain model.
     */
    public void getInput() {
        CreateCityController.NameMapError nameError;
        CreateCityController.CenterError centerError;
        Scanner sc = new Scanner(System.in);

        do {
            System.out.print("CITY NAME: ");
            cityName = sc.nextLine().trim();

            nameError = ctrl.validateNameMap(cityName, mapName);

            if (nameError == NameMapError.MAP_NOT_FOUND) {
                System.out.print("ERROR: Map '" + mapName + "' does not exist.");
            } else if (nameError == NameMapError.NAME_DUPLICATE) {
                System.out.print("ERROR: City name '" + cityName + "' already used on map '" + mapName + "'.");
            }
        } while (nameError != NameMapError.OK);


        do {
            System.out.print("CITY'S CENTER X: ");
            centerX = Integer.parseInt(sc.nextLine());

            System.out.print("CITY'S CENTER Y: ");
            centerY = Integer.parseInt(sc.nextLine());

            centerError = ctrl.validateCenter(cityName, mapName, centerX, centerY);

            if (centerError == CenterError.OUT_OF_BOUNDS) {
                System.out.println("ERROR: Center (" + centerX + "," + centerY + ") outside map bounds.");
            } else if (centerError == CenterError.POSITION_TAKEN) {
                System.out.println("ERROR: Another city occupies (" + centerX + "," + centerY + ").");
            } else if (centerError == CenterError.BLOCK_PRESENT)
                System.out.println("ERROR: A house block exists at (" + centerX + "," + centerY + ").");
        } while (centerError != CenterError.OK);

        City city = ctrl.createCity(cityName, mapName, centerX, centerY);
        System.out.println("\n'" + city.getName() + "' SUCCESSFULLY CREATED ON MAP '" + city.getMapName() + "'.");

        MapRepository mapRepository = Repositories.getInstance().getMapRepository();
        ScenarioRepository scenarioRepository = Repositories.getInstance().getScenarioRepository();

        Optional<Map> mapOptional = mapRepository.getMap(city.getMapName());
        if (mapOptional.isPresent()) {
            Map currentMap = mapOptional.get();

            List<Scenario> scenariosForMap = scenarioRepository.getScenarioListByMap(currentMap);

            if (!scenariosForMap.isEmpty()) {
                Scenario currentScenario = scenariosForMap.get(0);
                DisplayMapUI displayMapUI = new DisplayMapUI(currentMap, currentScenario);
                displayMapUI.run();
            } else {
                System.out.println("No scenarios available for this map.");
            }
        } else {
            System.out.println("Map not found: " + city.getMapName());
        }


        do {
            System.out.println("--------------------------- ADD HOUSE BLOCKS ---------------------------");
            System.out.print("\nINSERT HOUSE BLOCKS (MANUALLY (M) OR AUTOMATICALLY (A)): ");
            String line = sc.nextLine().trim().toUpperCase();
            blockMode = line.isEmpty() ? ' ' : line.charAt(0);
        } while (blockMode != 'M' && blockMode != 'A');

        if (blockMode == 'M') {
            System.out.print("\nNUMBER OF HOUSE BLOCKS: ");
            houseBlocks = Integer.parseInt(sc.nextLine());

            List<HouseBlock> addedBlocks = new ArrayList<>();

            for (int i = 0; i < houseBlocks; i++) {
                CreateCityController.BlockError blockError;
                HouseBlock block;
                int blockX, blockY;

                do {
                    System.out.print("X POSITION OF BLOCK " + (i + 1) + " : ");
                    blockX = Integer.parseInt(sc.nextLine());

                    System.out.print("Y POSITION OF BLOCK " + (i + 1) + " : ");
                    blockY = Integer.parseInt(sc.nextLine());

                    block = new HouseBlock(blockX, blockY);
                    blockError = ctrl.validateBlock(city.getMapName(), block);

                    if (blockError == BlockError.OUT_OF_BOUNDS) {
                        System.out.println("ERROR: Block (" + blockX + "," + blockY + ") outside map bounds.");
                    } else if (blockError == BlockError.COLLIDES_CITY_CENTER) {
                        System.out.println("ERROR: Block collides with a city center at (" + blockX + "," + blockY + ").");
                    } else if (blockError == BlockError.COLLIDES_BLOCK) {
                        System.out.println("ERROR: Block conflicts with existing block at (" + blockX + "," + blockY + ").");
                    }
                } while (blockError != BlockError.OK);

                ctrl.addBlockManual(city, block);
                addedBlocks.add(block);
            }

            System.out.println("\nHOUSE BLOCKS ADDED MANUALLY.");
            for (int j = 0; j < addedBlocks.size(); j++) {
                HouseBlock addedBlock = addedBlocks.get(j);
                System.out.println("Block " + (j + 1) + " added at (" + addedBlock.getX() + "," + addedBlock.getY() + ").");
            }

            if (mapOptional.isPresent()) {
                Map currentMap = mapOptional.get();

                List<Scenario> scenariosForMap = scenarioRepository.getScenarioListByMap(currentMap);

                if (!scenariosForMap.isEmpty()) {
                    Scenario currentScenario = scenariosForMap.get(0);
                    DisplayMapUI displayMapUI = new DisplayMapUI(currentMap, currentScenario);
                    displayMapUI.run();
                } else {
                    System.out.println("No scenarios available for this map.");
                }
            } else {
                System.out.println("Map not found: " + city.getMapName());
            }


        } else {
            System.out.println("\n--------------------------- INSERT HOUSE BLOCKS AUTOMATICALLY -------------------------");
            System.out.print("\nNUMBER OF HOUSE BLOCKS: ");
            int n = Integer.parseInt(sc.nextLine());

            System.out.print("SPREAD OF HOUSE BLOCKS: ");
            double sigma = Double.parseDouble(sc.nextLine());

            List<HouseBlock> blocksBeforeAuto = new ArrayList<>(city.getBlocks());

            ctrl.assignBlocksAuto(city, n, sigma);

            List<HouseBlock> blocksAfterAuto = city.getBlocks();

            System.out.println("\nHOUSE BLOCKS ADDED AUTOMATICALLY.");

            int addedCount = 0;
            for (int i = blocksBeforeAuto.size(); i < blocksAfterAuto.size(); i++) {
                HouseBlock addedBlock = blocksAfterAuto.get(i);
                addedCount++;
                System.out.println("Block " + addedCount + " added at (" + addedBlock.getX() + "," + addedBlock.getY() + ").");
            }
        }
        System.out.println("\n'" + city.getName() + "' SUCCESSFULLY CREATED ON MAP '" + city.getMapName() + "'.");

        if (mapOptional.isPresent()) {
            Map currentMap = mapOptional.get();

            List<Scenario> scenariosForMap = scenarioRepository.getScenarioListByMap(currentMap);

            if (!scenariosForMap.isEmpty()) {
                Scenario currentScenario = scenariosForMap.get(0);
                DisplayMapUI displayMapUI = new DisplayMapUI(currentMap, currentScenario);
                displayMapUI.run();
            } else {
                System.out.println("No scenarios available for this map.");
            }
        } else {
            System.out.println("Map not found: " + city.getMapName());
        }
    }

    /**
     * Executes the AddCityUI workflow by displaying initial prompts and delegating
     * user input collection and processing to the `getInput` method.
     * <p>
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
