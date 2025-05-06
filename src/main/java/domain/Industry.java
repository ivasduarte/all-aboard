package domain;

import repositories.MapRepository;

public class Industry {
    /**
     * The map ID.
     */
    private int mapID;

    /**
     * The scenario ID.
     */
    private int scenarioID;

    /**
     * The position X that the industry will take on the map.
     */
    private int positionX;

    /**
     * The position Y that the industry will take on the map.
     */
    private int positionY;

    /**
     * The type of industry.
     */
    private String industryType;

    /**
     * This method is the constructor of the industry.
     *
     * @param mapID The ID of a map.
     * @param scenarioID The ID of a scenario.
     * @param positionX The position X of the industry.
     * @param positionY The position Y of the industry.
     * @param industryType The type of the industry.
     */
    public Industry(int mapID, int scenarioID, int positionX, int positionY, String industryType) {
        this.mapID = mapID;
        this.scenarioID = scenarioID;
        this.positionX = positionX;
        this.positionY = positionY;
        this.industryType = industryType;
    }

    /**
     * This method returns the ID of the used map.
     *
     * @return The map ID.
     */
    public int getMapID() {
        return mapID;
    }

    /**
     * This method returns the ID of the used scenario.
     *
     * @return The scenario ID.
     */
    public int getScenarioID() {
        return scenarioID;
    }

    /**
     * This method returns the position X that the industry will occupy.
     *
     * @return The position X of the industry
     */
    public int getPositionX() {
        return positionX;
    }

    /**
     * This method returns the position Y that the industry will occupy.
     *
     * @return The position Y of the industry
     */
    public int getPositionY() {
        return positionY;
    }

    /**
     * This method returns the type of the industry added.
     *
     * @return The industry type.
     */
    public String getIndustryType() {
        return industryType;
    }

    MapRepository mr = new MapRepository();
    Map map = mr.getMapList().get(mapID);

    /**
     * This method verifies if the selected position exists on the map.
     *
     * @return True if the position exists.
     */
    public boolean validatePosition() {
        if (positionX < 0 || positionX > map.getWidth()) {
            return false;
        }
        if (positionY < 0 || positionY > map.getHeight()) {
            return false;
        }
        return true;
    }
}
