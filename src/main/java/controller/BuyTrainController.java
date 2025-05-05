package controller;

import domain.*;
import repositories.CarriageRepository;
import repositories.LocomotiveRepository;
import repositories.Repositories;
import repositories.TrainRepository;

import java.util.ArrayList;
import java.util.List;

public class BuyTrainController {
    public String addTrain(String name, int id, Date dateOfPurchase, List<Locomotive> selectedLocomotives, List<Carriage> selectedCarriages) {
        TrainRepository trainRepository = Repositories.getInstance().getTrainRepository();
        Player player = ApplicationSession.getInstance().getCurrentSession().getCurrentPlayer();

        Train train = new Train(name, id, dateOfPurchase, selectedLocomotives, selectedCarriages);

        // validations (ERROR messages)

        trainRepository.save(train);
        return "Train " + name + " saved successfully.";
    }

    public Locomotive selectLocomotive(int selection) {
        Locomotive selectedLocomotive;

        LocomotiveRepository lr = new LocomotiveRepository();
        List<Locomotive> locomotives = lr.getLocomotiveList();

        selectedLocomotive = locomotives.get(selection-1);

        return selectedLocomotive;
    }

    public Carriage selectCarriage(int selection) {
        Carriage selectedCarriage;

        CarriageRepository cr = new CarriageRepository();
        List<Carriage> carriages = cr.getCarriageList();

        selectedCarriage = carriages.get(selection-1);

        return selectedCarriage;
    }
}
