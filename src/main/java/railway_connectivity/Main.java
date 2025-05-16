package railway_connectivity;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {
    static Scanner sc = new Scanner(System.in);

    public static void main(String[] args) throws IOException {
        // getting input
        String linesFile = askFileName("Enter the name of the lines file: ");         // lines file name
        String stationsFile = askFileName("Enter the name of the stations file: ");   // stations file name

        File lines = new File(linesFile);
        File stations = new File(stationsFile);
    }

    // utilities methods
    public static String askFileName(String prompt) {   // introduzir verificacao se ficheiro existe com boolean "found"
        String name = "";
        boolean isCSV = false;

        while (!isCSV) {
            System.out.println(prompt);
            name = sc.nextLine().trim();

            if (name.endsWith(".csv")) {
                isCSV = true;
            }

            if (!isCSV) {
                System.out.println("This file is not CSV.");
            }
        }
        return name;
    }

    private static List<String[]> getScenarioLines(File linesFile) throws FileNotFoundException {
        Scanner fileSC = new Scanner(linesFile);
        List<String[]> scenarioEdges = new ArrayList<>();

        while (fileSC.hasNextLine()) {
            String line = fileSC.nextLine().trim();
            if (!line.isEmpty()) {
                scenarioEdges.add(line.split(";"));
            }
        }
        fileSC.close();
        return scenarioEdges;
    }

    // User stories implementation
    private static void us13Implementation(File linesFile) throws FileNotFoundException {
        // getting railway data
        List<String[]> edges = getScenarioLines(linesFile);

        // selection menu for train type
        System.out.println("Select a train type from the following list.");
        System.out.println("1. Steam");
        System.out.println("2. Diesel");
        System.out.println("3. Electric");
        System.out.println("Train type: ");
        String trainType = sc.nextLine().trim();

        // getting start and destination stations
        System.out.println("Enter the station where the route will start: ");
        String startStation = sc.nextLine().trim();
        System.out.println("Enter the station where the route will end: ");
        String destinationStation = sc.nextLine().trim();
    }
}
