package controller;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import ui.authorization.AuthenticationController;
import ui.menu.EditorUI;

import java.io.IOException;
import java.net.URL;

public class LoginController {

    @FXML
    private TextField emailField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private Label messageLabel;

    private final AuthenticationController authController = new AuthenticationController();

    private Runnable onLoginSuccess;

    public void setOnLoginSuccess(Runnable callback) {
        this.onLoginSuccess = callback;
    }

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
     * Checks if current user is an editor
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
     * Starts editor menu
     */
    private void startEditorConsole() {
        Stage currentStage = (Stage) emailField.getScene().getWindow();
        currentStage.close();

        Platform.runLater(() -> {
            new Thread(() -> {
                try {
                    Thread.sleep(500);
                    EditorUI editorUI = new EditorUI();
                    editorUI.run();
                } catch (Exception e) {
                    System.err.println("Error starting editor: " + e.getMessage());
                    e.printStackTrace();
                } finally {
                    System.out.println("\nEditor session ended.");
                    System.exit(0);
                }
            }).start();
        });
    }

    /**
     * Opens graphical interface for players
     */
    private void openPlayerInterface() {
        try {
            URL fxmlURL = getClass().getResource("/JavaFX/chooseScenario.fxml");
            System.out.println("FXML URL: " + fxmlURL);

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

    @FXML
    private void handleSignUpButton() {
        messageLabel.setText("To be implemented.");
    }

    @FXML
    private void handleEmailFieldClick() {
        messageLabel.setText("");
    }

    @FXML
    private void handlePasswordFieldClick() {
        messageLabel.setText("");
    }
}