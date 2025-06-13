package repositories;

import domain.Locomotive;
import domain.Train;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

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
    public List<Train> getAllTrains() {
        return Collections.unmodifiableList(trains);
    }

    /**
     * This method is responsible to get a specific train by its locomotive.
     *
     * @param locomotive the locomotive
     *
     * @return corresponding train
     */
    public Train getTrainByLocomotive(Locomotive locomotive) {
        for (Train train : trains) {
            if (train.getLocomotive().equals(locomotive)) {
                return train;
            }
        }

        return null;
    }

    /**
     * This method is responsible to get a specific train by its name.
     *
     * @param name the name of the train
     * @return Optional containing the train if found, empty otherwise
     * @throws IllegalArgumentException if name is null or empty
     */
    public Optional<Train> getTrainByName(String name) {
        if (name == null) {
            throw new IllegalArgumentException("Name cannot be null");
        }
        if (name.trim().isEmpty()) {
            throw new IllegalArgumentException("Name cannot be empty");
        }
        return trains.stream()
                .filter(train -> train.getLocomotive().getLocomotiveModel().equals(name))
                .findFirst();
    }
}
