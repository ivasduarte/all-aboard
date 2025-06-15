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
    private int    mapId;
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


    public int getStationID() { return id; }

    public String getName() { return name; }

    public String getStationType() { return type; }

    public int getX() { return x; }

    public int getY() { return y; }

    public int getCargo() { return cargo; }

    public List<String> getBuildings() { return List.copyOf(buildings); }

    public Station.Orientation getOrientation() { return orientation; }

    public String getMapName()      { return mapName; }

    public int    getMapId()        { return mapId; }

    public String getScenarioName() { return scenarioName; }

}
