package ui.authorization;

import domain.ApplicationSession;
import pt.ipp.isep.dei.repository.AuthenticationRepository;
import repositories.Repositories;

public class AuthenticationController {
    public static final String ROLE_ADMIN = "ADMINISTRATOR";
    public static final String ROLE_EMPLOYEE = "EMPLOYEE";

    private final ApplicationSession applicationSession;
    private final AuthenticationRepository authenticationRepository;

    public AuthenticationController(ApplicationSession applicationSession) {
        this.applicationSession = applicationSession;
        this.authenticationRepository = Repositories.getInstance().getAuthenticationRepository();
    }

    public boolean doLogin(String email, String password) {
        try{
            return authenticationRepository.doLogin(email, password);
        }catch(IllegalArgumentException ex){
            return false;
        }
    }


    public void doLogout() {
        authenticationRepository.doLogout();
    }

}
