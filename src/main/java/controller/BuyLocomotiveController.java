package controller;

import domain.ApplicationSession;
import domain.Date;
import domain.Locomotive;
import domain.Player;
import repositories.LocomotiveRepository;
import repositories.Repositories;

import java.util.List;

public class BuyLocomotiveController {
    private LocomotiveRepository locomotiveRepository;

    public String buyLocomotive(int serialNumber) {
        LocomotiveRepository locomotiveRepository = Repositories.getInstance().getLocomotiveRepository();
        Player player = ApplicationSession.getInstance().getCurrentSession().getCurrentPlayer();

        // validations (ERROR messages)

        Locomotive locomotive = locomotiveRepository.getLocomotiveBySerialNumber(serialNumber);
        locomotiveRepository.save(locomotive);

        return "Locomotive " + serialNumber + " bought successfully.";
    }

    public LocomotiveRepository getLocomotiveRepository() {
        if (locomotiveRepository == null) {
            Repositories repositories = Repositories.getInstance();
            locomotiveRepository = repositories.getLocomotiveRepository();
        }
        return locomotiveRepository;
    }
}
