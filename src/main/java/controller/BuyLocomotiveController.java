package controller;

import domain.ApplicationSession;
import domain.Date;
import domain.Locomotive;
import domain.Player;
import repositories.LocomotiveRepository;
import repositories.Repositories;

import java.util.List;

public class BuyLocomotiveController {
    public String buyLocomotive(Locomotive.LocomotiveType locomotiveType, Date purchaseDate, double price) {
        LocomotiveRepository locomotiveRepository = Repositories.getInstance().getLocomotiveRepository();
        Player player = ApplicationSession.getInstance().getCurrentSession().getCurrentPlayer();

        Locomotive locomotive = new Locomotive(locomotiveType, purchaseDate, price);

        // validations (ERROR messages)

        locomotiveRepository.save(locomotive);
        return "Locomotive bought successfully.";
    }

    public int getLocomotiveId(Locomotive locomotive) {
        LocomotiveRepository lr = new LocomotiveRepository();
        List<Locomotive> locomotives = lr.getLocomotiveList();

        return locomotives.indexOf(locomotive);
    }
}
