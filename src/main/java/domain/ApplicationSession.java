package domain;

/**
 * Represents the application-wide session and provides a singleton instance to manage the current user session.
 *
 * This class ensures that there is only one active instance of an application session and provides methods to
 * retrieve and set the current user session.
 */
public class ApplicationSession {
    private static ApplicationSession instance;
    private UserSession userSession;

    /**
     * Private constructor for the ApplicationSession class to prevent direct instantiation.
     *
     * This ensures that the ApplicationSession allows only one instance of the class to be created and accessed
     * globally through the static {@code getInstance()} method.
     */
    private ApplicationSession() {}

    /**
     * Retrieves the singleton instance of the {@code ApplicationSession}.
     *
     * This method ensures that only one instance of the {@code ApplicationSession} exists throughout the application
     * lifecycle. If no instance currently exists, a new one is created.
     *
     * @return      The singleton instance of {@code ApplicationSession}.
     */
    public static ApplicationSession getInstance() {
        if (instance == null) {
            instance = new ApplicationSession();
        }
        return instance;
    }

    /**
     * Retrieves the current user session associated with the application.
     *
     * @return          The {@code UserSession} representing the active user session, or {@code null} if no session is set.
     */
    public UserSession getCurrentSession() {
        return userSession;
    }

    /**
     * Sets the current user session associated with the application.
     *
     * This method updates the active {@code UserSession} instance managed by the application,
     * replacing any previously set session.
     *
     * @param session   The {@code UserSession} to be set as the current session.
     */
    public void setUserSession(UserSession session) {
        this.userSession = session;
    }
}