package dto;

import java.util.Objects;

public class MapDTO {
    private String name;
    private int width;
    private int height;
    private int cellSizeKm;

    public MapDTO(String name, int cellSizeInKm, int height, int width) {
        this.name = name;
        this.cellSizeKm = cellSizeInKm;
        this.height = height;
        this.width = width;
    }

    public String getName() {
        return name;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public int getCellSizeKm() {
        return cellSizeKm;
    }

    public double getTotalWidthKm() {
        return width * cellSizeKm;
    }

    public double getTotalHeightKm() {
        return height * cellSizeKm;
    }

    public double getAreaKm() {
        return getTotalWidthKm() * getTotalHeightKm();
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof MapDTO)) return false;
        MapDTO mapDTO = (MapDTO) o;
        return width == mapDTO.width && height == mapDTO.height && cellSizeKm == mapDTO.cellSizeKm && Objects.equals(name, mapDTO.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, width, height, cellSizeKm);
    }

    @Override
    public String toString() {
        return "Map '" + name + "' (" + width + "x" + height + "), Cell size: " + cellSizeKm + " km, Area: " + getAreaKm() + " km²";
    }
}
