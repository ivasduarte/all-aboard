package controller;

import domain.Map;
import domain.Scenario;
import domain.SimulationLoad;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import presentation.*;

public class PlayerMenuController {
    @FXML
    private VBox menuBox;

    @FXML
    private Label messageLabel;

    private Scenario currentScenario ;
    private Map currentMap;


    public void setData(Scenario currentScenario){
        this.currentScenario = currentScenario;
        this.currentMap = currentScenario.getMap();
        initialize();
    }


    private void initialize() {
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

}
