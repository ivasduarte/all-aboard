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
 * The CreateScenarioUI class provides a user interface for creating a new scenario
 * by interacting with the user to gather required inputs, such as the scenario name,
 * map details, restrictions, and other scenario-specific data.
 *
 * This class interacts with controllers to manage the workflow, validate user input,
 * and persist the scenario in the repository.
 *
 * Implements the Runnable interface to allow execution in a separate thread.
 */
public class CreateScenarioUI implements Runnable {
    private final CreateScenarioController ctrl = new CreateScenarioController();
    private DisplayMapController controller = new DisplayMapController();

    private String scenarioName;
    private Restrictions restrictions;
    private Map map;
    private String mapName;


    /**
     * Prompts the user to input details for creating a scenario, including map selection,
     * scenario name, date range, locomotive types, and historical events. The input is validated
     * and stored in the class fields if valid.
     *
     * The method performs the following steps:
     * 1. Displays a list of maps retrieved from the controller and prompts the user to select a map.
     * 2. Prompts the user to input a scenario name.
     * 3. Collects and parses start and end dates for the scenario time range.
     * 4. Requests input for allowed locomotive types and historical events, parsing them into lists.
     * 5. Validates the collected input. Displays an error and terminates if any validation fails.
     *
     * Validation errors may include the following:
     * - Invalid date format for start or end date.
     * - End date earlier than start date.
     * - Missing or invalid locomotive types.
     * - Missing or invalid historical event descriptions.
     *
     * If any exception occurs (e.g., invalid input values), the method logs a descriptive error message
     * using the {@code showError(String)} method.
     */
    public void getInput() {
        Scanner sc = new Scanner(System.in);

        System.out.print(controller.getMapList());
        System.out.print("\nSelect a map from the list: ");
        mapName = sc.nextLine().trim();

        System.out.print("Enter the name of the scenario: ");
        scenarioName = sc.nextLine().trim();

        try {
            System.out.print("\nEnter Start Date (yyyy-mm-dd): ");
            LocalDate startDate = LocalDate.parse(sc.nextLine());

            System.out.print("Enter End Date (yyyy-mm-dd): ");
            LocalDate endDate = LocalDate.parse(sc.nextLine());

            System.out.print("\nEnter Locomotive Types (comma-separated: steam, diesel, electric): ");
            List<String> locoTypes = Arrays.asList(sc.nextLine().split("\\s*,\\s*"));

            System.out.print("\nEnter Historical Events (comma-separated descriptions): ");
            List<String> events = Arrays.asList(sc.nextLine().split("\\s*,\\s*"));

            restrictions = new Restrictions(startDate, endDate, locoTypes, events);

            if (!restrictions.validate()) {
                showError("ERROR: Invalid restrictions provided.");
                restrictions = null;
                return;
            }
        } catch (DateTimeParseException e) {
            showError("ERROR: Invalid date format. Use yyyy-mm-dd.");
        } catch (Exception e) {
            showError("ERROR: " + e.getMessage());
        }
    }


    /**
     * Executes the process of creating a scenario in the system. This method serves
     * as the entry point for user action within the CreateScenarioUI workflow.
     *
     * The method performs the following sequence of actions:
     * 1. Displays a header indicating the start of the scenario creation process.
     * 2. Invokes the {@code getInput()} method to prompt the user for all necessary
     *    inputs, including selecting a map, specifying a scenario name, providing
     *    a date range, and defining locomotive types and historical events.
     * 3. Invokes the {@code submitData()} method to process and submit the collected
     *    input data.
     *
     * The success or failure of the scenario creation is handled within the
     * {@code getInput()} and {@code submitData()} methods, which display appropriate
     * messages to the user.
     */
    @Override
    public void run() {
        System.out.println("\n\n------------------------ Create Scenario ------------------------");
        getInput();
        submitData();
    }


    /**
     * Submits the scenario creation data for processing. This method validates the required inputs
     * before attempting to create a new scenario. If the input validations fail,
     * an error message is displayed and the operation is aborted. Otherwise, the data is submitted
     * using the controller's functionality.
     *
     * The following steps are performed:
     * 1. Validates whether the required `restrictions` object is present. If not, an error is displayed.
     * 2. Invokes the controller to create a scenario with the provided `scenarioName`, `restrictions`,
     *    and `map` fields.
     * 3. If the creation process fails (evident from the error prefix in the result string),
     *    an appropriate error message is displayed.
     * 4. If the creation is successful, a success message is displayed with the result.
     *
     * Error handling:
     * - Displays a validation error message if the `restrictions` object is null.
     * - Displays an error if the controller returns a result that begins with the prefix "ERROR".
     *
     * Success handling:
     * - Displays a success message with the details provided in the result from the controller.
     *
     * This method is invoked as part of the scenario creation workflow and ensures that only
     * valid input data is submitted for processing.
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
     * Displays an error message to the user.
     *
     * @param message the error message to be displayed
     */
    public void showError(String message) {
        System.out.println(message);
    }


    /**
     * Displays a success message to the user.
     *
     * @param message the success message to be displayed
     */
    public void showSuccess(String message) {
        System.out.println(message);
    }
}
