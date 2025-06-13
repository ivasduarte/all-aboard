package controller;

import domain.Map;
import domain.Scenario;
import domain.Station;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import repositories.Repositories;


import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ChangeStationTypeController {

    @FXML
    private Label labelPriceDiff;
    @FXML
    private Button backButton;
    @FXML
    private ComboBox<Station> stationComboBox;
    @FXML
    private Label labelTest;
    @FXML
    private ComboBox<Station.StationType> stationTypeComboBox;
    @FXML
    private Label labelBudget;

    private Scenario currentScenario;
    private Map currentMap;
    private boolean hasStations;

    public void setData(Scenario scenario) {
        this.currentScenario = scenario;
        this.currentMap = scenario.getMap();

        if (labelBudget != null && currentScenario.getBudget() != null) {

            labelBudget.setText("Budget: " + currentScenario.getBudget().getBudget() + "€");

        }

        setStationComboBox();
    }

    /**
     * Change the type of the station into a higher one
     *
     * @param station the station which type is changing
     * @param typeID the new type for the station
     * @return true if the change was successful, false otherwise
     */
    public boolean ChangeStationType(Station station, int typeID) {

        station.setStationType(Station.StationType.values()[typeID]);

        return true;

    }

    public boolean hasStations() {

        return hasStations;

    }


    @FXML
    public void setStationComboBox() {

        List<Station> stations = Repositories.getInstance().getStationRepository().getStations();

        List<Station> selectedStations = new ArrayList<>();

        for (Station s : stations) {

            String mapName = s.getMapName();

            String scenarioName = s.getScenarioName();

            if (mapName.equalsIgnoreCase(currentMap.getName()) && scenarioName.equalsIgnoreCase(currentScenario.getScenarioName())) {


                if(s.getStationType() != Station.StationType.TERMINAL){


                    selectedStations.add(s);

                }
            }
        }

        hasStations = !selectedStations.isEmpty();

        stationComboBox.getItems().setAll(selectedStations);

    }


    @FXML
    public void changeStationType(){

        Station station = stationComboBox.getValue();

        if(station != null){

            for (int i = 0; i < Station.StationType.values().length; i++) {

                if(i > station.getStationTypeID()) {

                    stationTypeComboBox.getItems().add(Station.StationType.values()[i]);

                }
            }
        }

        Station.StationType stationTypeSelected = stationTypeComboBox.getValue();

        if(station == null || stationTypeSelected == null){

            labelTest.setText("Please select both a station and a building.");

            return;

        }
        int initialPrice = station.getStationPrice();

        int finalPrice = stationTypeSelected.getPrice();

        int priceDiff = finalPrice - initialPrice;


        if(currentScenario.getBudget().getBudget() >= priceDiff){

            station.setStationType(stationTypeSelected);

            currentScenario.subtractBudget(priceDiff);

            labelTest.setText("Changed station type");


            if(station.getStationType() == Station.StationType.TERMINAL){

                    stationComboBox.getItems().remove(station);

            }

            stationComboBox.getSelectionModel().clearSelection();

            stationTypeComboBox.getItems().clear();

            labelPriceDiff.setText("");

            labelBudget.setText("Budget: " + currentScenario.getBudget().getBudget() + "€");

         } else {
             labelTest.setText("Not enough budget to upgrade this station.");
         }

        handleBackToMenu();

        }


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

            Stage currentStage = (Stage) backButton.getScene().getWindow();
            currentStage.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void onStationSelected() {
        updateStationTypes();
    }

    private void updateStationTypes() {
        stationTypeComboBox.getItems().clear();
        labelPriceDiff.setText("");
        Station station = stationComboBox.getValue();
        if (station != null) {
            int currentTypeID = station.getStationTypeID();
            int currentPrice = station.getStationPrice();

            for (int i = 0; i < Station.StationType.values().length; i++) {
                if (i > station.getStationTypeID()) {
                    stationTypeComboBox.getItems().add(Station.StationType.values()[i]);
                }
            }
            stationTypeComboBox.setOnAction(e -> {
                Station.StationType selectedType = stationTypeComboBox.getValue();
                if (selectedType != null) {
                    Station.StationType originalType = station.getStationType();
                    station.setStationType(selectedType);
                    int newPrice = station.getStationPrice();
                    station.setStationType(originalType);

                    int diff = newPrice - currentPrice;
                    labelPriceDiff.setText("Upgrade cost: " + diff + "€");

                }
            });
        }
        }
    }

