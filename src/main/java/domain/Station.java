package domain;

import repositories.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a station in the railway system with buildings, cargo management,
 * and position validation capabilities.
 */
public class Station {
    private static final int MAX_SLOTS = 7;
    private static int totalStations = 0;

    private int stationID;
    private String ownerPlayer;
    private String mapName;
    private String scenarioName;
    private int mapID;
    private int positionX;
    private int positionY;

    private String stationType;
    private int stationTypeID;
    private String orientation;
    private List<Building> buildings;
    private int cargo;

    public final static String[] stationTypes = {"Depot", "Station", "Terminal"};
    public final static int[] typesSizes = {3, 4, 5};
    public final static int[] typesPrices = {50, 100, 200};
    public final static String[] rotations = {"NE", "NW", "SE", "SW"};

    /**
     * Main constructor for creating a station with all parameters.
     *
     * @param mapID id of the map
     * @param positionX station's X position
     * @param positionY station's Y position
     * @param stationType station type
     * @param orientation station orientation
     */
    public Station(int mapID, int positionX, int positionY, String stationType, String orientation) {
        this.mapID = mapID;
        this.positionX = positionX;
        this.positionY = positionY;
        this.stationType = stationType;

        for(int i = 0; i < stationTypes.length; i++)
        {
            if(stationType.equalsIgnoreCase(stationTypes[i]))
                stationTypeID = i;
        }

        this.orientation = orientation;
        this.stationID = totalStations;
        this.buildings = new ArrayList<>();
        this.cargo = 0;
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
     * Retrieves the amount of cargo associated with the station.
     *
     * @return the cargo amount as an integer.
     */
    public int getCargo() {
        return cargo;
    }

    /**
     * Adds cargo to the station.
     *
     * @param amount amount to add (must be positive)
     */
    public void addCargo(int amount) {
        if (amount > 0) {
            this.cargo += amount;
        }
    }

    /**
     * Removes cargo from the station, cannot go below 0.
     *
     * @param amount amount to remove (must be positive)
     */
    public void removeCargo(int amount) {
        if (amount > 0) {
            this.cargo = Math.max(0, this.cargo - amount);
        }
    }

    /**
     * Verify if the given station type exists.
     *
     * @param type the type to be checked
     * @return the id the of the type, returns -1 if it doesn't exist
     */
    public static int checkStationType(String type)
    {
        int id = -1;
        for(int i = 0; i < Station.stationTypes.length; i++)
        {
            if(type.equalsIgnoreCase(Station.stationTypes[i]))
            {
                id = i;
            }
        }

        return id;
    }

    /**
     * Validates the station's position on the map.
     *
     * @param map station's map
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
    public String getStationType() {
        return stationType;
    }

    /**
     * Retrieves the orientation of the station.
     *
     * @return the orientation of the station as an {@code Orientation} enum value.
     */
    public String getOrientation() {
        return orientation;
    }

    /**
     * Retrieves the size of the station based on its type.
     *
     * @return the size of the station as an integer
     */
    public int getStationSize() {
        return typesSizes[stationTypeID];
    }

    /**
     * Retrieves the price of the station based on its type.
     *
     * @return the price of the station as an integer
     */
    public int getStationPrice() {
        return typesPrices[stationTypeID];
    }

    /**
     * Retrieves the numeric identifier for the station's type.
     *
     * @return an integer representing the ordinal value of the station's type.
     */
    public int getStationTypeID() {
        return stationTypeID;
    }

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
     * Updates the station type using a string representation.
     * The string is parsed to determine the corresponding {@code StationType} enum value.
     *
     * @param stationType the string representation of the station type.
     *                    It should match the display name of a valid {@code StationType}.
     *                    Case is ignored during comparison.
     * @throws IllegalArgumentException if the provided string does not match any valid station type.
     */
    public void setStationType(String stationType) {
        this.stationType = stationType;
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
        this.orientation = orientation;
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
}