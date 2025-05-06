package domain;

import repositories.MapRepository;

public class Station {
    private int mapID;
    private int scenarioID;
    private int positionX;
    private int positionY;

    private int stationID;
    private String stationName;
    private String stationType;

    private static int totalStations = 0;

    public Station(int mapID, int scenarioID, int positionX, int positionY, String stationName, String stationType)
    {
        this.mapID = mapID;
        this.scenarioID = scenarioID;
        this.positionX = positionX;
        this.positionY = positionY;
        this.stationName = stationName;
        this.stationType = stationType;
        stationID = totalStations;
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
    public String getStationName() {return stationName;}
    public String getStationType() {return stationType;}

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
    public void setStationName(String stationName) {this.stationName = stationName;}
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
}
