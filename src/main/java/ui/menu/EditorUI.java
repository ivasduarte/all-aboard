package ui.menu;

import presentation.*;
import pt.ipp.isep.dei.ui.console.ShowTextUI;
import ui.utils.DisplayMapUI;
import ui.utils.Utils;

import java.util.ArrayList;
import java.util.List;

public class EditorUI implements Runnable {

    public EditorUI() {}

    public void run() {
        List<MenuItem> options = new ArrayList<MenuItem>();
        options.add(new MenuItem("Create Map", new CreateMapUI()));
        options.add(new MenuItem("Display Map List", new DisplayMapUI()));
        options.add(new MenuItem("Add an Industry", new AddIndustryUI()));
        options.add(new MenuItem("Add a City", new AddCityUI()));
        options.add(new MenuItem("Create a Scenario", new CreateScenarioUI()));

        int option = 0;
        do {
            option = Utils.showAndSelectIndex(options, "\n\n------------------------- EDITOR MENU -------------------------");

            if ((option >= 0) && (option < options.size())) {
                options.get(option).run();
            }
        } while (option != -1);
    }
}
