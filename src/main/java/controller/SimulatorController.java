package controller;

import domain.Scenario;

import java.util.Scanner;
import java.util.concurrent.atomic.AtomicBoolean;

public class SimulatorController {

    private static volatile boolean running = true;

    public void PassTime() {

        Thread simulatorThread = new Thread(() -> {
            int value = 0;
            try {
                while (running) {
                    Thread.sleep(2000);
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
}
