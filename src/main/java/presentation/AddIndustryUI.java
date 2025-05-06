package presentation;

import controller.AddIndustryController;
import controller.DisplayMapController;
import domain.Map;
import repositories.IndustryRepository;
import repositories.MapRepository;
import repositories.ScenarioRepository;

import java.util.List;
import java.util.Optional;
import java.util.Scanner;

public class AddIndustryUI implements Runnable{
    private AddIndustryController ctrl = new AddIndustryController();
    private DisplayMapController controller = new DisplayMapController();

    private int mapID;
    private int scenarioID;
    private int positionX;
    private int positionY;
    private String industryType;

    /**
     * This method will be responsible to show the UI to user.
     * This method will also collect all the inputs related to the industry and pass them to the controller.
     */
    public void getInput() {
        Scanner sc = new Scanner(System.in);

        MapRepository mr = new MapRepository();

        List<Map> mapList = controller.getMapList();

        for (int i = 0; i < mapList.size(); i++) {
            Map map = mapList.get(i);
            System.out.println((i + 1) + ". " + map.getMapName());
            System.out.println();
        }

        System.out.println("Select a map from the list: ");
        int mapID = sc.nextInt() - 1;

        ScenarioRepository sr = new ScenarioRepository();
        sr.displayScenarioList();
        System.out.println("Enter the scenario ID: ");
        int scenarioID = sc.nextInt() - 1;

        System.out.println("\nEnter the position X of the industry: ");
        int positionX = sc.nextInt();

        System.out.println("\nEnter the position Y of the industry: ");
        int positionY = sc.nextInt();

        String[] industryTypes = new String[3];
        industryTypes[0] = "Primary sector industry";
        industryTypes[1] = "Transforming industry";
        industryTypes[2] = "Mixed industry";
        for (int i = 0; i < industryTypes.length; i++) {
            System.out.println((i+1) + ". " + industryTypes[i]);
        }
        System.out.println("Select the industry type you want: ");
        String industryType = sc.nextLine();

        String result = ctrl.addIndustry(mapID, scenarioID, positionX, positionY, industryType);

        if (result.startsWith("ERROR")) {
            showError(result);
        } else {
            showSuccess(result);
        }
    }

    @Override
    /**
     * This method will be responsible to show the UI related to adding an industry to the editor when this action is selected.
     */
    public void run() {
        System.out.println("\n\n------------------------ Add Industry ------------------------");
        getInput();
        submitData();
    }

    /**
     * This method will be responsible to show the success or error message to the Editor.
     */
    private void submitData() {
        Optional<String> result = Optional.ofNullable(ctrl.addIndustry(mapID, scenarioID, positionX, positionY, industryType));

        if (result.isPresent()) {
            System.out.println("\nIndustry successfully added!");
        } else {
            System.out.println("\nIndustry not added!");
        }
    }

    public void showError(String result) {
        System.out.println("ERROR: " + result);
    }
    public void showSuccess(String result) {
        System.out.println("SUCCESS: " + result);
    }
}
