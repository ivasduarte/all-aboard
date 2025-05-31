package controller;

import domain.*;
import repositories.*;

import java.io.Serializable;
import java.util.List;
import java.util.Optional;

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

    private void displayLocomotiveStats(Locomotive locomotive, int maintenanceCost) {
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
        List<Locomotive> locomotives = locoRep.getPlayerLocomotives();

        try {
            runSimulationLoop(industries, locomotives, cargoMultiplier, costMultiplier, events);
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

    private void runSimulationLoop(List<Industry> industries, List<Locomotive> locomotives, double cargoMultiplier, double costMultiplier, List<String> events) throws InterruptedException {
        int year = scenario.getCurrentYear();
        int endYear = scenario.getRestrictions().getEndDate().getYear();
        double currentCargoMultiplier = cargoMultiplier;

        while (running && !stopped && year <= endYear) {
            while (paused && !stopped) {
                Thread.sleep(500);
            }

            Thread.sleep(SIMULATION_SLEEP_TIME);

            if (!paused && !stopped) {
                int totalEarnings = processYearSimulation(industries, locomotives, currentCargoMultiplier, costMultiplier);
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

    private int processYearSimulation(List<Industry> industries, List<Locomotive> locomotives, double cargoMultiplier, double costMultiplier) {
        return processIndustries(industries, cargoMultiplier, costMultiplier)
                + processHouseBlockCargo()
                + processLocomotives(locomotives, costMultiplier);
    }

    private int processIndustries(List<Industry> industries, double cargoMultiplier, double costMultiplier) {
        int total = 0;
        for (Industry industry : industries) {
            double modRate = scenario.getModificationRateFor();
            int cargo = (int) (industry.getCargoAmount() * cargoMultiplier * modRate);
            Station s = findClosestStation(industry);
            if (s != null) s.addCargo(cargo);
            displayIndustryStats(industry, cargo);
        }
        return total;
    }

    private int processHouseBlockCargo() {

        int total = 0;
        for (City city : cityRep.getCities()) {
            int blocks = city.getBlocks().size();
            int cargo = blocks * 2;
            stRep.getStations().stream().filter(s -> s.getMapName().equals(city.getMapName())).filter(s -> currentUser.getEmail().equals(s.getOwnerPlayer())).forEach(s -> s.addCargo(cargo));
            //total += cargo;
        }
        return total;
    }

    private int processLocomotives(List<Locomotive> locomotives, double costMultiplier) {
        if(locomotives == null || locomotives.isEmpty()){
            return 0;
        }

        int totalEarned = 0, totalLost = 0;
        for (Locomotive l : locomotives) {
            int fuel = (int) (l.getFuelCost() * costMultiplier);
            int maintenance = (int) (l.getMaintenanceCost() * costMultiplier);
            int totalCost = fuel + maintenance;

            int earnings = 0;
            if (l.getRoute() != null && !l.getRoute().isEmpty()) {
                double dist = calculateRouteDistance(l.getRoute());
                double bMulti = calculateStationBuildingMultiplier(l.getRoute().get(l.getRoute().size() - 1));
                earnings = simulateTrainTrip(l, costMultiplier);
            } else {
                displayLocomotiveStats(l, totalCost);
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

    private double calculateArrivalTime(Locomotive loco, List<Station> route, boolean isDoubleTrack, Train train) {
        double baseSpeed = loco.getMaxSpeed();
        if (!isDoubleTrack) {
            baseSpeed *= 0.8;
        }

        int degradation = Math.min(30, train.getNumberOfCarriages() * 5);
        double effectiveSpeed = baseSpeed * (1 - degradation / 100.0);

        double distance = calculateRouteDistance(route);
        return distance / effectiveSpeed;
    }

    private int simulateTrainTrip(Locomotive loco, double costMultiplier) {
        if (loco.getRoute() == null || loco.getRoute().isEmpty()) return 0;

        List<Station> route = loco.getRoute();
        boolean isDoubleTrack = loco.isOnDoubleTrack();

        double timeToArrive = calculateArrivalTime(loco, route, isDoubleTrack, new Train(loco, route.get(0), route.get(route.size() - 1), 2011, 5, false));

        double distance = calculateRouteDistance(route);
        double buildingMultiplier = calculateStationBuildingMultiplier(route.get(route.size() - 1));

        return (int) (distance * ROUTE_EARNINGS_PER_STATION * buildingMultiplier * costMultiplier);
    }


    private double calculateRouteDistance(List<Station> route) {
        double dist = 0;
        for (int i = 0; i < route.size() - 1; i++) {
            int dx = route.get(i).getPositionX() - route.get(i + 1).getPositionX();
            int dy = route.get(i).getPositionY() - route.get(i + 1).getPositionY();
            dist += Math.sqrt(dx * dx + dy * dy);
        }
        return dist;
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
        int maintenance = 0, fuel = 0;

        for (Locomotive l : locoRep.getPlayerLocomotives()) {
            maintenance += l.getMaintenanceCost();

            if (l.getRoute() != null) {
                double dist = calculateRouteDistance(l.getRoute());
                int fuelCost = l.getFuelCost();

                fuel += (int) (dist * fuelCost) / 100;
            }
        }

        financialController.addFinancialResults(0, maintenance, fuel, 0);
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