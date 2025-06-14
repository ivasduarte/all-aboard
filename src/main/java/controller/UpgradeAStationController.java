package controller;

import domain.*;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import repositories.BuildingRepository;
import repositories.Repositories;
import repositories.StationRepository;
import domain.Station;
import domain.Scenario;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Controller responsible for managing the logic of upgrading stations with buildings.
 * Handles validation of building availability, retrieval of building types,
 * replacement of obsolete buildings, and budget checks.
 * <p>
 * Acts as the bridge between the UI and domain services like {@code StationUpgradeService}.
 */
public class UpgradeAStationController {
    private final StationUpgradeService upgradeService;
    private final StationRepository stationRepository;
    private final BuildingRepository buildingRepository;
    private Scenario currentScenario;
    private Map currentMap;
    private boolean hasStations;

    @FXML
    private ComboBox<Station> stationComboBox;
    @FXML
    private ComboBox<BuildingType> buildingComboBox;
    @FXML
    private Label labelTest;
    @FXML
    private Label labelBudget;

    /**
     * Constructs a new instance of {@code UpgradeAStationController},
     * initializing references to required repositories and services.
     */
    public UpgradeAStationController() {
        this.upgradeService = new StationUpgradeService();
        this.stationRepository = Repositories.getInstance().getStationRepository();
        this.buildingRepository = Repositories.getInstance().getBuildingRepository();
    }

    /**
     * Retrieves the list of available stations from a specific map.
     *
     * @param mapName The name of the map to filter stations by.
     * @return A list of stations available for upgrades on the given map.
     */
    public List<Station> getAvailableStations(String mapName) {
        return stationRepository.getAvailableStationsByMapName(mapName);
    }

    /**
     * Retrieves all registered building types in the system.
     *
     * @return A list of all {@code BuildingType} objects available in the repository.
     */
    public List<BuildingType> getAvailableBuildingTypes() {
        return buildingRepository.getBuildingTypes();
    }

    /**
     * Attempts to upgrade the given station with the specified building type,
     * considering the current simulation year.
     *
     * @param station          The station to upgrade.
     * @param buildingTypeName The name of the building type to add.
     * @param currentYear      The current year of the scenario (used to check availability).
     * @return {@code true} if the building was successfully added; {@code false} otherwise.
     */
    public boolean upgradeStation(Station station, String buildingTypeName, int currentYear) {
        if (station == null || buildingTypeName == null || buildingTypeName.isBlank()) {
            throw new IllegalArgumentException("Station and building type must be provided.");
        }

        BuildingType type = buildingRepository.getBuildingType(buildingTypeName);
        if (type == null) {
            System.out.println("ERROR: Invalid building type '" + buildingTypeName + "'");
            return false;
        }

        Building building = new Building(type);
        return upgradeService.upgradeStation(station, building, currentYear);
    }

    /**
     * Retrieves a building type by its name (case-insensitive).
     *
     * @param name The name of the building type to search.
     * @return The corresponding {@code BuildingType}, or {@code null} if not found.
     */
    public BuildingType getBuildingTypeByName(String name) {
        return buildingRepository.getBuildingType(name);
    }

    /**
     * Automatically removes obsolete buildings from a station based on the current year.
     * Specifically, removes the "Telegraph" building if the year is >= 1920,
     * and replaces it with "Telephone" if applicable.
     *
     * @param station     The station whose buildings are to be validated.
     * @param currentYear The current simulation year used to determine obsolescence.
     */
    public void removeObsoleteBuildings(Station station, int currentYear) {
        boolean hadTelegraph = false;

        if (currentYear >= 1920) {
            hadTelegraph = station.getBuildings().removeIf(b -> b.getBuildingType().equalsIgnoreCase("Telegraph"));

            if (hadTelegraph) {
                BuildingType telephone = Repositories.getInstance().getBuildingRepository().getBuildingType("Telephone");
                if (telephone != null) {
                    station.addBuilding(new Building(telephone));
                    System.out.println("INFO: Telegraph was replaced by Telephone at station " + station.getStationID());
                }
            }
        }
    }

    public void setData(Scenario scenario) {
        this.currentScenario = scenario;
        this.currentMap = scenario.getMap();

        if (labelBudget != null && currentScenario.getBudget() != null) {
            labelBudget.setText("Budget: " + currentScenario.getBudget().getBudget() + "€");
        }
        setStationComboBox();
    }

