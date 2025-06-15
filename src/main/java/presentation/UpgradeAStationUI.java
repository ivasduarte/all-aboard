package presentation;

import controller.UpgradeAStationController;
import domain.*;

import domain.Map;
import dto.BuildingTypeDTO;

import java.util.List;
import java.util.Scanner;

/**
 * This class provides a user interface for upgrading a station in the simulation.
 * It displays all available stations, filters building types based on the current year,
 * removes obsolete buildings automatically (e.g., Telegraph after 1920),
 * and allows the player to add a new building to a selected station.
 * The available building types are filtered using historical availability logic,
 * ensuring that players only see and select buildings appropriate to the scenario's current year.
 * This UI also deducts the cost of the selected building from the scenario's budget
 * upon successful construction.
 */
public class UpgradeAStationUI implements Runnable {
    private Map map;
    private Scenario scenario;

    /**
     * Constructs the UI for upgrading a station based on the given map and scenario.
     *
     * @param map      The current map in the simulation.
     * @param scenario The current scenario being simulated.
     */
    public UpgradeAStationUI(Map map, Scenario scenario) {
        this.map = map;
        this.scenario = scenario;
    }

    /**
     * Displays the station upgrade menu, including:
     * - List of available stations to upgrade
     * - Filtering and displaying of valid buildings based on the scenario year
     * - Removal of obsolete buildings (e.g., Telegraph after 1920)
     * - Input from the user to select station and building
     * - Upgrade execution and budget deduction
     * <p>
     * Provides validation for station selection, building existence, availability by year,
     * and budget constraints.
     */
    public void getInput() {
        System.out.println("\n\n------------------------ Upgrade Station ----------------------");
        Scanner sc = new Scanner(System.in);
        UpgradeAStationController controller = new UpgradeAStationController();
        int budget = scenario.getBudget().getBudget();
        List<Station> stations = controller.getAvailableStations(map.getName());

        if (stations.isEmpty()) {
            System.out.println("There are no available stations to upgrade.");
            return;
        }

        System.out.println("\nAVAILABLE STATIONS TO UPGRADE:");
        for (int i = 0; i < stations.size(); i++) {
            Station s = stations.get(i);
            System.out.println((i + 1) + ". Station ID: " + s.getStationID() + " | Type: " + s.getStationType() +
                    " | Position: (" + s.getPositionX() + ", " + s.getPositionY() + ")");
        }

        System.out.print("\nSELECT A STATION TO UPGRADE: ");
        int selectedIndex = Integer.parseInt(sc.nextLine()) - 1;

        if (selectedIndex < 0 || selectedIndex >= stations.size()) {
            System.out.println("ERROR: Invalid station selection.");
            return;
        }

        Station selectedStation = stations.get(selectedIndex);
        System.out.println();

        System.out.println("CURRENT BUDGET: " + budget + "€");

        int currentYear = scenario.getCurrentYear();
        System.out.println("\nCURRENT SIMULATION YEAR: " + currentYear);

        controller.removeObsoleteBuildings(selectedStation, currentYear);

        System.out.println("\nAVAILABLE BUILDINGS TO ADD TO STATION: ");
        List<BuildingTypeDTO> availableBuildings = controller.getFilteredAvailableBuildingTypesDTO(selectedStation, currentYear);
        if (availableBuildings.isEmpty()) {
            System.out.println("No available buildings for this station at this time.");
        } else {
            for (BuildingTypeDTO bt : availableBuildings) {
                System.out.println(bt.getName() + " | " + bt.getCost() + "€ | Effect: " + bt.getEffect());
            }
        }

        System.out.print("\nENTER THE BUILDING TYPE YOU WISH TO ADD: ");
        String buildingTypeName = sc.nextLine().trim();

        BuildingType selectedBuildingType = controller.getBuildingTypeByName(buildingTypeName);
        if (selectedBuildingType == null) {
            System.out.println("ERROR: Invalid building type.");
            return;
        }

        if (budget < selectedBuildingType.getCost()) {
            System.out.println("ERROR: Not enough budget to build " + buildingTypeName + ".");
            return;
        }

        boolean success = controller.upgradeStation(selectedStation, buildingTypeName, currentYear);
        controller.DeductBudget(selectedStation, selectedBuildingType, scenario);

        if (success) {
            System.out.println("SUCCESS: Building '" + buildingTypeName + "' added to station.");
        } else {
            System.out.println("ERROR: Failed to upgrade the station with building '" + buildingTypeName + "'.");
        }
    }

    /**
     * Entry point when the UI is executed as a Runnable.
     * Calls {@code getInput()} to start the interaction.
     */
    @Override
    public void run() {
        getInput();
    }
}