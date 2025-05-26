package controller;

import domain.*;
import domain.Industry;
import domain.Locomotive;
import domain.Map;
import domain.Scenario;
import repositories.*;

import java.util.List;
import java.util.Optional;
import java.util.Scanner;
import java.util.stream.Collectors;

/**
 * The SimulatorController class is responsible for managing and executing the simulation
 * logic of a game scenario. It handles the simulation loop, including processing the
 * financial gains, industry activities, locomotive operations, and impacts of historical events.
 * The simulation runs based on the defined parameters in the provided map and scenario objects.
 */
public class SimulatorController implements Runnable {
    private final User currentUser = UserSession.getInstance().getUser();
    private final SimulatorRepository simRep = Repositories.getInstance().getSimRep();
    private final MapRepository mapRep = Repositories.getInstance().getMapRepository();
    private final ScenarioRepository scenarioRep = Repositories.getInstance().getScenarioRepository();
    private final StationRepository stRep = Repositories.getInstance().getStationRepository();
    private final CityRepository cityRep = Repositories.getInstance().getCityRepository();
    //private final BudgetRepository budgetRepo = Repositories.getInstance().getBudgetRepository();
    private final List<City> cities = cityRep.getCities();

    private final int SIMULATION_SLEEP_TIME = 5000;
    private final double PANDEMIC_INITIAL_MULTIPLIER = 0.25;
    private final double PANDEMIC_RECOVERY_RATE = 0.1;
    private final double WAR_CARGO_MULTIPLIER = 1.5;
    private final double WAR_COST_MULTIPLIER = 1.65;
    private final int ROUTE_EARNINGS_PER_STATION = 50;

    private final ShowFinancialResultsController financialController = new ShowFinancialResultsController();
    private boolean running = true;

    private final Map map;
    private final Scenario scenario;

    /**
     * Constructs a new SimulatorController object with the specified map and scenario.
     *
     * @param map      The map on which the simulation is based. Must not be null.
     * @param scenario The scenario to simulate within the given map. Must not be null.
     * @throws IllegalArgumentException if either the map or scenario is null.
     */
    public SimulatorController(Map map, Scenario scenario) {
        this.map = map;
        this.scenario = scenario;
    }

    /**
     * Simulates the game by initializing and starting the simulation process.
     * <p>
     * This method manages the overall simulation behavior and ensures that it starts only when
     * valid conditions are met.
     */
    public void SimulateGame() {
        if (!canStartSimulation()) {
            return;
        }
        displaySimulationHeader();

        running = true;
        initializeAndRunSimulation();
    }

    /**
     * Executes the main simulation process by invoking the {@code SimulateGame} method.
     * <p>
     * This method is defined as part of the {@code Runnable} interface implementation, enabling
     * instances of the containing class to be executed in a multithreaded environment.
     */
    @Override
    public void run() {
        SimulateGame();
    }

    /**
     * Determines whether the simulation can start based on the current year and restriction end year.
     *
     * @return {@code true} if the simulation can start; {@code false} otherwise.
     */
    private boolean canStartSimulation() {
        int endYear = scenario.getRestrictions().getEndDate().getYear();
        if (scenario.getCurrentYear() > endYear) {
            System.out.println("\nCannot start simulation, game is over!");
            return false;
        }
        return true;
    }

    /**
     * Displays the header information for the simulation, including the scenario name, map name,
     * simulation start and end dates, initial budget, and historical events associated with the scenario.
     * <p>
     * The information displayed includes:
     * - Scenario name and map name.
     * - Start and end dates of the simulation.
     * - Initial budget for the simulation.
     * - Historical events that may influence the simulation.
     */
    private void displaySimulationHeader() {
        System.out.println("Starting simulation in " + scenario.getScenarioName() + ", " + map.getName());
        System.out.println("The starting date is " + scenario.getRestrictions().getStartDate() +
                " and the end date is " + scenario.getRestrictions().getEndDate());
        System.out.println("The budget is " + scenario.getBudget().getBudget() + "€");
        displayHistoricalEvents();
    }

    /**
     * Displays the historical events associated with the current simulation scenario.
     * <p>
     * Tasks:
     * - Retrieves the list of historical events.
     * - Filters out events labeled as "none".
     * - Prints the filtered events.
     * <p>
     * Note that this method assumes the scenario and its restrictions are properly initialized,
     * and the historical events list is non-null.
     */
    private void displayHistoricalEvents() {
        List<String> historicalEvents = scenario.getRestrictions().getHistoricalEvents();
        System.out.println("Historical events: ");
        for (String event : historicalEvents) {
            if (!event.equalsIgnoreCase("none")) {
                System.out.print(event + " ");
            }
        }
        System.out.println();
    }

