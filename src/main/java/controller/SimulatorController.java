package controller;

import domain.*;
import presentation.SimulationUI;
import repositories.*;

import java.io.Serializable;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Controller class responsible for managing the simulation of the railway system.
 * Handles the execution, pausing, resuming, and stopping of simulations, as well as
 * processing financial results and managing train operations.
 */
public class SimulatorController implements Runnable, Serializable {
    private final User currentUser = UserSession.getInstance().getUser();
    private final SimulatorRepository simRep = Repositories.getInstance().getSimRep();
    private final StationRepository stRep = Repositories.getInstance().getStationRepository();
    private final LocomotiveRepository locoRep = Repositories.getInstance().getLocomotiveRepository();
    private final CityRepository cityRep = Repositories.getInstance().getCityRepository();

    private final int SIMULATION_SLEEP_TIME = 5000;
    private final double PANDEMIC_INITIAL_MULTIPLIER = 0.25;
    private final double PANDEMIC_RECOVERY_RATE = 0.1;
    private final double WAR_CARGO_MULTIPLIER = 1.5;
    private final double WAR_COST_MULTIPLIER = 1.65;
    private final int ROUTE_EARNINGS_PER_STATION = 50;
    private final int TRACK_MAINTENANCE_COST_PER_100KM = 25;

    private final ShowFinancialResultsController financialController = new ShowFinancialResultsController();
    private volatile boolean running = false;
    private volatile boolean paused = false;
    private volatile boolean stopped = false;

    private final Map map;
    private final Scenario scenario;
    private Simulator simulator;
    private SimulationUI ui;

    /**
     * Constructs a new SimulatorController with the specified map and scenario.
     *
     * @param map The map on which the simulation will run.
     * @param scenario The scenario containing simulation parameters and constraints.
     */
    public SimulatorController(Map map, Scenario scenario) {
        this.map = map;
        this.scenario = scenario;
    }

    /**
     * Sets the UI instance for this controller.
     *
     * @param ui The SimulationUI instance to set
     */
    public void setUI(SimulationUI ui) {
        this.ui = ui;
    }

    /**
     * Pauses the current simulation.
     * The simulation will stop at the next iteration until resumed.
     */
    public void pauseSimulation() {
        paused = true;
        if (ui != null) {
            ui.appendLog("Simulation pause requested...");
        }
    }

    /**
     * Resumes a paused simulation.
     * The simulation will continue from where it was paused.
     */
    public void resumeSimulation() {
        paused = false;
        if (ui != null) {
            ui.appendLog("Simulation resumed...");
        }
    }

    /**
     * Stops the current simulation.
     * The simulation will terminate at the next iteration.
     */
    public void stopSimulation() {
        running = false;
        stopped = true;
        paused = false;
        if (ui != null) {
            ui.appendLog("Simulation stop requested...");
        }
    }

    /**
     * Checks if the simulation is currently running.
     *
     * @return true if the simulation is running, false otherwise.
     */
    public boolean isRunning() {
        return running;
    }

    /**
     * Checks if the simulation is currently paused.
     *
     * @return true if the simulation is paused, false otherwise.
     */
    public boolean isPaused() {
        return paused;
    }

    /**
     * Checks if the simulation has been stopped.
     *
     * @return true if the simulation has been stopped, false otherwise.
     */
    public boolean isStopped() {
        return stopped;
    }

    /**
     * Starts a new simulation or resumes an existing one.
     * If a simulation already exists for the current map and scenario, it will be resumed.
     * Otherwise, a new simulation will be created.
     *
     * @return The Simulator instance for the current simulation.
     */
    public Simulator startOrResumeSimulation() {
        Optional<Simulator> optionalSim = findBy(map, scenario);
        if (optionalSim.isPresent()) {
            this.simulator = optionalSim.get();
        } else {
            this.simulator = new Simulator(currentUser, map, scenario, scenario.getBudget(), scenario.getCurrentYear());
            simRep.add(simulator);
        }
        return simulator;
    }

