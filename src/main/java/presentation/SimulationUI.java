package presentation;

import controller.SimulatorController;
import domain.*;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import repositories.Repositories;

import java.io.IOException;
import java.io.Serializable;
import java.util.List;

/**
 * User interface for managing the simulation.
 * Implements Runnable and Serializable to support concurrent execution and object persistence.
 */
public class SimulationUI implements Runnable, Serializable {

    private final SimulatorController controller;
    private final Map selectedMap;
    private final Scenario selectedScenario;
    private Stage primaryStage;

    @FXML private Label statusLabel;
    @FXML private Button resumeButton;
    @FXML private Button pauseButton;
    @FXML private Button stopButton;
    @FXML private TextArea logArea;
    @FXML private Label mapLabel;
    @FXML private Label scenarioLabel;
    @FXML private Label yearLabel;
    @FXML private Label budgetLabel;

    private static final String DEFAULT_SIMULATION_FILE = "simulations.sim";

    /**
     * Creates a new SimulationUI instance with the specified map and scenario.
     *
     * @param map The map to simulate
     * @param scenario The scenario to simulate
     */
    public SimulationUI(Map map, Scenario scenario) {
        this.selectedMap = map;
        this.selectedScenario = scenario;
        this.controller = new SimulatorController(map, scenario);
        this.controller.setUI(this);
        Repositories.getInstance().getSimRep().setUI(this);
    }

    /**
     * Implementation of the Runnable interface.
     * Manages the simulation lifecycle including starting, pausing, resuming, and stopping.
     * Provides a graphical interface for controlling the simulation.
     */
    @Override
    public void run() {
        Platform.runLater(() -> {
            try {
                primaryStage = new Stage();
                primaryStage.setTitle("Simulation");

                if (selectedMap == null || selectedScenario == null) {
                    showError("Map or scenario not selected.");
                    return;
                }

                // Primeiro carregamos o FXML para inicializar a área de log
                loadFXML();
                
                // Depois configuramos a UI em todas as classes necessárias
                Repositories.getInstance().getSimRep().setUI(this);
                
                // Agora podemos carregar a simulação
                loadSimulation();

                Simulator simulator = controller.startOrResumeSimulation();
                new Thread(controller).start();

                displaySimulationHeader();
            } catch (Exception e) {
                showError("Error starting simulation: " + e.getMessage());
            }
        });
    }

    private void loadFXML() throws IOException {
        try {
            // Create a new FXMLLoader
            FXMLLoader loader = new FXMLLoader();
            
            // Set the location of the FXML file
            loader.setLocation(getClass().getResource("/JavaFX/SimulationUI.fxml"));
            
            // Set this instance as the controller
            loader.setController(this);
            
            // Load the FXML
            Parent root = loader.load();
            
            // Create and set the scene
            Scene scene = new Scene(root);
            primaryStage.setScene(scene);
            
            // Verify that all components were initialized
            if (logArea == null || mapLabel == null || scenarioLabel == null || 
                yearLabel == null || budgetLabel == null || statusLabel == null ||
                resumeButton == null || pauseButton == null || stopButton == null) {
                throw new IllegalStateException("FXML components not properly initialized");
            }
            
            // Initialize labels
            mapLabel.setText("Map: " + selectedMap.getName());
            scenarioLabel.setText("Scenario: " + selectedScenario.getScenarioName());
            yearLabel.setText("Current Year: " + selectedScenario.getCurrentYear());
            budgetLabel.setText("Budget: " + selectedScenario.getBudget().getBudget() + "€");
            
            // Show the stage
            primaryStage.show();
            
            // Update status and log
            updateStatus("RUNNING");
            appendLog("Simulation started successfully.");
            
        } catch (Exception e) {
            System.err.println("Error loading FXML: " + e.getMessage());
            e.printStackTrace();
            throw new IOException("Failed to load interface: " + e.getMessage(), e);
        }
    }

    @FXML
    private void handleResume() {
        controller.resumeSimulation();
        updateStatus("RUNNING");
        appendLog("Simulation resumed.");
    }

    @FXML
    private void handlePause() {
        controller.pauseSimulation();
        updateStatus("PAUSED");
        appendLog("Simulation paused. You can now navigate the menu.");
    }

    @FXML
    private void handleStop() {
        controller.stopSimulation();
        saveSimulation();
        updateStatus("STOPPED");
        appendLog("Simulation stopped and saved.");
        primaryStage.close();
    }

    private void displaySimulationHeader() {
        appendLog("-------------------------------------------------------------------------");
        appendLog("\nSIMULATION STARTING IN SCENARIO " + selectedScenario.getScenarioName() + 
                 ", FROM " + selectedScenario.getRestrictions().getStartDate() + 
                 " TO " + selectedScenario.getRestrictions().getEndDate());
        appendLog("\nYOUR CURRENT BUDGET IS " + selectedScenario.getBudget().getBudget() + "€.");
        displayHistoricalEvents();
    }

