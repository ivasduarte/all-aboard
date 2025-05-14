package controller;

import domain.*;
import repositories.LocomotiveRepository;
import repositories.Repositories;


public class BuyLocomotiveController {
    private LocomotiveRepository locomotiveRepository;

    /**
     * This method will be responsible to add the bought locomotive to the repository.
     *
     * @param locomotiveModel locomotive model
     *
     * @return Will show a success message if it is possible to buy the locomotive. Otherwise, it will show a message related to the error that occurred.
     */
    public String buyLocomotive(String locomotiveModel) {
        LocomotiveRepository locomotiveRepository = Repositories.getInstance().getLocomotiveRepository();
        User user = UserSession.getInstance().getUser();

        Locomotive locomotive = locomotiveRepository.getLocomotiveByModel(locomotiveModel);

        if (user.getBudget1() >= locomotive.getPrice()){
            user.subtractBudget(locomotive.getPrice());
            locomotiveRepository.save(locomotive);
        } else {
            return "ERROR: Player doesn't have enough budget";
        }

        return "Locomotive " + locomotiveModel + " bought successfully.";
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
