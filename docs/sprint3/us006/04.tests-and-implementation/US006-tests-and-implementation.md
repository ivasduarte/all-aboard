# US006 - Upgrade a Station with a building

## 4. Tests

**Test 1:** Return building Type

```java

void gettersReturnValuesFromType() {
    BuildingType type = sampleType();
    Building building = new Building(type);
    assertEquals(type.getName(),   building.getBuildingType());
    assertEquals(type.getCost(),   building.getCost());
    assertEquals(type.getEffect(), building.getEffect());
}
```

**Test 2:** Exception if building = null

```java
void constructorRejectsNull() {
    IllegalArgumentException ex =
            assertThrows(IllegalArgumentException.class,
                    () -> new Building(null));

    assertEquals("Invalid building type", ex.getMessage());
}
```

**Test 3:** Check that a valid upgrade works correctly.

```java
void testUpgradeStationSuccess() {
    Station station = new Station("Map", 1, 0, 0, Station.StationType.STATION, Station.Orientation.NE, "S", "Sc");
    BuildingType bt = new BuildingType("Large Hotel", 1000, "Increases passengers", 1900);
    Building b = new Building(bt);

    StationUpgradeService service = new StationUpgradeService();

    boolean result = service.upgradeStation(station, b, 1950);

    assertTrue(result);
    assertEquals(1, station.getBuildings().size());
    assertEquals("Large Hotel", station.getBuildings().get(0).getBuildingType());
}
```

**Test 4:** Ensures that the method throws exceptions when it receives null arguments.

```java
void testUpgradeStationThrowsWithNullArgs() {
    StationUpgradeService service = new StationUpgradeService();
    Building b = new Building(new BuildingType("Large Hotel", 1000, "Stores", 1900));

    assertThrows(IllegalArgumentException.class, () -> service.upgradeStation(null, b, 1950));
    Station s = new Station("Map", 1, 0, 0, Station.StationType.STATION, Station.Orientation.NE, "S", "Sc");
    assertThrows(IllegalArgumentException.class, () -> service.upgradeStation(s, null, 1950));
}
```

**Test 5:** Guarantees that the building is not added to if the year is before the one available.

```java
void testUpgradeStationFailsDueToInvalidYear() {
    Station s = new Station("Map", 1, 0, 0, Station.StationType.STATION, Station.Orientation.NE, "S", "Sc");
    Building b = new Building(new BuildingType("Telephone", 500, "Communication", 1920));

    StationUpgradeService service = new StationUpgradeService();
    boolean result = service.upgradeStation(s, b, 1900);

    assertFalse(result);
    assertTrue(s.getBuildings().isEmpty());
}
```

**Test 6:** Tests conflict between hotels (mutually exclusive).

```java
void testUpgradeStationFailsDueToMutualExclusivity() {
    Station s = new Station("Map", 1, 0, 0, Station.StationType.STATION, Station.Orientation.NE, "S", "Sc");
    s.addBuilding(new Building(new BuildingType("Small Hotel", 800, "Provides lodging", 1900)));

    Building b = new Building(new BuildingType("Large Hotel", 1500, "More lodging", 1900));

    StationUpgradeService service = new StationUpgradeService();
    boolean result = service.upgradeStation(s, b, 1950);

    assertFalse(result);
    assertEquals(1, s.getBuildings().size());
}
```

**Test 7:** Test conflict between cafés (also exclusive).

```java
void testUpgradeStationFailsDueToCafeConflict() {
    Station s = new Station("Map", 1, 0, 0, Station.StationType.STATION, Station.Orientation.NE, "S", "Sc");
    s.addBuilding(new Building(new BuildingType("Small Cafe", 600, "Provides food", 1900)));

    Building b = new Building(new BuildingType("Large Cafe", 700, "Fancier cafe", 1900));

    StationUpgradeService service = new StationUpgradeService();
    boolean result = service.upgradeStation(s, b, 1950);

    assertFalse(result);
    assertEquals(1, s.getBuildings().size());
}
```

**Test 8:** Ensure repository returns all building types.

```java
void testGetBuildingTypes() {
    BuildingRepository repository = new BuildingRepository();
    List<BuildingType> buildingTypes = repository.getBuildingTypes();

    assertEquals(10, buildingTypes.size());
    assertTrue(buildingTypes.stream().anyMatch(bt -> bt.getName().equals("Telegraph")));
    assertTrue(buildingTypes.stream().anyMatch(bt -> bt.getName().equals("Telephone")));
    assertTrue(buildingTypes.stream().anyMatch(bt -> bt.getName().equals("Small Cafe")));
    assertTrue(buildingTypes.stream().anyMatch(bt -> bt.getName().equals("Large Cafe")));
    assertTrue(buildingTypes.stream().anyMatch(bt -> bt.getName().equals("Post Office")));
    assertTrue(buildingTypes.stream().anyMatch(bt -> bt.getName().equals("Small Hotel")));
    assertTrue(buildingTypes.stream().anyMatch(bt -> bt.getName().equals("Large Hotel")));
    assertTrue(buildingTypes.stream().anyMatch(bt -> bt.getName().equals("Silo")));
    assertTrue(buildingTypes.stream().anyMatch(bt -> bt.getName().equals("Liquid Storage")));
    assertTrue(buildingTypes.stream().anyMatch(bt -> bt.getName().equals("Customs")));
}
```

**Test 9:** Verify details of known building types.

