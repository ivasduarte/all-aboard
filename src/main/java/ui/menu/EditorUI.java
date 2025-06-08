package ui.menu;

import domain.MapLoad;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import presentation.*;
import ui.utils.Utils;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Represents the Editor User Interface, implementing a menu-driven interface
 * to facilitate user interaction with various editor functionalities.
 * <p>
 * The EditorUI class provides functionalities for creating maps, loading maps,
 * selecting maps, and signing off from the editor. It integrates with a JavaFX
 * application for graphical interface elements and handles user interactions
 * in a loop until an exit is requested.
 */
public class EditorUI implements Runnable {
    private boolean exitRequested = false;

    /**
     * Initializes a new instance of the EditorUI class.
     * <p>
     * This constructor establishes the foundation for the editor's
     * operational capabilities without requiring external inputs or parameters.
     */
    public EditorUI() {
    }

    /**
     * Executes the main loop of the Editor UI.
     * <p>
     * The method displays these options to the user in a menu format,
     * allowing the user to interact by selecting an option to execute.
     */
    public void run() {
        List<MenuItem> options = new ArrayList<MenuItem>();
        options.add(new MenuItem("Create Map", new CreateMapUI()));
        options.add(new MenuItem("Choose Map", new ChooseMapUI()));
        options.add(new MenuItem("Load Map from File", new MapLoad()));
        options.add(new MenuItem("Sign Off", () -> handleSignOff()));

        int option = 0;
        do {
            option = Utils.showAndSelectIndex(options, "\n\n------------------------- EDITOR MENU -------------------------");

            if ((option >= 0) && (option < options.size())) {
                options.get(option).run();
                if (exitRequested) {
                    option = -1;
                }
            }
        } while (option != -1);
    }

    /**
     * Handles the sign-off action.
     * Closes the current console and opens the login window.
     */
    private void handleSignOff() {
        exitRequested = true;

        try {
            Platform.setImplicitExit(false);

            new Thread(() -> {
                try {
                    Thread.sleep(100);

                    Platform.runLater(() -> {
                        createLoginWindow();
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }).start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Creates and displays a login window as a new stage in the JavaFX application.
     * <p>
     * If the `login.fxml` file is not found, the method exits without creating the window.
     * Any exceptions that occur during resource loading or window creation are caught
     * and logged to standard error.
     */
    private void createLoginWindow() {
        try {
            URL fxmlURL = getClass().getResource("/JavaFX/login.fxml");
            if (fxmlURL == null) {
                return;
            }

            FXMLLoader loader = new FXMLLoader(fxmlURL);
            Parent root = loader.load();

            Stage loginStage = new Stage();
            loginStage.setTitle("Login");
            loginStage.setScene(new Scene(root));

            loginStage.setResizable(true);
            loginStage.centerOnScreen();

            loginStage.show();
            loginStage.toFront();
            loginStage.requestFocus();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}