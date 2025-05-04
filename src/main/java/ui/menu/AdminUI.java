package ui.menu;

import pt.ipp.isep.dei.ui.console.ShowTextUI;
import ui.utils.CreateMapUI;
import ui.utils.Utils;

import java.util.ArrayList;
import java.util.List;

public class AdminUI implements Runnable {

    public AdminUI() {}

    public void run() {
        List<MenuItem> options = new ArrayList<MenuItem>();
        options.add(new MenuItem("Create Map", (Runnable) new CreateMapUI()));
        options.add(new MenuItem("Task2", new ShowTextUI("You have chosen Task2.")));
        options.add(new MenuItem("Task3", new ShowTextUI("You have chosen Task3.")));
        options.add(new MenuItem("Task4", new ShowTextUI("You have chosen Task4.")));

        int option = 0;
        do {
            option = Utils.showAndSelectIndex(options, "\n\n--- ADMIN MENU -------------------------");

            if ((option >= 0) && (option < options.size())) {
                options.get(option).run();
            }
        } while (option != -1);
    }
}
