package repositories;


public class Repositories {
    private static Repositories instance = new Repositories();
    private MapRepository mapRepository;
    private IndustryRepository industryRepository = new IndustryRepository();
    private LocomotiveRepository locomotiveRepository = new LocomotiveRepository();
    private final AuthenticationRepository authenticationRepository;
    private final TasksRepository tasksRepository;
    private final CityRepository cityRepo;
    private final StationRepository stationRepo;

    private Repositories() {
        tasksRepository = new TasksRepository();
        mapRepository = new MapRepository();
        authenticationRepository = new AuthenticationRepository();
        cityRepo = new CityRepository();
        stationRepo = new StationRepository();
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

    public StationRepository getStationRepository() {return stationRepo; }
}
