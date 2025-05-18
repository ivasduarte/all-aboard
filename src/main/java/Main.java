import ui.menu.MainMenuUI;

/**
 * The Main class serves as the entry point for the application.
 * It initializes and runs the necessary components such as the Bootstrap and MainMenuUI classes.
 */
public class Main {
    /**
     * The entry point of the application. This method initializes and executes the required components to start
     * the application.
     *
     * @param args      Command-line arguments passed to the application
     */
    public static void main(String[] args) {
        Bootstrap bootstrap = new Bootstrap();
        bootstrap.run();

        try {
            MainMenuUI menu = new MainMenuUI();
            menu.run();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}