package ui.authorization;

import domain.User;
import repositories.AuthenticationRepository;
import repositories.Repositories;

/**
 * The AuthenticationController class provides methods for managing user authentication
 * and retrieving user roles. It acts as a bridge between the presentation layer
 * and the authentication repository.
 */
public class AuthenticationController{
    public static final String ROLE_EDITOR = "EDITOR";
    public static final String ROLE_PLAYER = "PLAYER";
    private final AuthenticationRepository authenticationRepository;

    /**
     * Constructs an instance of AuthenticationController.
     * This constructor initializes the authentication repository by obtaining it
     * from the singleton instance of the Repositories class.
     * The AuthenticationController class serves as the intermediary between the presentation
     * layer and the authentication-related operations managed by the AuthenticationRepository.
     */
    public AuthenticationController() {
        this.authenticationRepository = Repositories.getInstance().getAuthenticationRepository();
    }

    /**
     * Attempts to authenticate a user using the provided email and password.
     * If the authentication is successful, the method returns true.
     * If the authentication fails or an invalid argument is provided, it returns false.
     *
     * @param email    The email address of the user attempting to log in.
     * @param password The password associated with the provided email address.
     * @return True if the login is successful, false otherwise.
     */
    public boolean doLogin(String email, String password) {
            return authenticationRepository.doLogin(email, password);
    }

    /**
     * Creates a new user in the system with the specified attributes if the user does not already exist.
     *
     * @param name          The name of the user. Must not be null or empty.
     * @param email         The email address of the user. Must be in a valid email format and unique.
     * @param password      The password for the user. Must not be null or empty.
     * @param roleID        The role ID to be assigned to the new user. Must not be null or empty.
     * @return              True if the user was successfully created;
     *                      false if the user already exists.
     */
    public boolean createNewUser(String name, String email, String password, String roleID) {
        if (!isValidPassword(password)) {
            System.out.println("Password does not meet the requirements.");
            return false;
        }

        User user = new User(name, email, roleID, password);
        return authenticationRepository.createNewUser(user);
    }


    /**
     * Validates the given password according to the client specifications:
     * - Exactly 7 alphanumeric characters
     * - At least 3 uppercase letters
     * - At least 2 digits
     *
     * @param password The password to validate.
     * @return True if the password is valid, false otherwise.
     */
    public static boolean isValidPassword(String password) {
        if (password == null || !(password.length() >= 7) ||!password.matches("[a-zA-Z0-9]+")) {
            return false;
        }

        int uppercaseCount = 0;
        int digitCount = 0;

        for (char c : password.toCharArray()) {
            if (Character.isUpperCase(c)) {
                uppercaseCount++;
            } else if (Character.isDigit(c)) {
                digitCount++;
            }
        }

        return uppercaseCount >= 3 && digitCount >= 2;
    }

    /**
     * Retrieves the role of the user associated with the specified email address.
     * If no user is found for the provided email, this method returns null.
     *
     * @param email     The email address of the user whose role is to be retrieved.
     *                  Must not be null and should be in a valid email format.
     * @return          The role of the user as a String, or null if the user is not found.
     */
    public String getRole(String email) {
        return authenticationRepository.getRole(email);
    }

    /**
     * Retrieves the email address of the currently authenticated user.
     * This method delegates the call to the {@code authenticationRepository}
     * to obtain the email associated with the current session.
     *
     * @return  The email address of the current user as a String, or null if no user is authenticated.
     */
    public String getCurrentUserEmail(){
        return authenticationRepository.getCurrentUserEmail();
    }

    /**
     * Logs out the currently authenticated user by interacting with the authentication repository.
     * This method terminates the user's session and ensures that they are properly logged out
     * from the system.
     */
    public void doLogout() {
        authenticationRepository.doLogout();
    }
}
