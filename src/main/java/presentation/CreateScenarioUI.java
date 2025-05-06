package presentation;

import controller.CreateScenarioController;
import controller.DisplayMapController;
import domain.Map;

import java.util.Scanner;

public class CreateScenarioUI implements Runnable{
    private CreateScenarioController ctrl = new CreateScenarioController();
    private DisplayMapController controller = new DisplayMapController();

    private String scenarioName;
    private Restrictions restrictions;
    private Map map;
    private String mapName;

    public void getInput() {
        Scanner sc = new Scanner(System.in);

        System.out.print(controller.getMapList());
        System.out.print("\nSelect a map from the list: ");
        mapName = sc.nextLine().trim();

        System.out.print("Enter the name of the scenario: ");
        scenarioName = sc.nextLine().trim();
    }

}
