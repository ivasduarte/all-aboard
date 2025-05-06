package ui.utils;

import controller.DisplayMapController;
import domain.Map;

import java.util.List;
import java.util.Optional;
import java.util.Scanner;

public class DisplayMapUI implements Runnable{
    private final DisplayMapController ctrl = new DisplayMapController();
    private String mapName;
    private String[][] mapArray;

    public void run(){
        System.out.println("\n\n------------------------ Display Map ------------------------");
        displayMapList();
        selectData();
        submitData();
        System.out.println();
        displayMap();
    }

    private void displayMap() {
        for (int i = 0; i < mapArray.length; i++) {
            for (int j = 0; j < mapArray[0].length; j++) {
                if (j == mapArray[0].length - 1) {
                    System.out.print(mapArray[i][j]); // no space after the last one
                } else {
                    System.out.print(mapArray[i][j] + " ");
                }
            }
            System.out.println();
        }
    }

    private void displayMapList() {
        System.out.println(ctrl.getMapList());
    }


    private void selectData(){
        mapName = requestMapName();
    }

    private String requestMapName(){
        Scanner sc = new Scanner(System.in);
        System.out.print("Select a map: ");
        return sc.nextLine().trim();
    }

    private String[][] submitData(){
        int width = 0, height = 0;

        Optional<Map> matchingMap = Optional.empty();
        List<Map> mapList = ctrl.getMapList();
        for (int i = 0; i < mapList.size(); i++) {
            Map map = mapList.get(i);
            if (map.getMapName().equals(mapName)) {
                matchingMap = Optional.of(map);
                break;
            }
        }

        if(ctrl.verifyIfMapExists(mapName)){
            width = matchingMap.get().getWidth();
            height = matchingMap.get().getHeight();
        }else{
            System.out.println("\nMap does not exist!");
        }

        mapArray = new String[height][width];

        for(int i = 0; i < mapArray.length; i++){
            for(int k = 0; k < mapArray[0].length; k++){
                mapArray[i][k] = "⬛";
            }
        }
        return mapArray;
    }
}
