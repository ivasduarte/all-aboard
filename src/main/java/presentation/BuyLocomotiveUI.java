package presentation;

import controller.BuyLocomotiveController;

import java.util.Optional;
import java.util.Scanner;

public class BuyLocomotiveUI implements Runnable {
    private BuyLocomotiveController ctrl = new BuyLocomotiveController();

    private String locomotiveModel;

    /**
     * This method will be responsible to show the UI to user.
     * This method will also collect all the related inputs of the locomotive and pass them to the controller.
     */
    public void getInput() {
        Scanner sc = new Scanner(System.in);

        getLocomotiveController().getLocomotiveRepository().displayAvailableLocomotives();
        System.out.println("Introduce the locomotive you want to buy: ");
        String locomotiveModel = sc.nextLine();

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