    /**
     * Runs the main simulation loop.
     * Processes each year of the simulation, handling industries, trains, and events.
     *
     * @param industries List of industries to process
     * @param trains List of trains to process
     * @param cargoMultiplier Multiplier for cargo generation
     * @param costMultiplier Multiplier for costs
     * @param events List of historical events affecting the simulation
     * @throws InterruptedException if the simulation is interrupted
     */
    private void runSimulationLoop(List<Industry> industries, List<Train> trains, double cargoMultiplier, double costMultiplier, List<String> events) throws InterruptedException {
        int year = scenario.getCurrentYear();
        int endYear = scenario.getRestrictions().getEndDate().getYear();
        double currentCargoMultiplier = cargoMultiplier;

        while (running && !stopped && year <= endYear) {
            while (paused && !stopped) {
                Thread.sleep(500);
            }

            Thread.sleep(SIMULATION_SLEEP_TIME);

            if (!paused && !stopped) {
                int totalEarnings = processYearSimulation(industries, trains, currentCargoMultiplier, costMultiplier);
                scenario.addBudget(totalEarnings);
                if (ui != null) {
                    ui.updateSimulationStats(year, totalEarnings);
                }
                processFinancialResults();
                updateStationDemand();
                checkNewLocomotives(year);

                if (events.contains("pandemic")) currentCargoMultiplier += PANDEMIC_RECOVERY_RATE;
                year++;
                scenario.setCurrentYear(year);
            }
        }

        running = false;
        if (year > endYear) {
            if (ui != null) {
                ui.appendLog("\n\nSIMULATION COMPLETED! End year reached.");
            }
        } else if (stopped) {
            if (ui != null) {
                ui.appendLog("\n\nSIMULATION STOPPED BY USER.");
            }
        }
    }

    private int processTrains(List<Train> trains, double costMultiplier) {
        if (trains == null || trains.isEmpty()) {
            return 0;
        }

        int totalEarned = 0, totalLost = 0;
        for (Train train : trains) {
            if (train.getRoute() != null && !train.getRoute().isEmpty()) {
                boolean validRoute = true;
                for (PointOfRoute point : train.getRoute()) {
                    Station station = point.getStation();
                    if (station.getMap() == null || !station.getMap().getName().equalsIgnoreCase(map.getName())) {
                        validRoute = false;
                        break;
                    }
                }

                if (!validRoute) {
                    continue;
                }

                int fuel = (int) (train.getLocomotive().getFuelCost() * costMultiplier);
                int maintenance = (int) (train.getLocomotive().getMaintenanceCost() * costMultiplier);
                int totalCost = fuel + maintenance;

                double dist = train.calculateRouteDistance();
                double bMulti = calculateStationBuildingMultiplier(train.getRoute().get(train.getRoute().size() - 1).getStation());
                int earnings = simulateTrainTrip(train, costMultiplier);

                totalEarned += earnings;
                totalLost += totalCost;
            }
        }
        int net = totalEarned - totalLost;
        if (ui != null) {
            ui.updateTrainStats(totalEarned, totalLost, net);
        }
        return net;
    }

