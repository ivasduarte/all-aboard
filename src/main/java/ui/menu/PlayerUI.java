package ui.menu;

import presentation.AddStationUI;
import presentation.AssignRouteUI;
import presentation.BuyLocomotiveUI;
import presentation.UpgradeAStationUI;
import pt.ipp.isep.dei.ui.console.ShowTextUI;
import ui.utils.DisplayMapUI;
import ui.utils.Utils;

import java.util.ArrayList;
import java.util.List;


/**
 * Handles the user interface for the player menu in a console-based application.
 *
 * The PlayerUI class implements the {@link Runnable} interface, allowing it
 * to be executed in a thread or directly invoked. It represents a menu-based
 * system where players can interact with the application's functionalities
 * such as building stations, upgrading stations, checking station details,
 * buying locomotives, and assigning routes to trains.
 *
 * When the run method is executed, this class dynamically builds a menu
 * by creating a list of {@link MenuItem} objects, each representing a
 * different action available to the player. The user can then interact with
 * this menu to perform operations. The menu remains active until the user selects
 * an exit option, at which point the application flow returns from this UI.
 *
 * Functionalities included in the menu:
 * - Build a station
 * - Upgrade a station
 * - Check station details
 * - Buy a locomotive
 * - Assign a train to a route
 *
 * Key Features:
 * - Modularity: Uses {@link MenuItem} and {@link Runnable} to ensure
 *   each menu option is self-contained and executable without tightly
 *   coupling the main UI logic with those functionalities.
 * - Loop-driven: The menu runs in a loop until the user decides to exit,
 *   allowing repeated interactions without restarting the interface.
 * - Extensibility: Additional menu options can be introduced by adding a new
 *   {@link MenuItem} with its associated logic linked via a {@link Runnable} instance.
 */
public class PlayerUI implements Runnable{


    /**
     * Constructs an instance of the PlayerUI class, initializing the main menu interface
     * for the player-related functionalities of the application. This constructor creates
     * an object that is responsible for handling user interactions such as building stations,
     * upgrading stations, checking station details, purchasing locomotives, and assigning
     * routes to trains via a menu-driven console interface.
     *
     * The PlayerUI class operates as part of a user interface within a console application,
     * allowing users to interact with player-specific features. The initialization of this
     * class prepares a modular and extensible system for displaying and executing menu items
     * associated with the player functionalities.
     */
    public PlayerUI(){}


    /**
     * Executes the main menu interface for the PlayerUI.
     *
     * This method is responsible for displaying a menu-driven console interface to the user,
     * allowing access to various player-related functionalities such as building and upgrading stations,
     * checking station details, purchasing locomotives, and assigning trains to routes.
     * The menu options are dynamically populated into a list and presented to the user in a loop-based mechanism.
     *
     * Core functionality:
     * - Initializes a list of {@link MenuItem} objects, each representing a menu option.
     * - Displays the menu to the user and prompts for an input selection using the {@link Utils#showAndSelectIndex} utility.
     * - Executes the corresponding menu item's action based on the user's selection by invoking its {@code run} method.
     * - Repeats until the user chooses to exit the menu interface by selecting an invalid option or cancelling the input.
     *
     * Usage Context:
     * - Typically invoked as part of the PlayerUI module within a console-based application.
     * - Provides a modular and extensible point for adding or modifying player-related features.
     *
     * Behavior:
     * - Continuous menu interaction loop handles user input and menu execution until the user exits.
     * - Ensures safe execution of chosen menu options by validating input and bounds.
     * - Delegates the execution of menu-specific actions to their respective {@link Runnable} implementations.
     */
    public void run(){
        List<MenuItem> options = new ArrayList<MenuItem>();
        options.add(new MenuItem("Build a Station", new AddStationUI()));
        options.add(new MenuItem("Upgrade a Station", new UpgradeAStationUI()));
        options.add(new MenuItem("Check Station Details", new ShowTextUI("You have chosen Check Station Details.")));
        options.add(new MenuItem("Buy a Locomotive", new BuyLocomotiveUI()));
        options.add(new MenuItem("Assign a Train to a Route", new AssignRouteUI()));

        int option = 0;
        do{
            option = Utils.showAndSelectIndex(options, "\n\n------------------------- PLAYER MENU -------------------------");

            if((option >= 0) && (option < options.size())){
                options.get(option).run();
            }
        }while(option != -1);
    }
}