    /**
     * Displays the statistics for a specified year, including the total earnings for that year
     * and the current budget.
     *
     * @param year          The year for which the statistics are being displayed.
     * @param totalEarnings The total earnings accumulated during the specified year.
     */
    private void displayYearStats(int year, int totalEarnings) {
        System.out.println("\n\n------------STATS OF YEAR " + year + "------------");
        System.out.println("You have earned a total of " + totalEarnings + "€. Your budget is now " +
                scenario.getBudget().getBudget() + "€");
    }

    /**
     * Displays the statistics for a specific industry, including its type, location,
     * the amount of cargo it has generated, and the total budget it has earned.
     *
     * @param industry       The industry whose statistics are being displayed. Must not be null.
     * @param cargoGenerated The amount of cargo generated by the industry.
     * @param budgetEarned   The total budget earned by the industry (in euros).
     */
    private void displayIndustryStats(Industry industry, int cargoGenerated, int budgetEarned) {
        System.out.println("Industry of type " + industry.getIndustryType() +
                " (" + industry.getPositionX() + "," + industry.getPositionY() +
                ") has generated " + cargoGenerated + " cargo, producing a total of " +
                budgetEarned + "€");
    }

    /**
     * Displays the statistics for a specified locomotive, including its model and maintenance cost.
     *
     * @param locomotive      The locomotive whose statistics are being displayed. Must not be null.
     * @param maintenanceCost The annual maintenance cost of the locomotive, in euros.
     */
    private void displayLocomotiveStats(Locomotive locomotive, int maintenanceCost) {
        System.out.println("The train of model " + locomotive.getLocomotiveModel() +
                " does not have a route. The maintenance cost is " + maintenanceCost + "€");
    }

    /**
     * Initializes and starts the simulation process based on the given scenario and map data.
     * <p>
     * Components involved:
     * - Industries and locomotives are fetched from their respective repositories to form the basis for the simulation.
     * - Historical events influence the behavior of the simulation through pre-defined multipliers.
     * <p>
     * Preconditions:
     * - The map and scenario must be properly initialized.
     * - Repository instances must be accessible and populated with valid data.
     * - The fetched historical events list should not be null or empty.
     * <p>
     * Postconditions:
     * - The simulation process is initiated in a separate thread.
     * - The simulation continues until user input is provided to halt it.
     */
    private void initializeAndRunSimulation() {
        List<String> historicalEvents = scenario.getRestrictions().getHistoricalEvents();
        double cargoGeneratorMultiplier = calculateCargoMultiplier(historicalEvents);
        double costMultiplier = calculateCostMultiplier(historicalEvents);

        List<Industry> industries = Repositories.getInstance().getIndustryRepository().getIndustriesByMap(map);
        List<Locomotive> locomotives = Repositories.getInstance().getLocomotiveRepository().getPlayerLocomotives();

        Thread simulatorThread = createSimulatorThread(industries, locomotives, cargoGeneratorMultiplier,
                costMultiplier, historicalEvents);
        simulatorThread.start();

        waitForUserInput();
        stopSimulation(simulatorThread);
    }

    /**
     * Calculates a multiplier for cargo generation based on historical events.
     * <p>
     * The multiplier starts at 1.0 and is adjusted based on specific historical events:
     * - If the list contains "pandemic", the multiplier is multiplied by PANDEMIC_INITIAL_MULTIPLIER.
     * - If the list contains "war", the multiplier is multiplied by WAR_CARGO_MULTIPLIER.
     *
     * @param historicalEvents a list of strings representing historical events. Must not be null.
     * @return the calculated cargo multiplier as a double.
     */
    private double calculateCargoMultiplier(List<String> historicalEvents) {
        double multiplier = 1.0;
        if (historicalEvents.contains("pandemic")) {
            multiplier *= PANDEMIC_INITIAL_MULTIPLIER;
        }
        if (historicalEvents.contains("war")) {
            multiplier *= WAR_CARGO_MULTIPLIER;
        }
        return multiplier;
    }

