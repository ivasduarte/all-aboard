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

    /**
     * This method will be responsible to add the bought locomotive to the repository.
     *
     * @param serialNumber serial number of the locomotive
     *
     * @return Will show a success message if it is possible to buy the locomotive. Otherwise, it will show a message related to the error that occurred.
     */
    public String buyLocomotive(int serialNumber) {
        LocomotiveRepository locomotiveRepository = Repositories.getInstance().getLocomotiveRepository();
        Player player = ApplicationSession.getInstance().getCurrentSession().getCurrentPlayer();

        // validate player budget

        Locomotive locomotive = locomotiveRepository.getLocomotiveBySerialNumber(serialNumber);
        locomotiveRepository.save(locomotive);

        return "Locomotive " + serialNumber + " bought successfully.";
    }

    /**
     * This method allows the controller to get the locomotive repository.
     *
     * @return locomotive repository
     */
    public LocomotiveRepository getLocomotiveRepository() {
        if (locomotiveRepository == null) {
            Repositories repositories = Repositories.getInstance();
            locomotiveRepository = repositories.getLocomotiveRepository();
        }
        return locomotiveRepository;
    }
}
