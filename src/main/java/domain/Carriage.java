package domain;

import java.io.Serializable;

public class Carriage implements Serializable {

    private final int MAX_SLOT = 3;

    private Cargo cargo;
    private int currentCargo;

    public Carriage(Cargo cargo, int currentCargo) {
        this.cargo = cargo;
        this.currentCargo = Math.min(currentCargo, MAX_SLOT);
    }

    public Cargo getCargo() {
        return cargo;
    }

    public Cargo.Type getCargoType() {
        return cargo.getType();
    }

    public int getCurrentCargo() {
        return currentCargo;
    }

    public int getMAX_SLOT() {return MAX_SLOT;}

    public boolean setCargo(Cargo newCargo) {
        if (currentCargo == 0)
            cargo = newCargo;

        return currentCargo == 0;
    }

    public void addCargo(int amount) {
        currentCargo = Math.min(currentCargo + amount, MAX_SLOT);
    }

    public void removeCargo(int amount) {
        currentCargo = Math.max(currentCargo - amount, 0);
    }

    public void fillCarriage() {
        addCargo(MAX_SLOT);
    }

    public void emptyCarriage() {
        removeCargo(currentCargo);
    }
}