    /**
     * Calculates a cost multiplier based on historical events.
     * <p>
     * The multiplier starts at 1.0 and is adjusted based on specific events:
     * - If the list contains "war", the multiplier is multiplied by {@code WAR_COST_MULTIPLIER}.
     *
     * @param historicalEvents a list of strings representing historical events. Must not be null.
     * @return the calculated cost multiplier as a double.
     */
    private double calculateCostMultiplier(List<String> historicalEvents) {
        double multiplier = 1.0;
        if (historicalEvents.contains("war")) {
            multiplier *= WAR_COST_MULTIPLIER;
        }
        return multiplier;
    }

    /**
     * Creates a new simulator thread to handle the simulation process for the given industries, locomotives,
     * and historical event-driven multipliers.
     * <p>
     * The created thread executes the simulation loop and terminates gracefully when interrupted.
     *
     * @param industries               A list of industries to be included in the simulation. Must not be null.
     * @param locomotives              A list of locomotives to be included in the simulation. Must not be null.
     * @param cargoGeneratorMultiplier The multiplier affecting cargo generation rates. Must be greater than 0.
     * @param costMultiplier           The multiplier affecting operational costs. Must be greater than 0.
     * @param historicalEvents         A list of historical events influencing the simulation. Must not be null.
     * @return A new thread that runs the simulation process.
     */
    private Thread createSimulatorThread(List<Industry> industries, List<Locomotive> locomotives,
                                         double cargoGeneratorMultiplier, double costMultiplier,
                                         List<String> historicalEvents) {
        return new Thread(() -> {
            try {
                runSimulationLoop(industries, locomotives, cargoGeneratorMultiplier, costMultiplier, historicalEvents);
            } catch (InterruptedException e) {
                System.out.println("Simulator interrupted.");
            }
            System.out.println("Stopped simulator.");
        });
    }

    /**
     * Executes the main simulation loop for the given industries, locomotives, and multipliers.
     * The loop runs annually until the simulation reaches the scenario's end year
     * or until the simulation is manually stopped.
     *
     * @param industries               A list of industries participating in the simulation. Must not be null.
     * @param locomotives              A list of locomotives participating in the simulation. Must not be null.
     * @param cargoGeneratorMultiplier The multiplier influencing cargo generation rates. Must be greater than 0.
     * @param costMultiplier           The multiplier influencing operational costs. Must be greater than 0.
     * @param historicalEvents         A list of historical events that can influence multipliers. Must not be null.
     * @throws InterruptedException If the simulation thread is interrupted while sleeping.
     */
    private void runSimulationLoop(List<Industry> industries, List<Locomotive> locomotives,
                                   double cargoGeneratorMultiplier, double costMultiplier,
                                   List<String> historicalEvents) throws InterruptedException {
        int year = scenario.getCurrentYear();
        int endYear = scenario.getRestrictions().getEndDate().getYear();
        double currentCargoMultiplier = cargoGeneratorMultiplier;

        while (running && year <= endYear) {
            Thread.sleep(SIMULATION_SLEEP_TIME);

            if (running) {
                int totalEarnings = processYearSimulation(industries, locomotives, currentCargoMultiplier, costMultiplier);
                displayYearStats(year, totalEarnings);

                scenario.addBudget(totalEarnings);

                if (historicalEvents.contains("pandemic")) {
                    currentCargoMultiplier += PANDEMIC_RECOVERY_RATE;
                }

                year++;
                scenario.setCurrentYear(year);
            }
        }
    }

    /**
     * Simulates the operations and earnings for a single year in the simulation.
     * Calculates earnings from both industries and locomotives using the provided multipliers.
     *
     * @param industries               A list of industries participating in the simulation. Must not be null.
     * @param locomotives              A list of locomotives contributing to the simulation. Must not be null.
     * @param cargoGeneratorMultiplier The multiplier applied to cargo generation rates. Must be greater than 0.
     * @param costMultiplier           The multiplier applied to operational costs. Must be greater than 0.
     * @return The total earnings for the simulated year, as an integer.
     */
    private int processYearSimulation(List<Industry> industries, List<Locomotive> locomotives,
                                      double cargoGeneratorMultiplier, double costMultiplier) {
        int total = 0;

        int industryEarnings = processIndustries(industries, cargoGeneratorMultiplier, costMultiplier);
        total += industryEarnings;

        int locomotiveEarnings = processLocomotives(locomotives, costMultiplier);
        total += locomotiveEarnings;

        return total;
    }

