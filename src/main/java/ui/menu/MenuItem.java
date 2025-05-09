package ui.menu;

import org.apache.commons.lang3.StringUtils;

import java.util.Objects;


/**
 * Represents a menu item with a textual description and an associated action.
 *
 * Each MenuItem encapsulates a description for display purposes and a
 * {@link Runnable} defining the behavior when this menu item is selected.
 * The {@code MenuItem} class is used in menu-driven console interfaces
 * where a user can interact with and execute different functionalities
 * in the application.
 *
 * Key features:
 * - Stores a description that represents the menu item.
 * - Contains a {@link Runnable} to define specific behavior when invoked.
 * - Ensures that the description is neither null nor empty, and the runnable is non-null.
 *
 * Use this class to represent individual options in a user interface menu.
 * The {@code run} method of the MenuItem delegates execution to its
 * associated {@link Runnable}, allowing the modular execution of
 * application features from a menu-driven interface.
 */
public class MenuItem {
    private final String description;
    private final Runnable ui;


    /**
     * Constructs a MenuItem with the specified description and associated action.
     *
     * @param description   A textual representation of the menu item that will be displayed to the user;
     *                      it cannot be null or empty.
     * @param ui            A {@link Runnable} defining the action to be executed when the menu item is selected;
     *                      it cannot be null.
     * @throws              IllegalArgumentException if the description is null, blank, or if the Runnable is null.
     */
    public MenuItem(String description, Runnable ui) {
        if (StringUtils.isBlank(description)) {
            throw new IllegalArgumentException("MenuItem description cannot be null or empty.");
        }
        if (Objects.isNull(ui)) {
            throw new IllegalArgumentException("MenuItem does not support a null UI.");
        }

        this.description = description;
        this.ui = ui;
    }


    /**
     * Executes the action associated with this MenuItem.
     *
     * This method invokes the {@code run} method of the underlying {@code Runnable} instance
     * that is tied to this MenuItem. It delegates the execution of the specific functionality
     * associated with this menu option. The action is defined when the MenuItem is instantiated
     * and will be executed upon method invocation.
     *
     * Usage Context:
     * - Typically called when a menu item is selected in a menu-driven user interface to
     *   perform the action associated with that menu item.
     *
     * Behavior:
     * - Provides a modular way to associate actions with menu items.
     * - Ensures the encapsulated action is executed without exposing its implementation.
     */
    public void run() {
        this.ui.run();
    }


    /**
     * Checks if the given description matches the description of this MenuItem.
     *
     * @param description   Description to compare with the MenuItem's description; cannot be null
     * @return              True if the given description matches the MenuItem's description, false otherwise
     */
    public boolean hasDescription(String description) {
        return this.description.equals(description);
    }


    /**
     * Returns the string representation of this MenuItem.
     *
     * The string representation is the description associated
     * with this MenuItem, which provides a textual representation
     * to be displayed to the user.
     *
     * @return  The description of this MenuItem.
     */
    public String toString() {
        return this.description;
    }
}
