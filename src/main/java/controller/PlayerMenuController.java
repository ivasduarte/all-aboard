package controller;

import domain.Map;
import domain.Scenario;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import presentation.*;

import java.io.IOException;
import java.net.URL;

/**
 * The PlayerMenuController class handles the user interface and actions for the player's main menu in the game.
 * It allows the player to perform various actions such as building and upgrading stations, buying locomotives,
 * assigning routes, checking financial results, and managing simulations through different menus and interfaces.
 *
 * The class leverages JavaFX to dynamically manage the UI components and handle the navigation between windows.
 * Each menu item triggers specific actions or opens corresponding user interfaces to perform player actions.
 */
public class PlayerMenuController {
    @FXML
    private VBox menuBox;
    @FXML
    private Label messageLabel;

    private Scenario currentScenario;
    private Map currentMap;

    /**
     * Sets the current scenario for the player menu controller.
     * This method initializes the controller based on the provided scenario
     * by updating the current scenario and associated map and invoking
     * the initialization process for menu buttons and UI elements.
     *
     * @param currentScenario The scenario to be set as the current one. Cannot be null.
     */
    public void setData(Scenario currentScenario) {
        this.currentScenario = currentScenario;
        this.currentMap = currentScenario.getMap();
        initialize();
    }

    /**
     * Initializes the player menu by clearing existing menu items and setting up new menu buttons.
     * Each menu button corresponds to a specific functionality in the application, such as building or upgrading stations,
     * viewing financial results, assigning trains, and running simulations.
     *
     * The method dynamically creates buttons using the {@code addMenuButton} method, associating each button
     * with a label and an action, which could be a runnable implementation or a method reference.
     * The actions linked to the buttons involve user interaction or navigation to different application screens.
     */
    private void initialize() {
        menuBox.getChildren().clear();

        addMenuButton("Build a Station", new AddStationUI(currentMap, currentScenario));
        addMenuButton("Upgrade a Station", () -> handleUpgradeAStation());
        addMenuButton("Upgrade Type of Station", new ChangeStationTypeUI(currentMap, currentScenario));
        addMenuButton("List All Stations", () -> handleListAllStations());
        addMenuButton("Buy a Locomotive", () -> handleBuyLocomotive());
        addMenuButton("Assign a Train to a Route", new AssignRouteUI(currentMap, currentScenario));
        addMenuButton("Check Financial Results", () -> handleShowFinancialResults());
        addMenuButton("Play Simulation", new SimulationUI(currentMap, currentScenario));
    }

    /**
     * Adds a button to the menu with a specified label and associated action.
     * The button is styled to match the menu layout and is set to perform the
     * provided action when clicked.
     *
     * @param label  The text to be displayed on the button.
     *               Cannot be null or empty.
     * @param action The action to be executed when the button is clicked.
     *               Should be a {@link Runnable} implementation that defines
     *               the desired behavior. Cannot be null.
     */
    private void addMenuButton(String label, Runnable action) {
        Button btn = new Button(label);
        btn.setMaxWidth(Double.MAX_VALUE);
        btn.setOnAction(e -> action.run());
        menuBox.getChildren().add(btn);
    }

    /**
     * Handles the action of initiating the "Buy Locomotive" process.
     * This method loads the "Buy Locomotive" screen, initializes it with the current map and scenario data,
     * and displays it in a new window. Upon successfully opening the new window, it closes the current menu window.
     */
    @FXML
    private void handleBuyLocomotive() {
        try {
            URL fxmlURL = getClass().getResource("/JavaFX/buyLocomotive.fxml");
            if (fxmlURL == null) {
                messageLabel.setText("Error: FXML URL not found.");
                return;
            }

            FXMLLoader loader = new FXMLLoader(fxmlURL);
            Parent root = loader.load();
            BuyLocomotiveController controller = loader.getController();
            controller.setData(currentMap, currentScenario);

            Stage stage = new Stage();
            stage.setTitle("Buy Locomotive");
            stage.setScene(new Scene(root));
            stage.show();

            Stage currentStage = (Stage) menuBox.getScene().getWindow();
            currentStage.close();

        } catch (IOException e) {
            e.printStackTrace();
            messageLabel.setText("Error: " + e.getMessage());
        }
    }

