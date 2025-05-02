package presentation;

import controller.CreateMapController;
import domain.Map;

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

        MapCreationResult result = controller.createMap(name, width, height);
        if (result.message.startsWith("ERROR")) {
            showError(result.message);
        } else {
            showSuccess(result.message);
        }
    }

    public static class MapCreationResult {
        public String message;
        public Map map;

        public MapCreationResult(String message, Map map) {
            this.message = message;
            this.map = map;
        }
    }

    public void showError(String result) {
        System.out.println(result);
    }

    public void showSuccess(String result) {
        System.out.println(result);
    }
}