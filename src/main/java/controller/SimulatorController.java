package controller;

import java.util.concurrent.TimeUnit;

public class SimulatorController {
    public void PassTime(int startYear, int endYear) throws InterruptedException {
        for(int i = startYear; i <= endYear; i++)
        {
            TimeUnit.SECONDS.sleep(1);
            System.out.println(i);
        }
    }
}
