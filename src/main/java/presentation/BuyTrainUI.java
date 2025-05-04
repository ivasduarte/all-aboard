package presentation;

import controller.BuyTrainController;

import java.util.Scanner;

public class BuyTrainUI {
    private BuyTrainController ctrl = new BuyTrainController();

    public void getInput() {
        Scanner sc = new Scanner(System.in);

        System.out.println("Select the desired locomotives from the following list: ");
        // present locomotives in the form of a list

        System.out.println("Select the desired carriage from the following list: ");
        // present carriage in the form of a list

        // ask if wants to add more locomotives - if "yes" loop

        // add methods to show success and fail
    }
}
