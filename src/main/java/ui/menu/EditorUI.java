package ui.menu;

import presentation.*;
import ui.utils.Utils;

import java.util.ArrayList;
import java.util.List;

/**
 * The EditorUI class provides the main menu interface for the editor module
 * of the application. It allows users to perform various functions such as
 * creating maps, displaying map lists, creating scenarios, and adding cities
 * or industries via a menu-driven console interface.
 *
 * This class implements the Runnable interface to enable execution in a
 * separate thread. It interacts with different UI implementations for each
 * menu option, delegating functionality to respective classes.
 */
public class EditorUI implements Runnable {
    /**
     * Constructs an instance of the EditorUI class, initializing the main menu interface
     * for the editor module of the application. This constructor creates an object that
     * is responsible for handling user interactions related to creating maps, displaying map
     * lists, managing scenarios, and adding cities or industries via a menu-driven console interface.
     *
     * The EditorUI class operates as a user interface within a console application, and its
     * initialization is required to provide access to editor-related functionalities.
     */
    public EditorUI() {}


    /**
     * Executes the main menu-driven interface for the editor module.
     *
     * This method initializes a list of menu options, each represented by a MenuItem
     * associated with a specific functionality of the editor module. The available
     * options include creating maps, displaying map lists, managing scenarios, and
     * adding cities or industries.
     *
     * Each option is displayed as a menu item, and the user can interact with the menu
     * via console input. The method captures the user selection and delegates the
     * corresponding functionality to the appropriate UI class by invoking its `run()`
     * method. The loop continues until the user chooses to exit the menu.
     *
     * Usage of this method ensures a structured flow for accessing and interacting
     * with various editor-related functionalities.
     */
    public void run() {
        List<MenuItem> options = new ArrayList<MenuItem>();
        options.add(new MenuItem("Create Map", new CreateMapUI()));
        options.add(new MenuItem("Choose Map", new ChooseMapUI()));

        int option = 0;
        do {
            option = Utils.showAndSelectIndex(options, "\n\n------------------------- EDITOR MENU -------------------------");

            if ((option >= 0) && (option < options.size())) {
                options.get(option).run();
            }
        } while (option != -1);
    }
}
