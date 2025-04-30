package presentation;

import controller.AddIndustryController;

import java.util.Scanner;

public class AddIndustryUI {
    private AddIndustryController ctrl = new AddIndustryController();

    public void getInput() {
        Scanner sc = new Scanner(System.in);
        System.out.println("Enter the map id: ");
        int mapID = sc.nextInt();
        System.out.println("Enter the scenario id: ");
        int scenarioID = sc.nextInt();
        System.out.println("Enter the position X of the industry: ");
        int positionX = sc.nextInt();
        System.out.println("Enter the position Y of the industry: ");
        int positionY = sc.nextInt();
        System.out.println("Enter the industry type: ");
        String industryType = sc.nextLine();

        String result = ctrl.addIndustry(mapID, scenarioID, positionX, positionY, industryType);

        if (result.startsWith("ERROR")) {
            showError(result);
        } else {
            showSuccess(result);
        }
    }

    public void showError(String result) {
        System.out.println("ERROR: " + result);
    }
    public void showSuccess(String result) {
        System.out.println("SUCCESS: " + result);
    }
}
