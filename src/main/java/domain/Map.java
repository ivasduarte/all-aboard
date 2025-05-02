package domain;

public class Map {
    private String name;
    private int width;
    private int height;
    private char[][] matrix;
    private Editor createdBy;

    public Map(String name, int width, int height) {
        this.name = name;
        this.width = width;
        this.height = height;
        this.matrix = new char[height][width];
        validateName();
        validateSize();
    }

    public String getMapName() {
        return name;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    private void validateName() {
        if (name == null || name.isEmpty()) {
            throw new IllegalArgumentException("Map name cannot be empty.");
        }
    }

    private void validateSize() {
        if (width <= 0 || height <= 0) {
            throw new IllegalArgumentException("Map dimensions must be positive.");
        }
    }

    public char[][] getMatrix() {
        return matrix;
    }

    public void setCreatedBy(Editor editor) {
        this.createdBy = editor;
    }

    public Editor getCreatedBy() {
        return createdBy;
    }
}