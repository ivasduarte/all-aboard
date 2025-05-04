package ui.utils;

import controller.CreateMapController;
import domain.Map;

import java.util.Optional;
import java.util.Scanner;

public class CreateMapUI {
    private final CreateMapController controller;
    private String mapName;
    private int width;
    private int height;

    public CreateMapUI() {
        controller = new CreateMapController();
    }

    private CreateMapController getMapController() {
        return controller;
    }

    public void run() {
        System.out.println("\n\n------------------------ Create Map ------------------------");
        requestData();
        submitData();
    }

    private void submitData() {
        Optional<presentation.CreateMapUI.MapCreationResult> map = Optional.ofNullable(getMapController().createMap(mapName, width, height));

        if (map.isPresent()) {
            System.out.println("\nTask successfully created!");
        } else {
            System.out.println("\nMap not created!");
        }
    }

    private void requestData() {
       mapName = requestMapName();
       width = requestMapWidth();
       height = requestMapHeight();
    }

   private String requestMapName(){
        Scanner sc = new Scanner(System.in);
        System.out.print("Enter map name: ");
        return sc.nextLine().trim();
   }

   private int requestMapHeight(){
        Scanner sc = new Scanner(System.in);
        int height = 0;
        System.out.print("Enter map height: ");

        do{
            height = sc.nextInt();
            if (height <= 0 || height > 256){
                System.out.println("Invalid height. Please enter a positive integer less than or equal to 256.");
            }
        } while (height <= 0 || height > 256);

        return height;
   }


   private int requestMapWidth(){
        Scanner sc = new Scanner(System.in);
        int width = 0;
        System.out.print("Enter map width: ");

        do{
            width = sc.nextInt();
            if (width <= 0 || width > 256){
                System.out.println("Invalid width. Please enter a positive integer less than or equal to 256.");
            }
        } while (width <= 0 || width > 256);

        return width;
   }

}
