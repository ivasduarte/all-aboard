package domain;

import java.util.Map;
import java.util.HashMap;

/**
 * Manages the session for users within the system, providing functionality for login, logout,
 * and session persistence using the singleton pattern. This class ensures that only one
 * instance of UserSession exists during the application's lifecycle.
 */
public class UserSession {
    private static UserSession session = null;
    private Map<String, User> user;

    /**
     * Constructs a new instance of UserSession.
     *
     * This is a private constructor to enforce the singleton pattern.
     * It initializes the internal data structure used to manage user session information.
     */
    private UserSession(){ user = new HashMap<>(); }

    /**
     * Retrieves the single instance of the UserSession class.
     *
     * This method ensures that only one instance of the UserSession class
     * exists throughout the application's lifecycle, following the singleton pattern.
     * If no instance exists, a new one is created and returned.
     *
     * @return The single instance of the UserSession class.
     */
    public static UserSession getInstance(){
        if(session == null){
            session = new UserSession();
        }
        return session;
    }

    /**
     * Retrieves the current user associated with the session.
     *
     * This method fetches the user stored with the key "currentUser" in the internal user map
     * of the session. If no user is associated with the session, this method may return null.
     *
     * @return The current User object associated with the session, or null if no user is set.
     */
    public User getUser(){
        return user.get("currentUser");
    }

    /**
     * Updates the current user in the session.
     *
     * This method associates the specified {@code User} instance with the key
     * "currentUser" in the session. Any previously set user will be replaced
     * with the new user. The {@code User} instance provided must not be null.
     *
     * @param usr   The {@code User} object to be set as the current user in the session.
     *              Must not be null.
     */
    public void setUser(User usr){ user.put("currentUser", usr);}

    /**
     * Logs in the specified user by updating the current session with the provided user instance.
     *
     * This method sets the provided {@code User} object as the current user in the session using
     * the {@code setUser} method. The user must be a valid, non-null {@code User} instance.
     * Upon successful login, this method returns {@code true}.
     *
     * @param usr   The {@code User} object to be logged in. Must not be null.
     * @return      {@code true} if the user was successfully logged in.
     */
    public boolean doLogin(User usr){
        setUser(usr);
        return true;
    }

    /**
     * Clears all entries in the internal user session data structure.
     *
     * This method removes all user mappings in the session, effectively resetting
     * the session's state to an empty state. It is typically used to log out all users
     * or to reset the session data entirely.
     */
    public void clear(){ user.clear(); }

    /**
     * Logs the current user out of the session by clearing all session data.
     *
     * This method invokes the {@code clear} method to reset the session's internal data structure,
     * effectively logging out the currently active user. Once the session is cleared, this method
     * returns {@code true} to indicate a successful logout operation.
     *
     * @return  {@code true} if the logout operation was successful.
     */
    public boolean doLogout(){
        clear();
        return true;
    }
}
