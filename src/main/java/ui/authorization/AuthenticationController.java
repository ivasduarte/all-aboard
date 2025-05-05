package ui.authorization;

import domain.ApplicationSession;
import repositories.Repositories;
import pt.isep.lei.esoft.auth.mappers.dto.UserRoleDTO;
import repositories.AuthenticationRepository;

import java.util.List;

public class AuthenticationController{
    public static final String ROLE_EDITOR = "EDITOR";
    public static final String ROLE_PLAYER = "PLAYER";
    private final AuthenticationRepository authenticationRepository;

    public AuthenticationController() {
        this.authenticationRepository = Repositories.getInstance().getAuthenticationRepository();
    }

    public boolean doLogin(String email, String password) {
        try{
            return authenticationRepository.doLogin(email, password);
        }catch(IllegalArgumentException ex){
            return false;
        }
    }

    public List<UserRoleDTO> getUserRoles() {
        if (authenticationRepository.getCurrentUserSession().isLoggedIn()) {
            return authenticationRepository.getCurrentUserSession().getUserRoles();
        }
        return null;
    }

    public void doLogout() {
        authenticationRepository.doLogout();
    }
}
