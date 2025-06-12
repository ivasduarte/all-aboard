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
    private final int TRACK_MAINTENANCE_COST_PER_100KM = 25;

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
        int totalEarnings = 0;
        List<Station> userStations = getUserStationsForMap();

        if (userStations.isEmpty()) {
            System.out.println("No user stations available for industry cargo generation");
            return 0;
        }

        for (Industry industry : industries) {
            List<Station> overlappingStations = findOverlappingStations(userStations, industry);

            if (!overlappingStations.isEmpty()) {
                int baseCargoAmount = getIndustryCargoAmount(industry);
                int adjustedCargo = (int) (baseCargoAmount * cargoMultiplier);

                if (adjustedCargo > 0) {
                    Station targetStation = overlappingStations.get(0);
                    int actualCargoAdded = addCargoToStationWithCapacity(targetStation, adjustedCargo);
                    distributeIndustryCargoByType(targetStation, industry, actualCargoAdded);

                    int industryEarnings = calculateIndustryEarnings(industry, actualCargoAdded);
                    totalEarnings += industryEarnings;

                    displayIndustryProcessingInfo(industry, actualCargoAdded, targetStation, industryEarnings);
                }
            } else {
                System.out.println("\n[WARNING] No overlapping stations found for industry '" +
                        industry.getIndustryType() + "' at (" + industry.getPositionX() + "," + industry.getPositionY() + ").");
            }
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
        System.out.println("\n\n------------INDUSTRY PROCESSING------------");
        System.out.println("INDUSTRY '" + industry.getIndustryType() + "' at (" +
                industry.getPositionX() + "," + industry.getPositionY() +
                ") generated " + cargoAdded + " cargo.");
        System.out.println("Cargo added to station '" + station.getName() + "'.");
        System.out.println("Station now has " + station.getCargo() + " total cargo.");
        System.out.println("Industry earnings: " + earnings + "€");
        System.out.println("-------------------------------------------");
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

                for (int i = 0; i < cityStations.size(); i++) {
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
            } else {
                displayLocomotiveStats();
            }
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

            if (from.getMap() == null || !from.getMap().getName().equalsIgnoreCase(map.getName()) ||
                to.getMap() == null || !to.getMap().getName().equalsIgnoreCase(map.getName())) {
                continue;
            }

            int availableCargo = from.getCargo();
            int maxTrainCapacity = train.getNumberOfCarriages() * 5;
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
                        "'.");

                int deliveryBonus = cargoToDeliver * 10; // 10€ per unit delivered
                earnings += deliveryBonus;

                System.out.println("\n\n[REVENUE] Station '" + to.getName() + "': " + earnings + "€ " +
                        "(Distance: " + (int) distance + ", Deliver bonus: " + deliveryBonus + "€)");
            } else {
                System.out.println("\n\n[REVENUE] Station '" + to.getName() + "': " + earnings + "€ " +
                        "(Distance: " + (int) distance + ", No cargo to deliver.)");
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
                System.out.println("New locomotive available: " + l.getLocomotiveModel());
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