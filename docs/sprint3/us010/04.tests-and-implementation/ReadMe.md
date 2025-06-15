# US010 - Assign Train To A Route

## 4. Tests

**Test 1:** Create train

```java
void testCreateTrain() {
    Locomotive locomotive = new Locomotive("Test Locomotive", "STEAM", 1900, 1950, 10000, 1000, 500, 100);
    int departureYear = 1900;

    Train train = new Train(locomotive, departureYear);

    assertNotNull(train);
    assertEquals(locomotive, train.getLocomotive());
    assertTrue(train.getRoute().isEmpty());
    assertTrue(train.getCarriages().isEmpty());
}
```

**Test 2:** Assign route to train

```java
void testAssignRouteToTrain() {
    Locomotive locomotive = new Locomotive("Test Locomotive", "STEAM", 1900, 1950, 10000, 1000, 500, 100);
    Train train = new Train(locomotive, 1900);

    Station station1 = new Station("Test Map", 1, 100, 100,
            Station.StationType.STATION, Station.Orientation.NE, "Station 1", "Test Scenario");
    Station station2 = new Station("Test Map", 1, 200, 200,
            Station.StationType.STATION, Station.Orientation.NE, "Station 2", "Test Scenario");

    List<Station> stations = new ArrayList<>();
    stations.add(station1);
    stations.add(station2);

    train.setRouteWithStations(stations);

    assertEquals(2, train.getRoute().size());
    assertEquals(station1, train.getRoute().get(0).getStation());
    assertEquals(station2, train.getRoute().get(1).getStation());
}
```

**Test 3:** Add carriage to train

```java
void testAddCarriageToTrain() {
    Locomotive locomotive = new Locomotive("Test Locomotive", "STEAM", 1900, 1950, 10000, 1000, 500, 100);
    Train train = new Train(locomotive, 1900);
    Cargo cargo = new Cargo(Cargo.Type.PASSENGERS);
    Carriage carriage = new Carriage(cargo, 100, 0);

    train.getCarriages().add(carriage);

    assertEquals(1, train.getNumberOfCarriages());
    assertTrue(train.getCarriages().contains(carriage));
}
```

**Test 4:** Calculate route distance

```java
void testCalculateRouteDistance() {
    Locomotive locomotive = new Locomotive("Test Locomotive", "STEAM", 1900, 1950, 10000, 1000, 500, 100);
    Train train = new Train(locomotive, 1900);

    Station station1 = new Station("Test Map", 1, 0, 0,
            Station.StationType.STATION, Station.Orientation.NE, "Station 1", "Test Scenario");
    Station station2 = new Station("Test Map", 1, 100, 100,
            Station.StationType.STATION, Station.Orientation.NE, "Station 2", "Test Scenario");

    List<Station> stations = new ArrayList<>();
    stations.add(station1);
    stations.add(station2);

    train.setRouteWithStations(stations);
    double distance = train.calculateRouteDistance();

    assertTrue(distance > 0);
    assertEquals(141.42, distance, 0.01);
}
```

**Test 5:** Validates getRoute()

```java
void testGetRoute() {
    assertTrue(train.getRoute().isEmpty());
    Station station = new Station("Test Map", 1, 100, 100,
            Station.StationType.STATION, Station.Orientation.NE, "Station 1", "Test Scenario");
    List<Station> stations = new ArrayList<>();
    stations.add(station);
    train.setRouteWithStations(stations);
    assertEquals(1, train.getRoute().size());
}
```



## 5. Construction (Implementation)

### Class AssignRouteController

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
import repositories.Repositories;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Controller class responsible for managing route assignments for trains.
 * Handles the creation and management of routes between stations.
 */
public class AssignRouteController {

    @FXML
    private ComboBox<Train> trainCmb;
    @FXML
    private ComboBox<Station> stationCmb;
    @FXML
    private ComboBox<CargoMode> cargoCmb;

    @FXML
    private ListView<PointOfRoute> stationList;

    @FXML
    private Button backBtn;
    @FXML
    private Button addStationBtn;
    @FXML
    private Button assignRouteBtn;

    @FXML
    private Label selectStationLbl;

    private Map map;
    private Scenario scenario;

    /**
     * Sets the initial data for the route assignment process.
     * @param map The map containing the stations
     * @param scenario The current scenario being played
     */
    public void setData(Map map, Scenario scenario)
    {
        this.map = map;
        this.scenario = scenario;

        if(checkValidation())
        {
            List<Station> stations = new ArrayList<>(this.map.getStations());
            List<Train> trains = new ArrayList<>(this.scenario.getTrains());

            trainCmb.setItems(FXCollections.observableList(trains));
            stationCmb.setItems(FXCollections.observableList(stations));

            cargoCmb.setItems(FXCollections.observableList(Arrays.stream(CargoMode.values()).toList()));
            assignRouteBtn.setVisible(stationList.getItems().size() >= 2);
        }
    }

    /**
     * Handles the addition of a new point to the route.
     * Validates the station and cargo mode selections before adding.
     */
    public void handleAddPointOfRoute()
    {
        if(stationCmb.getValue() == null)
        {
            setAlert(Alert.AlertType.ERROR, "Invalid station", "Insert a valid station to add a point of route!");
            return;
        }

        if(cargoCmb.getValue() == null)
        {
            setAlert(Alert.AlertType.ERROR, "Invalid cargo mode", "Insert a valid cargo mode to add a point of route!");
            return;
        }

        Station station = stationCmb.getValue();
        CargoMode cargoMode = cargoCmb.getValue();

        PointOfRoute pointOfRoute = new PointOfRoute(station, cargoMode);

        stationList.getItems().add(pointOfRoute);
        stationCmb.getItems().remove(station);

        assignRouteBtn.setVisible(stationList.getItems().size() >= 2);

        if(stationCmb.getItems().isEmpty()) {
            stationCmb.setVisible(false);
            addStationBtn.setVisible(false);
            selectStationLbl.setText("No more available stations!");
        }
    }

