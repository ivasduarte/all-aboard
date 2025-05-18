package repositories;

import domain.User;
import domain.UserSession;

import java.util.*;

/**
 * Handles user authentication and manages user-related operations within the system.
 * This class provides methods to create, read, update, and delete user accounts,
 * manage user sessions, and perform role and authentication-related actions.
 */
public class AuthenticationRepository {
    Set<User> users = new HashSet<>();
    UserSession session = UserSession.getInstance();

    /**
     * Creates a new user and adds them to the repository if they do not already exist.
     *
     * @param user  The user object to be added. Must contain valid information such as a unique email.
     * @return      True if the user was successfully added;
     *              false if the user already exists in the repository.
     */
    public boolean createNewUser(User user){
        if(!doesUserExist(user)){
            users.add(user);
            return true;
        }
        return false;
    }

    /**
     * Retrieves a list of all users in the repository.
     *
     * @return  A List of User objects representing all the users in the repository.
     */
    public List<User> getAllUsers(){
        List<User> userList = new ArrayList<>();
        for(User user : users){
            userList.add(user);
        }
        return userList;
    }

    /**
     * Retrieves a user from the repository based on their email address.
     *
     * @param email     The email address of the user to retrieve. Must not be null and should be in a valid email format.
     * @return          An {@code Optional<User>} containing the user if found; otherwise, an empty {@code Optional}.
     */
    public Optional<User> getUserByEmail(String email) {
        for (User user : users) {
            if (user.getEmail().equals(email)) {
                return Optional.of(user);
            }
        }
        return Optional.empty();
    }

    /**
     * Checks if a user already exists in the repository by comparing their email address.
     *
     * @param user      The user object to check for existence. Must not be null and must contain a valid email.
     * @return          True if a user with the same email address exists in the repository,
     *                  false otherwise.
     */
    private boolean doesUserExist(User user){
        for (User user1 : users) {
            if (user1.getEmail().equals(user.getEmail())) {
                return true;
            }
        }
        return false;
    }

    /**
     * Checks whether a user with the specified email address exists in the repository.
     *
     * @param email     The email address of the user to check for existence. Must not be null and should be
     *                  in a valid email format.
     * @return          True if a user with the specified email address exists;
     *                  false otherwise.
     */
    private boolean doesUserExist(String email) {
        for (User user1 : users) {
            if (user1.getEmail().equals(email)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Validates the provided password for a user identified by their email address.
     *
     * @param email         The email address of the user whose password is being validated. Must not be null and should
     *                      be in a valid email format.
     * @param password      The password to validate. Must not be null.
     * @return              True if the user exists and the provided password matches the stored password,
     *                      false otherwise.
     */
    private boolean isPasswordValid(String email, String password) {
        if (doesUserExist(email)) {
            Optional<User> usr = getUserByEmail(email);
            String userPwd = usr.get().getPassword();
            return userPwd.equals(password.toString());
        }else{
            return false;
        }
    }

    /**
     * Deletes a user from the repository based on their email address. If the user to be deleted
     * is the currently logged-in user, they will be logged out of the session automatically.
     *
     * @param email     The email address of the user to be deleted. Must not be null and should be
     *                  in a valid email format.
     * @return          True if the user was successfully deleted;
     *                  false if the user does not exist in the repository.
     */
    public boolean deleteUser(String email) {
        if (doesUserExist(email)) {
            Optional<User> usr = getUserByEmail(email);
            users.remove(usr.get());
            if (session.getUser().equals(usr.get())) {
                doLogout();
            }
            return true;
        }
        return false;
    }

    /**
     * Deletes a user from the repository if they exist. If the user to be deleted is currently
     * logged into the session, they will also be logged out automatically.
     *
     * @param user      The User object to be deleted. The user's email must not be null and should
     *                  be in a valid email format.
     * @return          True if the user was successfully deleted;
     *                  false if the user does not exist in the repository.
     */
    public boolean deleteUser(User user) {
        if (doesUserExist(user.getEmail())) {
            users.remove(user);
            if (session.getUser().equals(user)) {
                doLogout();
            }
            return true;
        }
        return false;
    }

    /**
     * Changes the role of a user identified by their email address.
     * If the user exists in the repository, their role is updated to the specified role ID.
     *
     * @param email     The email address of the user whose role is to be changed. Must not be null and
     *                  should be in a valid email format.
     * @param roleId    The new role ID to assign to the user. Cannot be null or empty.
     * @return          True if the user's role was successfully updated; false if the user does not exist.
     */
    public boolean changeRole(String email, String roleId) {
        if (doesUserExist(email)) {
            Optional<User> usr = getUserByEmail(email);
            usr.get().setRoleId(roleId);
            return true;
        }
        return false;
    }

    /**
     * Changes the role of a user if the user exists in the repository.
     * Updates the user's role to the specified role ID.
     *
     * @param user      The user whose role is to be changed. Must not be null and must contain a valid email.
     * @param roleId    The new role ID to assign to the user. Cannot be null or empty.
     * @return          True if the user's role was successfully updated;
     *                  false if the user does not exist.
     */
    public boolean changeRole(User user, String roleId) {
        if (doesUserExist(user)) {
            user.setRoleId(roleId);
            return true;
        }
        return false;
    }

    /**
     * Attempts to log a user into the system using the provided email and password.
     * Validates the user's credentials and starts a session if the credentials are correct.
     *
     * @param email         The email address of the user attempting to log in. Must not be null and should be in a valid email format.
     * @param password      The password of the user attempting to log in. Must not be null.
     * @return              True if the login was successful;
     *                      false otherwise.
     */
    public boolean doLogin(String email, String password) {
        if (isPasswordValid(email, password)){
            Optional<User> usr = getUserByEmail(email);
            return session.doLogin(usr.get());
        }
        return false;
    }

    /**
     * Logs the user out of the current session.
     *
     * This method clears the session data to log out the currently active user.
     *
     * @return      True if the logout operation was successful.
     */
    public boolean doLogout() {
        session.doLogout();
        return true;
    }

    /**
     * Retrieves the role ID of the user associated with the specified email address.
     * If the user with the given email address is not found, this method returns null.
     *
     * @param email     The email address of the user whose role ID is to be retrieved.
     *                  Must not be null and should be in a valid email format.
     * @return          The role ID of the user as a String, or null if the user is not found.
     */
    public String getRole(String email) {
        Optional<User> usr = getUserByEmail(email);
        return usr.map(User::getRoleId).orElse(null);
    }

    /**
     * Retrieves the current user session.
     *
     * @return  The {@code UserSession} object representing the current active session.
     */
    public UserSession getSession() {
        return session;
    }

    /**
     * Retrieves the email address of the currently logged-in user.
     *
     * This method fetches the email of the user associated with the current session.
     * If no user is currently logged in, the behavior of this method depends
     * on the implementation of {@code session.getUser()} and may result in a null pointer exception.
     *
     * @return      The email address of the current user as a String.
     */
    public String getCurrentUserEmail() {
        return session.getUser().getEmail();
    }
}
