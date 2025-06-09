package domain;

import java.io.Serializable;

public class Cargo implements Serializable {
    private final Type type;

    public Cargo(Type type) {
        this.type = type;
    }

    public enum Type {
        AUTO("Auto", 28000),
        COAL("Coal", 28000),
        IRON("Iron", 28000),
        MAIL("Mail", 65000),
        PASSENGERS("Passengers", 56000),
        STEEL("Steel", 35000),
        MFG_GOODS("Manufacturing goods", 42000),
        NONE("None", 0);

        private final String name;
        private final int price;

        Type(String name, int price) {
            this.name = name;
            this.price = price;
        }

        public String getName() {
            return name;
        }
        public int getPrice() {return price;}
    }

    public Type getType() {
        return type;
    }

    public String getName() {
        return type.name;
    }

    public int getPrice() {
        return type.price;
    }
}
