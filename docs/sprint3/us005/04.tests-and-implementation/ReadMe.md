# US005 - Build A Station

## 4. Tests

**Test 1:** Create A Station

```java
void testCreateStation() {
    String mapName = "Test Map";
    int mapID = 1;
    int posX = 100;
    int posY = 100;
    Station.StationType stationType = Station.StationType.STATION;
    Station.Orientation orientation = Station.Orientation.NE;
    String name = "Test Station";
    String scenarioName = "Test Scenario";

    Station station = new Station(mapName, mapID, posX, posY, stationType, orientation, name, scenarioName);

    assertNotNull(station);
    assertEquals(name, station.getName());
    assertEquals(mapName, station.getMapName());
    assertEquals(posX, station.getPositionX());
    assertEquals(posY, station.getPositionY());
    assertEquals(stationType, station.getStationType());
    assertEquals(orientation, station.getOrientation());
    assertEquals(0, station.getCargo());
    assertEquals(1000, station.getMaxCargo());
    assertTrue(station.getBuildings().isEmpty());
}
```

**Test 2:** Add building to station

```java
void testAddBuildingToStation() {
    Station station = new Station("Test Map", 1, 100, 100,
            Station.StationType.STATION, Station.Orientation.NE, "Test Station", "Test Scenario");
    BuildingType buildingType = new BuildingType("PassengerTerminal", 1000, "Handles passengers", 1900);
    Building building = new Building(buildingType);

    boolean result = station.addBuilding(building);

    assertTrue(result);
    assertEquals(1, station.getBuildings().size());
    assertTrue(station.getBuildings().contains(building));
}
```

**Test 3:** Update station cargo

```java
void testUpdateStationCargo() {
    Station station = new Station("Test Map", 1, 100, 100,
            Station.StationType.STATION, Station.Orientation.NE, "Test Station", "Test Scenario");
    int initialCargo = 100;
    int additionalCargo = 50;

    station.addCargo(initialCargo);
    station.addCargo(additionalCargo);

    assertEquals(initialCargo + additionalCargo, station.getCargo());
}
```

**Test 4:** Update station demand

```java
void testUpdateStationDemand() {
    Station station = new Station("Test Map", 1, 100, 100,
            Station.StationType.STATION, Station.Orientation.NE, "Test Station", "Test Scenario");
    int demand = 200;

    station.setDemand(demand);

    assertEquals(demand, station.getDemand());
}
```

**Test 5:** Create station with invalid parameters

```java
void testCreateStationWithInvalidParameters() {
    assertThrows(IllegalArgumentException.class, () -> {
        new Station(null, 1, 100, 100,
                Station.StationType.STATION, Station.Orientation.NE, "Test Station", "Test Scenario");
    });

    assertThrows(IllegalArgumentException.class, () -> {
        new Station("Test Map", 1, -1, 100,
                Station.StationType.STATION, Station.Orientation.NE, "Test Station", "Test Scenario");
    });

    assertThrows(IllegalArgumentException.class, () -> {
        new Station("Test Map", 1, 100, -1,
                Station.StationType.STATION, Station.Orientation.NE, "Test Station", "Test Scenario");
    });

    assertThrows(IllegalArgumentException.class, () -> {
        new Station("Test Map", 1, 100, 100,
                null, Station.Orientation.NE, "Test Station", "Test Scenario");
    });

    assertThrows(IllegalArgumentException.class, () -> {
        new Station("Test Map", 1, 100, 100,
                Station.StationType.STATION, null, "Test Station", "Test Scenario");
    });
}
```


## 5. Construction (Implementation)

### Class AddStationController

