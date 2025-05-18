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
     * Executes the initialization process for the application by invoking the following methods in sequence:
     *
     * 1. `addUsers`: Ensures the system is populated with predefined default users, such as an editor and a player,
     *    each assigned specific roles required for system functionality.
     *
     * 2. `createScenario`: Creates a scenario and associates it with a generated map. The map is fetched or created
     *    using the `createMap` method, which initializes a default map instance if not already available in the system.
     *
     * 3. `gameLocomotives`: Adds all locomotives available in the simulation to the repository upon startup, ensuring
     *    that locomotive data is consistently loaded for gameplay.
     *
     * This method is typically used during the startup of the application to properly set up the required entities
     * and resources for the system to run as expected.
     *
     * Preconditions:
     * - The database or repository should be properly initialized before the method is executed.
     *
     * Postconditions:
     * - Default users will exist in the system if they did not already exist.
     * - A map and a scenario will be created and added to their respective repositories if they are not already available.
     * - All available locomotives will be loaded into the repository for use in the simulation.
     */
    public void run() {
        addUsers();
        createScenario(createMap().get());
        gameLocomotives();
    }

    /**
     * Adds default users to the application. This method creates a set of default users
     * and assigns them predefined roles to facilitate the initialization of the system.
     *
     * The users created are:
     * - An "editor" user with the email "editor@isep.ipp.pt", password "editor", and the "EDITOR" role.
     * - A "player" user with the email "player@isep.ipp.pt", password "player", and the "PLAYER" role.
     *
     * This method is typically called during the initialization phase to ensure that the minimum
     * required set of users exists in the application.
     *
     * Preconditions:
     * - The email addresses used for user creation must be unique and adhere to the expected format.
     * - Roles "EDITOR" and "PLAYER" must be defined in the application.
     *
     * Postconditions:
     * - The system will contain the users specified with the given roles, provided they do not already exist.
     */
    public void addUsers(){
        AuthenticationController ctrl = new AuthenticationController();

        ctrl.createNewUser("editor", "editor@isep.ipp.pt", "editor", AuthenticationController.ROLE_EDITOR);
        ctrl.createNewUser("player", "player@isep.ipp.pt", "player", AuthenticationController.ROLE_PLAYER);
    }

    /**
     * Creates a map with a specified name and dimensions, adds it to the map repository,
     * and retrieves it from the repository. If a map with the specified name already exists,
     * the existing map is returned.
     *
     * @return      An {@code Optional<Map>} containing the created or retrieved map, or an empty
     *              {@code Optional} if the map could not be found or created.
     */
    public Optional<Map> createMap() {
        MapRepository mr = Repositories.getInstance().getMapRepository();
        mr.add(new Map("ExampleMap", 100, 100));

        return mr.getMap("ExampleMap");
    }

    /**
     * Creates a new scenario and associates it with the provided map.
     *
     * The scenario iS configured with predefined locomotive types, historical events, a date range, and a budget.
     * The created scenario is added to the specified map and stored in the scenario repository.
     *
     * @param map  The map to which the new scenario will be linked. Must not be null.
     */
    public void createScenario(Map map) {
        ScenarioRepository sr = Repositories.getInstance().getScenarioRepository();
        List<String> locomotiveTypes = new ArrayList<>();
        List<String> historicalEvents = new ArrayList<>();

        locomotiveTypes.add("steam");
        Scenario scenario = new Scenario("ExampleScenario", new Restrictions(LocalDate.parse("1880-01-01"), LocalDate.parse("1890-12-31"), locomotiveTypes, historicalEvents), map, new Budget(1000));

        map.addNewScenario(scenario);
        sr.add(scenario);
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
