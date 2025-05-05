package presentation;

import controller.BuyTrainController;
import domain.Carriage;
import domain.Locomotive;
import repositories.CarriageRepository;
import repositories.LocomotiveRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class BuyTrainUI {
    private BuyTrainController ctrl = new BuyTrainController();

    public void getInput() {
        Scanner sc = new Scanner(System.in);

        System.out.println("Select a locomotive from the following list: ");
        LocomotiveRepository lr = new LocomotiveRepository();
        lr.displayLocomotiveList();
        int locomotive = sc.nextInt();
        List<Locomotive> selectedLocomotives = new ArrayList<>();
        selectedLocomotives.add(ctrl.selectLocomotive(locomotive-1));

        System.out.println("Select a carriage from the following list: ");
        CarriageRepository cr = new CarriageRepository();
        cr.displayCarriageList();
        int carriage = sc.nextInt();
        List<Carriage> selectedCarriages = new ArrayList<>();
        selectedCarriages.add(ctrl.selectCarriage(carriage-1));

        // loop to add more locomotives and carriages

        // add methods to show success and fail
    }
}
