package repositories;

import domain.SimulationLoad;
import domain.SimulationSave;

import java.io.Serializable;

/**
 * The Repositories class acts as a centralized container for various repository instances
 * used in the application. Each repository serves a specific purpose and provides
 * methods for managing corresponding types of data.
 *
 * This class follows the Singleton design pattern to ensure that only one instance of
 * the Repositories class is created during the application's lifecycle.
 */
public class Repositories implements Serializable {
    private static Repositories instance = new Repositories();

    private final MapRepository mapRepository;
    private final IndustryRepository industryRepository;
    private final LocomotiveRepository locomotiveRepository;
    private final TrainRepository trainRepository;
    private final AuthenticationRepository authenticationRepository;
    private final CityRepository cityRepo;
    private final StationRepository stationRepo;
    private final BuildingRepository buildingRepo;
    private final FinancialResultsRepository financialResultsRepository;
    private ScenarioRepository scenarioRepository;
    private SimulatorRepository simRep;


    /**
     * Constructs a new instance of the {@code Repositories} class.
     * This constructor initializes all repository instances used for managing various types of data.
     * Each repository is responsible for handling specific data operations and interactions
     * within the application.
     *
     * This constructor is private to enforce the Singleton design pattern. It ensures that
     * only one instance of the {@code Repositories} class is created during the application's lifecycle
     * and prevents external instantiation.
     */
    private Repositories() {
        mapRepository = new MapRepository();
        industryRepository = new IndustryRepository();
        locomotiveRepository = new LocomotiveRepository();
        trainRepository = new TrainRepository();
        cityRepo = new CityRepository();
        scenarioRepository = new ScenarioRepository();
        authenticationRepository = new AuthenticationRepository();
        stationRepo = new StationRepository();
        buildingRepo = new BuildingRepository();
        financialResultsRepository = new FinancialResultsRepository();
        simRep = new SimulatorRepository();


        SimulationLoad simFile = new SimulationLoad("simulations.sim");
        simFile.load(this.simRep, this.stationRepo, this.buildingRepo);
    }

    /**
     * Retrieves the singleton instance of the Repositories class.
     * This method ensures that the same instance is returned for all calls,
     * following the Singleton design pattern.
     *
     * @return The single instance of the Repositories class.
     */
    public static Repositories getInstance() {
        return instance;
    }

    /**
     * Retrieves the instance of the MapRepository.
     * The MapRepository is responsible for managing a collection of Map objects
     * and provides methods for interaction with the stored data.
     *
     * @return The MapRepository instance used by the application.
     */
    public MapRepository getMapRepository() {
        return mapRepository;
    }

    /**
     * Retrieves the instance of the IndustryRepository.
     * The IndustryRepository is responsible for managing a collection of Industry objects
     * and provides methods for interaction with the stored data.
     *
     * @return The IndustryRepository instance used by the application.
     */
    public IndustryRepository getIndustryRepository() {
        return industryRepository;
    }

    /**
     * Retrieves the instance of the LocomotiveRepository.
     * The LocomotiveRepository is responsible for managing collections
     * of locomotives, including available and player-owned locomotives,
     * and provides methods for interaction with the stored data.
     *
     * @return The LocomotiveRepository instance used by the application.
     */
    public LocomotiveRepository getLocomotiveRepository() {
        return locomotiveRepository;
    }

    /**
     * Retrieves the instance of the TrainRepository.
     * The TrainRepository is responsible for managing collections
     * of trains, including owned trains,
     * and provides methods for interaction with the stored data.
     *
     * @return The TrainRepository instance used by the application.
     */
    public TrainRepository getTrainRepository() {
        return trainRepository;
    }

    /**
     * Retrieves the instance of the AuthenticationRepository.
     * The AuthenticationRepository is responsible for managing user authentication,
     * including login and logout operations, user session management, and user role assignments.
     *
     * @return The AuthenticationRepository instance used by the application.
     */
    public AuthenticationRepository getAuthenticationRepository() {
        return authenticationRepository;
    }

    /**
     * Retrieves the instance of the CityRepository.
     * The CityRepository is responsible for managing a collection of City objects,
     * providing methods to add new cities, validate city-related data, and retrieve
     * stored cities.
     *
     * @return The CityRepository instance used by the application.
     */
    public CityRepository getCityRepository() { return cityRepo; }

    /**
     * Retrieves the instance of the ScenarioRepository.
     * The ScenarioRepository is responsible for managing a collection of scenarios,
     * providing methods for adding, retrieving, validating, and ensuring unique entries
     * by scenario name.
     *
     * @return The ScenarioRepository instance used by the application.
     */
    public ScenarioRepository getScenarioRepository() { return scenarioRepository; }

    /**
     * Retrieves the instance of the StationRepository.
     * The StationRepository is responsible for managing a collection of Station objects,
     * providing methods for saving, retrieving, and managing station-related data.
     *
     * @return The StationRepository instance used by the application.
     */
    public StationRepository getStationRepository() {return stationRepo; }

    /**
     * Retrieves the instance of the BuildingRepository.
     * The BuildingRepository is responsible for managing a collection of BuildingType objects,
     * providing methods to retrieve specific building types and access all available building types.
     *
     * @return The BuildingRepository instance used by the application.
     */
    public BuildingRepository getBuildingRepository() {return buildingRepo; }

    /**
     * Retrieves the instance of the FinancialResultsRepository.
     * The FinancialResultsRepository is responsible for managing a collection of FinancialResults objects,
     * providing methods to retrieve yearly specifics financial results.
     *
     * @return The FinancialResultsRepository instance used by the application.
     */
    public FinancialResultsRepository getFinancialResultsRepository() {
        return financialResultsRepository;
    }

    /**
     * This method will be used just for testing purposes.
     *
     * @param scenarioRepository The scenario repository used for testing
     */
    public void setScenarioRepository(ScenarioRepository scenarioRepository) {
        this.scenarioRepository = scenarioRepository;
    }

    /**
     * Retrieves the instance of the SimulatorRepository.
     * The SimulatorRepository is responsible for managing simulator-related data
     * and provides methods to interact with the simulator's functionalities and operations.
     *
     * @return The SimulatorRepository instance used by the application.
     */
    public SimulatorRepository getSimRep() {
        return simRep;
    }

    /**
     * Sets the SimulatorRepository instance for the Repositories class.
     * This method allows assigning a specific instance of SimulatorRepository,
     * which is used to manage simulator-related data and operations.
     *
     * @param simRep The SimulatorRepository instance to be set. Cannot be null.
     */
    public void setSimRep(SimulatorRepository simRep) {
        this.simRep = simRep;
    }
}
