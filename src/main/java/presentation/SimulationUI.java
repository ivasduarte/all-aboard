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
 * User interface for managing the simulation of a railway system.
 * Implements Runnable and Serializable to support concurrent execution and object persistence.
 * 
 * This class provides a graphical interface for:
 * - Controlling simulation execution (start, pause, resume, stop)
 * - Displaying real-time simulation statistics
 * - Showing financial results and budget updates
 * - Logging simulation events and activities
 * - Managing train operations and cargo movements
 * - Handling industry operations and cargo generation
 * 
 * The UI is built using JavaFX and provides interactive controls for users
 * to monitor and control the simulation process.
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
     * Initializes the controller and sets up the UI for simulation management.
     *
     * @param map The map to simulate, containing stations, industries, and routes
     * @param scenario The scenario to simulate, containing simulation parameters and constraints
     * @throws IllegalArgumentException if map or scenario is null
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
     * 
     * This method:
     * - Initializes the JavaFX stage and scene
     * - Loads the FXML interface
     * - Sets up UI components and event handlers
     * - Starts the simulation in a separate thread
     * - Displays initial simulation information
     * 
     * @throws IllegalStateException if required components fail to initialize
     * @throws IOException if there are issues loading the FXML file
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

    /**
     * Loads the FXML interface and initializes all UI components.
     * Sets up the scene, labels, and buttons for the simulation interface.
     * 
     * This method:
     * - Creates and configures the FXMLLoader
     * - Loads the FXML file
     * - Initializes all UI components
     * - Sets up event handlers
     * - Configures the stage and scene
     * - Displays initial simulation information
     *
     * @throws IOException if there are issues loading the FXML file
     * @throws IllegalStateException if UI components fail to initialize
     */
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

    /**
     * Handles the resume button click event.
     * Resumes the simulation and updates the UI status.
     * 
     * This method:
     * - Resumes the simulation through the controller
     * - Updates the UI status to "RUNNING"
     * - Logs the resume action
     * - Updates button states if necessary
     */
    @FXML
    private void handleResume() {
        controller.resumeSimulation();
        updateStatus("RUNNING");
        appendLog("Simulation resumed.");
    }

    /**
     * Handles the pause button click event.
     * Pauses the simulation and updates the UI status.
     * 
     * This method:
     * - Pauses the simulation through the controller
     * - Updates the UI status to "PAUSED"
     * - Logs the pause action
     * - Updates button states if necessary
     */
    @FXML
    private void handlePause() {
        controller.pauseSimulation();
        updateStatus("PAUSED");
        appendLog("Simulation paused. You can now navigate the menu.");
    }

    /**
     * Handles the stop button click event.
     * Stops the simulation, saves the current state, and closes the window.
     * 
     * This method:
     * - Stops the simulation through the controller
     * - Saves the current simulation state
     * - Updates the UI status to "STOPPED"
     * - Logs the stop action
     * - Closes the simulation window
     */
    @FXML
    private void handleStop() {
        controller.stopSimulation();
        saveSimulation();
        updateStatus("STOPPED");
        appendLog("Simulation stopped and saved.");
        primaryStage.close();
    }

    /**
     * Displays the simulation header with scenario information and date range.
     * 
     * This method:
     * - Shows the scenario name and date range
     * - Displays the current budget
     * - Shows historical events that will affect the simulation
     * - Formats the information for clear presentation
     */
    private void displaySimulationHeader() {
        appendLog("-------------------------------------------------------------------------");
        appendLog("\nSIMULATION STARTING IN SCENARIO " + selectedScenario.getScenarioName() + 
                 ", FROM " + selectedScenario.getRestrictions().getStartDate() + 
                 " TO " + selectedScenario.getRestrictions().getEndDate());
        appendLog("\nYOUR CURRENT BUDGET IS " + selectedScenario.getBudget().getBudget() + "€.");
        displayHistoricalEvents();
    }

    /**
     * Displays the historical events that will affect the simulation.
     * 
     * This method:
     * - Retrieves the list of historical events
     * - Filters out "none" events
     * - Formats and displays each event
     * - Provides context for how events will affect the simulation
     */
    private void displayHistoricalEvents() {
        List<String> historicalEvents = selectedScenario.getRestrictions().getHistoricalEvents();
        appendLog("\nYOUR SIMULATION WILL BE CONTINUED WITH THE FOLLOWING EVENTS: ");
        for (String event : historicalEvents) {
            if (!event.equalsIgnoreCase("none")) {
                appendLog(event);
            }
        }
    }

    /**
     * Updates the simulation statistics display with current year and earnings.
     * 
     * This method:
     * - Updates the current year display
     * - Shows total earnings for the period
     * - Updates the budget display
     * - Formats the information for clear presentation
     *
     * @param currentYear The current year of the simulation
     * @param totalEarnings The total earnings for the current period
     */
    public void updateSimulationStats(int currentYear, int totalEarnings) {
        Platform.runLater(() -> {
            yearLabel.setText("Current Year: " + currentYear);
            budgetLabel.setText("Budget: " + selectedScenario.getBudget().getBudget() + "€");
            
            appendLog("\n------------STATS OF YEAR " + currentYear + "------------");
            appendLog("\nYOU'VE EARNED A TOTAL OF " + totalEarnings + "€.");
            appendLog("YOUR BUDGET IS NOW " + selectedScenario.getBudget().getBudget() + "€.");
        });
    }

    /**
     * Updates the industry statistics display with cargo generation information.
     * 
     * This method:
     * - Shows the industry type and location
     * - Displays the amount of cargo generated
     * - Formats the information for clear presentation
     *
     * @param industry The industry being updated
     * @param cargo The amount of cargo generated
     */
    public void updateIndustryStats(Industry industry, int cargo) {
        Platform.runLater(() -> {
            appendLog("\n------------INDUSTRY STATS------------");
            appendLog("\nINDUSTRY '" + industry.getIndustryType() + "', LOCATED AT (" + 
                     industry.getPositionX() + "," + industry.getPositionY() +
                     ") HAS GENERATED " + cargo + " CARGO.");
        });
    }

    /**
     * Updates the train statistics display with financial information.
     * 
     * This method:
     * - Shows total earnings from trains
     * - Displays total losses
     * - Calculates and shows the net balance
     * - Formats the information for clear presentation
     *
     * @param totalEarned Total amount earned from trains
     * @param totalLost Total amount lost from trains
     * @param net Current net balance
     */
    public void updateTrainStats(int totalEarned, int totalLost, int net) {
        Platform.runLater(() -> {
            appendLog("\n------------TRAIN STATS------------");
            appendLog("\nYOU'VE GAINED A TOTAL OF " + totalEarned + "€ FROM TRAINS.");
            appendLog("HOWEVER, YOU'VE LOST A TOTAL OF " + totalLost + "€.");
            appendLog("YOUR CURRENT BALANCE IS " + net + "€.");
        });
    }

    /**
     * Updates the train loading information display.
     * 
     * This method:
     * - Shows the locomotive model and cargo mode
     * - Displays the station name
     * - Shows available cargo and current load
     * - Formats the information for clear presentation
     *
     * @param locomotiveModel The model of the locomotive
     * @param cargoMode The type of cargo being loaded
     * @param stationName The name of the station
     * @param availableCargo The amount of cargo available
     * @param currentCargoLoad The current cargo load on the train
     */
    public void updateTrainLoading(String locomotiveModel, String cargoMode, String stationName, 
                                 int availableCargo, int currentCargoLoad) {
        Platform.runLater(() -> {
            appendLog("\n[LOADING] Train '" + locomotiveModel +
                     "' waiting for cargo mode '" + cargoMode +
                     "' at station '" + stationName + "'.\nAvailable cargo: " + availableCargo +
                     "\nCurrent cargo on the train: " + currentCargoLoad);
        });
    }

    /**
     * Updates the status label with the current simulation status.
     * 
     * This method:
     * - Updates the status label text
     * - Ensures thread safety using Platform.runLater
     * - Maintains UI responsiveness
     *
     * @param status The new status to display
     */
    private void updateStatus(String status) {
        Platform.runLater(() -> statusLabel.setText("Status: " + status));
    }

    /**
     * Appends a message to the log area.
     * Ensures thread safety by using Platform.runLater.
     * 
     * This method:
     * - Adds the message to the log area
     * - Scrolls to the bottom of the log
     * - Handles null log area gracefully
     * - Ensures thread safety for UI updates
     *
     * @param message The message to append to the log
     */
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

    /**
     * Displays an error message in a dialog box and logs it.
     * 
     * This method:
     * - Shows an error dialog with the message
     * - Logs the error to the log area
     * - Handles cases where the log area is not initialized
     * - Ensures thread safety for UI updates
     *
     * @param message The error message to display
     */
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
     * 
     * This method:
     * - Creates a SimulationSave instance
     * - Sets up the UI reference
     * - Starts the save operation in a new thread
     * - Logs the save attempt
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
     * This method:
     * - Creates a SimulationLoad instance
     * - Sets up the UI reference
     * - Attempts to load the simulation
     * - Logs the result of the load attempt
     *
     * @return true if the simulation was successfully loaded, false otherwise
     * @throws Exception if there are issues during the load process
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