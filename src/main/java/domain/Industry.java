package domain;

import controller.DisplayMapController;
import repositories.CityRepository;
import repositories.IndustryRepository;

import java.io.Serializable;
import java.util.List;

public class Industry implements Serializable {
    private DisplayMapController mapController = new DisplayMapController();
    private IndustryRepository indRepo = new IndustryRepository();
    private CityRepository cityRepo = new CityRepository();

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
     * The amount of cargo generated, per year.
     */
    private int cargoAmount;

    /**
     * The amount of money the cargo gives to the player.
     */
    private int cargoPrice;

    /**
     * This method is the constructor of the industry.
     *
     * @param mapID The ID of a map.
     * @param scenarioID The ID of a scenario.
     * @param positionX The position X of the industry.
     * @param positionY The position Y of the industry.
     * @param industryType The type of the industry.
     * @param cargoAmount The amount of cargo generated, per year.
     */
    public Industry(int mapID, int scenarioID, int positionX, int positionY, String industryType, int cargoAmount, int cargoPrice) {
        this.mapID = mapID;
        this.scenarioID = scenarioID;
        this.positionX = positionX;
        this.positionY = positionY;
        this.industryType = industryType;
        this.cargoAmount = cargoAmount;
        this.cargoPrice = cargoPrice;
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

    /**
     * This method returns the amount of cargo generated, per year.
     *
     * @return The amount of carge generated, per year.
     */
    public int getCargoAmount() { return cargoAmount; }

    /**
     * This method returns the amount of money the cargo gives to the player.
     *
     * @return The amount of money the cargo gives to the player.
     */
    public int getCargoPrice() { return cargoPrice; }

    /**
     * This method verifies if the selected position exists on the map.
     *
     * @return True if the position exists.
     */
    public boolean validatePosition() {
        List<Map> mapList = mapController.getMapList();
        Map map = mapList.get(mapID);

        if (map == null) {
            return false;
        }

        if (positionX < 0 || positionX > map.getWidth()) {
            return false;
        }
        if (positionY < 0 || positionY > map.getHeight()) {
            return false;
        }
        return true;
    }

    /**
     * This method verifies if the position where the editor is trying to add the industry is already occupied by any type of building.
     *
     * @return True if the position is empty.
     */
    public boolean checkOverbuilding() {
        List<Map> mapList = mapController.getMapList();
        Map map = mapList.get(mapID);

        List<Industry> industries = indRepo.getIndustryList();
        List<City> cities = cityRepo.getCities();

        if (map == null) {
            return false;
        }

        for (int i = 0; i < industries.size(); i++) {
            if (industries.get(i).getPositionX() == positionX && industries.get(i).getPositionY() == positionY) {
                return false;
            }
        }

        for (int i = 0; i < cities.size(); i++) {
            for (int j = 0; j < cities.get(i).getBlocks().size(); j++) {
                List<HouseBlock> hb = cities.get(i).getBlocks();

                if (hb.get(j).getX() == positionX && hb.get(j).getY() == positionY) {
                    return false;
                }
            }
        }
        return true;
    }
}
