package presentation;

import controller.CreateScenarioController;
import domain.Map;
import domain.PortBehavior;
import domain.Restrictions;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

/**
 * The CreateScenarioUI class is responsible for handling the user interface
 * for creating new scenarios. It provides methods for collecting user input
 * and displaying relevant information about the scenario creation process.
 */
public class CreateScenarioUI implements Runnable {
    private final static Scanner sc = new Scanner(System.in);

    private final CreateScenarioController controller;
    private String scenarioName;
    private final Map map;
    private Restrictions restrictions;

    /**
     * Constructs a new instance of the CreateScenarioUI class.
     * Initializes the controller and sets up the necessary parts
     * for scenario creation.
     */
    public CreateScenarioUI(Map map) {
        this.controller = new CreateScenarioController();
        this.map = map;
    }

    /**
     * Displays an error message to the user.
     *
     * @param message The error message to be displayed
     */
    private void showError(String message) {
        System.out.println("\nERROR: " + message);
    }

    /**
     * Collects user input for creating a new scenario.
     * This method guides the user through the process of entering
     * scenario details such as name, date range, and restrictions.
     */
    public void getInput() {
        Scanner scanner = new Scanner(System.in);

        // Get scenario name
        do {
            System.out.print("\nENTER SCENARIO NAME: ");
            scenarioName = scanner.nextLine().trim();
            if (!scenarioName.isEmpty()) {
                break;
            }
            showError("ERROR: Scenario name cannot be empty.");
        } while (true);

        // Get date range
        LocalDate startDate;
        LocalDate endDate;
        do {
            try {
                System.out.println("\n=== SCENARIO RESTRICTIONS ===");
                System.out.print("\nENTER THE START DATE (YYYY-MM-DD): ");
                startDate = LocalDate.parse(scanner.nextLine());
                System.out.print("ENTER THE END DATE (YYYY-MM-DD): ");
                endDate = LocalDate.parse(scanner.nextLine());
                if (endDate.isAfter(startDate)) {
                    break;
                }
                showError("ERROR: End date must be after start date.");
            } catch (DateTimeParseException e) {
                showError("ERROR: Invalid date format. Please use YYYY-MM-DD.");
            }
        } while (true);

        // Get locomotive types
        List<String> locomotiveTypes = new ArrayList<>();
        System.out.println("\n\nLOCOMOTIVE RESTRICTIONS:");
        System.out.println("Available locomotive types: steam, diesel, electric\n-> Note:  comma-seperated, or press 'Enter' to skip");
        System.out.print("Enter wished locomotive types: ");
        String locoTypesInput = scanner.nextLine().trim();
        if (!locoTypesInput.isEmpty()) {
            locomotiveTypes.addAll(Arrays.asList(locoTypesInput.split(",")));
        }

        // Get historical events
        List<String> historicalEvents = new ArrayList<>();
        System.out.println("\n\nHISTORICAL RESTRICTIONS:");
        System.out.println("--> Note: E.g. 'none, pandemic, great war'.");
        System.out.println("--> Note: In a pandemic situation, cargo demand is lowered, however increases every year.");
        System.out.println("--> Note: In a great war situation, all costs are increased, however cargo demand is greatly increased.");
        System.out.println("-> Note: must be comma-seperated. If wished, press 'Enter' to skip");
        System.out.print("\nEnter historical restrictions: ");
        String eventsInput = scanner.nextLine().trim();
        if (!eventsInput.isEmpty()) {
            historicalEvents.addAll(Arrays.asList(eventsInput.split(",")));
        }

        // Create restrictions
        List<PortBehavior> portBehaviors = new ArrayList<>();

        System.out.println("\n\nPORT BEHAVIOR:");
        System.out.println("\nAvailable ports:");
        PortBehavior[] portValues = PortBehavior.values();
        for (int i = 0; i < portValues.length; i++) {
            PortBehavior port = portValues[i];
            String imports = String.join(", ", port.getImports());
            String exports = String.join(", ", port.getExports());
            String transforms = String.join(", ", port.getTransformations());
            System.out.println((i + 1) + ". " + port.name() +
                    " (Imports: " + imports +
                    ", Exports: " + exports +
                    ", Transforms: " + transforms + ")");
        }

        System.out.print("\nEnter port number for port behavior: ");
        String[] portIndexes = sc.nextLine().split(",");
        List<PortBehavior> ports = new ArrayList<>();

        for (String indexStr : portIndexes) {
            try {
                int index = Integer.parseInt(indexStr.trim()) - 1;
                if (index >= 0 && index < portValues.length) {
                    ports.add(portValues[index]);
                } else {
                    System.out.println("Invalid port index: " + index);
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid input (not a number): " + indexStr);
            }
        }
        restrictions = new Restrictions(startDate, endDate, locomotiveTypes, historicalEvents, portBehaviors);
    }

    /**
     * Creates a new scenario using the collected user input.
     * This method validates the input and creates the scenario
     * if all requirements are met.
     */
    private void createScenario() {
        if (scenarioName == null || scenarioName.isEmpty()) {
            showError("Scenario name is required.");
            return;
        }

        if (restrictions == null) {
            showError("Restrictions are required.");
            return;
        }

        try {
            controller.createScenario(scenarioName, restrictions, map);
            System.out.println("\nScenario created successfully!");
        } catch (Exception e) {
            showError("Failed to create scenario: " + e.getMessage());
        }
    }

    /**
     * Executes the scenario creation process.
     * This method is called when the Runnable is executed.
     */
    @Override
    public void run() {
        System.out.println("\n\n------------------------ Create Scenario ------------------------");
        getInput();
        createScenario();
    }
}
