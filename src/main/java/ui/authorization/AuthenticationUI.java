package ui.authorization;

import pt.ipp.isep.dei.controller.AuthenticationController;
import ui.utils.Utils;

public class AuthenticationUI implements Runnable {
    private final AuthenticationController ctrl;

    public AuthenticationUI() {
        ctrl = new AuthenticationController();
    }

    public void run() {
        boolean success = doLogin();
    }

    private boolean doLogin() {
        System.out.println("\n\n--- LOGIN UI ---------------------------");

        int maxAttempts = 3;
        boolean success = false;
        do{
            maxAttempts--;
            String id = Utils.readLineFromConsole("Enter UserId/Email: ");
            String password = Utils.readLineFromConsole("Enter Password: ");

            success = ctrl.doLogin(id, password);
            if(!success){
                System.out.println("Invalid UserId and/or Password. \n You have  " + maxAttempts + " more attempt(s).");
            }
        }while(!success && maxAttempts > 0);
        return success;
    }

    private void logout() {
        ctrl.doLogout();
    }
}
