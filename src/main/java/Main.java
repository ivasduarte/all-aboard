import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import controller.LoginController;

public class Main extends Application {
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

    public static void main(String[] args) {
        Platform.setImplicitExit(false);
        launch(args);
    }
}
