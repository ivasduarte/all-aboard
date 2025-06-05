package controller;

import domain.*;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;

import javafx.stage.Stage;
import presentation.*;

import repositories.Repositories;
import repositories.ScenarioRepository;

import java.io.IOException;
import java.net.URL;
import java.util.List;

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

    @FXML
    public void initialize() {
        ScenarioRepository scenarioRepo = Repositories.getInstance().getScenarioRepository();
        List<Scenario> scenarios = scenarioRepo.getScenarioList();

        if (scenarios == null || scenarios.isEmpty()) {
            showAlert("No scenarios available!");
            return;
        }

        scenarioComboBox.getItems().addAll(scenarios);
        scenarioComboBox.setOnAction(e -> updateScenarioInfo());
    }

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

    private void showAlert(String msg) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setContentText(msg);
        alert.showAndWait();
    }

    @FXML
    private void handleContinue() {
        if (currentScenario == null) {
            showAlert("Please select a scenario before continuing.");
            return;
        }

        try {
            URL fxmlURL = getClass().getResource("/JavaFX/playerMenu.fxml");
            System.out.println("FXML URL: " + fxmlURL);

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