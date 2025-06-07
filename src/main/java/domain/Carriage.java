package domain;

import java.io.Serializable;

public class Carriage implements Serializable {

    public Cargo cargo;
    public int maxCargo;
    public int currentCargo;

    public Carriage(Cargo cargo, int maxCargo, int currentCargo)
    {
        this.cargo = cargo;
        this.maxCargo = maxCargo;
        this.currentCargo = currentCargo;
    }

    public Cargo getCargo() {return cargo;}

    public Cargo.Type getCargoType() {return cargo.getType();}

    public int getMaxCargo() {return maxCargo;}

    public int getCurrentCargo() {return currentCargo;}


    public boolean setCargo(Cargo newCargo){
        if(currentCargo == 0)
            cargo = newCargo;

        return currentCargo == 0;
    }

    public void addCargo(int amount)
    {
        currentCargo = Math.min(currentCargo+amount, maxCargo);
    }

    public void removeCargo(int amount)
    {
        currentCargo = Math.max(currentCargo-amount, 0);
    }

    public void fillCarriage()
    {
        addCargo(maxCargo);
    }

    public void emptyCarriage()
    {
        removeCargo(currentCargo);
    }
}