    /**
     * Processes a list of industries to calculate cargo generation, earnings,
     * and facilitates interaction with the closest station.
     *
     * @param industries               the list of industries to process; must not be null or empty
     * @param cargoGeneratorMultiplier the multiplier applied to each industry's cargo generation
     * @param costMultiplier           the multiplier applied to each industry's cargo pricing
     * @return the total earnings from all processed industries based on cargo generation and pricing calculations
     */
    private int processIndustries(List<Industry> industries, double cargoGeneratorMultiplier, double costMultiplier) {
        if (industries == null || industries.isEmpty()) {
            return 0;
        }

        System.out.println("----Stats for industries----");
        int totalEarned = 0;

        for (Industry industry : industries) {
            int cargoGenerated = (int) (industry.getCargoAmount() * cargoGeneratorMultiplier);
            int budgetEarned = (int) (industry.getCargoPrice() * cargoGenerated * costMultiplier);

            Station closestStation = findClosestStation(industry);
            if (closestStation != null) {
                closestStation.addCargo(cargoGenerated);
            }

            displayIndustryStats(industry, cargoGenerated, budgetEarned);
            totalEarned += budgetEarned;
        }

        System.out.println("You gained a total of " + totalEarned + "€ from industries!");
        return totalEarned;
    }

    /**
     * Processes a list of locomotives by calculating their earnings and costs based on the provided cost multiplier.
     * Displays statistical details for each locomotive and computes the net financial earnings.
     *
     * @param locomotives    the list of locomotives to process; may be null or empty
     * @param costMultiplier the multiplier applied to fuel and maintenance costs as well as earnings
     * @return the net earnings after deducting costs from total earnings
     */
    private int processLocomotives(List<Locomotive> locomotives, double costMultiplier) {
        if (locomotives == null || locomotives.isEmpty()) {
            return 0;
        }

        System.out.println("\n----Stats for trains----");
        int totalEarned = 0;
        int totalLost = 0;
        int trainMaintenanceCost = 0;
        int trainFuelCost = 0;

        for (Locomotive locomotive : locomotives) {
            int fuelCost = (int) (locomotive.getFuelCost() * costMultiplier);
            int maintenanceCost = (int) (locomotive.getMaintenanceCost() * costMultiplier);
            int totalMaintenanceCostPerTrain = fuelCost + maintenanceCost;

            int routeEarnings = 0;
            if (locomotive.getRoute() != null && !locomotive.getRoute().isEmpty()) {
                routeEarnings = (int) (locomotive.getRoute().size() * ROUTE_EARNINGS_PER_STATION * costMultiplier);
            } else {
                displayLocomotiveStats(locomotive, totalMaintenanceCostPerTrain);
            }

            totalEarned += routeEarnings;
            totalLost += totalMaintenanceCostPerTrain;
            trainMaintenanceCost = maintenanceCost;
            trainFuelCost = fuelCost;
        }

        int netEarnings = totalEarned - totalLost;
        System.out.println("You gained a total of " + totalEarned + "€ from trains and lost a total of " +
                totalLost + "€! Total balance is " + netEarnings + "€!");

        financialController.addFinancialResults(0, trainMaintenanceCost, trainFuelCost, 0);

        return netEarnings;
    }

    /**
     * Finds the closest station to the given industry.
     *
     * @param industry the industry for which the closest station is to be found
     * @return the closest station to the specified industry, or null if no suitable station is found
     */
    private Station findClosestStation(Industry industry) {
        List<Station> stations = Repositories.getInstance().getStationRepository().getStations();
        Station closest = null;
        int closestDistance = Integer.MAX_VALUE;

        for (Station station : stations) {
            if (station.getMap().getName().equalsIgnoreCase(map.getName())) {
                int distance = calculateDistance(station, industry);
                if (distance < closestDistance) {
                    closestDistance = distance;
                    closest = station;
                }
            }
        }

        return closest;
    }

    /**
     * Calculates the squared distance between a station and an industry based on their positions.
     *
     * @param station  the station object whose position is used in the calculation
     * @param industry the industry object whose position is used in the calculation
     * @return the squared distance between the station and the industry
     */
    private int calculateDistance(Station station, Industry industry) {
        int dx = station.getPositionX() - industry.getPositionX();
        int dy = station.getPositionY() - industry.getPositionY();
        return dx * dx + dy * dy;
    }

