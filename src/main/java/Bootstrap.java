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

        ctrl.createNewUser("editor", "editor@isep.ipp.pt", "ETesT01", AuthenticationController.ROLE_EDITOR);
        ctrl.createNewUser("player", "player@isep.ipp.pt", "PTesT01", AuthenticationController.ROLE_PLAYER);

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

        Map map1 = new Map("ExampleMap1", 20, 20, 1);
        Map map2 = new Map("ExampleMap2", 36, 36, 8);
        Map map3 = new Map("ExampleMap3", 50, 50, 13);
        Map map4 = new Map("ExampleMap4", 100, 100, 25);
        Map map5 = new Map("ExampleMap5", 200, 200, 5);
        Map map6 = new Map("ExampleMap6", 256, 256, 10);

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
                new Scenario("ExampleScenario1", new Restrictions(LocalDate.parse("1880-01-01"), LocalDate.parse("1890-12-31"), locomotiveTypes, historicalEvents, portBehaviors), null, new Budget(1000000)),
                new Scenario("ExampleScenario2", new Restrictions(LocalDate.parse("1998-01-01"), LocalDate.parse("2001-12-31"), locomotiveTypes, historicalEvents, portBehaviors), null, new Budget(1500000)),
                new Scenario("ExampleScenario3", new Restrictions(LocalDate.parse("1993-01-01"), LocalDate.parse("2015-12-31"), locomotiveTypes, historicalEvents, portBehaviors), null, new Budget(2000000)),
                new Scenario("ExampleScenario4", new Restrictions(LocalDate.parse("2006-01-01"), LocalDate.parse("2020-12-31"), locomotiveTypes, historicalEvents, portBehaviors), null, new Budget(5000000)),
                new Scenario("ExampleScenario5", new Restrictions(LocalDate.parse("2001-01-01"), LocalDate.parse("2010-12-31"), locomotiveTypes, historicalEvents, portBehaviors), null, new Budget(10000000))
        );

        List<Map> allMaps = mr.getMapList();

        for (Map map : allMaps) {
            for (Scenario baseScenario : scenarios) {
                Scenario scenarioForMap = new Scenario(
                        baseScenario.getScenarioName(), baseScenario.getRestrictions(), map, baseScenario.getBudget()
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

        lr.saveLocomotive(new Locomotive("Trevithick-1", "Steam", 1800, 1835, 10000, 6000, 7543, 10));
        lr.saveLocomotive(new Locomotive("Stephenson Rocket", "Steam", 1829, 1851, 16000, 6000, 7992, 16));
        lr.saveLocomotive(new Locomotive("John Bull", "Steam", 1831, 1853, 23000, 7000, 9256, 25));
        lr.saveLocomotive(new Locomotive("Dewitt Clinton", "Steam", 1833, 1855, 18000, 5000, 7187, 20));
        lr.saveLocomotive(new Locomotive("Prussian", "Steam", 1837, 1857, 35000, 8000, 10822, 30));
        lr.saveLocomotive(new Locomotive("American-C", "Steam", 1848, 1885, 46000, 5000, 12606, 40));
        lr.saveLocomotive(new Locomotive("Iron Duke", "Steam", 1855, 1894, 78000, 9000, 30736, 54));
        lr.saveLocomotive(new Locomotive("Eight Wheeler", "Steam", 1868, 1903, 59000, 5000, 17808, 48));
        lr.saveLocomotive(new Locomotive("Vulcan", "Steam", 1872, 1900, 32000, 4000, 6393, 30));
        lr.saveLocomotive(new Locomotive("Consolidation", "Steam", 1877, 1905, 51000, 8000, 19512, 40));
        lr.saveLocomotive(new Locomotive("3-Truck Shay", "Steam", 1882, 1930, 43000, 17000, 18227, 15));
        lr.saveLocomotive(new Locomotive("Mastodon", "Steam", 1890, 1911, 60000, 13000, 22395, 45));
        lr.saveLocomotive(new Locomotive("Ten Wheeler", "Steam", 1892, 1911, 66000, 11000, 21727, 50));
        lr.saveLocomotive(new Locomotive("Mogul", "Steam", 1895, 1915, 83000, 12000, 25131, 48));
        lr.saveLocomotive(new Locomotive("Atlantic", "Steam", 1902, 1929, 93000, 18000, 43556, 80));
        lr.saveLocomotive(new Locomotive("Camelback", "Steam", 1905, 1932, 75000, 9000, 15000, 30));
        lr.saveLocomotive(new Locomotive("Pacific", "Steam", 1908, 1945, 119000, 21000, 62516, 87));
        lr.saveLocomotive(new Locomotive("Class G10", "Steam", 1910, 1939, 98000, 38000, 50521, 50));
        lr.saveLocomotive(new Locomotive("Prairie", "Steam", 1912, 1953, 85000, 11000, 34157, 60));
        lr.saveLocomotive(new Locomotive("D16sb", "Steam", 1914, 1934, 65000, 9000, 21024, 45));
        lr.saveLocomotive(new Locomotive("Class 13H", "Steam", 1917, 1942, 102000, 36000, 46289, 40));
        lr.saveLocomotive(new Locomotive("USRA", "Steam", 1918, 1942, 90000, 13000, 29993, 40));
        lr.saveLocomotive(new Locomotive("Mikado", "Steam", 1919, 1950, 133000, 32000, 51072, 55));
        lr.saveLocomotive(new Locomotive("Class B12", "Steam", 1923, 1939, 146000, 14000, 32407, 71));
        lr.saveLocomotive(new Locomotive("USRA II", "Steam", 1930, 1948, 98000, 18000, 32236, 45));
        lr.saveLocomotive(new Locomotive("Class A4 Mallard", "Steam", 1935, 1955, 200000, 19000, 55136, 126));
        lr.saveLocomotive(new Locomotive("Hudson", "Steam", 1937, 1953, 210000, 25000, 54017, 90));
        lr.saveLocomotive(new Locomotive("Daylight", "Steam", 1937, 1952, 230000, 30000, 63611, 80));
        lr.saveLocomotive(new Locomotive("J3A Streamliner", "Steam", 1938, 1955, 255000, 28000, 112234, 103));
        lr.saveLocomotive(new Locomotive("Big Boy", "Steam", 1941, 1955, 375000, 75000, 84040, 68));
        lr.saveLocomotive(new Locomotive("T1", "Steam", 1945, 99999, 284000, 28000, 78213, 85));
        lr.saveLocomotive(new Locomotive("DB 18 201", "Steam", 1998, 99999, 400000, 75000, 144560, 120));

        lr.saveLocomotive(new Locomotive("F3A+B", "Diesel", 1945, 1968, 265000, 16000, 52150, 85));
        lr.saveLocomotive(new Locomotive("Alco PA-1", "Diesel", 1946, 1990, 210000, 16000, 52800, 90));
        lr.saveLocomotive(new Locomotive("F9", "Diesel", 1949, 1970, 337000, 18000, 63000, 110));
        lr.saveLocomotive(new Locomotive("GP9", "Diesel", 1954, 1980, 235000, 11000, 48028, 71));
        lr.saveLocomotive(new Locomotive("GP18", "Diesel", 1958, 1975, 245000, 15000, 55131, 83));
        lr.saveLocomotive(new Locomotive("V200", "Diesel", 1959, 1985, 160000, 19000, 53878, 87));
        lr.saveLocomotive(new Locomotive("Class 55 Deltic", "Diesel", 1961, 1981, 480000, 15000, 52155, 100));
        lr.saveLocomotive(new Locomotive("FP45", "Diesel", 1968, 99999, 366000, 14000, 65740, 106));
        lr.saveLocomotive(new Locomotive("SD45", "Diesel", 1972, 1994, 280000, 21000, 36009, 65));
        lr.saveLocomotive(new Locomotive("SDP40", "Diesel", 1973, 1989, 292000, 18000, 61966, 103));
        lr.saveLocomotive(new Locomotive("Dash-9", "Diesel", 1993, 99999, 478000, 18000, 68410, 70));
        lr.saveLocomotive(new Locomotive("AMD-103", "Diesel", 1993, 99999, 425000, 25000, 88068, 105));
        lr.saveLocomotive(new Locomotive("Class 232", "Diesel", 1997, 99999, 492000, 35000, 72625, 75));

        lr.saveLocomotive(new Locomotive("1-3 Bobo", "Electric", 1895, 1935, 85000, 6000, 33116, 54));
        lr.saveLocomotive(new Locomotive("Be 4/6 II", "Electric", 1920, 1950, 61000, 11000, 14701, 35));
        lr.saveLocomotive(new Locomotive("Ee 3/3", "Electric", 1932, 1948, 47000, 7000, 11880, 30));
        lr.saveLocomotive(new Locomotive("Class 1045", "Electric", 1927, 1950, 95000, 6000, 11636, 40));
        lr.saveLocomotive(new Locomotive("GG1", "Electric", 1935, 1970, 285000, 19000, 42721, 100));
        lr.saveLocomotive(new Locomotive("Class E18", "Electric", 1936, 1966, 93000, 16000, 27493, 93));
        lr.saveLocomotive(new Locomotive("Ae 8/14", "Electric", 1939, 1955, 210000, 24000, 47906, 68));
        lr.saveLocomotive(new Locomotive("Class 1020", "Electric", 1941, 1981, 119000, 18000, 21545, 56));
        lr.saveLocomotive(new Locomotive("E69", "Electric", 1955, 1970, 86000, 8000, 12837, 31));
        lr.saveLocomotive(new Locomotive("TGVx", "Electric", 1957, 1973, 850000, 80000, 71672, 155));
        lr.saveLocomotive(new Locomotive("Penn. E44", "Electric", 1960, 1985, 370000, 22000, 37971, 70));
        lr.saveLocomotive(new Locomotive("Shinkansen Bullet", "Electric", 1966, 2002, 650000, 66000, 66680, 130));
        lr.saveLocomotive(new Locomotive("E60CP", "Electric", 1973, 1998, 310000, 19000, 38043, 85));
        lr.saveLocomotive(new Locomotive("TGV", "Electric", 1973, 99999, 750000, 72000, 81664, 155));
        lr.saveLocomotive(new Locomotive("Class E111", "Electric", 1974, 99999, 390000, 17000, 38016, 85));
        lr.saveLocomotive(new Locomotive("E656 FS", "Electric", 1975, 1995, 226000, 17000, 37734, 93));
        lr.saveLocomotive(new Locomotive("Thalys Bullet", "Electric", 1994, 99999, 1000000, 78000, 111756, 186));
        lr.saveLocomotive(new Locomotive("Eurostar", "Electric", 1994, 99999, 1600000, 65000, 96135, 175));
        lr.saveLocomotive(new Locomotive("Brenner", "Electric", 1999, 99999, 1250000, 175000, 69292, 140));
        lr.saveLocomotive(new Locomotive("Mag-Lev TBX-1", "Electric", 2008, 99999, 2500000, 200000, 305609, 280));
    }
}