    private int simulateTrainTrip(Train train, double costMultiplier) {
        if (train.getRoute() == null || train.getRoute().isEmpty()) return 0;

        List<PointOfRoute> route = train.getRoute();
        int totalEarnings = 0;
        int currentCargoLoad = 0;

        for (int i = 0; i < route.size() - 1; i++) {
            PointOfRoute currentPoint = route.get(i);
            PointOfRoute nextPoint = route.get(i + 1);
            Station from = currentPoint.getStation();
            Station to = nextPoint.getStation();

            if (from.getMap() == null || !from.getMap().getName().equalsIgnoreCase(map.getName()) ||
                to.getMap() == null || !to.getMap().getName().equalsIgnoreCase(map.getName())) {
                continue;
            }

            int availableCargo = from.getCargo();
            int maxTrainCapacity = train.getNumberOfCarriages() * train.getCarriages().get(0).getMaxCargo();
            int spaceAvailable = maxTrainCapacity - currentCargoLoad;

            if (!train.isReadyToDepart(i, currentCargoLoad)) {
                if (ui != null) {
                    ui.updateTrainLoading(train.getLocomotive().getLocomotiveModel(),
                                        currentPoint.getCargoMode().toString(),
                                        from.getName(),
                                        availableCargo,
                                        currentCargoLoad);
                }
            }

            while (!train.isReadyToDepart(i, currentCargoLoad)) {
                int targetCargo = 0;
                switch (currentPoint.getCargoMode()) {
                    case FULL:
                        targetCargo = maxTrainCapacity;
                        break;
                    case HALF:
                        targetCargo = maxTrainCapacity / 2;
                        break;
                }
                int cargoToLoad = Math.min(availableCargo, Math.abs(targetCargo - currentCargoLoad));
                if (currentPoint.getCargoMode() == CargoMode.AVAILABLE) {
                    cargoToLoad = Math.min(availableCargo, spaceAvailable);
                }
                if (cargoToLoad > 0) {
                    from.removeCargo(cargoToLoad); // remove cargo from the station
                    currentCargoLoad += cargoToLoad; // add cargo to the train
                    if (ui != null) {
                        ui.appendLog("\n\n[CARGO REMOVED] Train '" + train.getLocomotive().getLocomotiveModel() +
                                "' removed " + cargoToLoad + " amount from station '" + from.getName() +
                                "'.\nAvailable cargo at station: " + from.getCargo() +
                                "\nTotal cargo on train: " + currentCargoLoad);
                    }
                }
            }

            if (ui != null) {
                ui.appendLog("\n\n[TRAVEL] Train '" + train.getLocomotive().getLocomotiveModel() +
                        "' left from '" + from.getName() + "' to '" + to.getName() +
                        "' with " + currentCargoLoad + " amount of cargo.");
            }

            double distance = from.distanceTo(to);
            double buildingMultiplier = calculateStationBuildingMultiplier(to);
            int earnings = (int) (distance * ROUTE_EARNINGS_PER_STATION * buildingMultiplier * costMultiplier);

            if (currentCargoLoad > 0) {
                int cargoToDeliver = currentCargoLoad;
                to.addDeliveredCargo(cargoToDeliver);
                currentCargoLoad = 0;

                if (ui != null) {
                    ui.appendLog("\n\n[CARGO DELIVERED] Train '" + train.getLocomotive().getLocomotiveModel() +
                            "' delivered " + cargoToDeliver + " amount of cargo to station '" + to.getName() +
                            "'.");

                    int deliveryBonus = cargoToDeliver * 10; // 10€ per unit delivered
                    earnings += deliveryBonus;

                    ui.appendLog("\n\n[REVENUE] Station '" + to.getName() + "': " + earnings + "€ " +
                            "(Distance: " + (int) distance + ", Deliver bonus: " + deliveryBonus + "€)");
                }
            } else {
                if (ui != null) {
                    ui.appendLog("\n\n[REVENUE] Station '" + to.getName() + "': " + earnings + "€ " +
                            "(Distance: " + (int) distance + ", No cargo to deliver.)");
                }
            }

            totalEarnings += earnings;

            if (ui != null) {
                ui.appendLog("\n\n[ARRIVAL] Train '" + train.getLocomotive().getLocomotiveModel() +
                        "' arrived at '" + to.getName() + "'.");
            }
        }

        if (ui != null) {
            ui.appendLog("\n\n[TRAVEL SUM] Train '" + train.getLocomotive().getLocomotiveModel() +
                    "' completed its route.\nTotal revenue: " + totalEarnings + "€");
        }

        return totalEarnings;
    }

    private void processIndustry(Industry industry, double cargoMultiplier) {
        int cargo = (int) (industry.getCargoAmount() * cargoMultiplier);
        Station closestStation = findClosestStation(industry);
        if (closestStation != null) {
            int availableCapacity = closestStation.getMaxCargo() - closestStation.getCargo();
            int cargoToAdd = Math.min(cargo, availableCapacity);
            
            if (cargoToAdd > 0) {
                closestStation.addCargo(cargoToAdd);
                if (ui != null) {
                    ui.updateIndustryStats(industry, cargoToAdd);
                    if (cargoToAdd < cargo) {
                        ui.appendLog("\n[WARNING] Station '" + closestStation.getName() + 
                                   "' reached maximum capacity. " + 
                                   (cargo - cargoToAdd) + " cargo could not be added.");
                    }
                }
            }
        }
    }

