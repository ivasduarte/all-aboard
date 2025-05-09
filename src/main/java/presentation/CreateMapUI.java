package presentation;

import controller.CreateMapController;
import domain.Map;
import repositories.MapRepository;

import java.util.Optional;
import java.util.Scanner;

public class CreateMapUI implements Runnable {
    private CreateMapController controller = new CreateMapController();
    private MapRepository mapRepository = new MapRepository();

    private String mapName;
    private int width = 0, height = 0;

    private CreateMapController getController() {
        return controller;
    }

    private MapRepository getMapRepository() {
        return mapRepository;
    }

    public void run() {
        System.out.println("\n\n------------------------ Create Map ------------------------");
        getInput();
        submitData();
    }

    public void getInput() {
        mapName = requestMapName();
        width = requestWidth();
        height = requestHeight();
    }

    private String requestMapName() {
        Scanner sc = new Scanner(System.in);
        System.out.print("Enter the name of the map: ");
        String mapName = sc.nextLine();
        if (mapRepository.existsByName(mapName)) {
            System.out.print("\nName already exists. Please enter a different name: ");
        }
        return mapName.trim();
    }

    private int requestWidth() {
        Scanner sc = new Scanner(System.in);
        System.out.print("Enter the width of the map: ");
        int width;
        do {
            width = sc.nextInt();
            if (width <= 0 || width > 256) {
                System.out.println("Invalid width. Please enter a positive integer less than or equal to 256.");
            }
        } while (width <= 0 || width > 256);
        return width;
    }


    private int requestHeight() {
        Scanner sc = new Scanner(System.in);
        System.out.print("Enter the height of the map: ");
        int height;
        do {
            height = sc.nextInt();
            if (height <= 0 || height > 256) {
                System.out.println("Invalid height. Please enter a positive integer less than or equal to 256.");
            }
        } while (height <= 0 || height > 256);
        return height;
    }

    private void submitData() {
        try {
            Map map = getController().createMap(mapName, width, height);
            System.out.println("\nMap '" + map.getMapName() + "' successfully created!");
        } catch (IllegalArgumentException e) {
            System.out.println("\nERROR: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("\nERROR: Unexpected error occurred. Please try again later.");
        }
    }
}