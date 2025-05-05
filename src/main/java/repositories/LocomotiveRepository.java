package repositories;

import domain.Locomotive;

import java.util.ArrayList;
import java.util.List;

public class LocomotiveRepository {
    private List<Locomotive> locomotives = new ArrayList<>();

    public void save(Locomotive locomotive) {
        locomotives.add(locomotive);
    }

    public List<Locomotive> getLocomotiveList() {
        return locomotives;
    }

    public void displayLocomotiveList() {
        for (int i = 0; i < locomotives.size(); i++) {
            System.out.println((i+1) + ". " + locomotives.get(i));
        }
    }
}
