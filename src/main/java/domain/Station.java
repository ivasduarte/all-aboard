package domain;

import repositories.MapRepository;
import repositories.Repositories;

import java.util.ArrayList;
import java.util.List;

public class Station {
    private int mapID;
    private int scenarioID;
    private int positionX;
    private int positionY;

    private int stationID;
    private String stationType;

    private static int totalStations = 0;

    private static final int MAX_SLOTS = 7;
    private List<Building> buildings;

    public final static String[] stationTypes = {"Depot", "Station", "Terminal"};
    public final static int[] typesSizes = {3, 4, 5};

    /**
     * Constructs a station, in a certain map and scenario, with a certain position and a type
     * @param mapID id of the map
     * @param scenarioID id of the scenario
     * @param positionX station's X position
     * @param positionY station's Y position
     * @param stationType station type
     */
    public Station(int mapID, int scenarioID, int positionX, int positionY, String stationType)
    {
        this.mapID = mapID;
        this.scenarioID = scenarioID;
        this.positionX = positionX;
        this.positionY = positionY;
        this.stationType = stationType;
        stationID = totalStations;
        this.buildings = new ArrayList<>();
    }

    /**
     * Add a building on the station
     * @param buildingToAdd the building to add on the station
     * @return true if successfully added, false otherwise
     */
    public boolean addBuilding(Building buildingToAdd){

        if(buildingToAdd == null){
            throw new IllegalArgumentException("Invalid building to add");
        }

        if (buildings.size() >= MAX_SLOTS) {

            System.out.println("Warning: Maximum number of buildings reached.");
            return false;
        }

        for (int i = 0; i < buildings.size(); i++) {
            if(buildings.get(i).getBuildingType().equalsIgnoreCase(buildingToAdd.getBuildingType())){
                return false;

            }
        }

        buildings.add(buildingToAdd);

        return true;

    }

    /**
     * Validade the station's position on the map
     * @param map station's map
     * @param X station's X position
     * @param Y station's Y position
     * @param size size for each side of the station
     * @return true if position is valid, false otherwise
     */
    public boolean ValidatePosition(Map map, int X, int Y, int size)
    {
        if(size % 2 != 0)
            return X - size >= 0 && X <= map.getWidth() + size && Y - size >= 0 && Y <= map.getHeight() + size;
        else
            return X - size >= 0 && X <= map.getWidth() + size + 1 && Y - size >= 0 && Y <= map.getHeight() + size + 1;
    }

    /**
     * Increase the total of existing stations
     */
    public static void IncreaseTotalStations()
    {
        totalStations++;
    }

    /**
     * Get the station's map
     * @return station's map
     */
    public Map getMap() {
        MapRepository mr = Repositories.getInstance().getMapRepository();

        return mr.getMapList().get(mapID);
    }

    /**
     * Get the station's map id
     * @return station's map id
     */
    public int getMapID() {
        return mapID;
    }

    /**
     * Get the station's scenario id
     * @return station's scenario id
     */
    public int getScenarioID() {
        return scenarioID;
    }
    /**
     * Get the station's X position
     * @return station's X position
     */
    public int getPositionX() {
        return positionX;
    }
    /**
     * Get the station's Y position
     * @return station's Y position
     */
    public int getPositionY() {
        return positionY;
    }
    /**
     * Get the station's id
     * @return station's id
     */
    public int getStationID() { return stationID; }
    /**
     * Get the station's type
     * @return station's type
     */
    public String getStationType() {return stationType;}
    /**
     * Get the station's type id
     * @return station's type id
     */
    public int getStationTypeID()
    {
        for(int i = 0; i < stationTypes.length; i++)
        {
            if(stationType.equalsIgnoreCase(stationTypes[i]))
                return i;
        }

        return 0;
    }

    /**
     * Set station's map id
     * @param mapID new map id
     */
    public void setMapID(int mapID) {
        this.mapID = mapID;
    }

    /**
     * Set station's scenario id
     * @param scenarioID new scenario id
     */
    public void setScenarioID(int scenarioID) {
        this.scenarioID = scenarioID;
    }

    /**
     * Set station's position
     * @param positionX new X position
     * @param positionY new Y position
     */
    public void setPosition(int positionX, int positionY) {
        this.positionX = positionX; this.positionY = positionY;
    }

    /**
     * Set station's type
     * @param stationType new station type
     */
    public void setStationType(String stationType) {this.stationType = stationType;}

    /**
     * Check if station has available building spots
     * @return true if more buildings can be added, false otherwise
     */
    public boolean isAvailable() {
        return buildings.size() < MAX_SLOTS;
    }

    /**
     * Get the buildings of the station
     * @return the buildings of the station
     */
    public List<Building> getBuildings() {
        return buildings;
    }


}
