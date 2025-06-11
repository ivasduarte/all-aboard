package controller;

import domain.*;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListView;
import javafx.stage.Stage;

import java.io.IOException;
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

    private Map map;
    private Scenario scenario;

    public void setData(Map map, Scenario scenario)
    {
        this.map = map;
        this.scenario = scenario;

        List<Station> stations;
        List<Train> trains;

        stations = this.map.getStations();
        trains = this.scenario.getTrains();

        trainCmb.setItems(FXCollections.observableList(trains));
        stationCmb.setItems(FXCollections.observableList(stations));
        cargoCmb.setItems(FXCollections.observableList(Arrays.stream(CargoMode.values()).toList()));

        System.out.println("trainCmb items: " + trainCmb.getItems());
        System.out.println("stationCmb items: " + stationCmb.getItems());
        System.out.println("cargoCmb items: " + cargoCmb.getItems());
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
     * @param stations the list of stations that define the route for the train
     */
    public void assignRoute(Train train, List<Station> stations) {
        train.setRoute(stations);
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