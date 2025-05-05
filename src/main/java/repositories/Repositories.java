package repositories;


public class Repositories {
    private static Repositories instance = new Repositories();
    private MapRepository mapRepository = new MapRepository();
    private IndustryRepository industryRepository = new IndustryRepository();
    private TrainRepository trainRepository = new TrainRepository();
    private final AuthenticationRepository authenticationRepository;
    private final TasksRepository tasksRepository;

    private Repositories() {
        tasksRepository = new TasksRepository();
        mapRepository = new MapRepository();
        authenticationRepository = new AuthenticationRepository();
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

    public TrainRepository getTrainRepository() {
        return trainRepository;
    }

    public AuthenticationRepository getAuthenticationRepository() {
        return authenticationRepository;
    }

    public TasksRepository getTasksRepository() {
        return tasksRepository;
    }
}
