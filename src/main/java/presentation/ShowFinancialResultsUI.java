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

    public void getInput() {
        Scanner sc = new Scanner(System.in);

        displayScenarioYears();
        System.out.println();

        System.out.println("Select the year you want to analyse the financial results: ");
        yearSelected = sc.nextInt() - currentScenario.getRestrictions().getStartDate().getYear();
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
        getInput();
        System.out.println(ctrl.getFinancialResultsByYear(yearSelected));
    }
}