    private double calculateStationBuildingMultiplier(Station station) {
        if (station.getMap() == null || !station.getMap().getName().equalsIgnoreCase(map.getName())) {
            return 1.0;
        }

        double m = 1.0;
        for (Building b : station.getBuildings()) {
            switch (b.getBuildingType()) {
                case "PassengerTerminal":
                    m += 0.2;
                    break;
                case "Warehouse":
                    m += 0.1;
                    break;
            }
        }
        return m;
    }

    private void displaySimulationHeader() {
        if (ui != null) {
            ui.appendLog("-------------------------------------------------------------------------");
            ui.appendLog("\nSIMULATION STARTING IN SCENARIO " + scenario.getScenarioName() + ", FROM " + scenario.getRestrictions().getStartDate() + " TO " + scenario.getRestrictions().getEndDate());
            ui.appendLog("\nYOUR CURRENT BUDGET IS " + scenario.getBudget().getBudget() + "€.");
            displayHistoricalEvents();
        }
    }

    private void displayHistoricalEvents() {
        List<String> historicalEvents = scenario.getRestrictions().getHistoricalEvents();

        if (ui != null) {
            ui.appendLog("YOUR SIMULATION WILL BE CONTINUED WITH THE FOLLOWING EVENTS: ");
            for (String event : historicalEvents) {
                if (!event.equalsIgnoreCase("none")) {
                    ui.appendLog(event);
                }
            }
        }
    }

    private void displayStats(int currentYear, int totalEarnings, Scenario scenario) {
        if (ui != null) {
            ui.appendLog("\n\n------------STATS OF YEAR " + currentYear + "------------");
            ui.appendLog("\nYOU'VE EARNED A TOTAL OF " + totalEarnings + "€.");
            ui.appendLog("YOUR BUDGET IS NOW " + scenario.getBudget().getBudget() + "€.");
        }
    }

    private void displayIndustryStats(Industry industry, int cargo) {
        if (ui != null) {
            ui.appendLog("\n\n------------INDUSTRY STATS------------");
            ui.appendLog("\nINDUSTRY '" + industry.getIndustryType() + "', LOCATED AT (" + industry.getPositionX() + "," + industry.getPositionY() +
                    ") HAS GENERATED " + cargo + " CARGO.");
        }
    }

    /**
     * Displays locomotive statistics in the UI.
     */
    private void displayLocomotiveStats() {
        if (ui != null) {
            ui.appendLog("\n\n------------LOCOMOTIVE STATS------------");
            ui.appendLog("\n");
        }
    }

    /**
     * Starts the simulation process.
     * This method initializes the simulation and begins processing game events.
     */
    public void SimulateGame() {
        if (!canStartSimulation()) return;
        running = true;
        stopped = false;
        initializeAndRunSimulation();
    }

    /**
     * Implementation of the Runnable interface.
     * This method is called when the simulation is started in a separate thread.
     */
    @Override
    public void run() {
        SimulateGame();
    }

    /**
     * Checks if the simulation can be started based on the current year.
     *
     * @return true if the simulation can be started, false otherwise
     */
    private boolean canStartSimulation() {
        return scenario.getCurrentYear() <= scenario.getRestrictions().getEndDate().getYear();
    }

    /**
     * Initializes and runs the simulation.
     * Sets up initial parameters and starts the simulation loop.
     */
    private void initializeAndRunSimulation() {
        List<String> events = scenario.getRestrictions().getHistoricalEvents();
        double cargoMultiplier = calculateCargoMultiplier(events);
        double costMultiplier = calculateCostMultiplier(events);

        List<Industry> industries = Repositories.getInstance().getIndustryRepository().getIndustriesByMap(map);
        List<Train> trains = Repositories.getInstance().getTrainRepository().getAllTrains();

        try {
            runSimulationLoop(industries, trains, cargoMultiplier, costMultiplier, events);
        } catch (InterruptedException e) {
            if (ui != null) {
                ui.appendLog("Simulation was interrupted.");
            }
        }
    }

