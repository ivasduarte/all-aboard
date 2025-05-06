package ui.menu;

import presentation.AddIndustryUI;
import presentation.CreateMapUI;
import presentation.CreateScenarioUI;
import pt.ipp.isep.dei.ui.console.ShowTextUI;
import presentation.AddCityUI;
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
        options.add(new MenuItem("Build a Station", new ShowTextUI("You have chosen Build a Station.")));
        options.add(new MenuItem("Upgrade a Station", new ShowTextUI("You have chosen Upgrade a Station.")));
        options.add(new MenuItem("Check Station Details", new ShowTextUI("You have chosen Check Station Details.")));
        options.add(new MenuItem("Build a Railway Line", new ShowTextUI("You have chosen Build a Railway Line.")));
        options.add(new MenuItem("Buy a Locomotive", new ShowTextUI("You have chosen Buy a Locomotive.")));
        options.add(new MenuItem("Assign a Train to a Route", new ShowTextUI("You have chosen Assign a Train to a Route.")));
        options.add(new MenuItem("Display Train List", new ShowTextUI("You have chosen Display Train List.")));

        int option = 0;
        do {
            option = Utils.showAndSelectIndex(options, "\n\n------------------------- EDITOR MENU -------------------------");

            if ((option >= 0) && (option < options.size())) {
                options.get(option).run();
            }
        } while (option != -1);
    }
}
