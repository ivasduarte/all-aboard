package presentation;

import controller.CreateMapController;

import java.util.Scanner;

public class CreateMapUI {
    private CreateMapController controller = new CreateMapController();

    public void getInput() {
        Scanner sc = new Scanner(System.in);
        System.out.print("Enter the name of the map: ");
        String name = sc.nextLine();
        System.out.print("Enter the width of the map: ");
        int width = sc.nextInt();
        System.out.print("Enter the height of the map: ");
        int height = sc.nextInt();

        String result = controller.createMap(name, width, height);

        if (result.startsWith("ERROR")){
            showError(result);
        } else {
            showSuccess(result);
        }
    }

    public void showError(String result){
        System.out.println("ERROR: " + result);
    }

    public void showSuccess(String result){
        System.out.println("SUCCESS: " + result);

    }

}