```java
package controller;

import domain.*;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import repositories.MapRepository;
import repositories.Repositories;
import repositories.StationRepository;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class AddStationController {

    @FXML
    private Button backBtn;
    @FXML
    private Button addBtn;

    @FXML
    private ComboBox<Station.StationType> stationTypeCmb;
    @FXML
    private ComboBox<Station.Orientation> orientationCmb;

    @FXML
    private Label mapLbl;
    @FXML
    private Label scenarioLbl;
    @FXML
    private Label budgetLbl;

    @FXML
    private Label orientationLbl;
    @FXML
    private Label stationPriceLbl;

    @FXML
    private TextField XPosTxt;
    @FXML
    private TextField YPosTxt;

    private Map map;
    private Scenario scenario;

    public void setData(Map map, Scenario scenario) {
        this.map = map;
        this.scenario = scenario;

        mapLbl.setText("Map: " + this.map.getName());
        scenarioLbl.setText("Scenario: " + this.scenario.getScenarioName());
        budgetLbl.setText("Budget: " + this.scenario.getBudget().getBudget() + "€");

        stationTypeCmb.setItems(FXCollections.observableList(Arrays.stream(Station.StationType.values()).toList()));
        orientationCmb.setItems(FXCollections.observableList(Arrays.stream(Station.Orientation.values()).toList()));

        orientationCmb.setVisible(false);
        orientationLbl.setVisible(false);
        stationPriceLbl.setVisible(false);
    }

    @FXML
    public void initialize() {}

    @FXML
    public void handleXPosTxt() {
        try {
            int value = Integer.parseInt(XPosTxt.getText());
            if (value < 1 || value > map.getWidth()) {
                showAlert("Invalid X position", "X must be between 1 and " + map.getWidth());
                XPosTxt.setText("");
            }
        } catch (NumberFormatException e) {
            showAlert("Invalid input", "X must be a number");
            XPosTxt.setText("");
        }
    }

    @FXML
    public void handleYPosTxt() {
        try {
            int value = Integer.parseInt(YPosTxt.getText());
            if (value < 1 || value > map.getHeight()) {
                showAlert("Invalid Y position", "Y must be between 1 and " + map.getHeight());
                YPosTxt.setText("");
            }
        } catch (NumberFormatException e) {
            showAlert("Invalid input", "Y must be a number");
            YPosTxt.setText("");
        }
    }

    @FXML
    public void handleTypeChange() {
        Station.StationType stationType = stationTypeCmb.getValue();
        stationPriceLbl.setVisible(stationType != null);

        if (stationType != null) {
            stationPriceLbl.setText("Station price: " + stationType.getPrice() + "€");
            boolean needsOrientation = stationType.getSize() % 2 == 0;
            orientationCmb.setVisible(needsOrientation);
            orientationLbl.setVisible(needsOrientation);
        }
    }

    @FXML
    public void handleAddStation() {
        int mapID = -1;

        MapRepository mapRepository = Repositories.getInstance().getMapRepository();
        List<Map> maps = mapRepository.getMapList();

        for (int i = 0; i < maps.size(); i++) {
            if (maps.get(i).equals(map))
                mapID = i;
        }

        Alert errorAlert = new Alert(Alert.AlertType.ERROR);

        if (mapID == -1) {
            errorAlert.setTitle("Map not valid");
            errorAlert.setContentText("The selected map isn't valid, select a valid map!");
            errorAlert.showAndWait();
            return;
        }

        if (stationTypeCmb.getValue() == null) {
            errorAlert.setTitle("Station type not valid");
            errorAlert.setContentText("Station type isn't valid, select a valid type before continuing!");
            errorAlert.showAndWait();
            return;
        }

        if (stationTypeCmb.getValue().getSize() % 2 == 0 && orientationCmb.getValue() == null) {
            errorAlert.setTitle("Orientation not valid");
            errorAlert.setContentText("Orientation isn't valid, select a valid orientation before continuing!");
            errorAlert.showAndWait();
            return;
        }

        if (stationTypeCmb.getValue().getPrice() > scenario.getBudget().getBudget()) {
            errorAlert.setTitle("Insufficient funds");
            errorAlert.setContentText("Your budget is too low to afford the desired station!");
            errorAlert.showAndWait();
            return;
        }

        int positionX, positionY;
        try {
            positionX = Integer.parseInt(XPosTxt.getText()) - 1;
            positionY = Integer.parseInt(YPosTxt.getText()) - 1;
        } catch (NumberFormatException e) {
            showAlert("Invalid coordinates", "Please enter valid numeric coordinates for X and Y.");
            return;
        }

        Station.StationType stationType = stationTypeCmb.getValue();
        Station.Orientation stationOrientation = orientationCmb.getValue();

        if (stationOrientation == null)
            stationOrientation = Station.Orientation.values()[0];

        String stationName = generateStationName(map.getName(), positionX, positionY, stationType.getDisplayName());
        Station newStation = new Station(map.getName(), mapID, positionX, positionY, stationType, stationOrientation, stationName, scenario.getScenarioName());
        newStation.setOwnerPlayer(UserSession.getInstance().getUser().getEmail());

        if (!newStation.validatePosition(map, stationType.getSize() / 2)) {
            showAlert("Position not valid", "The station's range is out of bounds, enter a different position!");
            return;
        }

        if (!newStation.checkOverbuilding()) {
            showAlert("Station overbuilding", "The selected position is already occupied by another station/industry or city!");
            return;
        }

        Repositories.getInstance().getStationRepository().save(newStation);
        map.addNewStation(newStation);
        Station.increaseTotalStations();
        scenario.subtractBudget(stationType.getPrice());

        Alert infoAlert = new Alert(Alert.AlertType.CONFIRMATION);
        infoAlert.setTitle("Station added");
        infoAlert.setContentText("Station has been added successfully under the name " + newStation.getName() + " at (" + (newStation.getPositionX() + 1) + "," + (newStation.getPositionY() + 1) + ")");
        infoAlert.showAndWait();
        handleBackToMenu();
    }

    @FXML
    private void handleBackToMenu() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/JavaFX/playerMenu.fxml"));
            Parent root = loader.load();

            PlayerMenuController controller = loader.getController();
            controller.setData(scenario);

            Stage stage = new Stage();
            stage.setTitle("Player Menu");
            stage.setScene(new Scene(root));
            stage.show();

            Stage currentStage = (Stage) backBtn.getScene().getWindow();
            currentStage.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String AddStation(Map map, Scenario scenario, int positionX, int positionY, int stationType, String rotation) {
        int mapID = -1;

        MapRepository mapRepository = Repositories.getInstance().getMapRepository();
        List<Map> maps = mapRepository.getMapList();

        for (int i = 0; i < maps.size(); i++) {
            if (maps.get(i).equals(map))
                mapID = i;
        }

        StationRepository stationRepository = Repositories.getInstance().getStationRepository();

        if (stationType < 0 || stationType > Station.StationType.values().length - 1) {
            return "Invalid station type";
        }

        Station.StationType type = Station.StationType.values()[stationType];

        Station.Orientation orientation;
        try {
            orientation = Station.Orientation.fromString(rotation);
        } catch (IllegalArgumentException e) {
            return "Invalid orientation";
        }

        int size = Station.StationType.values().length / 2;

        int budget = scenario.getBudget().getBudget();

        if (type.getPrice() > budget) {
            return "Not enough budget for purchase";
        }

        String stationName = generateStationName(map.getName(), positionX, positionY, type.getDisplayName());
        Station newStation = new Station(map.getName(), mapID, positionX, positionY, type, orientation, stationName, scenario.getScenarioName());
        newStation.setOwnerPlayer(UserSession.getInstance().getUser().getEmail());

        if (!newStation.validatePosition(map, size)) {
            return "Invalid position";
        }

        if (!newStation.checkOverbuilding()) {
            return "Station overbuilding";
        }

        stationRepository.save(newStation);
        map.addNewStation(newStation);
        Station.increaseTotalStations();
        scenario.subtractBudget(type.getPrice());
        return "Station added successfully under the name " + stationName;
    }

    public String generateStationName(String mapName, int posX, int posY, String type) {
        List<City> cities = Repositories.getInstance().getCityRepository().getCitiesByMap(mapName);
        String stationName = type;

        if (!cities.isEmpty()) {
            double smallestDistance = Math.sqrt(Math.pow(cities.get(0).getCenterX() - posX, 2) + Math.pow(cities.get(0).getCenterY() - posY, 2));
            City closestCity = cities.get(0);

            for (City city : cities) {
                int cityX = city.getCenterX();
                int cityY = city.getCenterY();
                double distance = Math.sqrt(Math.pow(cityX - posX, 2) + Math.pow(cityY - posY, 2));

                if (distance < smallestDistance) {
                    closestCity = city;
                    smallestDistance = distance;
                }
            }

            stationName = closestCity.getName() + " " + type;
        }

        return stationName;
    }

    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setContentText(content);
        alert.showAndWait();
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