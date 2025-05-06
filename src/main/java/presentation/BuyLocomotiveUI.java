package presentation;

import controller.BuyLocomotiveController;

import java.util.Optional;
import java.util.Scanner;

public class BuyLocomotiveUI implements Runnable {
    private BuyLocomotiveController ctrl = new BuyLocomotiveController();

    private int serialNumber;

    public void getInput() {
        Scanner sc = new Scanner(System.in);

        getLocomotiveController().getLocomotiveRepository().displayAvailableLocomotives();
        System.out.println("Select the locomotive you want to buy: ");
        int serialNumber = sc.nextInt() - 1;

        String result = ctrl.buyLocomotive(serialNumber);

        if (result.startsWith("ERROR")) {
            showError(result);
        } else {
             showSuccess(result);
        }
    }

    public BuyLocomotiveController getLocomotiveController() {
        return ctrl;
    }

    @Override
    public void run() {
        System.out.println("\n\n------------------------ Buy Locomotive ------------------------");
        getInput();
        submitData();
    }

    public void submitData() {
        Optional<String> result = Optional.ofNullable(ctrl.buyLocomotive(serialNumber));

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
