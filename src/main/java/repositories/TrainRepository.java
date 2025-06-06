package repositories;

import domain.Locomotive;
import domain.Train;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class TrainRepository implements Serializable {
    private List<Train> trains = new ArrayList<>();

    /**
     * This method is responsible to add trains to the list of all trains available in the simulator.
     *
     * @param train predefined trains
     */
    public void saveTrain(Train train) {
        trains.add(train);
    }

    /**
     * This method is responsible to give the list of all trains in the game.
     *
     * @return List of all trains
     */
    public List<Train> getAlltrains() {
        return trains;
    }

    /**
     * This method is responsible to get a specific train by its locomotive.
     *
     * @param locomotive the locomotive
     *
     * @return corresponding train
     */
    public Train gettrainByLocomotive(Locomotive locomotive) {
        for (Train train : trains) {
            if (train.getLocomotive().equals(locomotive)) {
                return train;
            }
        }

        return null;
    }
}
