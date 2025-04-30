package domain;

public class Industry {
    private int mapID;
    private int scenarioID;
    private int positionX;
    private int positionY;
    private String industryType;

    public Industry(int mapID, int scenarioID, int positionX, int positionY, String industryType) {
        this.mapID = mapID;
        this.scenarioID = scenarioID;
        this.positionX = positionX;
        this.positionY = positionY;
        this.industryType = industryType;
    }

    // getters
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
    public String getIndustryType() {
        return industryType;
    }

    // setters
    public void setMapID(int mapID) {
        this.mapID = mapID;
    }
    public void setScenarioID(int scenarioID) {
        this.scenarioID = scenarioID;
    }
    public void setPositionX(int positionX) {
        this.positionX = positionX;
    }
    public void setPositionY(int positionY) {
        this.positionY = positionY;
    }
    public void setIndustryType(String industryType) {
        this.industryType = industryType;
    }

    // validation
    public boolean validatePosition() {

    }
}
