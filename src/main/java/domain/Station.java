package domain;

import controller.DisplayMapController;
import repositories.*;

import java.util.ArrayList;
import java.util.List;

public class Station {
    private int mapID;
    private int positionX;
    private int positionY;

    private int stationID;
    private String stationType;

    private static int totalStations = 0;

    private static final int MAX_SLOTS = 7;
    private List<Building> buildings;

    private int cargo;

    public final static String[] stationTypes = {"Depot", "Station", "Terminal"};
    public final static int[] typesSizes = {3, 4, 5};
    public final static int[] typesPrices = {50, 100, 200};
    /**
     * Constructs a station, in a certain map and scenario, with a certain position and a type
     * @param mapID id of the map
     * @param positionX station's X position
     * @param positionY station's Y position
     * @param stationType station type
     */
    public Station(int mapID, int positionX, int positionY, String stationType)
    {
        this.mapID = mapID;
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
     * @param size size for each side of the station
     * @return true if position is valid, false otherwise
     */
    public boolean ValidatePosition(Map map, int size)
    {
        if(size % 2 != 0)
            return positionX - size >= 0 && positionX + size <= map.getWidth() && positionY - size >= 0 && positionY + size <= map.getHeight();
        else
            return positionX - size >= 0 && positionX + size + 1 <= map.getWidth() && positionY - size >= 0 && positionY + size + 1 <= map.getHeight();
    }

    /**
     * Check if station overbuilds existing houseblocks, industries or other stations
     * @return false if overbuilding occurs, true if not
     */
    public boolean checkOverbuilding() {
        MapRepository mapController = Repositories.getInstance().getMapRepository();
        IndustryRepository indRepo = Repositories.getInstance().getIndustryRepository();
        CityRepository cityRepo = Repositories.getInstance().getCityRepository();
        StationRepository stationRepository = Repositories.getInstance().getStationRepository();

        List<Map> mapList = mapController.getMapList();
        Map map = mapList.get(mapID);

        List<Industry> industries = indRepo.getIndustryList();
        List<City> cities = cityRepo.getCities();
        List<Station> stations = stationRepository.getStations();

        if (map == null) {
            return false;
        }

        for (int i = 0; i < industries.size(); i++) {
            if (industries.get(i).getPositionX() == positionX && industries.get(i).getPositionY() == positionY) {
                return false;
            }
        }

        for (int i = 0; i < stations.size(); i++) {
            if (stations.get(i).getPositionX() == positionX && stations.get(i).getPositionY() == positionY) {
                return false;
            }
        }

        for (int i = 0; i < cities.size(); i++) {
            for (int j = 0; j < cities.get(i).getBlocks().size(); j++) {
                List<HouseBlock> hb = cities.get(i).getBlocks();

                if (hb.get(j).getX() == positionX && hb.get(j).getY() == positionY) {
                    return false;
                }
            }
        }
        return true;
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

    /**
     * Get the amount of cargo stored in the station
     * @return current cargo
     */
    public int getCargo() {
        return cargo;
    }

    /**
     * Adds cargo to the station
     * @param amount amount to add
     */
    public void addCargo(int amount) {
        this.cargo += amount;
    }

    /**
     * Removes cargo from the station, cannot go below 0
     * @param amount amount to remove
     */
    public void removeCargo(int amount) {
        this.cargo = Math.max(0, this.cargo - amount);
    }
}
