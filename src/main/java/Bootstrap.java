import domain.Locomotive;
import repositories.AuthenticationRepository;
import repositories.LocomotiveRepository;
import repositories.TasksRepository;
import repositories.Repositories;
import domain.TaskCategory;
import ui.authorization.AuthenticationController;

import static domain.Locomotive.LocomotiveType.*;

public class Bootstrap implements Runnable {

    public void run() {
        addCategories();
        addUsers();
    }

    public void addCategories(){
        TasksRepository tasksRepository = Repositories.getInstance().getTasksRepository();

        tasksRepository.add(new TaskCategory("Analysis").clone());
        tasksRepository.add(new TaskCategory("Design").clone());
        tasksRepository.add(new TaskCategory("Implementation").clone());
        tasksRepository.add(new TaskCategory("Development").clone());
        tasksRepository.add(new TaskCategory("Testing").clone());
        tasksRepository.add(new TaskCategory("Deployment").clone());
        tasksRepository.add(new TaskCategory("Maintenance").clone());
    }

    public void addUsers(){
        AuthenticationRepository authenticationRepository = Repositories.getInstance().getAuthenticationRepository();

        authenticationRepository.addUserRole(AuthenticationController.ROLE_EDITOR, AuthenticationController.ROLE_EDITOR);
        authenticationRepository.addUserRole(AuthenticationController.ROLE_PLAYER, AuthenticationController.ROLE_PLAYER);

        authenticationRepository.addUserWithRole("editor", "editor@isep.ipp.pt", "editor", AuthenticationController.ROLE_EDITOR);
        authenticationRepository.addUserWithRole("player", "player@isep.ipp.pt", "player", AuthenticationController.ROLE_PLAYER);
    }

    public void addLocomotives() {
        LocomotiveRepository lr = Repositories.getInstance().getLocomotiveRepository();

        lr.saveLocomotive(new Locomotive(1, Steam, 125, 1950, 25000.00, 70000.00, 250000.00));
        lr.saveLocomotive(new Locomotive(2, Diesel, 170, 1980, 50000.00, 250000.00, 1000000.00));
        lr.saveLocomotive(new Locomotive(3, Electricity, 200, 2006, 75000.00, 100000.00, 2500000.00));
    }
}
