package domain;

public class ApplicationSession {
    private static ApplicationSession instance = new ApplicationSession();
    private UserSession currentSession;

    private ApplicationSession() {}

    public static ApplicationSession getInstance() {
        return instance;
    }

    public UserSession getCurrentSession() {
       return currentSession;
    }
}
