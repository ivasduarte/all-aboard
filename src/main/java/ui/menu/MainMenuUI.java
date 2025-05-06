package ui.menu;

import ui.authorization.AuthenticationUI;
import ui.utils.DevelopmentTeamUI;
import ui.utils.Utils;

import java.util.ArrayList;
import java.util.List;

public class MainMenuUI implements Runnable{

    public void run() {
        List<MenuItem> options = new ArrayList<MenuItem>();
        options.add(new MenuItem("Login", new AuthenticationUI()));
        options.add(new MenuItem("Development Team", new DevelopmentTeamUI()));

        int option = 0;
        do {
            option = Utils.showAndSelectIndex(options, "\n\n-------------------------- MAIN MENU --------------------------");

            if ((option >= 0) && (option < options.size())) {
                options.get(option).run();
            }
        } while (option != -1);
    }
}
