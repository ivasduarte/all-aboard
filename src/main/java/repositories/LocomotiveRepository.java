package repositories;

import domain.Locomotive;

import java.util.ArrayList;
import java.util.List;

public class LocomotiveRepository {
    private List<Locomotive> locomotives = new ArrayList<>();
    private List<Locomotive> playerLocomotives = new ArrayList<>();

    public void save(Locomotive locomotive) {
        playerLocomotives.add(locomotive);
    }

    // method to obtain available locomotives based on scenario restrictions and current date

    public Locomotive getLocomotiveBySerialNumber(int serialNumber) {
        return playerLocomotives.get(serialNumber);
    }
}
