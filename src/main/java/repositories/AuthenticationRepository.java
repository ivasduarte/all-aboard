package repositories;

import pt.isep.lei.esoft.auth.AuthFacade;
import pt.isep.lei.esoft.auth.UserSession;

public class AuthenticationRepository {
    private final AuthFacade authentication;

    public AuthenticationRepository() {
        authentication = new AuthFacade();
    }

    public boolean doLogin(String email, String password){
        return authentication.doLogin(email, password).isLoggedIn();
    }

    public void doLogout(){
        authentication.doLogout();
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
