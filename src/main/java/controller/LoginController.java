package controller;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import ui.authorization.AuthenticationController;
import ui.menu.EditorUI;

import java.io.IOException;
import java.net.URL;

/**
 * The LoginController class manages user interactions for the login screen
 * in a JavaFX application. It provides methods for handling login attempts,
 * navigating to the sign-up screen, and interacting with other components
 * based on user roles.
 */
public class LoginController {
    @FXML
    private TextField emailField;
    @FXML
    private PasswordField passwordField;
    @FXML
    private Label messageLabel;

    private final AuthenticationController authController = new AuthenticationController();

    /**
     * Handles the login button click event by validating the input credentials and attempting to authenticate
     * the user. Displays appropriate feedback messages for invalid inputs, failed login attempts,
     * or successful authentication.
     */
    @FXML
    private void handleLoginButton() {
        String email = emailField.getText();
        String password = passwordField.getText();

        if (email == null || email.isEmpty()) {
            messageLabel.setText("Insert a valid email address.");
            return;
        }

        if (password == null || password.isEmpty()) {
            messageLabel.setText("Insert a valid password.");
            return;
        }

        boolean success = authController.doLogin(email, password);

        if (success) {
            messageLabel.setText("Logged in successfully.");
            if (isCurrentUserEditor()) {
                startEditorConsole();
            } else {
                openPlayerInterface();
            }
        } else {
            messageLabel.setText("Invalid credentials.");
        }
    }

    /**
     * Checks whether the current user is assigned the role of "EDITOR".
     * This method retrieves the email of the currently authenticated user
     * and determines their role by querying the authentication controller.
     *
     * @return true if the currently authenticated user has the role "EDITOR",
     *         false otherwise or if no user is authenticated.
     */
    private boolean isCurrentUserEditor() {
        String currentUserEmail = authController.getCurrentUserEmail();
        if (currentUserEmail != null) {
            String role = authController.getRole(currentUserEmail);
            return AuthenticationController.ROLE_EDITOR.equals(role);
        }
        return false;
    }

    /**
     * Initializes and starts the editor console interface within a separate thread.
     * This method is invoked to display the editor functionality to the user after
     * closing the current login window.
     */
    private void startEditorConsole() {
        Platform.setImplicitExit(false);
        Stage currentStage = (Stage) emailField.getScene().getWindow();
        currentStage.close();

        new Thread(() -> {
            try {
                EditorUI editorUI = new EditorUI();
                editorUI.run();
            } catch (Exception e) {
                System.err.println("Error starting editor: " + e.getMessage());
                e.printStackTrace();
            }
        }).start();
    }

    /**
     * Opens the player interface by loading the "chooseScenario.fxml" file
     * and displaying it in a new stage. Closes the current login window
     * after successfully displaying the new interface.
     */
    private void openPlayerInterface() {
        try {
            URL fxmlURL = getClass().getResource("/JavaFX/chooseScenario.fxml");

            if (fxmlURL == null) {
                messageLabel.setText("Error: FXML URL not found.");
                return;
            }

            FXMLLoader loader = new FXMLLoader(fxmlURL);
            Parent root = loader.load();

            Stage stage = new Stage();
            stage.setTitle("Choose Scenario.");
            stage.setScene(new Scene(root));
            stage.show();

            Stage currentStage = (Stage) emailField.getScene().getWindow();
            currentStage.close();

        } catch (IOException e) {
            e.printStackTrace();
            messageLabel.setText("Error: " + e.getMessage());
        }
    }

    /**
     * Handles the action of the "Sign Up" button in the login interface.
     *
     * This method loads the "signUp.fxml" user interface from the resources, initializes
     * the corresponding controller, and links it with the current authentication controller.
     * It then displays the sign-up stage (a new window) to the user. If an error occurs
     * while loading the FXML resource, the stack trace of the exception is printed to the console.
     */
    @FXML
    private void handleSignUpButton() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/JavaFX/signUp.fxml"));
            Parent root = loader.load();

            SignUpController signUpController = loader.getController();
            signUpController.setController(authController);

            Stage stage = new Stage();
            stage.setTitle("Sign Up");
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Handles the click event on the email text field in the login interface.
     * This method clears any text displayed in the message label, typically resetting
     * feedback messages displayed to the user.
     */
    @FXML
    private void handleEmailFieldClick() {
        messageLabel.setText("");
    }

    /**
     * Handles the click event on the password field in the login interface.
     */
    @FXML
    private void handlePasswordFieldClick() {
        messageLabel.setText("");
    }

    /**
     * Handles the click event for the "Development Team" button.
     */
    @FXML
    private void handleDevButton() {
        try {
            URL fxmlURL = getClass().getResource("/JavaFX/devTeam.fxml");
            if (fxmlURL == null) {
                return;
            }

            FXMLLoader loader = new FXMLLoader(fxmlURL);
            Parent root = loader.load();

            Stage stage = new Stage();
            stage.setTitle("Development Team");
            stage.setScene(new Scene(root));
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
            messageLabel.setText("Error: " + e.getMessage());
        }
    }
}