    /**
     * Calculates the cargo multiplier based on historical events.
     *
     * @param events List of historical events
     * @return The calculated cargo multiplier
     */
    private double calculateCargoMultiplier(List<String> events) {
        double m = 1.0;
        if (events.contains("pandemic")) m *= PANDEMIC_INITIAL_MULTIPLIER;
        if (events.contains("war")) m *= WAR_CARGO_MULTIPLIER;
        return m;
    }

    private double calculateCostMultiplier(List<String> events) {
        return events.contains("war") ? WAR_COST_MULTIPLIER : 1.0;
    }

    private int processYearSimulation(List<Industry> industries, List<Train> trains, double cargoMultiplier, double costMultiplier) {
        int industryEarnings = processIndustries(industries, cargoMultiplier);
        int houseBlockEarnings = processHouseBlockCargo();
        int trainEarnings = processTrains(trains, costMultiplier);
        int totalEarnings = industryEarnings + houseBlockEarnings + trainEarnings;

        if (ui != null) {
            ui.appendLog("\n\n--- YEAR SIMULATION SUMMARY ---");
            ui.appendLog("INDUSTRIES CONTRIBUTION: " + industryEarnings + "€.");
            ui.appendLog("HOUSE BLOCK EARNINGS: " + houseBlockEarnings + "€.");
            ui.appendLog("\nTOTAL YEAR EARNINGS: " + totalEarnings + "€.");
        }

        return totalEarnings;
    }

    private int processIndustries(List<Industry> industries, double cargoMultiplier) {
        List<Station> userStations = getUserStationsForMap();
        if (userStations.isEmpty()) {
            if (ui != null) {
                ui.appendLog("No user stations available for industry cargo generation");
            }
            return 0;
        }

        int totalEarnings = 0;
        for (Industry industry : industries) {
            List<Station> overlappingStations = findOverlappingStations(userStations, industry);
            if (overlappingStations.isEmpty()) {
                if (ui != null) {
                    ui.appendLog("\n[WARNING] No overlapping stations found for industry '" + 
                               industry.getIndustryType() + "' at (" + 
                               industry.getPositionX() + "," + industry.getPositionY() + ")");
                }
                continue;
            }
            processIndustry(industry, cargoMultiplier);
        }
        return totalEarnings;
    }

    private List<Station> getUserStationsForMap() {
        return stRep.getAvailableStationsByMapName(map.getName()).stream()
                .filter(s -> s.getOwnerPlayer() != null && s.getOwnerPlayer().equals(currentUser.getEmail()))
                .collect(Collectors.toList());
    }

    private List<Station> findOverlappingStations(List<Station> stations, Industry industry) {
        return stations.stream()
                .filter(station -> hasStationIndustryOverlap(station, industry))
                .collect(Collectors.toList());
    }

    private boolean hasStationIndustryOverlap(Station station, Industry industry) {
        double industryRadius = (double) industry.getArea() / 2;
        int industryX = industry.getPositionX();
        int industryY = industry.getPositionY();

        StationBounds bounds = getStationBounds(station);

        return !(bounds.maxX < industryX - industryRadius ||
                bounds.minX > industryX + industryRadius ||
                bounds.maxY < industryY - industryRadius ||
                bounds.minY > industryY + industryRadius);
    }

    private StationBounds getStationBounds(Station station) {
        int w, h, minX, minY;

        String stationType = String.valueOf(station.getStationType());

        switch (stationType.toUpperCase()) {
            case "DEPOT":
                w = h = 3;
                minX = station.getPositionX() - 1;
                minY = station.getPositionY() - 1;
                break;
            case "TERMINAL":
                w = h = 5;
                minX = station.getPositionX() - 2;
                minY = station.getPositionY() - 2;
                break;
            case "STATION":
            default:
                w = h = 4;
                minX = station.getPositionX() - 2;
                minY = station.getPositionY() - 2;
                break;
        }

        return new StationBounds(minX, minY, minX + w - 1, minY + h - 1);
    }

