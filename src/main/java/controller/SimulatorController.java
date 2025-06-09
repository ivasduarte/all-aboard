package controller;

import domain.*;
import repositories.*;

import java.io.Serializable;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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

    private final ShowFinancialResultsController financialController = new ShowFinancialResultsController();
    private volatile boolean running = false;
    private volatile boolean paused = false;
    private volatile boolean stopped = false;

    private final Map map;
    private final Scenario scenario;
    private Simulator simulator;

    public SimulatorController(Map map, Scenario scenario) {
        this.map = map;
        this.scenario = scenario;
    }

    public void pauseSimulation() {
        paused = true;
        System.out.println("Simulation pause requested...");
    }

    public void resumeSimulation() {
        paused = false;
        System.out.println("Simulation resumed...");
    }

    public void stopSimulation() {
        running = false;
        stopped = true;
        paused = false;
        System.out.println("Simulation stop requested...");
    }

    public boolean isRunning() {
        return running;
    }

    public boolean isPaused() {
        return paused;
    }

    public boolean isStopped() {
        return stopped;
    }

    public Simulator startOrResumeSimulation() {
        Optional<Simulator> optionalSim = findBy(map, scenario);
        if (optionalSim.isPresent()) {
            this.simulator = optionalSim.get();
        } else {
            this.simulator = new Simulator(currentUser, map, scenario, scenario.getBudget(), scenario.getCurrentYear());
            simRep.add(simulator);
        }
        displaySimulationHeader();
        return simulator;
    }

    private void displaySimulationHeader() {
        System.out.println("-------------------------------------------------------------------------");
        System.out.println("\nSIMULATION STARTING IN SCENARIO " + scenario.getScenarioName() + ", FROM " + scenario.getRestrictions().getStartDate() + " TO " + scenario.getRestrictions().getEndDate());
        System.out.println("\nYOUR CURRENT BUDGET IS " + scenario.getBudget().getBudget() + "€.");
        displayHistoricalEvents();
    }

    private void displayHistoricalEvents() {
        List<String> historicalEvents = scenario.getRestrictions().getHistoricalEvents();

        System.out.println("YOUR SIMULATION WILL BE CONTINUED WITH THE FOLLOWING EVENTS: ");
        for (String event : historicalEvents) {
            if (!event.equalsIgnoreCase("none")) {
                System.out.print(event + " ");
            }
        }
        System.out.println();
    }

    private void displayStats(int currentYear, int totalEarnings, Scenario scenario) {
        System.out.println("\n\n------------STATS OF YEAR " + currentYear + "------------");
        System.out.println("\nYOU'VE EARNED A TOTAL OF " + totalEarnings + "€.");
        System.out.println("YOUR BUDGET IS NOW " + scenario.getBudget().getBudget() + "€.");
    }

    private void displayIndustryStats(Industry industry, int cargo) {
        System.out.println("\n\n------------INDUSTRY STATS------------");
        System.out.println("\nINDUSTRY '" + industry.getIndustryType() + "', LOCATED AT (" + industry.getPositionX() + "," + industry.getPositionY() +
                ") HAS GENERATED " + cargo + " CARGO.");
    }

    private void displayLocomotiveStats() {
        System.out.println("\n\n------------LOCOMOTIVE STATS------------");
        System.out.println("\n");
    }

    public void SimulateGame() {
        if (!canStartSimulation()) return;
        running = true;
        stopped = false;
        initializeAndRunSimulation();
    }

    @Override
    public void run() {
        SimulateGame();
    }

    private boolean canStartSimulation() {
        return scenario.getCurrentYear() <= scenario.getRestrictions().getEndDate().getYear();
    }

    private void initializeAndRunSimulation() {
        List<String> events = scenario.getRestrictions().getHistoricalEvents();
        double cargoMultiplier = calculateCargoMultiplier(events);
        double costMultiplier = calculateCostMultiplier(events);

        List<Industry> industries = Repositories.getInstance().getIndustryRepository().getIndustriesByMap(map);
        List<Train> trains = Repositories.getInstance().getTrainRepository().getAlltrains();

        try {
            runSimulationLoop(industries, trains, cargoMultiplier, costMultiplier, events);
        } catch (InterruptedException e) {
            System.out.println("Simulation was interrupted.");
        }
    }

    private double calculateCargoMultiplier(List<String> events) {
        double m = 1.0;
        if (events.contains("pandemic")) m *= PANDEMIC_INITIAL_MULTIPLIER;
        if (events.contains("war")) m *= WAR_CARGO_MULTIPLIER;
        return m;
    }

    private double calculateCostMultiplier(List<String> events) {
        return events.contains("war") ? WAR_COST_MULTIPLIER : 1.0;
    }

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
                displayStats(year, totalEarnings, scenario);
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
            System.out.println("\n\nSIMULATION COMPLETED! End year reached.");
        } else if (stopped) {
            System.out.println("\n\nSIMULATION STOPPED BY USER.");
        }
    }

    private int processYearSimulation(List<Industry> industries, List<Train> trains, double cargoMultiplier, double costMultiplier) {
        int industryEarnings = processIndustries(industries, cargoMultiplier);
        int houseBlockEarnings = processHouseBlockCargo();
        int trainEarnings = processTrains(trains, costMultiplier);

        int totalEarnings = industryEarnings + houseBlockEarnings + trainEarnings;

        System.out.println("\n\n--- YEAR SIMULATION SUMMARY ---");
        System.out.println("INDUSTRIES CONTRIBUTION: " + industryEarnings + "€.");
        System.out.println("HOUSE BLOCK EARNINGS: " + houseBlockEarnings + "€.");
        System.out.println("\nTOTAL YEAR EARNINGS: " + totalEarnings + "€.");

        return totalEarnings;
    }

    private int processIndustries(List<Industry> industries, double cargoMultiplier) {
        int total = 0;
        for (Industry industry : industries) {
            double modRate = 1.0;
            System.out.println("\nMODE RATE: " + modRate);
            System.out.println("Cargo multiplier: " + cargoMultiplier);

            int cargo = (int) (industry.getCargoAmount() * cargoMultiplier * modRate);
            Station closestStation = findClosestStation(industry);
            if (closestStation != null) {
                closestStation.addCargo(cargo);
                System.out.println("\nAdded " + cargo + " cargo to station " + closestStation.getName() + " from industry at (" + industry.getPositionX() + "," + industry.getPositionY() + ").");
                System.out.println("Station '" + closestStation.getName() + " now has a total of " + closestStation.getCargo() + " cargo.");
            } else {
                System.out.println("\n[WARNING] No station found for industry at (" + industry.getPositionX() + "," + industry.getPositionY() + ").");
            }
            displayIndustryStats(industry, cargo);
            int industryEarnings = cargo * 5;
            total += industryEarnings;
        }
        return total;
    }

    private int processHouseBlockCargo() {
        int total = 0;
        for (City city : cityRep.getCities()) {
            int blocks = city.getBlocks().size();
            int cargo = blocks * 2;

            // find stations in city owned by current player
            List<Station> cityStations = stRep.getStations().stream().filter(s -> s.getName().contains(city.getName())).collect(Collectors.toList());

            if (!cityStations.isEmpty()) {
                // distribute cargo among user's stations in this city
                int cargoPerStation = cargo / cityStations.size();
                int remainingCargo = cargo % cityStations.size();

                for(int i = 0; i < cityStations.size(); i++) {
                    Station station = cityStations.get(i);
                    int stationCargo = cargoPerStation + (i < remainingCargo ? 1 : 0);
                    station.addCargo(stationCargo);
                    System.out.println("\n[HOUSE BLOCK] Station '" + station.getName() + "' now has a total of " + station.getCargo() + " cargo.");
                }
                // add earnings from city operations
                int cityEarnings = cargo * 3;
                total += cityEarnings;

                System.out.println("\nCITY '" + city.getName() + "' has " + blocks + " house blocks and generated " + cargo + " cargo.");
                System.out.println("IT EARNED A TOTAL OF " + cityEarnings + "€.");
            }
        }
        return total;
    }

    private int processTrains(List<Train> trains, double costMultiplier) {
        if(trains == null || trains.isEmpty()){
            return 0;
        }

        int totalEarned = 0, totalLost = 0;
        for (Train train : trains) {
            int fuel = (int) (train.getLocomotive().getFuelCost() * costMultiplier);
            int maintenance = (int) (train.getLocomotive().getMaintenanceCost() * costMultiplier);
            int totalCost = fuel + maintenance;

            int earnings = 0;
            if (train.getRoute() != null && !train.getRoute().isEmpty()) {
                double dist = train.calculateRouteDistance();
                double bMulti = calculateStationBuildingMultiplier(train.getRoute().get(train.getRoute().size() - 1).getStation());
                earnings = simulateTrainTrip(train, costMultiplier);
            } else {
                displayLocomotiveStats();
            }
            totalEarned += earnings;
            totalLost += totalCost;
        }
        int net = totalEarned - totalLost;
        System.out.println("\n\nYOU'VE GAINED A TOTAL OF " + totalEarned + "€ FROM TRAINS.");
        System.out.println("HOWEVER, YOU'VE LOST A TOTAL OF " + totalLost + "€.");
        System.out.println("YOUR CURRENT BALANCE IS " + net + "€.");
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

            int availableCargo = from.getCargo();
            int maxTrainCapacity = train.getNumberOfCarriages();
            int spaceAvailable = maxTrainCapacity - currentCargoLoad;

            if (!train.isReadyToDepart(i, availableCargo)) {
                System.out.println("\n\n[LOADING] Train '" + train.getLocomotive().getLocomotiveModel() +
                        "' waiting for cargo mode '" + currentPoint.getCargoMode() +
                        "' at station '" + from.getName() + "'.\nAvailable cargo: " + availableCargo +
                        "\nCurrent cargo on the train: " + currentCargoLoad);
                continue;
            }

            int cargoToLoad = Math.min(availableCargo, spaceAvailable);
            if (cargoToLoad > 0) {
                from.removeCargo(cargoToLoad); // remove cargo from the station
                currentCargoLoad += cargoToLoad; // add cargo to the train
                System.out.println("\n\n[CARGO REMOVED] Train '" + train.getLocomotive().getLocomotiveModel() +
                        "' removed " + cargoToLoad + " amount from station '" + from.getName() +
                        "'.\nAvailable cargo at station: " + from.getCargo() +
                        "\nTotal cargo on train: " + currentCargoLoad);
            }

            System.out.println("\n\n[TRAVEL] Train '" + train.getLocomotive().getLocomotiveModel() +
                    "' left from '" + from.getName() + "' to '" + to.getName() +
                    "' with " + currentCargoLoad + " amount of cargo.");

            double distance = from.distanceTo(to);
            double buildingMultiplier = calculateStationBuildingMultiplier(to);
            int earnings = (int) (distance * ROUTE_EARNINGS_PER_STATION * buildingMultiplier * costMultiplier);

            if (currentCargoLoad > 0) {
                int cargoToDeliver = currentCargoLoad;
                to.addDeliveredCargo(cargoToDeliver);
                currentCargoLoad = 0;

                System.out.println("\n\n[CARGO DELIVERED] Train '" + train.getLocomotive().getLocomotiveModel() +
                        "' delivered " + cargoToDeliver + " amount of cargo to station '" + to.getName() +
                        "'.\nTrain is currently empty.");

                int deliveryBonus = cargoToDeliver * 10; // 10€ per unit delivered
                earnings += deliveryBonus;

                System.out.println("\n\n[REVENUE] Station '" + to.getName() + "': " + earnings + "€ " +
                        "(Distance: " + (int)distance + ", Deliver bonus: " + deliveryBonus + "€)");
            } else {
                System.out.println("\n\n[REVENUE] Station '" + to.getName() + "': " + earnings + "€ " +
                        "(Distance: " + (int)distance + ", No cargo to deliver.)");
            }

            totalEarnings += earnings;

            System.out.println("\n\n[ARRIVAL] Train '" + train.getLocomotive().getLocomotiveModel() +
                    "' arrived at '" + to.getName() + "'.");
        }

        System.out.println("\n\n[TRAVEL SUM] Train '" + train.getLocomotive().getLocomotiveModel() +
                "' completed its route.\nTotal revenue: " + totalEarnings + "€");

        return totalEarnings;
    }

    private double calculateStationBuildingMultiplier(Station station) {
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

    private void updateStationDemand() {
        for (Station s : stRep.getStations()) {
            int lastDelivered = s.getLastYearDeliveries();
            int currentDemand = s.getDemand();
            s.setDemand((int) (currentDemand * 0.8 + lastDelivered * 0.2));
        }
    }

    private void checkNewLocomotives(int year) {
        for (Locomotive l : locoRep.getAllLocomotives()) {
            if (l.getStartYear() == year && !locoRep.isAvailableToPlayer(l)) {
                locoRep.unlockForPlayer(l);
                System.out.println("New locomotive available: " + l.getLocomotiveModel());
            }
        }
    }

    private void processFinancialResults() {
        int maintenance = 0, fuel = 0, deliveries = 0;

        for (Locomotive l : locoRep.getPlayerLocomotives()) {
            maintenance += l.getMaintenanceCost();

            Train train = Repositories.getInstance().getTrainRepository().getTrainByLocomotive(l);
            if (train.getRoute() != null) {
                double dist = train.calculateRouteDistance();
                int fuelCost = l.getFuelCost();

                fuel += (int) (dist * fuelCost) / 500;
            }
        }

        for (Station s : stRep.getStations()) {
            deliveries += s.getLastYearDeliveries();
        }

        financialController.addFinancialResults(0, maintenance, fuel, deliveries);
    }

    private Station findClosestStation(Industry i) {
        Station closest = null;
        int minDist = Integer.MAX_VALUE;
        for (Station s : stRep.getStations()) {
            if (!s.getMap().getName().equalsIgnoreCase(map.getName())) continue;
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

    public Map getMap() {
        return map;
    }

    public Scenario getScenario() {
        return scenario;
    }

    public Optional<Simulator> findBy(Map map, Scenario scenario) {
        return simRep.findBy(map, scenario, currentUser);
    }

    public void saveSimulation(Simulator simulator) {
        simRep.add(simulator);
    }
}