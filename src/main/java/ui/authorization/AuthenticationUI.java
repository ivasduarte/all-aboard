package ui.authorization;

import ui.menu.EditorUI;
import pt.ipp.isep.dei.ui.console.menu.MenuItem;
import pt.isep.lei.esoft.auth.mappers.dto.UserRoleDTO;
import ui.menu.PlayerUI;
import ui.utils.Utils;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;


/**
 * The AuthenticationUI class provides a graphical interface for user authentication.
 * It handles user login, role selection, and redirection to appropriate role-specific UIs.
 * Implements the Runnable interface to allow execution in a dedicated thread.
 */
public class AuthenticationUI implements Runnable {
    private final AuthenticationController ctrl;


    /**
     * Constructs an instance of AuthenticationUI.
     * This class serves as the user interface for handling authentication-related actions,
     * including login, role selection, and redirection to role-specific menus.
     * It leverages an instance of AuthenticationController to perform authentication-related operations.
     */
    public AuthenticationUI() {
        ctrl = new AuthenticationController();
    }


    /**
     * Executes the authentication process, including user login, role selection,
     * and redirection to the appropriate role-specific user interface.
     *
     * <ul>
     *   <li>Attempts to log in the user by asking for credentials.</li>
     *   <li>If the login is successful, retrieves the roles assigned to the user.</li>
     *   <li>If the user has roles assigned, prompts the user to select a role.</li>
     *   <li>Redirects the user to the appropriate UI for the selected role, if available.</li>
     *   <li>If no roles are assigned or selected, appropriate messages are displayed.</li>
     *   <li>Performs a logout operation at the end regardless of the outcome.</li>
     * </ul>
     */
    public void run() {
        boolean success = doLogin();

        if (success) {
            List<UserRoleDTO> roles = this.ctrl.getUserRoles();
            if ((roles == null) || (roles.isEmpty())) {
                System.out.println("No role assigned to user.");
            } else {
                UserRoleDTO role = selectsRole(roles);
                if (!Objects.isNull(role)) {
                    List<MenuItem> rolesUI = getMenuItemForRoles();
                    this.redirectToRoleUI(rolesUI, role);
                } else {
                    System.out.println("No role selected.");
                }
            }
        }
        this.logout();

    }


    /**
     * Attempts to log in a user by validating their credentials. The login process allows
     * a fixed number of attempts before failing. During each attempt, the user is prompted
     * to enter their user ID or email and password. If authentication is successful,
     * a success status is returned.
     *
     * @return  True if the user is successfully authenticated, false otherwise.
     */
    private boolean doLogin() {
        System.out.println("\n\n--- LOGIN UI ---------------------------");

        int maxAttempts = 3;
        boolean success = false;
        do{
            maxAttempts--;
            String id = Utils.readLineFromConsole("Enter UserId/Email: ");
            String password = Utils.readLineFromConsole("Enter Password: ");

            success = ctrl.doLogin(id, password);
            if(!success){
                System.out.println("Invalid UserId and/or Password. \n You have  " + maxAttempts + " more attempt(s).");
            }
        }while(!success && maxAttempts > 0);
        return success;
    }


    /**
     * Logs out the currently authenticated user by invoking the appropriate method
     * from the authentication controller. Clears the user's session and
     * terminates the authentication process.
     */
    private void logout() {
        ctrl.doLogout();
    }


    /**
     * Redirects the user to the appropriate UI based on their selected role.
     * If a matching role-specific UI exists in the provided list, it is executed.
     * Otherwise, a message is displayed indicating that no UI is available for the user's role.
     *
     * @param rolesUI   A list of {@code MenuItem} objects corresponding to available role-specific UIs.
     * @param role      The {@code UserRoleDTO} representing the user's selected role.
     */
    private void redirectToRoleUI(List<MenuItem> rolesUI, UserRoleDTO role) {
        boolean found = false;
        Iterator<MenuItem> it = rolesUI.iterator();
        while (it.hasNext() && !found) {
            MenuItem item = it.next();
            found = item.hasDescription(role.getDescription());
            if (found) {
                item.run();
            }
        }
        if (!found) {
            System.out.println("There is no UI for users with role '" + role.getDescription() + "'");
        }
    }


    /**
     * Selects a user role from the provided list of roles. If the list contains only one role,
     * that role is automatically selected. Otherwise, it prompts the user to choose a role
     * from the list using the provided utility method.
     *
     * @param roles     A list of {@code UserRoleDTO} objects representing the roles available for selection.
     * @return          The selected {@code UserRoleDTO}, or {@code null} if no selection was made.
     */
    private UserRoleDTO selectsRole(List<UserRoleDTO> roles) {
        if (roles.size() == 1) {
            return roles.get(0);
        } else {
            return (UserRoleDTO) pt.ipp.isep.dei.ui.console.utils.Utils.showAndSelectOne(roles, "Select the role you want to adopt in this session:");
        }
    }


    /**
     * Creates and returns a list of MenuItem objects that map user roles to their specific UI components.
     * The method adds predefined roles and their associated UIs to the list.
     * Additional roles and their corresponding UIs can be introduced later.
     *
     * @return  A list of MenuItem objects representing role-specific user interfaces.
     */
    private List<MenuItem> getMenuItemForRoles() {
        List<MenuItem> rolesUI = new ArrayList<>();
        rolesUI.add(new MenuItem(AuthenticationController.ROLE_EDITOR, new EditorUI()));
        rolesUI.add(new MenuItem(AuthenticationController.ROLE_PLAYER, new PlayerUI()));

        //TODO: Complete with other user roles and related RoleUI
        return rolesUI;
    }
}
