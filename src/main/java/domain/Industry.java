package domain;

import controller.DisplayMapController;
import repositories.CityRepository;
import repositories.IndustryRepository;

import java.io.Serializable;

public class Industry implements Serializable {
    private DisplayMapController mapController = new DisplayMapController();
    private IndustryRepository indRepo = new IndustryRepository();
    private CityRepository cityRepo = new CityRepository();

    /**
     * The map ID.
     */
    private int mapID;

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
     * @param mapID        The ID of a map.
     * @param positionX    The position X of the industry.
     * @param positionY    The position Y of the industry.
     * @param industryType The type of the industry.
     * @param cargoAmount  The amount of cargo generated, per year.
     */
    public Industry(int mapID, int positionX, int positionY, String industryType, int cargoAmount, int cargoPrice) {
        this.mapID = mapID;
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
    public int getCargoAmount() {
        return cargoAmount;
    }

    /**
     * This method returns the amount of money the cargo gives to the player.
     *
     * @return The amount of money the cargo gives to the player.
     */
    public int getCargoPrice() {
        return cargoPrice;
    }
}
