package controller;

import domain.Industry;
import domain.Locomotive;
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

        running = true;

        int startYear = scenario.getRestrictions().getStartDate().getYear();
        int endYear = scenario.getRestrictions().getEndDate().getYear();

        Thread simulatorThread = new Thread(() -> {
            try {

                System.out.println("Starting simulation in " + scenario.getScenarioName() + ", " + map.getMapName());
                System.out.println("The starting date is " + scenario.getRestrictions().getStartDate() + " and the end date is " + scenario.getRestrictions().getEndDate());

                System.out.println("The budget is " + scenario.getBudget().getBudget() + "€");

                List<String> historicalEvents = scenario.getRestrictions().getHistoricalEvents();

                System.out.println("Historical events: ");
                for(String event : historicalEvents)
                {
                    if(!event.equalsIgnoreCase("none"))
                        System.out.print(event + " ");
                }
                System.out.println();

                double cargoGeneratorMultiplier = 1;
                double costMultiplier = 1;

                if(historicalEvents.contains("pandemic"))
                {
                    cargoGeneratorMultiplier *= 0.25;
                }

                if(historicalEvents.contains("war"))
                {
                    cargoGeneratorMultiplier *= 1.5;
                    costMultiplier *= 1.65;
                }

                List<Industry> industries = Repositories.getInstance().getIndustryRepository().getIndustriesByMap(map);
                List<Locomotive> locomotives = Repositories.getInstance().getLocomotiveRepository().getPlayerLocomotives();

                int year = startYear;

                while (running) {
                    Thread.sleep(5000);

                    if(running)
                    {
                        System.out.println("\n------------Stats of year " + year + "------------");

                        if (industries != null && !industries.isEmpty()) {
                            System.out.println("----Stats for industries----");
                            for(int i = 0; i < industries.size(); i++)
                            {
                                Industry industry = industries.get(i);
                                int cargoGenerated = (int)(industry.getCargoAmount() * cargoGeneratorMultiplier);
                                int budgetEarned = (int)(industry.getCargoPrice() * cargoGenerated * costMultiplier);

                                System.out.println("Industry of type " + industry.getIndustryType() + " (" + industry.getPositionX() + "x" + industry.getPositionY() + ") has generated " + cargoGenerated + " cargo, producing a total of " + budgetEarned + "€");

                                scenario.addBudget(budgetEarned);
                            }
                        }

                        System.out.println("Your budget is now " + scenario.getBudget().getBudget() + "€");

                        if(historicalEvents.contains("pandemic"))
                        {
                            cargoGeneratorMultiplier += 0.1;
                        }

                        year++;
                        scenario.setCurrentYear(year);

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
