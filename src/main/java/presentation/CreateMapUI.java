package presentation;

import controller.CreateMapController;
import domain.Map;

import java.util.Optional;
import java.util.Scanner;

public class CreateMapUI implements Runnable{
    private CreateMapController controller = new CreateMapController();

    private String mapName;
    private int width = 0, height = 0;

    public void getInput() {
        Scanner sc = new Scanner(System.in);
        System.out.print("Enter the name of the map: ");
        String name = sc.nextLine();

        System.out.print("\nEnter the width of the map: ");
        do{
            width = sc.nextInt();
            if (width <= 0 || width > 256){
                System.out.println("Invalid width. Please enter a positive integer less than or equal to 256.");
            }
        } while (width <= 0 || width > 256);

        System.out.print("\nEnter the height of the map: ");
        do{
            height = sc.nextInt();
            if (height <= 0 || height > 256){
                System.out.println("Invalid height. Please enter a positive integer less than or equal to 256.");
            }
        } while (height <= 0 || height > 256);

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

    public void run() {
        System.out.println("\n\n------------------------ Create Map ------------------------");
        getInput ();
        submitData();
    }

    private CreateMapController getMapController() {
        return controller;
    }

    private void submitData() {
        Optional<MapCreationResult> map = Optional.ofNullable(getMapController().createMap(mapName, width, height));

        if (map.isPresent()) {
            System.out.println("\nMap successfully created!");
        } else {
            System.out.println("\nMap not created!");
        }
    }

    public void showError(String result) {
        System.out.println(result);
    }

    public void showSuccess(String result) {
        System.out.println(result);
    }
}