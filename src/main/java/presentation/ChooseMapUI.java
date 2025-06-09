package presentation;

import controller.EditorChooseMapController;
import domain.Map;
import dto.MapDTO;
import ui.menu.MenuItem;
import ui.utils.Utils;

import java.util.List;

public class ChooseMapUI implements Runnable {
    private EditorChooseMapController controller;

    public ChooseMapUI() {
        this.controller = new EditorChooseMapController();
    }

    @Override
    public void run() {
        Map selectedMap = getInput();
        if (selectedMap == null) {
            return;
        }

        List<MenuItem> options = controller.createEditorMenuOptions(selectedMap);

        int option = 0;
        do {
            option = Utils.showAndSelectIndex(options, "\n\n------------------------- EDITOR MENU -------------------------");

            if ((option >= 0) && (option < options.size())) {
                options.get(option).run();
            }
        } while (option != -1);
    }

    public Map getInput() {
        List<MapDTO> mapDTOs = controller.getAllMaps();

        if (mapDTOs.isEmpty()) {
            System.out.println("ERROR: No maps available.");
            return null;
        }

        System.out.println("\n\n------------------------ Choose Map ------------------------");
        System.out.println("\nMAP LIST:");

        for (int i = 0; i < mapDTOs.size(); i++) {
            MapDTO mapDTO = mapDTOs.get(i);
            System.out.println((i + 1) + ". " + mapDTO.toString());
        }

        System.out.print("\nCHOOSE A MAP FROM LIST ABOVE: ");

        try {
            int userInput = Integer.parseInt(System.console().readLine());
            int mapIndex = controller.convertToZeroBasedIndex(userInput);

            if (!controller.isValidMapSelection(mapIndex, controller.getTotalMapsCount())) {
                System.out.println("ERROR: Invalid map selection.");
                return null;
            }

            return controller.getMapByIndex(mapIndex);

        } catch (NumberFormatException e) {
            System.out.println("ERROR: Please enter a valid number.");
            return null;
        } catch (Exception e) {
            System.out.println("ERROR: An unexpected error occurred.");
            return null;
        }
    }
}