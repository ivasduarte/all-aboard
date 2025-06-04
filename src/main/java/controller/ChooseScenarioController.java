package controller;

import domain.*;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;

import presentation.*;

import repositories.Repositories;
import repositories.ScenarioRepository;

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
    private VBox menuBox;

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

        menuBox.getChildren().clear();

        addMenuButton("Build a Station", new AddStationUI(currentMap, currentScenario));
        addMenuButton("Upgrade a Station", new UpgradeAStationUI(currentMap, currentScenario));
        addMenuButton("Upgrade Type of Station", new ChangeStationTypeUI(currentMap, currentScenario));
        addMenuButton("List All Stations", new ListStationsUI(currentMap));
        addMenuButton("Buy a Locomotive", new BuyLocomotiveUI(currentMap, currentScenario));
        addMenuButton("Assign a Train to a Route", new AssignRouteUI(currentMap, currentScenario));
        addMenuButton("Play Simulation", new SimulationUI(currentMap, currentScenario));
        addMenuButton("Check Financial Results", new ShowFinancialResultsUI(currentScenario));
        addMenuButton("Load Simulation", new SimulationLoad(currentMap.getName() + ".simu"));
    }

    private void addMenuButton(String label, Runnable action) {
        Button btn = new Button(label);
        btn.setMaxWidth(Double.MAX_VALUE);
        btn.setOnAction(e -> action.run());
        menuBox.getChildren().add(btn);
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
    }
}