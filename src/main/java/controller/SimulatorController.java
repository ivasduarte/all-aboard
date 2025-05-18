package controller;

import domain.Map;
import domain.Scenario;

import java.util.Scanner;
public class SimulatorController implements  Runnable {

    private static boolean running = true;

    private Map map;
    private Scenario scenario;

    public SimulatorController(Map map, Scenario scenario)
    {
        this.map = map;
        this.scenario = scenario;
    }

    public void PassTime() {

        int startYear = scenario.getRestrictions().getStartDate().getYear();
        int endYear = scenario.getRestrictions().getEndDate().getYear();

        Thread simulatorThread = new Thread(() -> {
            try {
                int year = startYear;
                while (running) {
                    System.out.println(year);
                    year++;

                    Thread.sleep(2000);

                    if(year > endYear)
                        running = false;
                }
            } catch (InterruptedException e) {
                System.out.println("Simulator interrupted.");
            }
            System.out.println("Stopped simulator.");
        });

        simulatorThread.start();

        Scanner scanner = new Scanner(System.in);
        System.out.println("Press ENTER to stop the simulator");
        scanner.nextLine();

        running = false;

        try {
            simulatorThread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run()
    {
        PassTime();
    }
}
