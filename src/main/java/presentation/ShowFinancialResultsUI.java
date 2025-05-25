package presentation;

import controller.ShowFinancialResultsController;

import java.util.Scanner;

public class ShowFinancialResultsUI {
    private ShowFinancialResultsController ctrl;

    public void getInput() {
        Scanner sc = new Scanner(System.in);

        // apresentar lista com os anos do cenario a ser jogado
        int yearSelected = sc.nextInt();   // subtrair data inicial do cenario para obter posicao no array
    }

    public ShowFinancialResultsController getShowFinancialResultsController() {
        return ctrl;
    }
}
