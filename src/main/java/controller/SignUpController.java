package controller;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import ui.authorization.AuthenticationController;

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

    public void setController(AuthenticationController ctrl) {
        this.ctrl = ctrl;
    }

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

    @FXML
    private void handleBackButton() {
        Stage stage = (Stage) backButton.getScene().getWindow();
        stage.close();
    }
}