    private static class StationBounds {
        final int minX, minY, maxX, maxY;

        StationBounds(int minX, int minY, int maxX, int maxY) {
            this.minX = minX;
            this.minY = minY;
            this.maxX = maxX;
            this.maxY = maxY;
        }
    }

    private int getIndustryCargoAmount(Industry industry) {
        String industryType = String.valueOf(industry.getIndustryType());

        switch (industryType) {
            case "IRON_MINE":
                return industry.getGeneratedIron();
            case "COAL_MINE":
                return industry.getGeneratedCoal();
            case "STEEL_MILL":
                if (industry.getStoredCoal() > 0 && industry.getStoredIron() > 0) {
                    return 1;
                }
                return 0;
            default:
                return industry.getCargoAmount();
        }
    }

    private int addCargoToStationWithCapacity(Station station, int cargoToAdd) {
        int currentCapacity = station.getCargo();
        int maxCapacity = station.getMaxCargo();

        if (currentCapacity >= maxCapacity) {
            return 0;
        }

        int actualCargoAdded = Math.min(cargoToAdd, maxCapacity - currentCapacity);
        station.addCargo(actualCargoAdded);

        return actualCargoAdded;
    }

    private void distributeIndustryCargoByType(Station station, Industry industry, int cargoAmount) {
        String industryType = String.valueOf(industry.getIndustryType());

        switch (industryType) {
            case "IRON_MINE":
                if (station.hasIronStorage()) {
                    station.addIronCargo(cargoAmount);
                }
                break;
            case "COAL_MINE":
                if (station.hasCoalStorage()) {
                    station.addCoalCargo(cargoAmount);
                }
                break;
            case "STEEL_MILL":
                if (cargoAmount > 0) {
                    if (station.hasSteelStorage()) {
                        station.addSteelCargo(cargoAmount);
                    }
                    industry.consumeIron(1);
                    industry.consumeCoal(1);
                }
                break;
            default:
                break;
        }
    }

    private int calculateIndustryEarnings(Industry industry, int cargoAmount) {
        String industryType = String.valueOf(industry.getIndustryType());

        switch (industryType) {
            case "IRON_MINE":
                return cargoAmount * 6; // iron value
            case "COAL_MINE":
                return cargoAmount * 4; // coal value
            case "STEEL_MILL":
                return cargoAmount * 10; // steel value
            default:
                return cargoAmount * 5;
        }
    }

    private void displayIndustryProcessingInfo(Industry industry, int cargoAdded, Station station, int earnings) {
        if (ui != null) {
            ui.appendLog("\nProcessing industry: " + industry.getIndustryType());
            ui.appendLog("Added " + cargoAdded + " cargo to station: " + station.getName());
            ui.appendLog("Earnings from this industry: " + earnings + "€");
        }
    }

