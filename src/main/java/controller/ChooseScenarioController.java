package controller;

import domain.*;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import presentation.LoadScenarioUI;
import repositories.Repositories;
import repositories.ScenarioRepository;

import java.io.IOException;
import java.net.URL;
import java.util.List;

/**
 * Controller class responsible for managing scenario selection in the railway system.
 * Handles the user interface for choosing and loading scenarios.
 */
public class ChooseScenarioController {
    @FXML
    private ComboBox<Scenario> scenarioComboBox;
    @FXML
    private Label mapLabel;
    @FXML
    private Label budgetLabel;
    @FXML
    private Label startLabel;
    @FXML
    private Label endLabel;
    @FXML
    private Label messageLabel;

    private Scenario currentScenario;

    /**
     * Initializes the controller and sets up event listeners.
     * Updates the scenario list and listens for scenario loading events.
     */
    @FXML
    public void initialize() {
        updateScenarioList();

        // Adiciona um listener para a propriedade scenarioLoaded
        LoadScenarioUI.scenarioLoaded.addListener((observable, oldValue, newValue) -> {
            if (newValue) {
                updateScenarioList();
                // Reseta a propriedade para false
                LoadScenarioUI.scenarioLoaded.set(false);
            }
        });
    }

    /**
     * Updates the list of available scenarios in the combo box.
     * Retrieves scenarios from the repository and populates the UI.
     */
    private void updateScenarioList() {
        ScenarioRepository scenarioRepo = Repositories.getInstance().getScenarioRepository();
        List<Scenario> scenarios = scenarioRepo.getScenarioList();

        if (scenarios == null || scenarios.isEmpty()) {
            showAlert("No scenarios available!");
            return;
        }

        scenarioComboBox.getItems().clear();
        scenarioComboBox.getItems().addAll(scenarios);
        scenarioComboBox.setOnAction(e -> updateScenarioInfo());
    }

    /**
     * Updates the displayed information about the selected scenario.
     * Shows map name, budget, and date range information.
     */
    @FXML
    private void updateScenarioInfo() {
        currentScenario = scenarioComboBox.getValue();
        if (currentScenario == null) return;

        Map currentMap = currentScenario.getMap();
        Budget budget = currentScenario.getBudget();

        mapLabel.setText("Map: " + currentMap.getName());
        startLabel.setText("Start Date: " + currentScenario.getRestrictions().getStartDate());
        endLabel.setText("End Date: " + currentScenario.getRestrictions().getEndDate());
        budgetLabel.setText("Budget: " + budget.getBudget() + "€");
    }

    /**
     * Displays an error alert with the specified message.
     * @param msg The error message to display
     */
    private void showAlert(String msg) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setContentText(msg);
        alert.showAndWait();
    }

    /**
     * Refreshes the list of available scenarios in the combo box.
     * Can be called from other classes when a new scenario is loaded.
     */
    public void refreshScenarioList() {
        updateScenarioList();
    }

    /**
     * Handles the continue action when a scenario is selected.
     * Opens the player menu for the selected scenario.
     */
    @FXML
    private void handleContinue() {
        if (currentScenario == null) {
            showAlert("Please select a scenario before continuing.");
            return;
        }

        try {
            URL fxmlURL = getClass().getResource("/JavaFX/playerMenu.fxml");
            if (fxmlURL == null) {
                messageLabel.setText("Error: FXML URL not found.");
                return;
            }

            FXMLLoader loader = new FXMLLoader(fxmlURL);
            Parent root = loader.load();
            PlayerMenuController controller = loader.getController();

            controller.setData(currentScenario);

            Stage stage = new Stage();
            stage.setTitle("Player Menu.");
            stage.setScene(new Scene(root));
            stage.show();

            Stage currentStage = (Stage) scenarioComboBox.getScene().getWindow();
            currentStage.close();

        } catch (IOException e) {
            e.printStackTrace();
            messageLabel.setText("Error: " + e.getMessage());
        }
    }
}