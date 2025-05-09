package ui.authorization;

import repositories.Repositories;
import pt.isep.lei.esoft.auth.mappers.dto.UserRoleDTO;
import repositories.AuthenticationRepository;

import java.util.List;


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
        try{
            return authenticationRepository.doLogin(email, password);
        }catch(IllegalArgumentException ex){
            return false;
        }
    }


    /**
     * Retrieves the list of user roles associated with the currently authenticated user.
     * If the user is logged in, the roles are obtained from the current user session.
     * Otherwise, if the user is not logged in, it returns null.
     *
     * @return  A list of {@code UserRoleDTO} objects representing the roles assigned to the current user,
     *          or {@code null} if the user is not logged in.
     */
    public List<UserRoleDTO> getUserRoles() {
        if (authenticationRepository.getCurrentUserSession().isLoggedIn()) {
            return authenticationRepository.getCurrentUserSession().getUserRoles();
        }
        return null;
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
