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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Controller class responsible for managing the addition of new stations to the railway system.
 * Handles user interface interactions and station creation logic.
 */
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
    private Slider XPosSli;
    @FXML
    private Slider YPosSli;

    @FXML
    private TextField XPosTxt;
    @FXML
    private TextField YPosTxt;

    private Map map;
    private Scenario scenario;

    /**
     * Sets the initial data for the station creation process.
     * @param map The map where the station will be added
     * @param scenario The current scenario being played
     */
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

        XPosSli.setMin(1);
        XPosSli.setMax(map.getWidth());
        XPosSli.setValue(1);
        XPosTxt.setText(String.valueOf(XPosSli.getValue()));

        YPosSli.setMin(1);
        YPosSli.setMax(map.getHeight());
        YPosSli.setValue(1);
        YPosTxt.setText(String.valueOf(YPosSli.getValue()));
    }

    /**
     * Initializes the controller and sets up event listeners for position sliders.
     */
    @FXML
    public void initialize() {
        XPosSli.valueProperty().addListener((observable, oldValue, newValue) -> {
            XPosTxt.setText(Double.toString(newValue.intValue()));
        });

        YPosSli.valueProperty().addListener((observable, oldValue, newValue) -> {
            YPosTxt.setText(Double.toString(newValue.intValue()));
        });
    }

    /**
     * Handles changes in the X position text field.
     * Validates and updates the X position slider accordingly.
     */
    @FXML
    public void handleXPosTxt() {
        try {
            int value = Integer.parseInt(XPosTxt.getText());

            if (value > XPosSli.getMax())
                XPosSli.setValue(XPosSli.getMax());
            else if (value < XPosSli.getMin())
                XPosSli.setValue(XPosSli.getMin());
            else
                XPosSli.setValue(value);

        } catch (NumberFormatException e) {
            XPosTxt.setText(String.valueOf(XPosSli.getValue()));
        }
    }

    /**
     * Handles changes in the Y position text field.
     * Validates and updates the Y position slider accordingly.
     */
    @FXML
    public void handleYPosTxt() {
        try {
            int value = Integer.parseInt(YPosTxt.getText());

            if (value > YPosSli.getMax())
                YPosSli.setValue(YPosSli.getMax());
            else if (value < YPosSli.getMin())
                YPosSli.setValue(YPosSli.getMin());
            else
                YPosSli.setValue(value);

        } catch (NumberFormatException e) {
            YPosTxt.setText(String.valueOf(YPosSli.getValue()));
        }
    }

    /**
     * Handles changes in the station type selection.
     * Updates the UI based on the selected station type.
     */
    @FXML
    public void handleTypeChange() {
        Station.StationType stationType = stationTypeCmb.getValue();
        stationPriceLbl.setVisible(stationType != null);

        if (stationType != null) {
            stationPriceLbl.setText("Station price: " + stationType.getPrice() + "€");
            orientationCmb.setVisible(stationType.getSize() % 2 == 0);
            orientationLbl.setVisible(stationType.getSize() % 2 == 0);
        }
    }

    /**
     * Handles the station addition process.
     * Validates inputs and creates a new station if all conditions are met.
     */
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
            System.out.println(mapID);
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

        int positionX = (int) XPosSli.getValue() - 1;
        int positionY = (int) YPosSli.getValue() - 1;
        Station.StationType stationType = stationTypeCmb.getValue();
        Station.Orientation stationOrientation = orientationCmb.getValue();

        if (stationOrientation == null)
            stationOrientation = Station.Orientation.values()[0];

        String stationName = generateStationName(map.getName(), positionX, positionY, stationType.getDisplayName());
        Station newStation = new Station(map.getName(), mapID, positionX, positionY, stationType, stationOrientation, stationName, scenario.getScenarioName());
        newStation.setOwnerPlayer(UserSession.getInstance().getUser().getEmail());

        if (!newStation.validatePosition(map, stationType.getSize() / 2)) {
            errorAlert.setTitle("Position not valid");
            errorAlert.setContentText("The station's range is out of bounds, enter a different position!");
            errorAlert.showAndWait();
            return;
        }

        if (!newStation.checkOverbuilding()) {
            errorAlert.setTitle("Station overbuilding");
            errorAlert.setContentText("The select position is already occupied by another station/industry or city!");
            errorAlert.showAndWait();
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

    /**
     * Handles navigation back to the main menu.
     * Closes the current window and opens the player menu.
     */
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
     * Adds a station to the specified map in the given scenario.
     * @param map The map where the station will be added
     * @param scenario The scenario in which the budget will be subtracted from
     * @param positionX The X position of the station
     * @param positionY The Y position of the station
     * @param stationType The type of station to be added
     * @param rotation The orientation of the station
     * @return A message indicating the result of the operation
     */
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

    /**
     * Generates a unique name for a new station based on its properties.
     * @param mapName The name of the map
     * @param posX The X position of the station
     * @param posY The Y position of the station
     * @param type The type of the station
     * @return A generated station name
     */
    public String generateStationName(String mapName, int posX, int posY, String type) {
        List<City> cities = Repositories.getInstance().getCityRepository().getCitiesByMap(mapName);
        String stationName = type;

        if (!cities.isEmpty()) {
            double smallestDistance = Math.sqrt(Math.pow(cities.get(0).getCenterX() - posX, 2) + Math.pow(cities.get(0).getCenterY() - posY, 2));
            City closestCity = cities.get(0);

            for (City city : cities) {
                int cityX = city.getCenterX();
                int cityY = city.getCenterX();
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
}
