package presentation;

import java.util.stream.Collectors;
import controller.SaveScenarioController;
import domain.Map;
import domain.Scenario;

import java.util.List;
import java.util.Scanner;

/**
 * User interface for saving a scenario to a file.
 * Implements Runnable to support concurrent execution.
 */
public class SaveScenarioUI implements Runnable {

    private final SaveScenarioController controller = new SaveScenarioController();
    private final Map selectedMap;
    Scanner ler = new Scanner(System.in);

    /**
     * Creates a new SaveScenarioUI instance with the specified map.
     *
     * @param selectedMap The map containing the scenarios to be saved
     */
    public SaveScenarioUI(Map selectedMap) {
        this.selectedMap = selectedMap;
    }

    /**
     * Implementation of the Runnable interface.
     * Displays available scenarios for the selected map and handles user selection.
     * Saves the selected scenario to a file.
     */
    @Override
    public void run() {
        List<Scenario> scenarios = selectedMap.getScenarios();


        if (scenarios.isEmpty()) {
            System.out.println("No scenarios available for this map.");
            return;
        }

        System.out.println("\nAvailable scenarios for map: " + selectedMap.getName());
        for (int i = 0; i < scenarios.size(); i++) {
            System.out.printf("%d - %s%n", i + 1, scenarios.get(i).getScenarioName());
        }

        System.out.println("0 - Cancel");

        System.out.print("\nSelect a scenario number to save: ");
        try {
            int option = Integer.parseInt(ler.nextLine());

            if (option == 0) {
                System.out.println("Operation cancelled.");
                return;
            }

            if (option < 1 || option > scenarios.size()) {
                System.out.println("Invalid option.");
                return;
            }

            Scenario selectedScenario = scenarios.get(option - 1);
            controller.saveScenarioInFile(selectedScenario);

        } catch (NumberFormatException e) {
            System.out.println("Invalid input. Please enter a number.");
        }
    }
}