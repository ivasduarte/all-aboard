package domain;

import repositories.MapRepository;

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

    public boolean addBuilding(Building buildingToAdd, int currentYear){

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


    public static void IncreaseTotalStations()
    {
        totalStations++;
    }

    public Map getMap() {
        MapRepository mr = new MapRepository();

        return mr.getMapList().get(mapID);
    }


    public int getMapID() {
        return mapID;
    }
    public int getScenarioID() {
        return scenarioID;
    }
    public int getPositionX() {
        return positionX;
    }
    public int getPositionY() {
        return positionY;
    }
    public int getStationID() { return stationID; }
    public String getStationType() {return stationType;}
    public String[] getStationTypes() {return stationTypes;}

    public void setMapID(int mapID) {
        this.mapID = mapID;
    }
    public void setScenarioID(int scenarioID) {
        this.scenarioID = scenarioID;
    }
    public void setPosition(int positionX, int positionY) {
        this.positionX = positionX; this.positionY = positionY;
    }
    public void setStationID(int stationID) { this.stationID = stationID; }
    public void setStationType(String stationType) {this.stationType = stationType;}

    public boolean validatePosition(Map map) {
        if (positionX < 0 || positionX > map.getWidth()) {
            return false;
        }
        if (positionY < 0 || positionY > map.getHeight()) {
            return false;
        }
        return true;
    }

    public boolean isAvailable() {
        return buildings.size() < MAX_SLOTS;
    }

}