    private int processHouseBlockCargo() {
        int total = 0;
        for (City city : cityRep.getCities()) {
            int blocks = city.getBlocks().size();
            int cargo = blocks * 2;

            // find stations in city owned by current player
            List<Station> cityStations = stRep.getAvailableStationsByMapName(map.getName()).stream()
                    .filter(s -> s.getName().contains(city.getName()))
                    .collect(Collectors.toList());

            if (!cityStations.isEmpty()) {
                // distribute cargo among user's stations in this city
                int cargoPerStation = cargo / cityStations.size();
                int remainingCargo = cargo % cityStations.size();
                int totalCargoAdded = 0;

                for (int i = 0; i < cityStations.size(); i++) {
                    Station station = cityStations.get(i);
                    int stationCargo = cargoPerStation + (i < remainingCargo ? 1 : 0);
                    
                    // Check available capacity
                    int availableCapacity = station.getMaxCargo() - station.getCargo();
                    int actualCargoToAdd = Math.min(stationCargo, availableCapacity);
                    
                    if (actualCargoToAdd > 0) {
                        station.addCargo(actualCargoToAdd);
                        totalCargoAdded += actualCargoToAdd;
                        
                        if (ui != null) {
                            ui.appendLog("\n[HOUSE BLOCK] Station '" + station.getName() + 
                                       "' received " + actualCargoToAdd + " cargo" +
                                       (actualCargoToAdd < stationCargo ? 
                                        " (could not add " + (stationCargo - actualCargoToAdd) + 
                                        " cargo due to capacity limit)" : "") +
                                       ". Total cargo: " + station.getCargo());
                        }
                    }
                }
                
                // add earnings from city operations (only for cargo that was actually added)
                int cityEarnings = totalCargoAdded * 3;
                total += cityEarnings;

                if (ui != null) {
                    ui.appendLog("\nCITY '" + city.getName() + "' has " + blocks + " house blocks and generated " + cargo + " cargo.");
                    ui.appendLog("Successfully added " + totalCargoAdded + " cargo to stations.");
                    ui.appendLog("IT EARNED A TOTAL OF " + cityEarnings + "€.");
                }
            }
        }
        return total;
    }

    private void updateStationDemand() {
        for (Station s : stRep.getAvailableStationsByMapName(map.getName())) {
            int lastDelivered = s.getLastYearDeliveries();
            int currentDemand = s.getDemand();
            s.setDemand((int) (currentDemand * 0.8 + lastDelivered * 0.2));
        }
    }

    private void checkNewLocomotives(int year) {
        for (Locomotive l : locoRep.getAllLocomotives()) {
            if (l.getStartYear() == year && !locoRep.isAvailableToPlayer(l)) {
                locoRep.unlockForPlayer(l);
                if (ui != null) {
                    ui.appendLog("New locomotive available: " + l.getLocomotiveModel());
                }
            }
        }
    }

    private void processFinancialResults() {
        int maintenance = 0, fuel = 0, deliveries = 0, track = 0;

        for (Locomotive l : locoRep.getPlayerLocomotives()) {
            maintenance += l.getMaintenanceCost();

            Train train = Repositories.getInstance().getTrainRepository().getTrainByLocomotive(l);
            if (train.getRoute() != null) {
                double dist = train.calculateRouteDistance();
                int fuelCost = l.getFuelCost();

                fuel += (int) (dist * fuelCost) / 100;
                track += (int) (dist * TRACK_MAINTENANCE_COST_PER_100KM) / 100;
            }
        }

        for (Station s : stRep.getAvailableStationsByMapName(map.getName())) {
            deliveries += s.getLastYearDeliveries();
        }

        financialController.addFinancialResults(track, maintenance, fuel, deliveries);
    }

    private Station findClosestStation(Industry i) {
        Station closest = null;
        int minDist = Integer.MAX_VALUE;
        for (Station s : stRep.getAvailableStationsByMapName(map.getName())) {
            int dx = s.getPositionX() - i.getPositionX();
            int dy = s.getPositionY() - i.getPositionY();
            int dist = dx * dx + dy * dy;
            if (dist < minDist) {
                minDist = dist;
                closest = s;
            }
        }
        return closest;
    }

    /**
     * Gets the map associated with this simulation.
     *
     * @return The map being used in the simulation.
     */
    public Map getMap() {
        return map;
    }

    /**
     * Gets the scenario associated with this simulation.
     *
     * @return The scenario being used in the simulation.
     */
    public Scenario getScenario() {
        return scenario;
    }

    /**
     * Finds an existing simulation for the given map and scenario.
     *
     * @param map The map to search for.
     * @param scenario The scenario to search for.
     * @return An Optional containing the found Simulator, or empty if none exists.
     */
    public Optional<Simulator> findBy(Map map, Scenario scenario) {
        return simRep.findBy(map, scenario, currentUser);
    }

    /**
     * Saves the current simulation state.
     *
     * @param simulator The simulator instance to save.
     */
    public void saveSimulation(Simulator simulator) {
        simRep.add(simulator);
    }
}