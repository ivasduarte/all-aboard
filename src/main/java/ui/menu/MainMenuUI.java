package ui.menu;

import ui.authorization.AuthenticationUI;
import ui.utils.DevelopmentTeamUI;
import ui.utils.Utils;

import java.util.ArrayList;
import java.util.List;


public class MainMenuUI implements Runnable {
    public void run() {
        List<MenuItem> options = new ArrayList<>();
        options.add(new MenuItem("Login", new AuthenticationUI(0)));
        options.add(new MenuItem("Create User", new AuthenticationUI(1)));
        options.add(new MenuItem("Development Team", new DevelopmentTeamUI()));

        int option;
        do {
            option = Utils.showAndSelectIndex(options, "\n\n-------------------------- MAIN MENU --------------------------");
            if (option >= 0 && option < options.size()) {
                options.get(option).run();
            }
        } while (option != -1);
    }
}