    /**
     * Gets a filtered list of available building types for a station based on:
     * - Current year availability
     * - Existing buildings (to prevent duplicates)
     * - Mutual exclusivity rules (e.g., only one hotel type allowed)
     * - Historical constraints (e.g., Telegraph not available after 1920)
     *
     * @param station     The station to check available buildings for.
     * @param currentYear The current simulation year.
     * @return A filtered list of building types that can be added to the station.
     */
    public List<BuildingType> getFilteredAvailableBuildingTypes(Station station, int currentYear) {
        List<BuildingType> filteredBuildingTypes = new ArrayList<>();
        List<BuildingType> allTypes = buildingRepository.getBuildingTypes();
        List<Building> existingBuildings = station.getBuildings();

        boolean hasHotel = false;
        boolean hasCafe = false;

        for (Building building : existingBuildings) {
            String type = building.getBuildingType().toLowerCase();

            if (type.contains("hotel")) {
                hasHotel = true;
            }

            if (type.contains("cafe")) {
                hasCafe = true;
            }
        }

        for (BuildingType type : allTypes) {
            String name = type.getName();
            String namelower = name.toLowerCase();

            boolean exists = false;

            for (int i = 0; i < existingBuildings.size(); i++) {
                if (existingBuildings.get(i).getBuildingType().toLowerCase().equals(namelower)) {
                    exists = true;
                    i = existingBuildings.size();
                }
            }
            if (exists) {
                continue;
            }

            if (namelower.contains("hotel") && hasHotel) {
                continue;
            }

            if (namelower.contains("cafe") && hasCafe) {
                continue;
            }

            if (type.getAvailableYear() > currentYear) {
                continue;
            }

            if (namelower.equals("telegraph") && currentYear >= 1920) {
                continue;
            }
            filteredBuildingTypes.add(type);
        }
        return filteredBuildingTypes;
    }

    /**
     * Deducts the cost of a building from the scenario's budget.
     *
     * @param station  The station being upgraded.
     * @param Building The building type being added.
     * @param Scenario The current scenario containing the budget.
     * @return {@code true} if the budget was sufficient and deducted; {@code false} otherwise.
     */
    public boolean DeductBudget(Station station, BuildingType Building, Scenario Scenario) {
        int currentBudget = Scenario.getBudget().getBudget();
        int buildingBudget = Building.getCost();

        if (currentBudget >= buildingBudget) {
            Scenario.subtractBudget(buildingBudget);
            return true;
        } else {
            System.out.println("ERROR: Budget exceeds budget");
            return false;
        }
    }

    /**
     * Populates the station combo box with available stations from the current map and scenario.
     * Updates the {@code hasStations} flag based on whether any stations are available.
     */
    @FXML
    public void setStationComboBox() {
        List<Station> stationList = stationRepository.getStations();
        List<Station> selectedStations = new ArrayList<>();

        for (Station s : stationList) {
            String mapName = s.getMapName();
            String scenarioName = s.getScenarioName();

            if (mapName.equalsIgnoreCase(currentMap.getName()) && scenarioName.equalsIgnoreCase(currentScenario.getScenarioName())) {
                selectedStations.add(s);
            }
        }
        hasStations = !selectedStations.isEmpty();
        stationComboBox.getItems().setAll(selectedStations);
    }

    /**
     * Updates the building combo box with available building types for the selected station.
     * Filters buildings based on the current year and existing buildings at the station.
     *
     * @param event The action event that triggered this method.
     */
    @FXML
    public void setBuildingComboBox(javafx.event.ActionEvent event) {
        Station station = stationComboBox.getValue();

        if (station == null || currentScenario == null) {
            labelTest.setText("Error: Missing station or scenario");
            return;
        }

        int year = currentScenario.getCurrentYear();

        List<BuildingType> buildings = getFilteredAvailableBuildingTypes(station, year);
        buildingComboBox.getItems().setAll(buildings);
    }

    /**
     * Handles the upgrade action when the user clicks the upgrade button.
     * Validates the selection, checks budget, and adds the building if all conditions are met.
     */
    @FXML
    public void onUpgrade() {
        Budget budget = currentScenario.getBudget();
        Station station = stationComboBox.getValue();

        BuildingType selectedType = buildingComboBox.getValue();
        Alert successAlert = new Alert(Alert.AlertType.INFORMATION);

        if (station == null || selectedType == null) {
            labelTest.setText("Please select both a station and a building.");
            return;
        }

        boolean success = DeductBudget(station, selectedType, currentScenario);

        if (success) {
            station.addBuilding(new Building(selectedType));
            setBuildingComboBox(null);
            buildingComboBox.setValue(null);

            successAlert.setTitle("Success!");
            successAlert.setContentText("Building successfully bought.");
            successAlert.showAndWait();
        } else {
            successAlert.setTitle("Unsuccessful operation");
            successAlert.setContentText("Not enough budget to buy this building.");
            successAlert.showAndWait();
        }
        handleBackToMenu();
    }

    /**
     * Checks if there are any stations available for upgrade in the current scenario.
     *
     * @return {@code true} if there are stations available; {@code false} otherwise.
     */
    public boolean hasStations() {
        return hasStations;
    }

    /**
     * Handles navigation back to the player menu.
     * Closes the current window and opens the player menu with the current scenario.
     */
    @FXML
    private void handleBackToMenu() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/JavaFX/playerMenu.fxml"));
            Parent root = loader.load();

            PlayerMenuController controller = loader.getController();
            controller.setData(currentScenario);

            Stage stage = new Stage();
            stage.setTitle("Player Menu");
            stage.setScene(new Scene(root));
            stage.show();

            Stage currentStage = (Stage) stationComboBox.getScene().getWindow();
            currentStage.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
