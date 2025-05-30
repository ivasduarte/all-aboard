package presentation;

import controller.CreateScenarioController;
import controller.DisplayMapController;
import domain.Map;
import domain.Restrictions;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

/**
 * The CreateScenarioUI class provides a user interface for creating a new scenario,
 * including input validation and interaction with the underlying controllers.
 * This class collects necessary user input, interacts with the CreateScenarioController
 * to create scenarios, and manages the display of success or error messages.
 *
 * This class implements the Runnable interface, allowing it to be executed within
 * a separate thread if required.
 */
public class CreateScenarioUI implements Runnable {
    private final CreateScenarioController ctrl = new CreateScenarioController();
    private DisplayMapController controller = new DisplayMapController();

    private String scenarioName;
    private Restrictions restrictions;
    private Map map;
    private String mapName;

    public CreateScenarioUI(Map map) {
        this.map = map;

    }

    /**
     * Collects input from the user to configure a new scenario.
     *
     * This method prompts the user to select a map from a list, enter a scenario name,
     * specify start and end dates, define locomotive types, and list historical events.
     * The collected inputs are then validated and used to set scenario restrictions.
     *
     * Error handling includes:
     * - Catching invalid date formats entered by the user.
     * - Catching generic exceptions and displaying their corresponding messages.
     *
     * If invalid inputs are provided, appropriate error messages are displayed using the
     * {@code showError} method.
     */
    public void getInput() {
        Scanner sc = new Scanner(System.in);

        System.out.print("\nENTER SCENARIO NAME: ");
        scenarioName = sc.nextLine().trim();

        try {
            System.out.println("\n=== SCENARIO RESTRICTIONS ===");

            System.out.print("\nSCENARIO'S START DATE (yyyy-mm-dd): ");
            LocalDate startDate = LocalDate.parse(sc.nextLine());

            System.out.print("SCENARIO'S END DATE (yyyy-mm-dd): ");
            LocalDate endDate = LocalDate.parse(sc.nextLine());

            System.out.print("\nLOCOMOTIVE RESTRICTIONS: ");
            System.out.println("\n--> Note: Locomotive types must be comma-seperated. E.g. 'steam, diesel, electric'.");
            System.out.print("\nENTER LOCOMOTIVE TYPES: ");
            List<String> locoTypes = Arrays.asList(sc.nextLine().split("\\s*,\\s*"));

            System.out.print("\n\nHISTORICAL RESTRICTIONS: ");
            System.out.println("\n--> Note: Events must be comma-seperated. E.g. 'none, pandemic, great war'.");
            System.out.println("--> Note: In a pandemic situation, cargo demand is lowered, however increases every year.");
            System.out.println("--> Note: In a great war situation, all costs are increased, however cargo demand is greatly increased.");
            System.out.print("\nENTER HISTORICAL EVENTS: ");
            List<String> events = Arrays.asList(sc.nextLine().split("\\s*,\\s*"));

            events.replaceAll(String::toLowerCase);

            restrictions = new Restrictions(startDate, endDate, locoTypes, events);

            if (!restrictions.validate()) {
                showError("ERROR: Invalid restrictions provided.");
                restrictions = null;
            }
        } catch (DateTimeParseException e) {
            showError("ERROR: Invalid date format. Use yyyy-mm-dd.");
        } catch (Exception e) {
            showError("ERROR: " + e.getMessage());
        }
    }

    /**
     * This method serves as the main execution point for the Create Scenario functionality.
     * It begins by displaying a header to indicate the start of the process, then:
     * - Collects and validates user input via the `getInput` method.
     * - Submits the collected data to create a new scenario using the `submitData` method,
     *   which handles scenario creation and provides success or error messages.
     *
     * Any input-related errors are handled during execution through error messaging.
     */
    @Override
    public void run() {
        System.out.println("\n\n------------------------ Create Scenario ------------------------");
        getInput();
        submitData();
    }

    /**
     * Submits the collected data to create a new scenario and displays the outcome.
     *
     * This method validates the necessary inputs and sends a request to create
     * a scenario using the provided data. If the required inputs (`restrictions`)
     * are null, an error message is displayed, and the execution is aborted.
     *
     * If the scenario creation request fails, the error message returned by the controller is shown to the user.
     * Otherwise, a success message is displayed.
     *
     * Error handling includes:
     * - Validating that `restrictions` isn't null.
     * - Interpreting error messages returned by the `createScenario` method.
     */
    public void submitData() {
        if (restrictions == null) {
            showError("Scenario creation aborted due to invalid input.");
            return;
        }

        String result = ctrl.createScenario(scenarioName, restrictions, map);
        if (result.startsWith("ERROR")) {
            showError(result);
        } else {
            showSuccess(result);
        }
    }

    /**
     * Displays an error message to the user by printing it to the console.
     *
     * @param message   The error message to be displayed
     */
    public void showError(String message) {
        System.out.println(message);
    }

    /**
     * Displays a success message to the user by printing it to the console.
     *
     * @param message   The success message to be displayed
     */
    public void showSuccess(String message) {
        System.out.println(message);
    }
}
