package domain;

import repositories.*;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Represents a station in the railway system with buildings, cargo management,
 * and position validation capabilities.
 */
public class Station implements Serializable
{
    private static final int MAX_SLOTS = 7;
    private static final int MAX_CARGO = 24;
    private static int totalStations = 0;

    private int stationID;
    private String ownerPlayer;
    private String mapName;
    private String scenarioName;
    private int mapID;
    private int positionX;
    private int positionY;
    private String name;

    private StationType stationType;
    private Orientation orientation;
    private List<Building> buildings;

    private int lastYearDeliveries;
    private int demand;

    private List<StationCargo> cargos;

    /**
     * Enum representing different types of orientation with their properties.
     */
    public enum Orientation {
        NW("Northwest", "NW"),
        NE("Northeast", "NE"),
        SW("Southwest", "SW"),
        SE("Southeast", "SE");

        private final String displayName;
        private final String shortening;

        Orientation(String displayName, String shortening) {
            this.displayName = displayName;
            this.shortening = shortening;
        }

        public String getDisplayName() {
            return displayName;
        }

        public String getShortening() {
            return shortening;
        }

        /**
         * Converts a string to an equivalent Orientation enumeration value.
         * Compares both enum constant names and display names (case-insensitive).
         *
         * @param orientation The string representation of the orientation
         * @return The corresponding Orientation enumeration value
         * @throws IllegalArgumentException if no matching orientation is found
         */
        public static Orientation fromString(String orientation) {
            for (Orientation orient : Orientation.values()) {
                if (orient.name().equalsIgnoreCase(orientation) ||
                        orient.displayName.equalsIgnoreCase(orientation)) {
                    return orient;
                }
            }
            throw new IllegalArgumentException("Invalid orientation: " + orientation);
        }

        @Override
        public String toString() {
            return displayName;
        }
    }

    /**
     * Enum representing different types of stations with their properties.
     */
    public enum StationType {
        DEPOT("Depot", 3, 50),
        STATION("Station", 4, 100),
        TERMINAL("Terminal", 5, 200);

        private final String displayName;
        private final int size;
        private final int price;

        StationType(String displayName, int size, int price) {
            this.displayName = displayName;
            this.size = size;
            this.price = price;
        }

        public String getDisplayName() {
            return displayName;
        }

        public int getSize() {
            return size;
        }

        public int getPrice() {
            return price;
        }

        /**
         * Parses a string to find a matching StationType based on display name.
         *
         * @param type the string representation of the station type
         * @return the corresponding StationType
         * @throws IllegalArgumentException if no valid StationType matches
         */
        public static StationType fromString(String type) {
            for (StationType stationType : StationType.values()) {
                if (stationType.displayName.equalsIgnoreCase(type)) {
                    return stationType;
                }
            }
            throw new IllegalArgumentException("Invalid station type: " + type);
        }

        @Override
        public String toString() {
            return displayName;
        }
    }

    public class StationCargo
    {
        private Cargo.Type type;
        private int amount;

        StationCargo(Cargo.Type type, int amount)
        {
            this.type = type;
            this.amount = amount;
        }

        public Cargo.Type getType() {return type;}
        public int getAmount() {return amount;}

        public void addCargo(int amount) {
            int totalCargo = getTotalCargo();
            int amountToAdd = amount;

            if(totalCargo + amount > MAX_CARGO)
            {
                amountToAdd = MAX_CARGO - totalCargo;
            }

            this.amount += amountToAdd;
        }

        public void removeCargo(int amount) {
            if (amount > 0) {
                this.amount = Math.max(0, this.amount - amount);
            }
        }
    }

    /**
     * Main constructor for creating a station with all parameters.
     *
     * @param mapID       id of the map
     * @param positionX   station's X position
     * @param positionY   station's Y position
     * @param stationType station type
     * @param orientation station orientation
     */
    public Station(String mapName, int mapID, int positionX, int positionY, StationType stationType, Orientation orientation, String name) {
        this.mapName = mapName;
        this.mapID = mapID;
        this.positionX = positionX;
        this.positionY = positionY;
        this.stationType = stationType;
        this.orientation = orientation;
        this.name = name;
        this.stationID = totalStations;
        this.buildings = new ArrayList<>();

        this.cargos = new ArrayList<>();
        for(Cargo.Type cargo : Cargo.Type.values())
        {
            if(cargo != Cargo.Type.NONE)
                cargos.add(new StationCargo(cargo, 0));
        }
    }

