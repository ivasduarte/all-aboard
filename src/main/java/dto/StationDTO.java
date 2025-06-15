package dto;

import java.util.List;

public class StationDTO {

    private int id;
    private String name;
    private String type;
    private int x;
    private int y;
    private int cargo;
    private List<String> buildings;

    public StationDTO(int id, String name, String type, int x, int y, int cargo, List<String> buildings) {
        this.id = id;
        this.name = name;
        this.type = type;
        this.x = x;
        this.y = y;
        this.cargo = cargo;
        this.buildings = buildings;

    }


    public int getStationID() { return id; }

    public String getName() { return name; }

    public String getStationType() { return type; }

    public int getX() { return x; }

    public int getY() { return y; }

    public int getCargo() { return cargo; }

    public List<String> getBuildings() { return List.copyOf(buildings); }

}
