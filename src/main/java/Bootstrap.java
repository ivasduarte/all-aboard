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

public class Bootstrap implements Runnable {

    public void run() {
        addUsers();
        createScenario(createMap().get());
        gameLocomotives();
    }


    public void addUsers(){
        AuthenticationController ctrl = new AuthenticationController();

        ctrl.createNewUser("editor", "editor@isep.ipp.pt", "editor", AuthenticationController.ROLE_EDITOR, Optional.empty());
        ctrl.createNewUser("player", "player@isep.ipp.pt", "player", AuthenticationController.ROLE_PLAYER, Optional.of(new Budget(1000)));
    }

    public Optional<Map> createMap() {
        MapRepository mr = Repositories.getInstance().getMapRepository();
        mr.add(new Map("ExampleMap", 100, 100));

        return mr.getMap("ExampleMap");
    }

    public void createScenario(Map map) {
        ScenarioRepository sr = Repositories.getInstance().getScenarioRepository();
        List<String> locomotiveTypes = new ArrayList<>();
        List<String> historicalEvents = new ArrayList<>();

        locomotiveTypes.add("steam");

        sr.add(new Scenario("ExampleScenario", new Restrictions(LocalDate.parse("1880-01-01"), LocalDate.parse("1890-12-31"), locomotiveTypes, historicalEvents), map));
    }

