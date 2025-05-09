import domain.Locomotive;
import repositories.AuthenticationRepository;
import repositories.LocomotiveRepository;
import repositories.TasksRepository;
import repositories.Repositories;
import domain.TaskCategory;
import ui.authorization.AuthenticationController;

public class Bootstrap implements Runnable {

    public void run() {
        addCategories();
        addUsers();
    }

    public void addCategories(){
        TasksRepository tasksRepository = Repositories.getInstance().getTasksRepository();

        tasksRepository.add(new TaskCategory("Analysis").clone());
        tasksRepository.add(new TaskCategory("Design").clone());
        tasksRepository.add(new TaskCategory("Implementation").clone());
        tasksRepository.add(new TaskCategory("Development").clone());
        tasksRepository.add(new TaskCategory("Testing").clone());
        tasksRepository.add(new TaskCategory("Deployment").clone());
        tasksRepository.add(new TaskCategory("Maintenance").clone());
    }

    public void addUsers(){
        AuthenticationRepository authenticationRepository = Repositories.getInstance().getAuthenticationRepository();

        authenticationRepository.addUserRole(AuthenticationController.ROLE_EDITOR, AuthenticationController.ROLE_EDITOR);
        authenticationRepository.addUserRole(AuthenticationController.ROLE_PLAYER, AuthenticationController.ROLE_PLAYER);

        authenticationRepository.addUserWithRole("editor", "editor@isep.ipp.pt", "editor", AuthenticationController.ROLE_EDITOR);
        authenticationRepository.addUserWithRole("player", "player@isep.ipp.pt", "player", AuthenticationController.ROLE_PLAYER);
    }

    public void gameLocomotives() {
        LocomotiveRepository lr = Repositories.getInstance().getLocomotiveRepository();

        lr.saveLocomotive(new Locomotive("Trevithick-1", "Steam", 1800, 1835, 10000, 6000, 7543, 10));
        lr.saveLocomotive(new Locomotive("Stephenson Rocket", "Steam", 1829, 1851, 16000, 6000, 7992, 16));
        lr.saveLocomotive(new Locomotive("John Bull", "Steam", 1831, 1853, 23000, 7000, 9256, 25));
        lr.saveLocomotive(new Locomotive("Dewitt Clinton", "Steam", 1833, 1855, 18000, 5000, 7187, 20));
        lr.saveLocomotive(new Locomotive("Prussian", "Steam", 1837, 1857, 35000, 8000, 10622, 30));
        lr.saveLocomotive(new Locomotive("American-C", "Steam", 1848, 1885, 46000, 5000, 12606, 40));
        lr.saveLocomotive(new Locomotive("Iron Duke", "Steam", 1855, 1894, 78000, 9000, 30969, 54));
        lr.saveLocomotive(new Locomotive("Eight Wheeler", "Steam", 1868, 1903, 59000, 5000, 17808, 48));
        lr.saveLocomotive(new Locomotive("Vulcan", "Steam", 1872, 1900, 32000, 4000, 6393, 30));
        lr.saveLocomotive(new Locomotive("Consolidation", "Steam", 1877, 1905, 51000, 8000, 19512, 40));
        lr.saveLocomotive(new Locomotive("3-Truck Shay", "Steam", 1882, 1930, 43000, 17000, 18227, 15));
        lr.saveLocomotive(new Locomotive("Mastodon", "Steam", 1890, 1911, 60000, 13000, 22995, 45));
        lr.saveLocomotive(new Locomotive("Ten Wheeler", "Steam", 1892, 1911, 66000, 11000, 21727, 50));
        lr.saveLocomotive(new Locomotive("Mogul", "Steam", 1895, 1915, 83000, 12000, 25131, 48));
        lr.saveLocomotive(new Locomotive("Atlantic", "Steam", 1902, 1929, 93000, 18000, 43556, 80));
        lr.saveLocomotive(new Locomotive("Camelback", "Steam", 1905, 1932, 75000, 9000, 15000, 30));
        lr.saveLocomotive(new Locomotive("Pacific", "Steam", 1908, 1945, 119000, 21000, 62516, 87));
        lr.saveLocomotive(new Locomotive("Class G10", "Steam", 1910, 1939, 98000, 38000, 50521, 50));
        lr.saveLocomotive(new Locomotive("Prairie", "Steam", 1912, 1953, 85000, 11000, 34157, 60));
        lr.saveLocomotive(new Locomotive("D16sb", "Steam", 1914, 1934, 65000, 9000, 21024, 45));
        lr.saveLocomotive(new Locomotive("Class 13H", "Steam", 1917, 1942, 102000, 36000, 46209, 40));
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
    }
}