```java
void testGetBuildingTypeExisting() {
    BuildingRepository repository = new BuildingRepository();

    BuildingType telegraph = repository.getBuildingType("Telegraph");
    BuildingType telephone = repository.getBuildingType("Telephone");
    BuildingType smallCafe = repository.getBuildingType("Small Cafe");

    assertNotNull(telegraph);
    assertEquals("Telegraph", telegraph.getName());
    assertEquals(50, telegraph.getCost());
    assertEquals("Improves communication", telegraph.getEffect());
    assertEquals(1850, telegraph.getAvailableYear());

    assertNotNull(telephone);
    assertEquals("Telephone", telephone.getName());
    assertEquals(80, telephone.getCost());
    assertEquals("Replaces Telegraph", telephone.getEffect());
    assertEquals(1920, telephone.getAvailableYear());

    assertNotNull(smallCafe);
    assertEquals("Small Cafe", smallCafe.getName());
    assertEquals(30, smallCafe.getCost());
    assertEquals("Slightly improves satisfaction", smallCafe.getEffect());
    assertEquals(1880, smallCafe.getAvailableYear());
}
```

**Test 10:** Verify building type lookup is case insensitive.

```java
void testGetBuildingTypeCaseInsensitive() {
    BuildingRepository repository = new BuildingRepository();

    BuildingType telegraph = repository.getBuildingType("telegraph");
    BuildingType telephone = repository.getBuildingType("TELEPHONE");
    BuildingType smallCafe = repository.getBuildingType("small CAFE");

    assertNotNull(telegraph);
    assertEquals("Telegraph", telegraph.getName());

    assertNotNull(telephone);
    assertEquals("Telephone", telephone.getName());

    assertNotNull(smallCafe);
    assertEquals("Small Cafe", smallCafe.getName());
}

```

**Test 11:** Return null when building type doesn't exist.
```java
void testGetBuildingTypeNonExistent() {
    BuildingRepository repository = new BuildingRepository();
    BuildingType nonExistent = repository.getBuildingType("Non Existent Building");
    assertNull(nonExistent);
}
```

**Test 12:** Ensure copyFrom creates an identical list of building types.
```java
void testCopyFrom() {
    BuildingRepository sourceRepository = new BuildingRepository();
    BuildingRepository targetRepository = new BuildingRepository();
    targetRepository.copyFrom(sourceRepository);

    List<BuildingType> sourceTypes = sourceRepository.getBuildingTypes();
    List<BuildingType> targetTypes = targetRepository.getBuildingTypes();
    assertEquals(sourceTypes.size(), targetTypes.size());

    for (int i = 0; i < sourceTypes.size(); i++) {
        BuildingType sourceType = sourceTypes.get(i);
        BuildingType targetType = targetTypes.get(i);

        assertEquals(sourceType.getName(), targetType.getName());
        assertEquals(sourceType.getCost(), targetType.getCost());
        assertEquals(sourceType.getEffect(), targetType.getEffect());
        assertEquals(sourceType.getAvailableYear(), targetType.getAvailableYear());
    }
}
```







## 5. Construction (Implementation)

### Class UpgradeAStationController

```java
package controller;

import domain.*;
import dto.BuildingTypeDTO;
import dto.BuildingTypeMapper;
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

    public List<BuildingTypeDTO> getFilteredAvailableBuildingTypesDTO(Station station, int year) {
        return BuildingTypeMapper.toDTO(getFilteredAvailableBuildingTypes(station, year));
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

```


### Class BuildingRepository

```java
package repositories;

import domain.BuildingType;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Repository responsible for managing all available building types in the simulation.
 * Provides methods to retrieve the full list of building types or search for a specific one by name.
 *
 * The building types are initialized with predefined names, costs, effects, and availability years.
 */
public class BuildingRepository implements Serializable {

    private final List<BuildingType> buildingTypes = new ArrayList<>();

    /**
     * Constructs a {@code BuildingRepository} and preloads it with a predefined list
     * of building types available in the simulation.
     * Each building type includes a name, cost, effect, and the year it becomes available.
     */
    public BuildingRepository() {
        buildingTypes.add(new BuildingType("Telegraph", 50, "Improves communication", 1850));
        buildingTypes.add(new BuildingType("Telephone", 80, "Replaces Telegraph", 1920));
        buildingTypes.add(new BuildingType("Small Cafe", 30, "Slightly improves satisfaction", 1880));
        buildingTypes.add(new BuildingType("Large Cafe", 50, "Greatly improves satisfaction", 1880));
        buildingTypes.add(new BuildingType("Post Office", 60, "Handles mail", 1870));
        buildingTypes.add(new BuildingType("Small Hotel", 100, "Short-term cargo", 1860));
        buildingTypes.add(new BuildingType("Large Hotel", 150, "Long-term cargo", 1890));
        buildingTypes.add(new BuildingType("Silo", 40, "Stores grain", 1855));
        buildingTypes.add(new BuildingType("Liquid Storage", 70, "Stores liquids", 1900));
        buildingTypes.add(new BuildingType("Customs", 90, "Facilitates international trade", 1865));
    }

    /**
     * Retrieves the complete list of building types available in the repository.
     *
     * @return A list of {@code BuildingType} objects representing all predefined building types.
     */
    public List<BuildingType> getBuildingTypes() {
        return buildingTypes;
    }

    /**
     * Searches for a building type by its name (case-insensitive).
     *
     * @param name The name of the building type to search for.
     * @return The matching {@code BuildingType}, or {@code null} if no match is found.
     */
    public BuildingType getBuildingType(String name) {
        for (BuildingType bt : buildingTypes) {
            if (bt.getName().equalsIgnoreCase(name)) return bt;
        }
        return null;
    }

    public void copyFrom(BuildingRepository bd2){
        this.buildingTypes.clear();
        this.buildingTypes.addAll(bd2.getBuildingTypes());
    }
}

```