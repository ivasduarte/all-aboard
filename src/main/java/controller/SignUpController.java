package controller;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import ui.authorization.AuthenticationController;

/**
 * Controller class responsible for managing the user registration process.
 * Handles user input validation and interaction with the authentication system
 * to create new user accounts.
 */
public class SignUpController {
    @FXML
    private TextField nameField;
    @FXML
    private TextField emailField;
    @FXML
    private TextField passField;
    @FXML
    private Label messageLabel;
    @FXML
    private Button signUpButton;
    @FXML
    private Button backButton;

    private AuthenticationController ctrl;

    /**
     * Sets the authentication controller instance to be used for user registration.
     *
     * @param ctrl The authentication controller instance that will handle user creation.
     */
    public void setController(AuthenticationController ctrl) {
        this.ctrl = ctrl;
    }

    /**
     * Handles the sign up button click event.
     * Validates user input and attempts to create a new user account.
     * Displays appropriate success or error messages based on the result.
     */
    @FXML
    private void handleSignUpButton() {
        String name = nameField.getText();
        String email = emailField.getText();
        String password = passField.getText();

        boolean success = ctrl.createNewUser(name, email, password, ctrl.ROLE_PLAYER);

        Alert alert;

        if (success) {
            alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Success");
            alert.setHeaderText(null);
            alert.setContentText("User created successfully!");
            alert.showAndWait();
        } else {
            alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText(null);
            alert.setContentText("User already exists or invalid data.");
            alert.showAndWait();
        }
    }

    /**
     * Handles the back button click event.
     * Closes the current registration window and returns to the previous screen.
     */
    @FXML
    private void handleBackButton() {
        Stage stage = (Stage) backButton.getScene().getWindow();
        stage.close();
    }
}