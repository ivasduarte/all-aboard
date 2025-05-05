package presentation;

import controller.BuyLocomotiveController;
import domain.Date;

import java.util.Scanner;

public class BuyLocomotiveUI {
    private BuyLocomotiveController ctrl = new BuyLocomotiveController();

    public void getInput() {
        Scanner sc = new Scanner(System.in);

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
