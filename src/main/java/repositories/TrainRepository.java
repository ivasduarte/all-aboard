package repositories;

import domain.Train;

import java.util.ArrayList;
import java.util.List;

public class TrainRepository {
    private List<Train> trains = new ArrayList<>();

    public void save(Train train) {
        trains.add(train);
    }

    public List<Train> getTrainList() {
        return trains;
    }

    public void displayTrainIndustry() {
        for (int i = 0; i < trains.size(); i++) {
            System.out.println((i+1) + ". " + trains.get(i));
        }
    }

    private boolean validateTrain(Train train) {
        boolean isValid = !trains.contains(train);
        return isValid;
    }
}