    /**
     * Adds a building to the station if possible.
     *
     * @param buildingToAdd the building to add
     * @return true if successfully added, false otherwise
     * @throws IllegalArgumentException if building is null
     */
    public boolean addBuilding(Building buildingToAdd) {
        if (buildingToAdd == null) {
            throw new IllegalArgumentException("Invalid building to add");
        }

        if (buildings.size() >= MAX_SLOTS) {
            System.out.println("Warning: Maximum number of buildings reached.");
            return false;
        }

        // check for duplicate building types
        for (Building building : buildings) {
            if (building.getBuildingType().equalsIgnoreCase(buildingToAdd.getBuildingType())) {
                return false;
            }
        }

        buildings.add(buildingToAdd);
        return true;
    }

    /**
     * Checks if station has available building spots.
     *
     * @return true if more buildings can be added, false otherwise
     */
    public boolean isAvailable() {
        return buildings.size() < MAX_SLOTS;
    }

    public List<Building> getBuildings() {
        return new ArrayList<>(buildings);
    }

    /**
     * Retrieves the name of the station.
     *
     * @return the name of the station.
     */
    public String getName() {
        return name;
    }

    /**
     * Validates the station's position on the map.
     *
     * @param map  station's map
     * @param size size for each side of the station
     * @return true if position is valid, false otherwise
     */
    public boolean validatePosition(Map map, int size) {
        if (map == null) {
            return false;
        }

        if (size % 2 != 0) {
            return positionX - size >= 0 && positionX + size <= map.getWidth() &&
                    positionY - size >= 0 && positionY + size <= map.getHeight();
        } else {
            return positionX - size >= 0 && positionX + size + 1 <= map.getWidth() &&
                    positionY - size >= 0 && positionY + size + 1 <= map.getHeight();
        }
    }

