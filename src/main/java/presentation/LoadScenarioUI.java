package presentation;

import controller.SaveScenarioController;
import domain.Map;
import domain.Scenario;

import java.io.File;
import java.util.Scanner;

public class LoadScenarioUI implements Runnable {

    static Scanner ler = new Scanner(System.in);

    private final SaveScenarioController controller = new SaveScenarioController();

    private final Map selectedMap;

    public LoadScenarioUI(Map selectedMap) {
        this.selectedMap = selectedMap;
    }

    @Override
    public void run() {
        File dir = new File("scenarios");
        File[] scenarioFiles = dir.listFiles((d, name) -> name.endsWith(".ser"));

        if (scenarioFiles == null || scenarioFiles.length == 0) {
            System.out.println("No scenario files found.");
            return;
        }

        System.out.println("Available scenario files:");
        for (int i = 0; i < scenarioFiles.length; i++) {
            System.out.printf("%d - %s%n", i + 1, scenarioFiles[i].getName());
        }

        Scanner scanner = new Scanner(System.in);

        System.out.println("0 - Cancel");

        System.out.print("Select a file number to load: ");

        try {

            int option = Integer.parseInt(scanner.nextLine());

            if (option == 0) {
                System.out.println("Operation cancelled.");
                return;
            }

            if (option < 1 || option > scenarioFiles.length) {
                System.out.println("Invalid option.");
                return;
            }

            Scenario scenario = controller.loadScenarioFromFile(scenarioFiles[option - 1].getPath(), selectedMap);
            System.out.printf("Loaded Scenario: %s | Map: %s | Budget: %d%n", scenario.getScenarioName(), scenario.getMap().getName(), scenario.getBudget().getBudget());

        } catch (NumberFormatException e) {
            System.out.println("Invalid input.");
        }
    }
}