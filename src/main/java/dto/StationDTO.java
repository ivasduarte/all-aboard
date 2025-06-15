package dto;

import domain.Station;

import java.util.List;

public class StationDTO {

    private int id;
    private String name;
    private String type;
    private int x;
    private int y;
    private int cargo;
    private List<String> buildings;

    private String mapName;
    private int mapId;
    private String scenarioName;
    private Station.Orientation orientation;

    public StationDTO(int id, String name, String type, int x, int y, int cargo, List<String> buildings, String mapName, int mapId, String scenarioName, Station.Orientation orientation) {
        this.id = id;
        this.name = name;
        this.type = type;
        this.x = x;
        this.y = y;
        this.cargo = cargo;
        this.buildings = buildings;
        this.mapName = mapName;
        this.mapId = mapId;
        this.scenarioName = scenarioName;
        this.orientation = orientation;

    }


    public int getStationID() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getStationType() {
        return type;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getCargo() {
        return cargo;
    }

    public List<String> getBuildings() {
        return List.copyOf(buildings);
    }

    public Station.Orientation getOrientation() {
        return orientation;
    }

    public String getMapName() {
        return mapName;
    }

    public int getMapId() {
        return mapId;
    }

    public String getScenarioName() {
        return scenarioName;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setOrientation(Station.Orientation orientation) {
        this.orientation = orientation;
    }

    public void setScenarioName(String scenarioName) {
        this.scenarioName = scenarioName;
    }

    public void setMapId(int mapId) {
        this.mapId = mapId;
    }

    public void setMapName(String mapName) {
        this.mapName = mapName;
    }

    public void setBuildings(List<String> buildings) {
        this.buildings = buildings;
    }

    public void setCargo(int cargo) {
        this.cargo = cargo;
    }

    public void setY(int y) {
        this.y = y;
    }

    public void setX(int x) {
        this.x = x;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setName(String name) {
        this.name = name;
    }
}