    /**
     * Checks if station would overbuild existing structures.
     *
     * @return false if overbuilding occurs, true if position is clear
     */
    public boolean checkOverbuilding() {
        Repositories repos = Repositories.getInstance();
        MapRepository mapRepo = repos.getMapRepository();
        IndustryRepository indRepo = repos.getIndustryRepository();
        CityRepository cityRepo = repos.getCityRepository();
        StationRepository stationRepo = repos.getStationRepository();

        List<Map> mapList = mapRepo.getMapList();
        if (mapID >= mapList.size()) {
            return false;
        }

        // check industries
        for (Industry industry : indRepo.getIndustryList()) {
            if (industry.getPositionX() == positionX && industry.getPositionY() == positionY) {
                return false;
            }
        }

        // check existing stations
        for (Station station : stationRepo.getStations()) {
            if (station.getPositionX() == positionX && station.getPositionY() == positionY) {
                return false;
            }
        }

        // check city blocks
        for (City city : cityRepo.getCities()) {
            for (HouseBlock block : city.getBlocks()) {
                if (block.getX() == positionX && block.getY() == positionY) {
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * Retrieves the unique identifier of the station.
     *
     * @return the station ID as an integer
     */
    public int getStationID() {
        return stationID;
    }

    /**
     * Retrieves the name or identifier of the player who owns the station.
     *
     * @return the owner of the station as a string.
     */
    public String getOwnerPlayer() {
        return ownerPlayer;
    }

    /**
     * Retrieves the name of the map associated with the station.
     *
     * @return the map name as a string.
     */
    public String getMapName() {
        return mapName;
    }

    /**
     * Retrieves the name of the scenario associated with the station.
     *
     * @return the scenario name as a string.
     */
    public String getScenarioName() {
        return scenarioName;
    }

    /**
     * Retrieves the unique identifier for the map associated with the station.
     *
     * @return the map ID as an integer
     */
    public int getMapID() {
        return mapID;
    }

    /**
     * Retrieves the X-coordinate of the station's position.
     *
     * @return the X-coordinate of the station as an integer.
     */
    public int getPositionX() {
        return positionX;
    }

    /**
     * Retrieves the Y-coordinate of the station's position.
     *
     * @return the Y-coordinate of the station as an integer.
     */
    public int getPositionY() {
        return positionY;
    }

    /**
     * Retrieves the type of the station.
     *
     * @return the station type as a {@code StationType} enum value
     */
    public StationType getStationType() {
        return stationType;
    }

    /**
     * Retrieves the orientation of the station.
     *
     * @return the orientation of the station as an {@code Orientation} enum value.
     */
    public Orientation getOrientation() {
        return orientation;
    }

    /**
     * Retrieves the size of the station based on its type.
     *
     * @return the size of the station as an integer
     */
    public int getStationSize() {
        return stationType.getSize();
    }

    /**
     * Retrieves the price of the station based on its type.
     *
     * @return the price of the station as an integer
     */
    public int getStationPrice() {
        return stationType.getPrice();
    }

    /**
     * Retrieves the numeric identifier for the station's type.
     *
     * @return an integer representing the ordinal value of the station's type.
     */
    public int getStationTypeID() {
        return stationType.ordinal();
    }

    /**
     * Retrieves the string representation of the station's type.
     *
     * @return the display name of the station type as a string.
     */
    public String getStationTypeString() {
        return stationType.getDisplayName();
    }

    /**
     * Retrieves the string representation of the station's orientation.
     *
     * @return the display name of the station's orientation as a string.
     */
    public String getOrientationString() {
        return orientation.getDisplayName();
    }

    public List<StationCargo> getStationCargo() { return cargos; }

    /**
     * Retrieves the map associated with the current station.
     * The map is fetched from a repository of maps using the station's map ID.
     * If the map ID is invalid or out of bounds, null is returned.
     *
     * @return the map object corresponding to the station's map ID, or null if the map ID is invalid.
     */
    public Map getMap() {
        MapRepository mr = Repositories.getInstance().getMapRepository();
        List<Map> maps = mr.getMapList();

        if (mapID >= 0 && mapID < maps.size()) {
            return maps.get(mapID);
        }
        return null;
    }

    /**
     * Sets the owner of the station.
     *
     * @param ownerPlayer the name or identifier of the player to be assigned as the owner of the station
     */
    public void setOwnerPlayer(String ownerPlayer) {
        this.ownerPlayer = ownerPlayer;
    }

    /**
     * Sets the name of the map associated with the station.
     *
     * @param mapName the name of the map to be assigned to the station
     */
    public void setMapName(String mapName) {
        this.mapName = mapName;
    }

    /**
     * Sets the name of the scenario associated with the station.
     *
     * @param scenarioName the name of the scenario to be assigned to the station
     */
    public void setScenarioName(String scenarioName) {
        this.scenarioName = scenarioName;
    }

    /**
     * Sets the map ID for the station.
     *
     * @param mapID the unique identifier for the map to which the station is associated
     */
    public void setMapID(int mapID) {
        this.mapID = mapID;
    }

    /**
     * Sets the position of the station by assigning X and Y coordinate values.
     *
     * @param positionX the X-coordinate of the station
     * @param positionY the Y-coordinate of the station
     */
    public void setPosition(int positionX, int positionY) {
        this.positionX = positionX;
        this.positionY = positionY;
    }

    /**
     * Sets the type of the station.
     *
     * @param stationType the {@code StationType} to be assigned to the station
     */
    public void setStationType(StationType stationType) {
        this.stationType = stationType;
    }

    /**
     * Updates the station type using a string representation.
     * The string is parsed to determine the corresponding {@code StationType} enum value.
     *
     * @param stationType the string representation of the station type.
     *                    It should match the display name of a valid {@code StationType}.
     *                    Case is ignored during comparison.
     * @throws IllegalArgumentException if the provided string does not match any valid station type.
     */
    public void setStationType(String stationType) {
        this.stationType = StationType.fromString(stationType);
    }

    /**
     * Sets the orientation of the station.
     *
     * @param orientation the desired orientation of the station, provided
     *                    as an {@code Orientation} enum value
     */
    public void setOrientation(Orientation orientation) {
        this.orientation = orientation;
    }

    /**
     * Sets the orientation of the station using a string representation.
     * The string is converted to an {@code Orientation} enumeration value.
     *
     * @param orientation the string representation of the desired orientation,
     *                    which can be either the name of the enum constant or its display name.
     *                    Comparison is case-insensitive.
     * @throws IllegalArgumentException if the provided string does not match any valid orientation.
     */
    public void setOrientation(String orientation) {
        this.orientation = Orientation.fromString(orientation);
    }


    /**
     * Increases the total count of existing stations.
     */
    public static void increaseTotalStations() {
        totalStations++;
    }

    /**
     * Retrieves the total number of stations created.
     *
     * @return the total number of stations as an integer.
     */
    public static int getTotalStations() {
        return totalStations;
    }

    /**
     * Verify if the given station type exists.
     *
     * @param type the type to be checked
     * @return the id the of the type, returns -1 if it doesn't exist
     */
    public static int checkStationType(int type) {
        int id = -1;
        for (int i = 0; i < StationType.values().length; i++) {
            if (Arrays.stream(Station.StationType.values()).anyMatch(type1 -> type1.ordinal() == type - 1)) {
                id = i;
            }
        }
        return id;
    }

    /**
     * Calculates the Euclidean distance to another station.
     *
     * @param other The destination station
     * @return The distance between this station and the other station
     */
    public double distanceTo(Station other) {
        if (other == null) {
            throw new IllegalArgumentException("Destination station cannot be null");
        }

        int dx = this.positionX - other.positionX;
        int dy = this.positionY - other.positionY;

        return Math.sqrt(dx * dx + dy * dy);
    }

    public int getLastYearDeliveries() {
        return lastYearDeliveries;
    }

    public void setLastYearDeliveries(int lastYearDeliveries) {
        this.lastYearDeliveries = lastYearDeliveries;
    }

    public int getDemand() {
        return demand;
    }

    public void setDemand(int demand) {
        this.demand = demand;
    }

    @Override
    public String toString() {
        return name + " | " + stationType.displayName + " | (" + positionX + "," + positionY + ")";
    }

    /**
     * Add delivered cargo to station's delivery count
     *
     * @param amount Amount of cargo delivered
     */
    public void addDeliveredCargo(int amount) {
        this.lastYearDeliveries += amount;
        // You might also want to track total deliveries or update demand
    }

    public List<Industry> getIndustriesInRange()
    {
        MapRepository mapRepository = Repositories.getInstance().getMapRepository();
        List<Industry> industries = mapRepository.getMap(mapName).get().getIndustries();

        List<Industry> industriesInRange = new ArrayList<>();

        int size = stationType.getSize() / 2;

        if(stationType.getSize() % 2 != 0)
        {
            for(int i = positionX - size; i < positionX + size; i++)
            {
                for(int j = positionY - size; j < positionY + size; j++)
                {
                    for(Industry industry : industries)
                    {
                        if(industry.getPositionX() == i && industry.getPositionY() == j)
                            industriesInRange.add(industry);
                    }
                }
            }
        }else
        {
            int left, right, up, down;
            if(orientation.getShortening().contains("N"))
            {
                up = 1;
                down = 0;
            }else
            {
                up = 0;
                down = 1;
            }

            if(orientation.getShortening().contains("E"))
            {
                left = 0;
                right = 1;
            }else
            {
                left = 1;
                right = 0;
            }

            for(int i = positionX - size + left; i < positionX + size - right; i++)
            {
                for(int j = positionY - size + up; j < positionY + size - down; j++)
                {
                    for(Industry industry : industries)
                    {
                        if(industry.getPositionX() == i && industry.getPositionY() == j)
                            industriesInRange.add(industry);
                    }
                }
            }
        }

        return industriesInRange;
    }

    public int getTotalCargo()
    {
        int totalCargo = 0;

        for(StationCargo cargo : cargos)
        {
            totalCargo += cargo.getAmount();
        }

        return totalCargo;
    }
}