package presentation;

import controller.SaveScenarioController;
import domain.Map;
import domain.Scenario;
import javafx.application.Platform;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.BooleanProperty;

import java.io.File;
import java.util.Scanner;

public class LoadScenarioUI implements Runnable {

    static Scanner ler = new Scanner(System.in);

    private final SaveScenarioController controller = new SaveScenarioController();
    private final Map selectedMap;

    // Propriedade estática para notificar quando um cenário é carregado
    public static final BooleanProperty scenarioLoaded = new SimpleBooleanProperty(false);

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

        System.out.println("\nAvailable scenario files:");
        for (int i = 0; i < scenarioFiles.length; i++) {
            System.out.printf("%d - %s%n", i + 1, scenarioFiles[i].getName());
        }

        Scanner scanner = new Scanner(System.in);

        System.out.println("0 - Cancel");

        System.out.print("\nSelect a file number to load: ");

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

            // Notifica que um novo cenário foi carregado
            Platform.runLater(() -> scenarioLoaded.set(true));

        } catch (NumberFormatException e) {
            System.out.println("Invalid input.");
        }
    }
}