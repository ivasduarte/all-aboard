import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import controller.LoginController;

/**
 * Main application class that serves as the entry point for the JavaFX application.
 * This class initializes the application, sets up the login screen, and manages the primary stage.
 */
public class Main extends Application {
    /**
     * Initializes and starts the JavaFX application.
     * This method:
     * 1. Initializes the application using Bootstrap
     * 2. Loads the login screen FXML
     * 3. Sets up the primary stage with the login scene
     *
     * @param primaryStage The primary stage for this application
     * @throws Exception if there is an error during initialization
     */
    @Override
    public void start(Stage primaryStage) throws Exception {
        Platform.setImplicitExit(false);
        Bootstrap bootstrap = new Bootstrap();
        bootstrap.run();

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/JavaFX/login.fxml"));
        Parent root = loader.load();

        LoginController loginController = loader.getController();

        primaryStage.setTitle("Login");
        primaryStage.setScene(new Scene(root));
        primaryStage.show();
    }

    /**
     * The main entry point for the application.
     * Launches the JavaFX application with the specified arguments.
     *
     * @param args The command line arguments
     */
    public static void main(String[] args) {
        Platform.setImplicitExit(false);
        launch(args);
    }
}
