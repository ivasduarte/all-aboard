package controller;

import domain.Industry;
import domain.Map;
import domain.Scenario;
import repositories.IndustryRepository;
import repositories.Repositories;

import java.util.List;
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

                System.out.println("Starting simulation in " + scenario.getScenarioName() + ", " + map.getMapName());
                System.out.println("The starting date is " + scenario.getRestrictions().getStartDate() + " and the end date is " + scenario.getRestrictions().getEndDate());

                System.out.println("The budget is " + scenario.getBudget().getBudget() + "€");

                List<String> historicalEvents = scenario.getRestrictions().getHistoricalEvents();

                double cargoGeneratorMultiplier = 1;
                double costMultiplier = 1;

                if(historicalEvents.contains("pandemic"))
                {
                    cargoGeneratorMultiplier = 0.25;
                }

                if(historicalEvents.contains("war"))
                {
                    cargoGeneratorMultiplier = 1.5;
                    costMultiplier = 1.75;
                }

                List<Industry> industries = Repositories.getInstance().getIndustryRepository().getIndustryByMap(map);

                int budget = scenario.getBudget().getBudget();
                int year = startYear;

                while (running) {
                    Thread.sleep(5000);

                    if(running)
                    {
                        scenario.setCurrentYear(year);
                        year++;

                        if(year > endYear)
                            running = false;
                    }
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
