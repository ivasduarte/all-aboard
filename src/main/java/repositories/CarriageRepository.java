package repositories;

import domain.Carriage;

import java.util.ArrayList;
import java.util.List;

public class CarriageRepository {
    private List<Carriage> carriages = new ArrayList<>();

    public void save(Carriage carriage) {
        carriages.add(carriage);
    }

    public List<Carriage> getCarriageList() {
        return carriages;
    }

    public void displayCarriageList() {
        for (int i = 0; i < carriages.size(); i++) {
            System.out.println((i+1) + ". " + carriages.get(i));
       }
    }
}
