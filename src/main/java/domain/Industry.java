package domain;

import java.io.Serializable;

/**
 * Represents an industry in the railway system.
 * Each industry has a specific type, position on the map, cargo generation amount,
 * and storage capacity for different types of resources.
 * This class is used to manage industries that can generate and consume different
 * types of cargo in the system.
 */
public class Industry implements Serializable {

    private int mapID;
    private int positionX;
    private int positionY;
    private IndustryInfo industryType;
    private int cargoAmount;

    private int storedIron = 0;
    private int storedCoal = 0;
    private final int area;

    /**
     * Constructs a new Industry instance with the specified parameters.
     *
     * @param mapID ID of the map where the industry will be located
     * @param positionX X position of the industry on the map
     * @param positionY Y position of the industry on the map
     * @param industryType Type of the industry
     * @param cargoAmount Amount of cargo generated per year
     */
    public Industry(int mapID, int positionX, int positionY, IndustryInfo industryType, int cargoAmount) {
        this.mapID = mapID;
        this.positionX = positionX;
        this.positionY = positionY;
        this.industryType = industryType;
        this.cargoAmount = cargoAmount;
        this.area = 15;
    }

    /**
     * Enum that defines the different types of industries available in the system.
     * Each type has a name, types of cargo it can supply and demand,
     * and an industry type (PRIMARY, TRANSFORMING, MIXED).
     */
    public enum IndustryInfo {
        /** Coal Mine - supplies coal */
        COAL_MINE("Coal Mine", new Cargo.Type[]{Cargo.Type.COAL}, null, IndustryType.PRIMARY),
        /** Housing - supplies passengers and mail */
        HOUSING("Housing", new Cargo.Type[]{Cargo.Type.PASSENGERS, Cargo.Type.MAIL}, null, IndustryType.PRIMARY),
        /** Iron Mine - supplies iron ore */
        IRON_MINE("Iron Mine", new Cargo.Type[]{Cargo.Type.IRON}, null, IndustryType.PRIMARY),
        /** Steel Mill - supplies steel, demands coal and iron ore */
        STEEL_MILL("Steel Mill", new Cargo.Type[]{Cargo.Type.STEEL}, new Cargo.Type[]{Cargo.Type.COAL, Cargo.Type.IRON}, IndustryType.TRANSFORMING),
        /** Tool and Die Factory - supplies manufactured goods, demands iron and steel */
        TOOL_AND_DIE_FACTORY("Tool and Die Factory", new Cargo.Type[]{Cargo.Type.MFG_GOODS}, new Cargo.Type[]{Cargo.Type.IRON, Cargo.Type.STEEL}, IndustryType.MIXED),
        /** Auto Plant - supplies automobiles, demands steel */
        AUTO_PLANT("Auto Plant", new Cargo.Type[]{Cargo.Type.AUTO}, new Cargo.Type[]{Cargo.Type.STEEL}, IndustryType.TRANSFORMING);

        private final String name;
        private final Cargo.Type[] supply;
        private final Cargo.Type[] demand;
        private final IndustryType type;

        /**
         * Constructs a new industry type with the specified parameters.
         *
         * @param name Name of the industry
         * @param supply Types of cargo the industry can supply
         * @param demand Types of cargo the industry demands
         * @param type Type of industry (PRIMARY, TRANSFORMING, MIXED)
         */
        IndustryInfo(String name, Cargo.Type[] supply, Cargo.Type[] demand, IndustryType type) {
            this.name = name;
            this.supply = supply;
            this.demand = demand;
            this.type = type;
        }

        /**
         * Enum that defines industry types based on their sector.
         */
        public enum IndustryType {
            /** Primary sector - resource extraction */
            PRIMARY("Primary sector"),
            /** Transforming sector - transforms resources into products */
            TRANSFORMING("Transforming"),
            /** Mixed sector - combines characteristics of primary and transforming sectors */
            MIXED("Mixed");

            private final String name;

            /**
             * Constructs a new industry type with the specified name.
             *
             * @param name Name of the industry type
             */
            IndustryType(String name) {
                this.name = name;
            }

            /**
             * Returns the name of the industry type.
             *
             * @return Name of the industry type
             */
            public String getName() {
                return name;
            }
        }

        /**
         * Returns the name of the industry.
         *
         * @return Name of the industry
         */
        public String getName() {
            return name;
        }

        /**
         * Returns the types of cargo the industry can supply.
         *
         * @return Array of cargo types supplied
         */
        public Cargo.Type[] getSupply() {
            return supply;
        }

        /**
         * Returns the types of cargo the industry demands.
         *
         * @return Array of cargo types demanded
         */
        public Cargo.Type[] getDemand() {
            return demand;
        }

        /**
         * Returns the type of the industry.
         *
         * @return Type of industry (PRIMARY, TRANSFORMING, MIXED)
         */
        public IndustryType getType() {
            return type;
        }

        @Override
        public String toString() {
            return name;
        }
    }

    /**
     * Returns the ID of the map where the industry is located.
     *
     * @return Map ID
     */
    public int getMapID() {
        return mapID;
    }

    /**
     * Returns the X position of the industry on the map.
     *
     * @return X position
     */
    public int getPositionX() {
        return positionX;
    }

    /**
     * Returns the Y position of the industry on the map.
     *
     * @return Y position
     */
    public int getPositionY() {
        return positionY;
    }

    /**
     * Returns the type of the industry.
     *
     * @return Industry type
     */
    public IndustryInfo getIndustryType() {
        return industryType;
    }

    /**
     * Returns the amount of cargo generated per year.
     *
     * @return Amount of cargo generated per year
     */
    public int getCargoAmount() {
        return cargoAmount;
    }

    /**
     * Returns the area occupied by the industry.
     *
     * @return Area of the industry
     */
    public int getArea() {
        return area;
    }

    /**
     * Consumes a specified amount of stored iron.
     * Only consumes if there is sufficient amount stored.
     *
     * @param amount Amount of iron to be consumed
     */
    public void consumeIron(int amount) {
        if (amount > 0 && this.storedIron >= amount) {
            this.storedIron -= amount;
        }
    }

    /**
     * Returns the amount of stored iron.
     *
     * @return Amount of stored iron
     */
    public int getStoredIron() {
        return storedIron;
    }

    /**
     * Adds a specified amount of iron to storage.
     * Only adds if the amount is positive.
     *
     * @param amount Amount of iron to be added
     */
    public void addStoredIron(int amount) {
        if (amount > 0) {
            this.storedIron += amount;
        }
    }

    /**
     * Consumes a specified amount of stored coal.
     * Only consumes if there is sufficient amount stored.
     *
     * @param amount Amount of coal to be consumed
     */
    public void consumeCoal(int amount) {
        if (amount > 0 && this.storedCoal >= amount) {
            this.storedCoal -= amount;
        }
    }

    /**
     * Returns the amount of stored coal.
     *
     * @return Amount of stored coal
     */
    public int getStoredCoal() {
        return storedCoal;
    }

    /**
     * Adds a specified amount of coal to storage.
     * Only adds if the amount is positive.
     *
     * @param amount Amount of coal to be added
     */
    public void addStoredCoal(int amount) {
        if (amount > 0) {
            this.storedCoal += amount;
        }
    }

    public int getGeneratedIron() {
        if (this.getIndustryType().toString().equals("IRON_MINE")) {
            return 10;
        }
        return 0;
    }

    public int getGeneratedCoal() {
        // This value could be based on industry efficiency, size, etc.
        if (this.getIndustryType().toString().equals("COAL_MINE")) {
            return 8;
        }
        return 0;
    }
}
