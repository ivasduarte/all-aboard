package ui.menu;

import org.apache.commons.lang3.StringUtils;

import java.util.Objects;

/**
 * Represents a menu item that consists of a description and an associated UI action
 * to be executed when the menu item is selected.
 *
 * Each MenuItem includes:
 * - A brief textual description that identifies the menu option.
 * - A Runnable component that defines the behavior or action performed upon selection.
 *
 * This class provides a structured way to encapsulate UI behaviors, enabling concise
 * and modular construction of menu-driven interfaces.
 */
public class MenuItem {
    private final String description;
    private final Runnable ui;

    /**
     * Constructs a MenuItem instance with a description and a corresponding UI action.
     *
     * @param description   A brief textual description of the menu item. Must not be null or empty.
     * @param ui            A Runnable representing the UI action to be executed when the menu item is selected.
     * @throws              IllegalArgumentException If the description is null, empty, or contains only whitespace.
     * @throws              IllegalArgumentException If the UI action is null.
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
     * Executes the UI functionality associated with this MenuItem.
     *
     * This method delegates the execution to the `run` method of the `Runnable`
     * instance associated with this MenuItem. The purpose of this method is
     * to trigger the behavior or action linked to the menu item when selected.
     *
     * It ensures that the encapsulated functionality defined during the
     * instantiation of the MenuItem is executed appropriately.
     */
    public void run() {
        this.ui.run();
    }

    /**
     * Returns a string representation of this MenuItem.
     *
     * The string representation typically includes the brief textual
     * description of the menu item, which is used to identify the
     * option in a UI context.
     *
     * @return      The description of this MenuItem as a String
     */
    public String toString() {
        return this.description;
    }

    /**
     * Retrieves the description of this MenuItem.
     *
     * The description provides a brief textual representation
     * of the menu option, typically used to identify the item
     * in a user interface context.
     *
     * @return      The description of this MenuItem as a String
     */
    public String getDescription() {
        return description;
    }
}
