package repositories;


public class Repositories {
    private static Repositories instance = new Repositories();
    private MapRepository mapRepository;
    private IndustryRepository industryRepository = new IndustryRepository();
    private LocomotiveRepository locomotiveRepository = new LocomotiveRepository();
    private ScenarioRepository scenarioRepository;
    private final AuthenticationRepository authenticationRepository;
    private final TasksRepository tasksRepository;
    private final CityRepository cityRepo;

    private Repositories() {
        tasksRepository = new TasksRepository();
        mapRepository = new MapRepository();
        authenticationRepository = new AuthenticationRepository();
        cityRepo = new CityRepository();
        scenarioRepository = new ScenarioRepository();
    }

    public static Repositories getInstance() {
        if (instance == null){
            instance = new Repositories();
        }
        return instance;
    }

    public MapRepository getMapRepository() {
        return mapRepository;
    }

    public IndustryRepository getIndustryRepository() {
        return industryRepository;
    }

    public LocomotiveRepository getLocomotiveRepository() {
        return locomotiveRepository;
    }

    public AuthenticationRepository getAuthenticationRepository() {
        return authenticationRepository;
    }

    public TasksRepository getTasksRepository() {
        return tasksRepository;
    }

    public CityRepository getCityRepository() { return cityRepo; }

    public ScenarioRepository getScenarioRepository() { return scenarioRepository; }
}
