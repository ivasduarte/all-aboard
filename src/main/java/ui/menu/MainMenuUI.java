package ui.menu;

import ui.authorization.AuthenticationUI;
import ui.utils.DevelopmentTeamUI;
import ui.utils.Utils;

import java.util.ArrayList;
import java.util.List;

/**
 * MainMenuUI serves as the main entry point of the application by providing a
 * console-based user interface for navigating through primary functionalities.
 *
 * This class implements the Runnable interface, allowing the menu to be
 * executed in a separate thread or as part of a multithreaded environment.
 * The menu presents several options to the user, including login, user
 * registration, and viewing the development team details.
 *
 * The menu follows a dynamic structure where options are configured with
 * descriptions and associated user interface components. The selected option
 * invokes the corresponding UI, ensuring modularity and extensibility.
 *
 * Once the menu is displayed, the application continuously prompts the user
 * for input until the quit option is chosen.
 */
public class MainMenuUI implements Runnable {
    public void run() {
        List<MenuItem> options = new ArrayList<>();
        options.add(new MenuItem("Login", new AuthenticationUI(0)));
        options.add(new MenuItem("Create User", new AuthenticationUI(1)));
        options.add(new MenuItem("Development Team", new DevelopmentTeamUI()));

        int option;
        do {
            option = Utils.showAndSelectIndex(options, "\n\n-------------------------- MAIN MENU --------------------------");
            if (option >= 0 && option < options.size()) {
                options.get(option).run();
            }
        } while (option != -1);
    }
}