    /**
     * Pauses the execution of the program and waits for user input.
     * Displays a prompt to the user instructing them to press the ENTER key.
     * Once the user presses ENTER, the method sets the 'running' flag to false,
     * signaling the program or simulator to stop.
     * <p>
     * This method uses the Scanner class to read input from the standard input stream.
     * It is intended to block program execution until the user provides the required input.
     */
    private void waitForUserInput() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Press ENTER to stop the simulator");
        scanner.nextLine();
        running = false;
    }

    /**
     * Stops the simulation by waiting for the provided simulator thread to finish execution.
     *
     * @param simulatorThread the thread running the simulation that needs to be stopped
     */
    private void stopSimulation(Thread simulatorThread) {
        try {
            simulatorThread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public Map getMap() {
        return map;
    }

    public Scenario getScenario(Map map) {
        return scenario;
    }

    public Optional<Simulator> findBy(Map map, Scenario scenario) {
        return simRep.findBy(map, scenario, currentUser);
    }

    public void displayMapData(Map map, Scenario scenario) {
        int width = map.getWidth();
        int height = map.getHeight();
        String[][] grid = new String[height + 1][width + 1];
        for (int y = 1; y <= height; y++) {
            for (int x = 1; x <= width; x++) {
                grid[y][x] = "⬛";
            }
        }

        addCitiesToMap(grid, map);
        addStationsToMap(grid, map, scenario, width, height);

        for (int y = 1; y <= height; y++) {
            for (int x = 1; x <= width; x++) {
                System.out.print(grid[y][x]);
            }
            System.out.println();
        }
    }

    private void addCitiesToMap(String[][] grid, Map map) {
        List<City> cities = cityRep.getCities().stream()
                .filter(c -> c.getMapName().equals(map.getName()))
                .collect(Collectors.toList());
        for (City city : cities) {
            grid[city.getCenterY()][city.getCenterX()] = "🌆";
            for (HouseBlock hb : city.getBlocks()) {
                grid[hb.getY()][hb.getX()] = "🏠";
            }
        }
    }

    private void addStationsToMap(String[][] grid, Map map, Scenario scenario, int width, int height) {
        if (currentUser != null) {
            String owner = currentUser.getEmail();
            for (Station st : stRep.getStations()) {
                if (!st.getMapName().equals(map.getName()) ||
                        !st.getOwnerPlayer().equals(owner) ||
                        !st.getScenarioName().equals(scenario.getScenarioName())) {
                    continue;
                }

                int w, h, ox, oy;
                switch (st.getStationType()) {
                    case DEPOT:
                        w = h = 3;
                        ox = st.getPositionX() - 1;
                        oy = st.getPositionY() - 1;
                        paintArea(grid, width, height, st.getPositionX(), st.getPositionY(), ox, oy, w, h);
                        break;
                    case TERMINAL:
                        w = h = 5;
                        ox = st.getPositionX() - 2;
                        oy = st.getPositionY() - 2;
                        paintArea(grid, width, height, st.getPositionX(), st.getPositionY(), ox, oy, w, h);
                        break;
                    case STATION:
                        w = h = 4;
                        ox = st.getPositionX() - 1;
                        oy = st.getPositionY() - 1;
                        int sdx = 1, sdy = 1;
                        switch (st.getOrientation()) {
                            case NW:
                                break;
                            case NE:
                                sdx = 2;
                                break;
                            case SW:
                                sdy = 2;
                                break;
                            case SE:
                                sdx = 2;
                                sdy = 2;
                                break;
                        }
                        for (int dy = 0; dy < h; dy++) {
                            for (int dx = 0; dx < w; dx++) {
                                int gx = ox + dx;
                                int gy = oy + dy;
                                if (gx >= 1 && gx <= width && gy >= 1 && gy <= height) {
                                    grid[gy][gx] = (dx == sdx && dy == sdy) ? "🚉" : "🅿️";
                                }
                            }
                        }
                        break;
                    default:
                        grid[st.getPositionY()][st.getPositionX()] = "🚉";
                }
            }
        }
    }

    private void paintArea(String[][] grid, int maxW, int maxH, int cx, int cy, int ox, int oy, int w, int h) {
        for (int dy = 0; dy < h; dy++) {
            for (int dx = 0; dx < w; dx++) {
                int x = ox + dx;
                int y = oy + dy;
                if (x >= 1 && x <= maxW && y >= 1 && y <= maxH) {
                    grid[y][x] = (x == cx && y == cy) ? "🚉" : "🅿️";
                }
            }
        }
    }
}