    private void displayHistoricalEvents() {
        List<String> historicalEvents = selectedScenario.getRestrictions().getHistoricalEvents();
        appendLog("\nYOUR SIMULATION WILL BE CONTINUED WITH THE FOLLOWING EVENTS: ");
        for (String event : historicalEvents) {
            if (!event.equalsIgnoreCase("none")) {
                appendLog(event);
            }
        }
    }

    public void updateSimulationStats(int currentYear, int totalEarnings) {
        Platform.runLater(() -> {
            yearLabel.setText("Current Year: " + currentYear);
            budgetLabel.setText("Budget: " + selectedScenario.getBudget().getBudget() + "€");
            
            appendLog("\n------------STATS OF YEAR " + currentYear + "------------");
            appendLog("\nYOU'VE EARNED A TOTAL OF " + totalEarnings + "€.");
            appendLog("YOUR BUDGET IS NOW " + selectedScenario.getBudget().getBudget() + "€.");
        });
    }

    public void updateIndustryStats(Industry industry, int cargo) {
        Platform.runLater(() -> {
            appendLog("\n------------INDUSTRY STATS------------");
            appendLog("\nINDUSTRY '" + industry.getIndustryType() + "', LOCATED AT (" + 
                     industry.getPositionX() + "," + industry.getPositionY() +
                     ") HAS GENERATED " + cargo + " CARGO.");
        });
    }

    public void updateTrainStats(int totalEarned, int totalLost, int net) {
        Platform.runLater(() -> {
            appendLog("\n------------TRAIN STATS------------");
            appendLog("\nYOU'VE GAINED A TOTAL OF " + totalEarned + "€ FROM TRAINS.");
            appendLog("HOWEVER, YOU'VE LOST A TOTAL OF " + totalLost + "€.");
            appendLog("YOUR CURRENT BALANCE IS " + net + "€.");
        });
    }

    public void updateTrainLoading(String locomotiveModel, String cargoMode, String stationName, 
                                 int availableCargo, int currentCargoLoad) {
        Platform.runLater(() -> {
            appendLog("\n[LOADING] Train '" + locomotiveModel +
                     "' waiting for cargo mode '" + cargoMode +
                     "' at station '" + stationName + "'.\nAvailable cargo: " + availableCargo +
                     "\nCurrent cargo on the train: " + currentCargoLoad);
        });
    }

    private void updateStatus(String status) {
        Platform.runLater(() -> statusLabel.setText("Status: " + status));
    }

    public void appendLog(String message) {
        if (logArea == null) {
            System.err.println("Log area not initialized: " + message);
            return;
        }
        Platform.runLater(() -> {
            try {
                logArea.appendText(message + "\n");
                logArea.setScrollTop(Double.MAX_VALUE);
            } catch (Exception e) {
                System.err.println("Error appending to log: " + e.getMessage());
            }
        });
    }

    private void showError(String message) {
        Platform.runLater(() -> {
            try {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText(null);
                alert.setContentText(message);
                alert.showAndWait();
                
                if (logArea != null) {
                    appendLog("ERROR: " + message);
                } else {
                    System.err.println("ERROR: " + message);
                }
            } catch (Exception e) {
                System.err.println("Error showing error dialog: " + e.getMessage());
            }
        });
    }

    /**
     * Saves the current simulation state to a file.
     * Uses a separate thread to perform the save operation.
     */
    private void saveSimulation() {
        SimulationSave saver = new SimulationSave(DEFAULT_SIMULATION_FILE, 
            Repositories.getInstance().getSimRep(), 
            Repositories.getInstance().getStationRepository(), 
            Repositories.getInstance().getBuildingRepository());
        saver.setUI(this);
        new Thread(saver).start();
        appendLog("Attempting to save simulation...");
    }

    /**
     * Attempts to load a previous simulation state from a file.
     *
     * @return true if the simulation was successfully loaded, false otherwise
     */
    private boolean loadSimulation() {
        try {
            SimulationLoad loader = new SimulationLoad(DEFAULT_SIMULATION_FILE);
            loader.setUI(this);
            boolean success = loader.load(
                Repositories.getInstance().getSimRep(),
                Repositories.getInstance().getStationRepository(),
                Repositories.getInstance().getBuildingRepository()
            );
            if (success) {
                appendLog("Previous simulation loaded successfully.");
            } else {
                appendLog("No previous simulation found or failed to load.");
            }
            return success;
        } catch (Exception e) {
            appendLog("Error loading simulation: " + e.getMessage());
            return false;
        }
    }
}