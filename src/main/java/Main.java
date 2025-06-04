import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import controller.LoginController;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        Bootstrap bootstrap = new Bootstrap();
        bootstrap.run();

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/JavaFX/login.fxml"));
        Parent root = loader.load();

        LoginController loginController = loader.getController();

        loginController.setOnLoginSuccess(() -> {
            try {
                FXMLLoader chooseLoader = new FXMLLoader(getClass().getResource("/JavaFX/chooseScenario.fxml"));
                Parent chooseRoot = chooseLoader.load();

                Stage chooseStage = new Stage();
                chooseStage.setTitle("Choose Scenario");
                chooseStage.setScene(new Scene(chooseRoot));
                chooseStage.show();

            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        primaryStage.setTitle("Login");
        primaryStage.setScene(new Scene(root));
        primaryStage.show();
    }
}