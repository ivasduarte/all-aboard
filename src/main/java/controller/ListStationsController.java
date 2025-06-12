package controller;

import domain.Building;
import domain.Map;
import domain.Scenario;
import domain.Station;
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
            if(station.getScenarioName().equalsIgnoreCase(currentScenario.getScenarioName())){
                selectedStations.add(station);
            }
        }
        if (selectedStations.isEmpty()) {
            showAlert("No stations available.");
            return;
        }

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

    /**
     * Displays an error alert dialog with the specified message.
     *
     * @param msg The message to be displayed in the alert dialog. Cannot be null or empty.
     */
    private void showAlert(String msg) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setContentText(msg);
        alert.showAndWait();
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