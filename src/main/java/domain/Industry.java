package domain;

import java.io.Serializable;

public class Industry implements Serializable {
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
    private IndustryInfo industryType;

    /**
     * The amount of cargo generated, per year.
     */
    private int cargoAmount;

    /**
     * This method is the constructor of the industry.
     *
     * @param mapID        The ID of a map.
     * @param positionX    The position X of the industry.
     * @param positionY    The position Y of the industry.
     * @param industryType The type of the industry.
     * @param cargoAmount  The amount of cargo generated, per year.
     */
    public Industry(int mapID, int positionX, int positionY, IndustryInfo industryType, int cargoAmount) {
        this.mapID = mapID;
        this.positionX = positionX;
        this.positionY = positionY;
        this.industryType = industryType;
        this.cargoAmount = cargoAmount;
    }

    /**
     * This is used to enumerate all the possible types of industries that exist in the simulator.
     */
    public enum IndustryInfo {
        COAL_MINE("Coal Mine", new Cargo.Type[]{Cargo.Type.COAL}, null, IndustryType.PRIMARY),
        HOUSING("Housing", new Cargo.Type[]{Cargo.Type.PASSENGERS, Cargo.Type.MAIL}, null, IndustryType.PRIMARY),
        IRON_MINE("Iron Mine", new Cargo.Type[]{Cargo.Type.IRON}, null, IndustryType.PRIMARY),
        STEEL_MILL("Steel Mill", new Cargo.Type[]{Cargo.Type.STEEL}, new Cargo.Type[]{Cargo.Type.COAL, Cargo.Type.IRON}, IndustryType.TRANSFORMING),
        TOOL_AND_DIE_FACTORY("Tool and Die Factory", new Cargo.Type[]{Cargo.Type.MFG_GOODS}, new Cargo.Type[]{Cargo.Type.IRON, Cargo.Type.STEEL}, IndustryType.MIXED),
        AUTO_PLANT("Auto Plant", new Cargo.Type[]{Cargo.Type.AUTO}, new Cargo.Type[]{Cargo.Type.STEEL}, IndustryType.TRANSFORMING);

        IndustryInfo(String name, Cargo.Type[] supply, Cargo.Type[] demand, IndustryType type) {
            this.name = name;
            this.supply = supply;
            this.demand = demand;
            this.type = type;
        }

        private final String name;
        private final Cargo.Type[] supply;
        private final Cargo.Type[] demand;
        private final IndustryType type;

        public enum IndustryType {
            PRIMARY("Primary sector"), TRANSFORMING("Transforming"), MIXED("Mixed");

            IndustryType(String name) {
                this.name = name;
            }

            private final String name;

            public String getName() {
                return name;
            }
        }

        public String getName() {
            return name;
        }

        public Cargo.Type[] getSupply() {
            return supply;
        }

        public Cargo.Type[] getDemand() {
            return demand;
        }

        public IndustryType getType() {
            return type;
        }

        @Override
        public String toString() {
            return name;
        }
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
    public IndustryInfo getIndustryType() {
        return industryType;
    }

    /**
     * This method returns the amount of cargo generated, per year.
     *
     * @return The amount of cargo generated, per year.
     */
    public int getCargoAmount() {
        return cargoAmount;
    }
}
