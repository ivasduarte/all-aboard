import domain.*;
import repositories.LocomotiveRepository;
import repositories.MapRepository;
import repositories.Repositories;
import repositories.ScenarioRepository;
import ui.authorization.AuthenticationController;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * This class is responsible to add the default scenario to the database.
 */
public class Bootstrap implements Runnable {

    /**
     * Executes the initialization process for the application by performing the following operations:
     *
     * 1. Calls {@link #addUsers()} to add default users to the application. This ensures that the system
     *    contains a minimum set of predefined users with specific roles.
     *
     * 2. Calls {@link #createMap()} to create and add predefined maps to the map repository. This step
     *    ensures that the application has a predefined set of maps required for gameplay scenarios.
     *
     * 3. Calls {@link #createScenario()} to create scenarios associated with the predefined maps. These
     *    scenarios include various restrictions, budgets, and locomotive types relevant to the simulation.
     *
     * 4. Calls {@link #gameLocomotives()} to add defined locomotives into the system repository. This ensures
     *    that all available locomotives are ready for the simulation each time the application starts.
     *
     * This method is intended to be the entry point for initializing the system, ensuring that all necessary
     * components and data are preloaded for proper application functionality.
     */
    public void run() {
        addUsers();
        createMap();
        createScenario();
        gameLocomotives();
    }

    /**
     * Adds default users to the system using the {@link AuthenticationController}.
     * This method creates two predefined users:
     *
     * 1. An editor with the role "EDITOR".
     * 2. A player with the role "PLAYER".
     *
     * The added users are intended for initial testing or setup purposes and
     * can be utilized in various parts of the application where these roles
     * are required.
     *
     * Preconditions:
     * - The {@link AuthenticationController} must be properly initialized and
     *   connected to the authentication repository.
     *
     * Postconditions:
     * - Two users with the specified roles, names, emails, and passwords are
     *   created and stored in the system's authentication repository.
     */
    public void addUsers() {
        AuthenticationController ctrl = new AuthenticationController();

        ctrl.createNewUser("editor", "editor@isep.ipp.pt", "editor", AuthenticationController.ROLE_EDITOR);
        ctrl.createNewUser("player", "player@isep.ipp.pt", "player", AuthenticationController.ROLE_PLAYER);
    }

    /**
     * Creates and adds multiple predefined maps to the map repository.
     * After adding the maps, it retrieves and returns a specific map from the repository
     * with the name "ExampleMap" if it exists.
     *
     * @return an {@code Optional<Map>} containing the map with the name "ExampleMap"
     *         if it is found in the repository, or an empty {@code Optional} if it is not found.
     */
    public Optional<Map> createMap() {
        MapRepository mr = Repositories.getInstance().getMapRepository();

        Map map1 = new Map("ExampleMap1", 20, 20);
        Map map2 = new Map("ExampleMap2", 36, 36);
        Map map3 = new Map("ExampleMap3", 50, 50);
        Map map4 = new Map("ExampleMap4", 100, 100);
        Map map5 = new Map("ExampleMap5", 200, 200);
        Map map6 = new Map("ExampleMap6", 256, 256);

        mr.add(map1);
        mr.add(map2);
        mr.add(map3);
        mr.add(map4);
        mr.add(map5);
        mr.add(map6);

        return mr.getMap("ExampleMap");
    }

    /**
     * Creates and assigns predefined scenarios to all available maps in the repository.
     *
     * This method performs the following tasks:
     * 1. Retrieves instances of {@code ScenarioRepository} and {@code MapRepository}.
     * 2. Defines a list of predefined scenarios, each with specific restrictions, budgets,
     *    and locomotive types. The predefined scenarios do not have an associated map initially.
     * 3. Retrieves all maps from the {@code MapRepository}.
     * 4. Iterates over all the maps and associates each map with every predefined scenario.
     *    For each map, it creates a new {@code Scenario} by appending the map's name to the
     *    base scenario name, assigns the map, and uses the same restrictions and budget
     *    from the base scenario.
     * 5. Adds each newly created scenario to the respective map and stores it in the
     *    {@code ScenarioRepository}.
     *
     * Preconditions:
     * - The repositories must be properly initialized and accessible via the {@code Repositories} class.
     *
     * Postconditions:
     * - Each map in the repository will have a set of associated scenarios based on the predefined list.
     * - The newly created scenarios will be added to both the map and the scenario repository.
     */
    public void createScenario() {
        ScenarioRepository sr = Repositories.getInstance().getScenarioRepository();
        MapRepository mr = Repositories.getInstance().getMapRepository();

        List<String> locomotiveTypes = new ArrayList<>();
        List<String> historicalEvents = new ArrayList<>();
        List<PortBehavior> portBehaviors = new ArrayList<>();
        locomotiveTypes.add("steam");

        List<Scenario> scenarios = List.of(
                new Scenario("ExampleScenario1", new Restrictions(LocalDate.parse("1880-01-01"), LocalDate.parse("1890-12-31"), locomotiveTypes, historicalEvents, portBehaviors), null, new Budget(1000)),
                new Scenario("ExampleScenario2", new Restrictions(LocalDate.parse("1998-01-01"), LocalDate.parse("2001-12-31"), locomotiveTypes, historicalEvents, portBehaviors), null, new Budget(1500)),
                new Scenario("ExampleScenario3", new Restrictions(LocalDate.parse("1993-01-01"), LocalDate.parse("2015-12-31"), locomotiveTypes, historicalEvents, portBehaviors), null, new Budget(2000)),
                new Scenario("ExampleScenario4", new Restrictions(LocalDate.parse("2006-01-01"), LocalDate.parse("2020-12-31"), locomotiveTypes, historicalEvents, portBehaviors), null, new Budget(5000)),
                new Scenario("ExampleScenario5", new Restrictions(LocalDate.parse("2001-01-01"), LocalDate.parse("2010-12-31"), locomotiveTypes, historicalEvents, portBehaviors), null, new Budget(10000))
        );

        List<Map> allMaps = mr.getMapList();

        for (Map map : allMaps) {
            for (Scenario baseScenario : scenarios) {
                Scenario scenarioForMap = new Scenario(
                        baseScenario.getScenarioName() + "_for_" + map.getName(),
                        baseScenario.getRestrictions(),
                        map,
                        baseScenario.getBudget()
                );
                map.addNewScenario(scenarioForMap);
                sr.add(scenarioForMap);
            }
        }
    }


