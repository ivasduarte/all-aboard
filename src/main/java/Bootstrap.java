import repositories.AuthenticationRepository;
import repositories.TasksRepository;
import repositories.Repositories;
import domain.TaskCategory;
import ui.authorization.AuthenticationController;

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
}
