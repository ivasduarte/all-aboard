# US007 - List all Stations

## 4. Tests

**Test 1:** Check that the repository starts out empty when it is created.

```java

void listAllStationsReturnsEmptyInitially() {
    List<Station> stations = repo.getStations();
    assertNotNull(stations);
    assertTrue(stations.isEmpty());
}
```

**Test 2:** Ensures that saved stations are correctly stored and retrieved.

```java

void listAllStationsReturnsAddedStations() {
    Station s1 = new Station("Map", 1, 0, 0,
            Station.StationType.STATION, Station.Orientation.NE, "Alpha", "Sc1");
    Station s2 = new Station("Map", 1, 10, 5,
            Station.StationType.DEPOT,   Station.Orientation.SE, "Beta",  "Sc1");

    repo.save(s1);
    repo.save(s2);

    List<Station> stations = repo.getStations();
    assertEquals(2, stations.size());
    assertTrue(stations.contains(s1));
    assertTrue(stations.contains(s2));
}
```

**Test 3:** It confirms that the copyFrom method completely replaces the content of the repository with that of another.

```java

void copyFromReplacesContent() {
    StationRepository otherRepo = new StationRepository();
    Station s = new Station("Map", 1, 5, 5,
            Station.StationType.TERMINAL, Station.Orientation.NW, "X", "Scenario");

    otherRepo.save(s);
    repo.save(new Station("Map", 1, 0, 0,
            Station.StationType.STATION, Station.Orientation.SE, "Old", "Scenario"));

    repo.copyFrom(otherRepo);

    List<Station> stations = repo.getStations();
    assertEquals(1, stations.size());
    assertEquals("X", stations.get(0).getName());
}
```



## 5. Construction (Implementation)

### Class ListStationsController

```java
package controller;

import domain.Building;
import domain.Map;
import domain.Scenario;
import domain.Station;
import dto.StationDTO;
import dto.StationMapper;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import repositories.Repositories;
import repositories.StationRepository;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Controller responsible for handling operations related to listing stations.
 */
public class ListStationsController {
    @FXML
    private ComboBox selectBox;
    @FXML
    private Label nameLabel;
    @FXML
    private Label idLabel;
    @FXML
    private Label typeLabel;
    @FXML
    private Label positionLabel;
    @FXML
    private Label cargoLabel;
    @FXML
    private Label buildingLabel;

    private final StationRepository StationRepo;
    private Scenario currentScenario;
    private Map currentMap;
    private boolean hasStations;

    /**
     * Sets the current scenario and updates the associated map.
     *
     * @param scenario The scenario object to be set. Must not be null.
     *                 Contains details including the map, restrictions, and other scenario data.
     */
    public void setData(Scenario scenario) {
        this.currentScenario = scenario;
        this.currentMap = scenario.getMap();
    }

    /**
     * Constructs a new instance of the ListStationsController class.
     * This constructor initializes the StationRepo field by obtaining the
     * StationRepository instance from the Repositories singleton.
     */
    public ListStationsController() {
        this.StationRepo = Repositories.getInstance().getStationRepository();
    }


    public List<StationDTO> getStationsDTOList() {
        List<StationDTO> result = new ArrayList<>();

        for (Station s : StationRepo.getStations()) {
            if (s.getMapName().equalsIgnoreCase(currentMap.getName())) {
                StationDTO dto = StationMapper.toDTO(s);
                if (dto != null) result.add(dto);
            }
        }
        return result;
    }


    /**
     * Initializes the controller by populating the select box with a list of stations
     * and setting up an event handler for station selection.
     */
    @FXML
    public void initialize() {

        selectBox.setOnAction(e -> showStationDetails());

    }

    public void loadStations(){

        List<Station> stations = StationRepo.getStations();
        List<Station> selectedStations = new ArrayList<>();
        for(Station station : stations) {
            if(station.getMapName().equalsIgnoreCase(currentScenario.getMap().getName()) && station.getScenarioName().equalsIgnoreCase(currentScenario.getScenarioName())) {
                selectedStations.add(station);
            }
        }

        hasStations = !selectedStations.isEmpty();

        selectBox.getItems().addAll(selectedStations);
        selectBox.setOnAction(e -> showStationDetails());

    }

    /**
     * Updates the UI to display detailed information about the currently selected station.
     */
    @FXML
    private void showStationDetails() {
        Station currentStation = (Station) selectBox.getValue();
        if (currentStation == null) return;

        String name = currentStation.getName();
        int id = currentStation.getStationID();
        Station.StationType type = currentStation.getStationType();
        int x = currentStation.getPositionX();
        int y = currentStation.getPositionY();
        int cargo = currentStation.getCargo();
        List<Building> buildings = currentStation.getBuildings();


        nameLabel.setText("Name: " + name);
        idLabel.setText("ID: " + id);
        typeLabel.setText("Type: " + type);
        positionLabel.setText("Position: (" + x + ", " + y + ")");
        cargoLabel.setText("Cargo: " + cargo);

        if (buildings.isEmpty()) {
            buildingLabel.setText("Buildings: none");
        } else {
            StringBuilder buildingsText = new StringBuilder("Buildings:\n");
            for (Building building : buildings) {
                buildingsText.append("  - ").append(building.getBuildingType()).append("\n");
            }
            buildingLabel.setText(buildingsText.toString());
        }
    }


    public boolean hasStations() {
        return hasStations;
    }

    /**
     * Handles the action event when navigating back to the player menu screen.
     * This method loads the "playerMenu.fxml" file, initializes the PlayerMenuController
     * with the current scenario data, and displays the player menu in a new stage.
     * It also closes the current stage associated with the selectBox element.
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

            Stage currentStage = (Stage) selectBox.getScene().getWindow();
            currentStage.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

```

### Class StationRepository

```java
package repositories;

import domain.Station;
import domain.Map;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class StationRepository implements Serializable {

    private List<Station> stations;

    public StationRepository() { stations = new ArrayList<>(); }

    /**
     * Saves a new station
     * @param station new station
     */
    public void save(Station station){ stations.add(station); }

    /**
     * Saves a new list of stations
     * @param stations new stations
     */
    public void save(List<Station> stations){
        for(Station station : stations)
            save(station);
    }

    /**
     * Get all the stations
     * @return all stations
     */
    public List<Station> getStations() { return stations; }

    /**
     * Show all the stations
     */
    public void displayAllStations(){
        for (int i = 0; i < stations.size(); i++) {
            System.out.println(stations.get(i));
        }
    }

    /**
     * Get all the stations with available building slots
     * @return all stations with available building slots
     */
    public List<Station> getAvailableStationsByMapName(String mapName) {
        List<Station> result = new ArrayList<>();
        for (Station station : stations) {
            Map stationMap = station.getMap();
            if (stationMap != null && stationMap.getName().equalsIgnoreCase(mapName)) {
                result.add(station);
            }
        }
        return result;
    }

    public void copyFrom(StationRepository st2){
        this.stations.clear();
        this.stations.addAll(st2.getStations());
    }
}


```