package domain;

public class Map {
    private String name;
    private int width;
    private int height;
    private Editor createdBy;

    public Map(String name, int width, int height) {
        this.name = name;
        this.width = width;
        this.height = height;
    }

    //getters
    public String getMapName() {
        return name;
    }

    //validation
    public boolean validateName() {
        if (name == null || name.isEmpty()) {
            return false;
        } else if (name.length() > 15) {
            return false;
        } else if (name.startsWith(" ") || name.startsWith("-") || name.startsWith("_")){
            return false;
        } else if (name.endsWith(" ") || name.endsWith("-") || name.endsWith("_")){
            return false;
        } else if (!name.matches("[a-zA-Z0-9]+")) {
            return false;
        }
        return (!name.chars().anyMatch(Character::isUpperCase));
    }


    public boolean validateSize() {
        return (width > 0 && width <= 256 && height > 0 && height <= 256);
    }

}
