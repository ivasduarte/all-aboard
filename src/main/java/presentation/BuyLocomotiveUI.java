package presentation;

import controller.BuyLocomotiveController;
import domain.Locomotive;
import domain.Map;
import domain.Scenario;
import repositories.Repositories;
import repositories.ScenarioRepository;

import java.util.List;
import java.util.Scanner;

public class BuyLocomotiveUI implements Runnable {
    private BuyLocomotiveController ctrl;

    private String locomotiveModel;

    private Map map;
    private Scenario scenario;

    public BuyLocomotiveUI(Map map, Scenario scenario)
    {
        this.map = map;
        this.scenario = scenario;
        this.ctrl = new BuyLocomotiveController(this.scenario);
    }

    /**
     * This method will be responsible to show the UI to user.
     * This method will also collect all the related inputs of the locomotive and pass them to the controller.
     */
    public void getInput() {
        Scanner sc = new Scanner(System.in);

        ScenarioRepository scenarioRepository = Repositories.getInstance().getScenarioRepository();
        List<Scenario> scenarios = scenarioRepository.getScenarioList();
        System.out.println(this.scenario.getScenarioName());
        int scenarioID = scenarios.indexOf(this.scenario);

        List<Locomotive> availableLocomotives = getLocomotiveController().getLocomotiveRepository().getAvailableLocomotives(scenarioID);
        for (int i = 0; i < availableLocomotives.size(); i++) {
            System.out.println((i+1) + ". " + availableLocomotives.get(i).getLocomotiveModel() + ": " + availableLocomotives.get(i).getPrice() + "€");
        }
        System.out.println("Introduce the locomotive model you want to buy: ");

        this.locomotiveModel = sc.nextLine().trim();
    }

    /**
     * This method returns the locomotive controller.
     *
     * @return locomotive controller
     */
    public BuyLocomotiveController getLocomotiveController() {
        return ctrl;
    }

    @Override
    /**
     * This method will be responsible to show the UI related to buying a locomotive to the player when this action is selected.
     */
    public void run() {
        System.out.println("\n\n------------------------ Buy Locomotive ------------------------");
        getInput();
        submitData();
    }

    /**
     * This method will be responsible to show the success or error message to the Player.
     */
    public void submitData() {
        String result = ctrl.buyLocomotive(locomotiveModel, map.getName());

        if (result.startsWith("ERROR")) {
            showError(result);
        } else {
            showSuccess(result);
        }
    }

    public void showError(String result) {
        System.out.println("ERROR: " + result);
    }
    public void showSuccess(String result) {
        System.out.println("SUCCESS: " + result);
    }
}
