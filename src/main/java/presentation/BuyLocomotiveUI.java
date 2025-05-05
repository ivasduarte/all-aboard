package presentation;

import controller.BuyLocomotiveController;
import domain.Date;

import java.util.Scanner;

public class BuyLocomotiveUI {
    private BuyLocomotiveController ctrl = new BuyLocomotiveController();

    public void getInput() {
        Scanner sc = new Scanner(System.in);

        System.out.println("Select a locomotive type from the following list: ");
        System.out.println("1. Steam");
        System.out.println("2. Diesel");
        System.out.println("3. Electricity");   // if possible compact the prints in "for" cycle
        String locomotiveType = sc.nextLine();

        // find a way to get the locomotive price and the current date

        // String result = ctrl.buyLocomotive(locomotiveType, purchaseDate, price);

        // if (result.startsWith("ERROR")) {
        //     showError(result);
        // } else {
        //     showSuccess(result);
        // }
    }

    public void showError(String result) {
        System.out.println("ERROR: " + result);
    }
    public void showSuccess(String result) {
        System.out.println("SUCCESS: " + result);
    }
}
