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

public class CreateScenarioUI implements Runnable {
    private final CreateScenarioController ctrl = new CreateScenarioController();
    private DisplayMapController controller = new DisplayMapController();

    private String scenarioName;
    private Restrictions restrictions;
    private int mapId;
    private String mapName;

    public void getInput() {
        Scanner sc = new Scanner(System.in);

        System.out.print(controller.getMapList());
        System.out.print("\nSelect a map from the list: ");
        mapName = sc.nextLine().trim();

        System.out.print("Enter the name of the scenario: ");
        scenarioName = sc.nextLine().trim();

        try {
            System.out.println("\nEnter Start Date (yyyy-mm-dd): ");
            LocalDate startDate = LocalDate.parse(sc.nextLine());

            System.out.println("Enter End Date (yyyy-mm-dd): ");
            LocalDate endDate = LocalDate.parse(sc.nextLine());

            System.out.println("\nEnter Locomotive Types (comma-separated: steam, diesel, electric): ");
            List<String> locoTypes = Arrays.asList(sc.nextLine().split("\\s*,\\s*"));

            System.out.println("\nEnter Historical Events (comma-separated descriptions): ");
            List<String> events = Arrays.asList(sc.nextLine().split("\\s*,\\s*"));

            System.out.print("\nEnter the ID of the map to associate with the scenario: ");
            mapId = sc.nextInt();

            restrictions = new Restrictions(startDate, endDate, locoTypes, events);

            if (!restrictions.validate()) {
                showError("ERROR: Invalid restrictions provided.");
                restrictions = null;
                return;
            }

            System.out.print("\nEnter the ID of the map to associate with the scenario: ");
            mapId = sc.nextInt();
        } catch (DateTimeParseException e) {
            showError("ERROR: Invalid date format. Use yyyy-mm-dd.");
        } catch (Exception e) {
            showError("ERROR: " + e.getMessage());
        }
    }

    @Override
    public void run() {
        System.out.println("\n\n------------------------ Create Scenario ------------------------");
        getInput();
        submitData();
    }

    public void submitData() {
        if (restrictions == null) {
            showError("Scenario creation aborted due to invalid input.");
            return;
        }

        String result = ctrl.createScenario(scenarioName, restrictions, mapId);
        if (result.startsWith("ERROR")) {
            showError(result);
        } else {
            showSuccess(result);
        }
    }

    public void showError(String message) {
        System.out.println(message);
    }

    public void showSuccess(String message) {
        System.out.println(message);
    }
}
