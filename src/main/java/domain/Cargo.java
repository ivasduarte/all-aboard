package domain;

import java.io.Serializable;

/**
 * Represents a cargo type in the railway system.
 * Each cargo has a specific type with associated properties like name and price.
 * This class is used to track and manage different types of cargo that can be transported
 * by trains between stations.
 */
public class Cargo implements Serializable {
    private final Type type;

    /**
     * Constructs a new Cargo instance with the specified type.
     *
     * @param type The type of cargo to be created.
     */
    public Cargo(Type type) {
        this.type = type;
    }

    /**
     * Enum representing different types of cargo that can be transported in the railway system.
     * Each type has a name and an associated price per unit.
     */
    public enum Type {
        /** Automobiles, priced at 28 units */
        AUTO("Auto", 28),
        /** Coal, priced at 28 units */
        COAL("Coal", 28),
        /** Iron ore, priced at 28 units */
        IRON("Iron", 28),
        /** Mail, priced at 65 units */
        MAIL("Mail", 65),
        /** Passengers, priced at 56 units */
        PASSENGERS("Passengers", 56),
        /** Steel, priced at 35 units */
        STEEL("Steel", 35),
        /** Manufacturing goods, priced at 42 units */
        MFG_GOODS("Manufacturing goods", 42);

        private final String name;
        private final int price;

        /**
         * Constructs a new cargo type with the specified name and price.
         *
         * @param name The display name of the cargo type.
         * @param price The price per unit of this cargo type.
         */
        Type(String name, int price) {
            this.name = name;
            this.price = price;
        }
    }

    /**
     * Retrieves the type of this cargo.
     *
     * @return The Type enum value representing this cargo's type.
     */
    public Type getType() {
        return type;
    }
}