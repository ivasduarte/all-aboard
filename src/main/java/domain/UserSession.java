package domain;

import java.util.Map;
import java.util.HashMap;


public class UserSession {
    private static UserSession session = null;
    private Map<String, User> user;

    private UserSession(){ user = new HashMap<>(); }

    public static UserSession getInstance(){
        if(session == null){
            session = new UserSession();
        }
        return session;
    }

    public User getUser(){
        return user.get("currentUser");
    }

    public void setUser(User usr){ user.put("currentUser", usr);}

    public boolean doLogin(User usr){
        setUser(usr);
        return true;
    }

    public void clear(){ user.clear(); }

    public boolean doLogout(){
        clear();
        return true;
    }
}
