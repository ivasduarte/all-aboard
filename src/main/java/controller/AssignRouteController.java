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
 * Provides functionality to assign routes, configure cargo modes, execute routes, and display route information for trains.
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