    /**
     * Handles the route assignment process.
     * Validates the train selection and assigns the route if all conditions are met.
     */
    public void handleAssignRoute()
    {
        if(trainCmb.getValue() == null)
        {
            setAlert(Alert.AlertType.ERROR, "Invalid train", "Select a valid train to assign a route!");
            return;
        }

        List<PointOfRoute> pointOfRoutes = new ArrayList<>(stationList.getItems());
        Train train = trainCmb.getValue();

        assignRoute(train, pointOfRoutes);

        setAlert(Alert.AlertType.CONFIRMATION, "Route assigned successfully", "The route has been assigned to " + train.getLocomotive().getLocomotiveModel());
        handleBackToMenu();
    }

    /**
     * Displays an alert dialog with the specified type, title and message.
     * @param alertType The type of alert to display
     * @param title The title of the alert
     * @param text The message to display
     */
    public void setAlert(Alert.AlertType alertType, String title, String text)
    {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setContentText(text);
        alert.showAndWait();
    }

    public boolean checkValidation()
    {
        List<Station> stations = this.map.getStations();
        List<Train> trains = this.scenario.getTrains();

        return stations.size() >= 2 && !trains.isEmpty();
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

    /**
     * Assigns a specified route to a train and prints the details of the assigned route,
     * including cargo availability and demand at each station.
     *
     * @param train    the train to which the route will be assigned
     * @param pointOfRoutes the list of points of route that define the route for the train
     */
    public void assignRoute(Train train, List<PointOfRoute> pointOfRoutes) {
        train.setRoute(pointOfRoutes);
        List<Station> stations = new ArrayList<>();

        for(PointOfRoute pointOfRoute : pointOfRoutes)
            stations.add(pointOfRoute.getStation());

        displayStations(train, stations);
    }

    /**
     * Assigns a specified route to a train and prints the details of the assigned route,
     * including cargo availability and demand at each station.
     *
     * @param train    the train to which the route will be assigned
     * @param stations the list of stations that define the route for the train
     */
    public void assignRouteWithStations(Train train, List<Station> stations) {
        train.setRouteWithStations(stations);
        displayStations(train, stations);
    }

    public void displayStations(Train train, List<Station> stations)
    {
        System.out.println("\n\n[ASSIGNED ROUTE] Train '" + train.getLocomotive().getLocomotiveModel() +
                "' received route with " + stations.size() + " stations:");

        for (int i = 0; i < stations.size(); i++) {
            Station station = stations.get(i);
            System.out.println("\n  " + (i + 1) + ". " + station.getName() +
                    " (" + station.getPositionX() + "," + station.getPositionY() + ")" +
                    " - Cargo: " + station.getCargo() +
                    " - Demand: " + station.getDemand());
        }
    }

    /**
     * Sets the cargo mode for a specific point in the train's route and provides
     * a visual sign of the configuration and behavior based on the mode.
     * If the specified point index corresponds to a valid station in the train's route,
     * additional details about the cargo mode configuration at the station are displayed.
     *
     * @param train      the train whose cargo mode is being configured
     * @param pointIndex the index of the point in the train's route where the cargo mode is to be set
     * @param mode       the cargo mode to be set, determining the departure conditions from the station
     */
    public void setCargoMode(Train train, int pointIndex, CargoMode mode) {
        train.setCargoMode(pointIndex, mode);

        if (train.getRoute() != null && pointIndex < train.getRoute().size()) {
            PointOfRoute point = train.getRoute().get(pointIndex);
            System.out.println("\n[CARGO MODE] Station '" + point.getStation().getName() +
                    "' set with mode: " + mode.toString());

            switch (mode) {
                case FULL:
                    System.out.println("  → Train only departs when full loaded");
                    break;
                case HALF:
                    System.out.println("  → Train only departs when half loaded");
                    break;
                case AVAILABLE:
                    System.out.println("  → Train only departs with whatever cargo is available");
                    break;
            }
        }
    }
}
```


### Class TrainRepository

```java
package repositories;

import domain.Locomotive;
import domain.Train;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class TrainRepository implements Serializable {
    private List<Train> trains = new ArrayList<>();

    /**
     * This method is responsible to add trains to the list of all trains available in the simulator.
     *
     * @param train predefined trains
     */
    public void saveTrain(Train train) {
        trains.add(train);
    }

    /**
     * This method is responsible to give the list of all trains in the game.
     *
     * @return List of all trains
     */
    public List<Train> getAllTrains() {
        return Collections.unmodifiableList(trains);
    }

    /**
     * This method is responsible to get a specific train by its locomotive.
     *
     * @param locomotive the locomotive
     *
     * @return corresponding train
     */
    public Train getTrainByLocomotive(Locomotive locomotive) {
        for (Train train : trains) {
            if (train.getLocomotive().equals(locomotive)) {
                return train;
            }
        }

        return null;
    }

    /**
     * This method is responsible to get a specific train by its name.
     *
     * @param name the name of the train
     * @return Optional containing the train if found, empty otherwise
     * @throws IllegalArgumentException if name is null or empty
     */
    public Optional<Train> getTrainByName(String name) {
        if (name == null) {
            throw new IllegalArgumentException("Name cannot be null");
        }
        if (name.trim().isEmpty()) {
            throw new IllegalArgumentException("Name cannot be empty");
        }
        return trains.stream()
                .filter(train -> train.getLocomotive().getLocomotiveModel().equals(name))
                .findFirst();
    }
}
```