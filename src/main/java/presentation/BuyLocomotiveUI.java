package presentation;

import controller.BuyLocomotiveController;
import controller.CreateScenarioController;
import domain.Locomotive;
import domain.Scenario;

import java.util.List;
import java.util.Optional;
import java.util.Scanner;

public class BuyLocomotiveUI implements Runnable {
    private BuyLocomotiveController ctrl = new BuyLocomotiveController();
    private CreateScenarioController scenarioController = new CreateScenarioController();

    private String locomotiveModel;

    /**
     * This method will be responsible to show the UI to user.
     * This method will also collect all the related inputs of the locomotive and pass them to the controller.
     */
    public void getInput() {
        Scanner sc = new Scanner(System.in);

        List<Scenario> scenarioList = scenarioController.getScenarioList();
        for (int i = 0; i < scenarioList.size(); i++) {
            Scenario scenario = scenarioList.get(i);
            System.out.println((i+1) + ". " + scenario.getScenarioName());
            System.out.println();
        }
        System.out.println("Enter the scenario ID: ");
        int scenarioID = sc.nextInt() - 1;

        System.out.println();
        sc.nextLine();

        List<Locomotive> availableLocomotives = getLocomotiveController().getLocomotiveRepository().getAvailableLocomotives(scenarioID);
        for (int i = 0; i < availableLocomotives.size(); i++) {
            System.out.println((i+1) + ". " + availableLocomotives.get(i).getLocomotiveModel() + ": " + availableLocomotives.get(i).getPrice() + "€");
        }
        System.out.println("Introduce the locomotive you want to buy: ");
        this.locomotiveModel = sc.nextLine();

        String result = ctrl.buyLocomotive(locomotiveModel);

        if (result.startsWith("ERROR")) {
            showError(result);
        } else {
             showSuccess(result);
        }
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
        Optional<String> result = Optional.ofNullable(ctrl.buyLocomotive(locomotiveModel));

        if (result.isPresent()) {
            System.out.println("\nLocomotive successfully bought!");
        } else {
            System.out.println("\nCouldn't buy Locomotive!");
        }
    }

    public void showError(String result) {
        System.out.println("ERROR: " + result);
    }
    public void showSuccess(String result) {
        System.out.println("SUCCESS: " + result);
    }
}
