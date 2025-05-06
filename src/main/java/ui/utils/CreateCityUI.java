package ui.utils;

import controller.CreateCityController;
import domain.City;
import domain.HouseBlock;

import java.util.Scanner;

public class CreateCityUI implements Runnable{
    private final CreateCityController ctrl = new CreateCityController();

    @Override public void run() {
        System.out.println("\n\n------------------------ Create City ------------------------");
        Scanner sc = new Scanner(System.in);
        String cityName, mapName;

        CreateCityController.NameMapError nm;
        do {
            System.out.print("Enter City Name: "); cityName = sc.nextLine().trim();
            System.out.print("Enter Map Name: "); mapName = sc.nextLine().trim();
            nm = ctrl.validateNameMap(cityName, mapName);
            if (nm == CreateCityController.NameMapError.MAP_NOT_FOUND)
                System.out.println("Error: Map '" + mapName + "' does not exist.");
            else if (nm == CreateCityController.NameMapError.NAME_DUPLICATE)
                System.out.println("Error: City name '" + cityName + "' already used on map '" + mapName + "'.");
        } while (nm != CreateCityController.NameMapError.OK);

        int cx, cy;
        CreateCityController.CenterError ce;
        do {
            System.out.print("Enter Center X: "); cx = Integer.parseInt(sc.nextLine());
            System.out.print("Enter Center Y: "); cy = Integer.parseInt(sc.nextLine());
            ce = ctrl.validateCenter(cityName, mapName, cx, cy);
            if (ce == CreateCityController.CenterError.OUT_OF_BOUNDS)
                System.out.println("Error: Center (" + cx + "," + cy + ") outside map bounds.");
            else if (ce == CreateCityController.CenterError.POSITION_TAKEN)
                System.out.println("Error: Another city occupies (" + cx + "," + cy + ").");
            else if (ce == CreateCityController.CenterError.BLOCK_PRESENT)
                System.out.println("Error: A house block exists at (" + cx + "," + cy + ").");
        } while (ce != CreateCityController.CenterError.OK);

        City city = ctrl.createCity(cityName, mapName, cx, cy);
        System.out.println("City created: " + city.getName() + " on map " + city.getMapName());

        char mode;
        do {
            System.out.print("Choose block mode (M=Manual, A=Auto): ");
            String line = sc.nextLine().trim().toUpperCase();
            mode = line.isEmpty() ? ' ' : line.charAt(0);
        } while (mode != 'M' && mode != 'A');

        if (mode == 'M') {
            System.out.print("How many blocks to add? ");
            int n = Integer.parseInt(sc.nextLine());
            for (int i = 0; i < n; i++) {
                HouseBlock b;
                CreateCityController.BlockError be;
                do {
                    System.out.print("Block " + (i+1) + " X: "); int bx = Integer.parseInt(sc.nextLine());
                    System.out.print("Block " + (i+1) + " Y: "); int by = Integer.parseInt(sc.nextLine());
                    b = new HouseBlock(bx, by);
                    be = ctrl.validateBlock(city.getMapName(), b);
                    if (be == CreateCityController.BlockError.OUT_OF_BOUNDS)
                        System.out.println("Error: Block (" + bx + "," + by + ") outside map bounds.");
                    else if (be == CreateCityController.BlockError.COLLIDES_CITY_CENTER)
                        System.out.println("Error: Block collides with a city center at (" + bx + "," + by + ").");
                    else if (be == CreateCityController.BlockError.COLLIDES_BLOCK)
                        System.out.println("Error: Block conflicts with existing block at (" + bx + "," + by + ").");
                } while (be != CreateCityController.BlockError.OK);
                ctrl.addBlockManual(city, b);
            }
            System.out.println("Blocks assigned manually.");
        } else {
            System.out.print("Number of blocks: "); int n = Integer.parseInt(sc.nextLine());
            System.out.print("Sigma (spread): "); double s = Double.parseDouble(sc.nextLine());
            ctrl.assignBlocksAuto(city, n, s);
            System.out.println("Blocks assigned automatically.");
        }

        System.out.println("City created with success.");
    }
}
