package domain;

public class ApplicationSession {
    private static ApplicationSession instance;
    private UserSession userSession;

    private ApplicationSession() {}

    public static ApplicationSession getInstance() {
        if (instance == null) {
            instance = new ApplicationSession();
        }
        return instance;
    }

    public UserSession getCurrentSession() {
        return userSession;
    }

    public void setUserSession(UserSession session) {
        this.userSession = session;
    }
}