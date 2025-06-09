package domain;

import java.io.Serializable;

/**
 * Represents a carriage capable of carrying cargo.
 * A carriage has a specific cargo type, a maximum allowed cargo capacity,
 * and tracks the current cargo amount it holds.
 */
public class Carriage implements Serializable {
    public Cargo cargo;
    public int maxCargo;
    public int currentCargo;

    /**
     * Creates a new Carriage for transporting cargo.
     * A carriage has a defined type of cargo, a maximum cargo capacity,
     * and a specified amount of current cargo it is holding.
     *
     * @param cargo       the type of cargo that the carriage is designated to hold
     * @param maxCargo    the maximum capacity of cargo the carriage can hold
     * @param currentCargo the initial amount of cargo stored in the carriage
     */
    public Carriage(Cargo cargo, int maxCargo, int currentCargo) {
        this.cargo = cargo;
        this.maxCargo = maxCargo;
        this.currentCargo = currentCargo;
    }

    /**
     * Retrieves the cargo currently assigned to the carriage.
     *
     * @return the current cargo carried by the carriage
     */
    public Cargo getCargo() {
        return cargo;
    }

    /**
     * Retrieves the type of the cargo currently assigned to the carriage.
     *
     * @return the type of cargo carried by the carriage
     */
    public Cargo.Type getCargoType() {
        return cargo.getType();
    }

    /**
     * Retrieves the maximum cargo capacity of the carriage.
     *
     * @return the maximum amount of cargo the carriage can hold
     */
    public int getMaxCargo() {
        return maxCargo;
    }

    /**
     * Retrieves the current amount of cargo in the carriage.
     *
     * @return the current cargo level in the carriage
     */
    public int getCurrentCargo() {
        return currentCargo;
    }

    /**
     * Sets the cargo for the carriage if the current cargo load is zero.
     * This method assigns the provided cargo to the carriage only if it is currently empty.
     *
     * @param newCargo the new cargo to be assigned to the carriage
     * @return true if the cargo was successfully set, false if the current cargo load is not zero
     */
    public boolean setCargo(Cargo newCargo) {
        if (currentCargo == 0)
            cargo = newCargo;

        return currentCargo == 0;
    }

    /**
     * Adds the specified amount of cargo to the carriage. The final cargo will
     * not exceed the maximum cargo capacity of the carriage.
     *
     * @param amount the amount of cargo to add to the carriage
     */
    public void addCargo(int amount) {
        currentCargo = Math.min(currentCargo + amount, maxCargo);
    }

    /**
     * Removes the specified amount of cargo from the carriage.
     * The resulting cargo level will not drop below zero.
     *
     * @param amount the amount of cargo to remove from the carriage
     */
    public void removeCargo(int amount) {
        currentCargo = Math.max(currentCargo - amount, 0);
    }

    /**
     * Fills the carriage to its maximum cargo capacity.
     * This method utilizes the {@code addCargo} method to add cargo
     * until the current cargo level reaches the maximum allowable capacity.
     */
    public void fillCarriage() {
        addCargo(maxCargo);
    }

    /**
     * Removes all current cargo from the carriage.
     * This method sets the current cargo level to zero
     * by removing an amount equal to the current cargo.
     */
    public void emptyCarriage() {
        removeCargo(currentCargo);
    }
}
