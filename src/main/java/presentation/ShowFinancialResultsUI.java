package presentation;

import controller.ShowFinancialResultsController;
import domain.Scenario;

import java.util.Scanner;

public class ShowFinancialResultsUI implements Runnable {
    private ShowFinancialResultsController ctrl = new ShowFinancialResultsController();

    private Scenario currentScenario;
    private int yearSelected;

    public ShowFinancialResultsUI(Scenario currentScenario) {
        this.currentScenario = currentScenario;
    }

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

    public ShowFinancialResultsController getShowFinancialResultsController() {
        return ctrl;
    }

    public void displayScenarioYears() {
        int startYear = currentScenario.getRestrictions().getStartDate().getYear();

        for (int i = startYear; i < currentScenario.getCurrentYear(); i++) {
            System.out.println(i + " - Financial Results");
        }
    }

    @Override
    public void run() {
        System.out.println("\n\n------------------------ Financial Results ------------------------");
        if (!getInput()) {
            return;
        }
        System.out.println(ctrl.getFinancialResultsByYear(yearSelected));
    }
}
