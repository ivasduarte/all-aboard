package presentation;

import java.util.stream.Collectors;
import controller.SaveScenarioController;
import domain.Map;
import domain.Scenario;

import java.util.List;
import java.util.Scanner;

public class SaveScenarioUI implements Runnable {

    private final SaveScenarioController controller = new SaveScenarioController();
    private final Map selectedMap;

    public SaveScenarioUI(Map selectedMap) {
        this.selectedMap = selectedMap;
    }

    @Override
    public void run() {
        List<Scenario> scenarios = controller.getScenarioList().stream()
                .filter(s -> s.getMap().equals(selectedMap))
                .collect(Collectors.toList());

        if (scenarios.isEmpty()) {
            System.out.println("No scenarios available for this map.");
            return;
        }

        System.out.println("Available scenarios for map: " + selectedMap.getName());
        for (int i = 0; i < scenarios.size(); i++) {
            System.out.printf("%d - %s%n", i + 1, scenarios.get(i).getScenarioName());
        }

        System.out.print("Select a scenario number to save: ");
        Scanner scanner = new Scanner(System.in);

        try {
            int option = Integer.parseInt(scanner.nextLine());

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
