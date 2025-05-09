package ui.menu;

import ui.authorization.AuthenticationUI;
import ui.utils.DevelopmentTeamUI;
import ui.utils.Utils;

import java.util.ArrayList;
import java.util.List;


/**
 * The MainMenuUI class provides the main user interface for navigating through
 * various application functionalities. It implements the Runnable interface to
 * facilitate integration and execution within threads or other multi-threaded
 * environments.
 *
 * The main menu displays a list of options that the user can select to access
 * different parts of the application. Each option is represented by a {@code MenuItem}
 * object, which contains both a description and a corresponding action to execute.
 *
 * The options currently included in the main menu are:
 * - Login (handled by {@code AuthenticationUI})
 * - Development Team (handled by {@code DevelopmentTeamUI})
 *
 * The selected option runs the associated user interface. The menu is presented
 * in a loop until the user opts to exit by providing the corresponding input.
 *
 * Responsibilities:
 * - Creates and manages the list of main menu options.
 * - Displays the menu to the user and handles their selection.
 * - Executes the associated user interface for the selected option.
 *
 * Implements:
 * - The {@code Runnable} interface, allowing asynchronous or separate execution.
 *
 * Usage Notes:
 * - Call the {@code run} method to start the main menu loop.
 * - The menu will terminate when the user provides an exit command (e.g., -1).
 */
public class MainMenuUI implements Runnable{


    /**
     * Executes the main operational loop for the main menu interface.
     *
     * This method initializes a list of menu options with predefined functionalities,
     * such as "Login" and "Development Team". Each option is represented by a {@code MenuItem}
     * that encapsulates a description and an associated user interface to run.
     *
     * The method operates in a loop, displaying the menu options to the user and handling their input.
     * Users can select an option by its index to run the associated functionality.
     * The loop continues until the user explicitly opts to exit by providing a termination input.
     *
     * Key Responsibilities:
     * - Initializes the menu with available {@code MenuItem} options.
     * - Displays the menu and collects user input for navigation.
     * - Executes the user interface associated with the selected menu item.
     * - Repeats until the user decides to exit by entering -1.
     *
     * Note:
     * - The indices displayed to the user correspond to the order of the menu items in the list.
     * - If the user selects an invalid index, the menu will repeat without performing any operation.
     */
    public void run() {
        List<MenuItem> options = new ArrayList<MenuItem>();
        options.add(new MenuItem("Login", new AuthenticationUI()));
        options.add(new MenuItem("Development Team", new DevelopmentTeamUI()));

        int option = 0;
        do {
            option = Utils.showAndSelectIndex(options, "\n\n-------------------------- MAIN MENU --------------------------");

            if ((option >= 0) && (option < options.size())) {
                options.get(option).run();
            }
        } while (option != -1);
    }
}
