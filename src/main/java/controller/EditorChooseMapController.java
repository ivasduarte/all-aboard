package controller;

import domain.Map;
import domain.MapSave;
import dto.MapDTO;
import dto.MapMapper;
import presentation.*;
import repositories.MapRepository;
import repositories.Repositories;
import ui.menu.MenuItem;

import java.util.ArrayList;
import java.util.List;

public class EditorChooseMapController {
    private MapRepository mapRepository;

    public EditorChooseMapController() {
        this.mapRepository = Repositories.getInstance().getMapRepository();
    }

    /**
     * Retrieves all available maps as DTOs for display
     * @return List of MapDTO objects
     */
    public List<MapDTO> getAllMaps() {
        List<Map> maps = mapRepository.getMapList();
        return MapMapper.toDTO(maps);
    }

    /**
     * Validates if the selected map index is within valid range
     * @param mapIndex The selected map index (0-based)
     * @param totalMaps Total number of available maps
     * @return true if valid, false otherwise
     */
    public boolean isValidMapSelection(int mapIndex, int totalMaps) {
        return mapIndex >= 0 && mapIndex < totalMaps;
    }

    /**
     * Retrieves a specific map by its index
     * @param mapIndex The index of the map to retrieve (0-based)
     * @return The Map object if found, null otherwise
     */
    public Map getMapByIndex(int mapIndex) {
        List<Map> maps = mapRepository.getMapList();

        if (isValidMapSelection(mapIndex, maps.size())) {
            return maps.get(mapIndex);
        }

        return null;
    }

    /**
     * Creates the menu options for the selected map
     * @param selectedMap The map for which to create menu options
     * @return List of MenuItem objects for the editor menu
     */
    public List<MenuItem> createEditorMenuOptions(Map selectedMap) {
        List<MenuItem> options = new ArrayList<>();

        // Note: These UI classes would need to be refactored to work with controllers,
        // or we could create controller methods for each action
        options.add(new MenuItem("Create a Scenario", new CreateScenarioUI(selectedMap)));
        options.add(new MenuItem("Add a City", new AddCityUI(selectedMap)));
        options.add(new MenuItem("Add an Industry", new AddIndustryUI(selectedMap)));
        options.add(new MenuItem("Save Scenario to File", new SaveScenarioUI(selectedMap)));
        options.add(new MenuItem("Load a Scenario from file", new LoadScenarioUI(selectedMap)));
        options.add(new MenuItem("Save Map to file", new MapSave(selectedMap)));

        return options;
    }

    /**
     * Converts 1-based user input to 0-based array index
     * @param userInput The 1-based input from user
     * @return 0-based index
     */
    public int convertToZeroBasedIndex(int userInput) {
        return userInput - 1;
    }

    /**
     * Gets the total number of available maps
     * @return Number of maps available
     */
    public int getTotalMapsCount() {
        return mapRepository.getMapList().size();
    }
}