    /**
     * Handles the action to display the "Financial Results" screen.
     *
     * This method loads the "Financial Results" interface from the corresponding FXML file
     * and initializes it with the current map and scenario data. Once loaded, it opens the
     * interface in a new window and closes the current menu window. If the FXML file cannot
     * be found or any error occurs during the loading process, the error is logged, and an
     * appropriate message is displayed on the `messageLabel`.
     */
    @FXML
    private void handleShowFinancialResults() {
        try {
            URL fxmlURL = getClass().getResource("/JavaFX/financialResults.fxml");
            if (fxmlURL == null) {
                messageLabel.setText("Error: Financial Results FXML URL not found.");
                return;
            }

            FXMLLoader loader = new FXMLLoader(fxmlURL);
            Parent root = loader.load();
            ShowFinancialResultsController controller = loader.getController();

            controller.setData(currentMap, currentScenario);

            Stage stage = new Stage();
            stage.setTitle("Financial Results");
            stage.setScene(new Scene(root));
            stage.show();

            Stage currentStage = (Stage) menuBox.getScene().getWindow();
            currentStage.close();

        } catch (IOException e) {
            e.printStackTrace();
            messageLabel.setText("Error loading Financial Results: " + e.getMessage());
        }
    }

    /**
     * Handles the action of upgrading a station by opening the "Upgrade a Station" screen.
     *
     * This method loads the "Upgrade a Station" user interface from the corresponding FXML file
     * and initializes it with the current scenario data. It then displays the screen in a new
     * window while closing the current menu window. If any errors occur during the loading or
     * initialization process, appropriate error messages are displayed on the `messageLabel`.
     */
    @FXML
    private void handleUpgradeAStation(){
        try {
            URL fxmlURL = getClass().getResource("/JavaFX/UpgradeAStationWithABuilding.fxml");
            if (fxmlURL == null) {
                messageLabel.setText("Error: FXML URL not found.");
                return;
            }

            FXMLLoader loader = new FXMLLoader(fxmlURL);
            Parent root = loader.load();
            UpgradeAStationController controller = loader.getController();
            controller.setData(currentScenario);

            Stage stage = new Stage();
            stage.setTitle("Upgrade a Station");
            stage.setScene(new Scene(root));
            stage.show();

            Stage currentStage = (Stage) menuBox.getScene().getWindow();
            currentStage.close();
        }
        catch (Exception e) {
            e.printStackTrace();
            messageLabel.setText("Error: " + e.getMessage());
        }
    }

    /**
     * Handles the navigation to the "List All Stations" screen.
     *
     * This method loads the corresponding FXML file for the "List All Stations" interface and initializes
     * the screen with the current scenario data by setting it in the associated controller. Once the screen
     * is loaded, it displays the interface in a new window, closing the current one. If any errors occur
     * during the loading or initialization process, the error is logged, and an appropriate message is displayed
     * in the `messageLabel`.
     */
    @FXML
    private void handleListAllStations(){
        try {
            URL fxmlURL = getClass().getResource("/JavaFX/ListAllStations.fxml");
            if (fxmlURL == null) {
                messageLabel.setText("Error: FXML URL not found.");
                return;
            }

            FXMLLoader loader = new FXMLLoader(fxmlURL);
            Parent root = loader.load();
            ListStationsController controller = loader.getController();
            controller.setData(currentScenario);

            Stage stage = new Stage();
            stage.setTitle("List All Stations");
            stage.setScene(new Scene(root));
            stage.show();

            Stage currentStage = (Stage) menuBox.getScene().getWindow();
            currentStage.close();
        }
        catch (Exception e) {
            e.printStackTrace();
            messageLabel.setText("Error: " + e.getMessage());
        }
    }

    /**
     * Handles the action of signing off from the current session and returning to the login screen.
     *
     * This method attempts to load the login interface from the "login.fxml" file and display
     * it in a new window. If the login FXML file is not found or cannot be loaded, an appropriate
     * error message is displayed on the `messageLabel`. The method also closes the current menu window
     * after displaying the login screen. Any IO-related exceptions encountered during the process are
     * logged, and an error message is shown to the user.
     */
    @FXML
    private void handleSignOffButton() {
        try {
            URL fxmlURL = getClass().getResource("/JavaFX/login.fxml");
            if (fxmlURL == null) {
                messageLabel.setText("Error: Login FXML URL not found.");
                return;
            }

            FXMLLoader loader = new FXMLLoader(fxmlURL);
            Parent root = loader.load();

            Stage stage = new Stage();
            stage.setTitle("Login");
            stage.setScene(new Scene(root));
            stage.show();

            Stage currentStage = (Stage) menuBox.getScene().getWindow();
            currentStage.close();
        } catch (IOException e) {
            e.printStackTrace();
            messageLabel.setText("Error returning to login: " + e.getMessage());
        }
    }
}