    /**
     * This method is responsible to add every locomotive available on the simulator to the repository, everytime the simulation starts.
     */
    public void gameLocomotives() {
        LocomotiveRepository lr = Repositories.getInstance().getLocomotiveRepository();

        lr.saveLocomotive(new Locomotive("Trevithick-1", "Steam", 1800, 1835, 10, 6000, 7543, 10));
        lr.saveLocomotive(new Locomotive("Stephenson Rocket", "Steam", 1829, 1851, 16, 6000, 7992, 16));
        lr.saveLocomotive(new Locomotive("John Bull", "Steam", 1831, 1853, 23, 7000, 9256, 25));
        lr.saveLocomotive(new Locomotive("Dewitt Clinton", "Steam", 1833, 1855, 18, 5000, 7187, 20));
        lr.saveLocomotive(new Locomotive("Prussian", "Steam", 1837, 1857, 35, 8000, 10622, 30));
        lr.saveLocomotive(new Locomotive("American-C", "Steam", 1848, 1885, 46, 5000, 12606, 40));
        lr.saveLocomotive(new Locomotive("Iron Duke", "Steam", 1855, 1894, 78, 9000, 30969, 54));
        lr.saveLocomotive(new Locomotive("Eight Wheeler", "Steam", 1868, 1903, 59, 5000, 17808, 48));
        lr.saveLocomotive(new Locomotive("Vulcan", "Steam", 1872, 1900, 32, 4000, 6393, 30));
        lr.saveLocomotive(new Locomotive("Consolidation", "Steam", 1877, 1905, 51, 8000, 19512, 40));
        lr.saveLocomotive(new Locomotive("3-Truck Shay", "Steam", 1882, 1930, 43, 17000, 18227, 15));
        lr.saveLocomotive(new Locomotive("Mastodon", "Steam", 1890, 1911, 60, 13000, 22995, 45));
        lr.saveLocomotive(new Locomotive("Ten Wheeler", "Steam", 1892, 1911, 66, 11000, 21727, 50));
        lr.saveLocomotive(new Locomotive("Mogul", "Steam", 1895, 1915, 83, 12000, 25131, 48));
        lr.saveLocomotive(new Locomotive("Atlantic", "Steam", 1902, 1929, 93, 18000, 43556, 80));
        lr.saveLocomotive(new Locomotive("Camelback", "Steam", 1905, 1932, 75, 9000, 15000, 30));
        lr.saveLocomotive(new Locomotive("Pacific", "Steam", 1908, 1945, 119, 21000, 62516, 87));
        lr.saveLocomotive(new Locomotive("Class G10", "Steam", 1910, 1939, 98, 38000, 50521, 50));
        lr.saveLocomotive(new Locomotive("Prairie", "Steam", 1912, 1953, 85, 11000, 34157, 60));
        lr.saveLocomotive(new Locomotive("D16sb", "Steam", 1914, 1934, 65, 9000, 21024, 45));
        lr.saveLocomotive(new Locomotive("Class 13H", "Steam", 1917, 1942, 102, 36000, 46209, 40));
        lr.saveLocomotive(new Locomotive("USRA", "Steam", 1918, 1942, 90, 13000, 29993, 40));
        lr.saveLocomotive(new Locomotive("Mikado", "Steam", 1919, 1950, 133, 32000, 51072, 55));
        lr.saveLocomotive(new Locomotive("Class B12", "Steam", 1923, 1939, 146, 14000, 32407, 71));
        lr.saveLocomotive(new Locomotive("USRA II", "Steam", 1930, 1948, 98, 18000, 32236, 45));
        lr.saveLocomotive(new Locomotive("Class A4 Mallard", "Steam", 1935, 1955, 200, 19000, 55136, 126));
        lr.saveLocomotive(new Locomotive("Hudson", "Steam", 1937, 1953, 210, 25000, 54017, 90));
        lr.saveLocomotive(new Locomotive("Daylight", "Steam", 1937, 1952, 230, 30000, 63611, 80));
        lr.saveLocomotive(new Locomotive("J3A Streamliner", "Steam", 1938, 1955, 255, 28000, 112234, 103));
        lr.saveLocomotive(new Locomotive("Big Boy", "Steam", 1941, 1955, 375, 75000, 84040, 68));
        lr.saveLocomotive(new Locomotive("T1", "Steam", 1945, 99999, 284, 28000, 78213, 85));
        lr.saveLocomotive(new Locomotive("DB 18 201", "Steam", 1998, 99999, 400, 75000, 144560, 120));

        lr.saveLocomotive(new Locomotive("F3A+B", "Diesel", 1945, 1968, 265, 16000, 52015, 85));
        lr.saveLocomotive(new Locomotive("Alco PA-1", "Diesel", 1946, 1990, 210, 16000, 52800, 90));
        lr.saveLocomotive(new Locomotive("F9", "Diesel", 1949, 1970, 337, 18000, 63000, 110));
        lr.saveLocomotive(new Locomotive("GP9", "Diesel", 1954, 1980, 235, 11000, 48028, 71));
        lr.saveLocomotive(new Locomotive("GP18", "Diesel", 1958, 1975, 245, 15000, 55131, 83));
        lr.saveLocomotive(new Locomotive("V200", "Diesel", 1959, 1985, 160, 19000, 53878, 87));
        lr.saveLocomotive(new Locomotive("Class 55 Deltic", "Diesel", 1961, 1981, 480, 15000, 52155, 100));
        lr.saveLocomotive(new Locomotive("FP45", "Diesel", 1968, 99999, 366, 14000, 65740, 106));
        lr.saveLocomotive(new Locomotive("SD45", "Diesel", 1972, 1994, 280, 21000, 36009, 65));
        lr.saveLocomotive(new Locomotive("SDP40", "Diesel", 1973, 1989, 292, 18000, 61966, 103));
        lr.saveLocomotive(new Locomotive("Dash-9", "Diesel", 1993, 99999, 478, 18000, 68410, 70));
        lr.saveLocomotive(new Locomotive("AMD-103", "Diesel", 1993, 99999, 425, 25000, 88068, 105));
        lr.saveLocomotive(new Locomotive("Class 232", "Diesel", 1997, 99999, 492, 35000, 72625, 75));

        lr.saveLocomotive(new Locomotive("1-3 Bobo", "Electric", 1895, 1935, 85, 6000, 33116, 54));
        lr.saveLocomotive(new Locomotive("Be 4/6 II", "Electric", 1920, 1950, 61, 11000, 14701, 35));
        lr.saveLocomotive(new Locomotive("Ee 3/3", "Electric", 1932, 1948, 47, 7000, 11880, 30));
        lr.saveLocomotive(new Locomotive("Class 1045", "Electric", 1927, 1950, 95, 6000, 11636, 40));
        lr.saveLocomotive(new Locomotive("GG1", "Electric", 1935, 1970, 285, 19000, 42721, 100));
        lr.saveLocomotive(new Locomotive("Class E18", "Electric", 1936, 1966, 93, 16000, 27493, 93));
        lr.saveLocomotive(new Locomotive("Ae 8/14", "Electric", 1939, 1955, 210, 24000, 47906, 68));
        lr.saveLocomotive(new Locomotive("Class 1020", "Electric", 1941, 1981, 119, 18000, 21545, 56));
        lr.saveLocomotive(new Locomotive("E69", "Electric", 1955, 1970, 86, 8000, 12837, 31));
        lr.saveLocomotive(new Locomotive("TGVx", "Electric", 1957, 1973, 850, 80000, 71672, 155));
        lr.saveLocomotive(new Locomotive("Penn. E44", "Electric", 1960, 1985, 370, 22000, 37971, 70));
        lr.saveLocomotive(new Locomotive("Shinkansen Bullet", "Electric", 1966, 2002, 650, 66000, 66680, 130));
        lr.saveLocomotive(new Locomotive("E60CP", "Electric", 1973, 1998, 310, 19000, 38043, 85));
        lr.saveLocomotive(new Locomotive("TGV", "Electric", 1973, 99999, 750, 72000, 81664, 155));
        lr.saveLocomotive(new Locomotive("Class E111", "Electric", 1974, 99999, 390, 17000, 38016, 85));
        lr.saveLocomotive(new Locomotive("E656 FS", "Electric", 1975, 1995, 226, 17000, 37734, 93));
        lr.saveLocomotive(new Locomotive("Thalys Bullet", "Electric", 1994, 99999, 1000, 78000, 111756, 186));
        lr.saveLocomotive(new Locomotive("Eurostar", "Electric", 1994, 99999, 1600, 65000, 96135, 175));
        lr.saveLocomotive(new Locomotive("Brenner", "Electric", 1999, 99999, 1250, 175000, 69292, 140));
        lr.saveLocomotive(new Locomotive("Mag-Lev TBX-1", "Electric", 2008, 99999, 2500, 200000, 305609, 280));
    }
}
