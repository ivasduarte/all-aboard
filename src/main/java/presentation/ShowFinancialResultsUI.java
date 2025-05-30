package presentation;

import controller.ShowFinancialResultsController;
import domain.Scenario;

import java.util.Scanner;

public class ShowFinancialResultsUI implements Runnable {
    private ShowFinancialResultsController ctrl = new ShowFinancialResultsController();

    private Scenario currentScenario;
    private int yearSelected;

    /**
     * This method is the constructor of ShowFinancialResultsUI.
     *
     * @param currentScenario scenario that is being played
     */
    public ShowFinancialResultsUI(Scenario currentScenario) {
        this.currentScenario = currentScenario;
    }

    /**
     * This method will be responsible to show the UI to user.
     * This method will also collect the year selected by the player and pass it to the controller.
     *
     * Upon successful execution, the financial results will be shown to the player.
     * If errors occur during the input process, appropriate error messages are displayed to guide the user.
     */
    public boolean getInput() {
        if (!ctrl.checkFinancialResultsExistence()) {
            System.out.println("There's still not enough data to give the financial results of any year!");
            return false;
        }

        Scanner sc = new Scanner(System.in);

        boolean validInput = false;

        while (!validInput) {
            displayScenarioYears();
            System.out.println("Select the year you want to analyse the financial results: ");

            if (!sc.hasNextInt()) {
                System.out.println("It is meant to insert an year!\n");
                sc.next();
                continue;
            }

            yearSelected = sc.nextInt() - currentScenario.getRestrictions().getStartDate().getYear();
            sc.nextLine();

            if (ctrl.isYearValid(yearSelected, currentScenario)) {
                validInput = true;
            } else {
                System.out.println("The year selected has no financial results available!\n");
            }
        }
        return true;
    }

    /**
     * This method returns the ShowFinancialResultsController.
     *
     * @return ShowFinancialResultsController
     */
    public ShowFinancialResultsController getShowFinancialResultsController() {
        return ctrl;
    }

    /**
     * This method will be responsible to show the player a list of the years that have available financial results to show.
     */
    public void displayScenarioYears() {
        int startYear = currentScenario.getRestrictions().getStartDate().getYear();

        for (int i = startYear; i < currentScenario.getCurrentYear(); i++) {
            System.out.println(i + " - Financial Results");
        }
    }

    @Override
    /**
     * This method will be responsible to show the UI related to showing financial results to the player.
     */
    public void run() {
        System.out.println("\n\n------------------------ FINANCIAL RESULTS ------------------------");
        if (!getInput()) {
            return;
        }
        System.out.println("\n------------------------ " + (yearSelected + currentScenario.getRestrictions().getStartDate().getYear()) + " - Financial Results ------------------------");
        System.out.println(ctrl.getFinancialResultsByYear(yearSelected));
    }
}