    /**
     * This method is responsible to add every locomotive available on the simulator to the repository, everytime the simulation starts.
     */
    public void gameLocomotives() {
        LocomotiveRepository lr = Repositories.getInstance().getLocomotiveRepository();

        lr.saveLocomotive(new Locomotive("Trevithick-1", "Steam", 1800, 1835, 10, 6, 8, 10));
        lr.saveLocomotive(new Locomotive("Stephenson Rocket", "Steam", 1829, 1851, 16, 6, 8, 16));
        lr.saveLocomotive(new Locomotive("John Bull", "Steam", 1831, 1853, 23, 7, 9, 25));
        lr.saveLocomotive(new Locomotive("Dewitt Clinton", "Steam", 1833, 1855, 18, 5, 7, 20));
        lr.saveLocomotive(new Locomotive("Prussian", "Steam", 1837, 1857, 35, 8, 11, 30));
        lr.saveLocomotive(new Locomotive("American-C", "Steam", 1848, 1885, 46, 5, 13, 40));
        lr.saveLocomotive(new Locomotive("Iron Duke", "Steam", 1855, 1894, 78, 9, 31, 54));
        lr.saveLocomotive(new Locomotive("Eight Wheeler", "Steam", 1868, 1903, 59, 5, 18, 48));
        lr.saveLocomotive(new Locomotive("Vulcan", "Steam", 1872, 1900, 32, 4, 6, 30));
        lr.saveLocomotive(new Locomotive("Consolidation", "Steam", 1877, 1905, 51, 8, 20, 40));
        lr.saveLocomotive(new Locomotive("3-Truck Shay", "Steam", 1882, 1930, 43, 17, 18, 15));
        lr.saveLocomotive(new Locomotive("Mastodon", "Steam", 1890, 1911, 60, 13, 23, 45));
        lr.saveLocomotive(new Locomotive("Ten Wheeler", "Steam", 1892, 1911, 66, 11, 22, 50));
        lr.saveLocomotive(new Locomotive("Mogul", "Steam", 1895, 1915, 83, 12, 25, 48));
        lr.saveLocomotive(new Locomotive("Atlantic", "Steam", 1902, 1929, 93, 18, 44, 80));
        lr.saveLocomotive(new Locomotive("Camelback", "Steam", 1905, 1932, 75, 9, 15, 30));
        lr.saveLocomotive(new Locomotive("Pacific", "Steam", 1908, 1945, 119, 21, 63, 87));
        lr.saveLocomotive(new Locomotive("Class G10", "Steam", 1910, 1939, 98, 38, 51, 50));
        lr.saveLocomotive(new Locomotive("Prairie", "Steam", 1912, 1953, 85, 11, 34, 60));
        lr.saveLocomotive(new Locomotive("D16sb", "Steam", 1914, 1934, 65, 9, 21, 45));
        lr.saveLocomotive(new Locomotive("Class 13H", "Steam", 1917, 1942, 102, 36, 46, 40));
        lr.saveLocomotive(new Locomotive("USRA", "Steam", 1918, 1942, 90, 13, 30, 40));
        lr.saveLocomotive(new Locomotive("Mikado", "Steam", 1919, 1950, 133, 32, 51, 55));
        lr.saveLocomotive(new Locomotive("Class B12", "Steam", 1923, 1939, 146, 14, 32, 71));
        lr.saveLocomotive(new Locomotive("USRA II", "Steam", 1930, 1948, 98, 18, 32, 45));
        lr.saveLocomotive(new Locomotive("Class A4 Mallard", "Steam", 1935, 1955, 200, 19, 55, 126));
        lr.saveLocomotive(new Locomotive("Hudson", "Steam", 1937, 1953, 210, 25, 54, 90));
        lr.saveLocomotive(new Locomotive("Daylight", "Steam", 1937, 1952, 230, 30, 64, 80));
        lr.saveLocomotive(new Locomotive("J3A Streamliner", "Steam", 1938, 1955, 255, 28, 112, 103));
        lr.saveLocomotive(new Locomotive("Big Boy", "Steam", 1941, 1955, 375, 75, 84, 68));
        lr.saveLocomotive(new Locomotive("T1", "Steam", 1945, 99999, 284, 28, 78, 85));
        lr.saveLocomotive(new Locomotive("DB 18 201", "Steam", 1998, 99999, 400, 75, 145, 120));

        lr.saveLocomotive(new Locomotive("F3A+B", "Diesel", 1945, 1968, 265, 16, 52, 85));
        lr.saveLocomotive(new Locomotive("Alco PA-1", "Diesel", 1946, 1990, 210, 16, 53, 90));
        lr.saveLocomotive(new Locomotive("F9", "Diesel", 1949, 1970, 337, 18, 63, 110));
        lr.saveLocomotive(new Locomotive("GP9", "Diesel", 1954, 1980, 235, 11, 48, 71));
        lr.saveLocomotive(new Locomotive("GP18", "Diesel", 1958, 1975, 245, 15, 55, 83));
        lr.saveLocomotive(new Locomotive("V200", "Diesel", 1959, 1985, 160, 19, 54, 87));
        lr.saveLocomotive(new Locomotive("Class 55 Deltic", "Diesel", 1961, 1981, 480, 15, 52, 100));
        lr.saveLocomotive(new Locomotive("FP45", "Diesel", 1968, 99999, 366, 14, 66, 106));
        lr.saveLocomotive(new Locomotive("SD45", "Diesel", 1972, 1994, 280, 21, 36, 65));
        lr.saveLocomotive(new Locomotive("SDP40", "Diesel", 1973, 1989, 292, 18, 62, 103));
        lr.saveLocomotive(new Locomotive("Dash-9", "Diesel", 1993, 99999, 478, 18, 68, 70));
        lr.saveLocomotive(new Locomotive("AMD-103", "Diesel", 1993, 99999, 425, 25, 88, 105));
        lr.saveLocomotive(new Locomotive("Class 232", "Diesel", 1997, 99999, 492, 35, 73, 75));

        lr.saveLocomotive(new Locomotive("1-3 Bobo", "Electric", 1895, 1935, 85, 6, 33, 54));
        lr.saveLocomotive(new Locomotive("Be 4/6 II", "Electric", 1920, 1950, 61, 11, 14, 35));
        lr.saveLocomotive(new Locomotive("Ee 3/3", "Electric", 1932, 1948, 47, 7, 12, 30));
        lr.saveLocomotive(new Locomotive("Class 1045", "Electric", 1927, 1950, 95, 6, 12, 40));
        lr.saveLocomotive(new Locomotive("GG1", "Electric", 1935, 1970, 285, 19, 43, 100));
        lr.saveLocomotive(new Locomotive("Class E18", "Electric", 1936, 1966, 93, 16, 27, 93));
        lr.saveLocomotive(new Locomotive("Ae 8/14", "Electric", 1939, 1955, 210, 24, 48, 68));
        lr.saveLocomotive(new Locomotive("Class 1020", "Electric", 1941, 1981, 119, 18, 22, 56));
        lr.saveLocomotive(new Locomotive("E69", "Electric", 1955, 1970, 86, 8, 13, 31));
        lr.saveLocomotive(new Locomotive("TGVx", "Electric", 1957, 1973, 850, 80, 72, 155));
        lr.saveLocomotive(new Locomotive("Penn. E44", "Electric", 1960, 1985, 370, 22, 38, 70));
        lr.saveLocomotive(new Locomotive("Shinkansen Bullet", "Electric", 1966, 2002, 650, 66, 67, 130));
        lr.saveLocomotive(new Locomotive("E60CP", "Electric", 1973, 1998, 310, 19, 38, 85));
        lr.saveLocomotive(new Locomotive("TGV", "Electric", 1973, 99999, 750, 72, 82, 155));
        lr.saveLocomotive(new Locomotive("Class E111", "Electric", 1974, 99999, 390, 17, 38, 85));
        lr.saveLocomotive(new Locomotive("E656 FS", "Electric", 1975, 1995, 226, 17, 38, 93));
        lr.saveLocomotive(new Locomotive("Thalys Bullet", "Electric", 1994, 99999, 1000, 78, 112, 186));
        lr.saveLocomotive(new Locomotive("Eurostar", "Electric", 1994, 99999, 1600, 65, 96, 175));
        lr.saveLocomotive(new Locomotive("Brenner", "Electric", 1999, 99999, 1250, 175, 69, 140));
        lr.saveLocomotive(new Locomotive("Mag-Lev TBX-1", "Electric", 2008, 99999, 2500, 200, 306, 280));
    }
}
