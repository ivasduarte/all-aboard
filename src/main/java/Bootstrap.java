import controller.AddIndustryController;
import controller.BuyTrainController;
import controller.CreateMapController;
import domain.ApplicationSession;
import domain.Editor;
import domain.Industry;
import domain.Player;
import domain.Train;
import repositories.IndustryRepository;
import repositories.Repositories;
import repositories.TrainRepository;
import presentation.AddIndustryUI;
import presentation.BuyTrainUI;
import presentation.CreateMapUI;

public class Bootstrap implements Runnable {

    public void run() {
        addEditor();
    }


    public void addEditor() {

    }
}
