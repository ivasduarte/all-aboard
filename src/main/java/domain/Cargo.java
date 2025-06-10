package domain;

import java.io.Serializable;

public class Cargo implements Serializable {
    private final Type type;

    public Cargo(Type type) {
        this.type = type;
    }

    public enum Type {
        AUTO("Auto", 28),
        COAL("Coal", 28),
        IRON("Iron", 28),
        MAIL("Mail", 65),
        PASSENGERS("Passengers", 56),
        STEEL("Steel", 35),
        MFG_GOODS("Manufacturing goods", 42);

        private final String name;
        private final int price;

        Type(String name, int price) {
            this.name = name;
            this.price = price;
        }
    }

    public Type getType() {
        return type;
    }
}