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
import repositories.LocomotiveRepository;
import repositories.Repositories;
import repositories.ScenarioRepository;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class BuyLocomotiveController {
    private Scenario scenario;
    private LocomotiveRepository locomotiveRepository;
    private Map currentMap;

    @FXML
    private ComboBox<Locomotive> locomotiveComboBox;
    @FXML
    private Label modelLabel;
    @FXML
    private Label typeLabel;
    @FXML
    private Label topSpeedLabel;
    @FXML
    private Label priceLabel;
    @FXML
    private Label budget;

    private Locomotive selectedLocomotive;

    public BuyLocomotiveController() {
    }

    public BuyLocomotiveController(Scenario scenario) {
        this.scenario = scenario;
    }

    public void setData(Map currentMap, Scenario scenario) {
        this.currentMap = currentMap;
        this.scenario = scenario;

        initializeLocomotives();
    }

    @FXML
    public void initialize() {
    }

    private void initializeLocomotives() {
        if (scenario == null) {
            showAlert("No scenario data available!");
            return;
        }

        List<Scenario> scenarios = Repositories.getInstance().getScenarioRepository().getScenarioList();
        int scenarioId = scenarios.indexOf(scenario);

        if (scenarioId == -1) {
            showAlert("Scenario not found in repository!");
            return;
        }

        viewBudget();

        LocomotiveRepository locoRepo = getLocomotiveRepository();
        List<Locomotive> availableLocos = locoRepo.getAvailableLocomotives(scenarioId);

        if (availableLocos == null || availableLocos.isEmpty()) {
            showAlert("No locomotives available for this scenario!");
            return;
        }

        locomotiveComboBox.getItems().clear();
        locomotiveComboBox.getItems().addAll(availableLocos);
        locomotiveComboBox.setOnAction(e -> updateLocomotiveInfo());
    }

    /**
     * This method will be responsible to add the bought locomotive to the repository.
     *
     * @param locomotiveModel locomotive model
     * @param mapName         the name of the map the player is playing
     * @return Will show a success message if it is possible to buy the locomotive. Otherwise, it will show a message related to the error that occurred.
     */
    public String buyLocomotive(String locomotiveModel, String mapName) {
        LocomotiveRepository locomotiveRepository = Repositories.getInstance().getLocomotiveRepository();

        if (scenario == null) {
            return "ERROR: No scenario found for the selected map.";
        }

        Locomotive locomotive = locomotiveRepository.getLocomotiveByModel(locomotiveModel);

        if (locomotive == null) {
            return "ERROR: Locomotive model not found.";
        }

        if (scenario.getBudget().getBudget() >= locomotive.getPrice()) {
            scenario.subtractBudget(locomotive.getPrice());
            locomotiveRepository.save(locomotive);
            return "Locomotive " + locomotiveModel + " bought successfully.";
        } else {
            return "ERROR: Player doesn't have enough budget.";
        }
    }

    /**
     * This method allows the controller to get the locomotive repository.
     *
     * @return locomotive repository
     */
    public LocomotiveRepository getLocomotiveRepository() {
        if (locomotiveRepository == null) {
            Repositories repositories = Repositories.getInstance();
            locomotiveRepository = repositories.getLocomotiveRepository();
        }
        return locomotiveRepository;
    }

    public boolean isModelValid(String locomotiveModel, List<Locomotive> availableLocomotives) {
        if (availableLocomotives == null || availableLocomotives.isEmpty()) {
            return false;
        }

        for (Locomotive loco : availableLocomotives) {
            if (loco.getLocomotiveModel().equalsIgnoreCase(locomotiveModel)) {
                return true;
            }
        }
        return false;
    }

    private void showAlert(String msg) {
        if (locomotiveComboBox != null) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setContentText(msg);
            alert.showAndWait();
        } else {
            System.out.println("ERROR: " + msg);
        }
    }

    private void viewBudget() {
        budget.setText("Player Budget: " + scenario.getBudget() + " €");
    }

    @FXML
    private void updateLocomotiveInfo() {
        selectedLocomotive = locomotiveComboBox.getValue();

        if (selectedLocomotive == null) {
            return;
        }

        modelLabel.setText("Locomotive Model: " + selectedLocomotive.getLocomotiveModel());
        typeLabel.setText("Type: " + selectedLocomotive.getLocomotiveFuelType());
        topSpeedLabel.setText("Top Speed: " + selectedLocomotive.getMaxSpeed() + "km/h");
        priceLabel.setText("PRICE: " + selectedLocomotive.getPrice() + "€");
    }

    @FXML
    public void handleBuy() {
        if (selectedLocomotive == null) {
            showAlert("Please select a locomotive before continuing.");
            return;
        }

        String result = buyLocomotive(selectedLocomotive.getLocomotiveModel(), currentMap.getName());

        if (result.startsWith("ERROR:")) {
            showAlert(result);
            return;
        }

        Alert successAlert = new Alert(Alert.AlertType.INFORMATION);
        successAlert.setTitle("Success");
        successAlert.setContentText(result);
        successAlert.showAndWait();

        navigateToPlayerMenu();
    }

    private void navigateToPlayerMenu() {
        try {
            URL fxmlURL = getClass().getResource("/JavaFX/playerMenu.fxml");
            System.out.println("FXML URL: " + fxmlURL);

            if (fxmlURL == null) {
                showAlert("FXML URL not found.");
                return;
            }

            FXMLLoader loader = new FXMLLoader(fxmlURL);
            Parent root = loader.load();
            PlayerMenuController controller = loader.getController();

            controller.setData(scenario);

            Stage stage = new Stage();
            stage.setTitle("Player Menu");
            stage.setScene(new Scene(root));
            stage.show();

            Stage currentStage = (Stage) locomotiveComboBox.getScene().getWindow();
            currentStage.close();

        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Navigation error: " + e.getMessage());
        }
    }
}