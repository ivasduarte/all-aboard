package ui.authorization;

import ui.menu.EditorUI;
import ui.menu.MenuItem;
import ui.menu.PlayerUI;
import ui.utils.Utils;

import java.util.ArrayList;
import java.util.List;

/**
 * The AuthenticationUI class provides a graphical interface for user authentication.
 * It handles user login, role selection, and redirection to appropriate role-specific UIs.
 * Implements the Runnable interface to allow execution in a dedicated thread.
 */
public class AuthenticationUI implements Runnable {
    private final AuthenticationController ctrl;
    private int option;

    /**
     * Initializes an instance of AuthenticationUI which provides the user interface
     * for managing authentication processes such as login and role-based navigation.
     *
     * @param option    An integer parameter to determine or configure specific behavior
     *                  of the authentication user interface.
     */
    public AuthenticationUI(int option) {
        ctrl = new AuthenticationController();
        this.option = option;
    }

    /**
     * Executes a specific operation based on the configured option.
     *
     * The behavior of this method is determined by the value of the `option` field:
     * - If the option is 0, it invokes the `run1` method to initiate the authentication process,
     *   including login and role-based navigation.
     * - If the option is 1, it calls the `createUser` method to display the user registration interface
     *   and facilitate new user creation.
     */
    public void run(){
        switch (option) {
            case 0:
                run1();
                break;
            case 1:
                createUser();
                break;
        }
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
    public void run1() {
        boolean success = doLogin();

        if (success) {
            String userEmail = ctrl.getCurrentUserEmail();
            String userRole = ctrl.getRole(userEmail);

            List<MenuItem> roleMenus = getMenuItemForRoles();

            for (MenuItem item : roleMenus) {
                if (item.getDescription().equals(userRole)) {
                    item.run(); // run appropriate UI
                    break;
                }
            }
        }

        this.logout();
    }

    /**
     * Creates and returns a list of MenuItem objects that map user roles to their specific UI components.
     * The method adds predefined roles and their associated UIs to the list.
     * Additional roles and their corresponding UIs can be introduced later.
     *
     * @return      A list of MenuItem objects representing role-specific user interfaces.
     */
    private List<MenuItem> getMenuItemForRoles() {
        List<MenuItem> rolesUI = new ArrayList<>();
        rolesUI.add(new MenuItem(AuthenticationController.ROLE_EDITOR, new EditorUI()));
        rolesUI.add(new MenuItem(AuthenticationController.ROLE_PLAYER, new PlayerUI()));

        //TODO: Complete with other user roles and related RoleUI
        return rolesUI;
    }

    /**
     * This method provides a registration UI where users can enter their name, email,
     * and password to create a new account. It allows users to retry the registration
     * process if account creation is unsuccessful due to an already existing user.
     *
     * The following steps are performed:
     * - Prompts the user to input their name, email, and password.
     * - Attempts to create a new user with the specified details and a default role.
     * - If the user already exists, informs the user and gives the option to try again.
     * - If the user creation is successful: confirms the creation and exits the prompt.
     *
     * Internally, uses the createNewUser method from the authentication controller
     * to perform the user creation process.
     */
    public void createUser() {
        System.out.println("\n\n-------------------------- REGISTRATION UI --------------------------");

        boolean tryAgain;
        do {
            String name = Utils.readLineFromConsole("Enter your name: ");
            String email = Utils.readLineFromConsole("Enter your email: ");
            String password = Utils.readLineFromConsole("Enter your password: ");
            String roleID = ctrl.ROLE_PLAYER;

            boolean success = ctrl.createNewUser(name, email, password, roleID);
            if (!success) {
                System.out.println("User has been previously created.");
                String answer = Utils.readLineFromConsole("Try again? (y/n): ");
                tryAgain = answer.equalsIgnoreCase("y");
            } else {
                tryAgain = false;
                System.out.println("User has been created.");
            }
        } while (tryAgain);
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
        System.out.println("\n\n--------------------------- LOGIN UI ---------------------------");

        int maxAttempts = 3;
        boolean success;
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

}
