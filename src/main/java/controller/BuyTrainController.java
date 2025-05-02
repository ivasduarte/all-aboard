package controller;

import domain.ApplicationSession;
import domain.Player;
import domain.Train;
import repositories.Repositories;
import repositories.TrainRepository;

public class BuyTrainController {
    public String addTrain(String name, String id) {   // add dateOfPurchase
        TrainRepository trainRepository = Repositories.getInstance().getTrainRepository();
        Player player = ApplicationSession.getInstance().getCurrentSession().getCurrentPlayer();

        Train train = new Train(name, id);   // add dateOfPurchase

        // validations (ERROR messages)

        trainRepository.save(train);
        return "Train " + name + " saved successfully.";
    }
}
