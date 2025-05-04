package repositories;

import domain.UserSession;
import ui.authorization.Authentication;
import ui.authorization.AuthenticationUI;

public class AuthenticationRepository {
    private final Authentication authentication;

    public AuthenticationRepository() {
        authentication = new Authentication();
    }

    public boolean doLogin(String email, String password){
        return authentication.doLogin(email, password).isLoggedIn();
    }

    public void doLogout(){
        return authentication.doLogout();
    }

    public UserSession getCurrentUserSession(){
        return authentication.getCurrentUserSession();
    }

    public boolean addUserRole(String id, String description){
        return authentication.addUserRole(id, description);
    }

    public boolean addUserWithRole(String id, String email, String password, String roleID){
        return authentication.addUserWithRole(id, email, password, roleID);
    }
}
