package performance_testing;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.*;

public class Main {
    static Scanner sc = new Scanner(System.in);

    public static void main(String[] args) throws IOException {
        us29Implementation();
    }

    private static void us29Implementation() throws IOException {
        System.out.println("Insert the directory path to the test files: ");
        String dirPath = sc.nextLine();

        File directory = new File(dirPath);
        if (!directory.exists() || !directory.isDirectory()) {
            System.err.println("Error: The inserted path does not refer to a directory!");
            return;
        }

        List<File> scenarioFiles = new ArrayList<>();
        List<File> stationsFiles = new ArrayList<>();

        File[] csvFiles = directory.listFiles((dir, name) -> name.toLowerCase().endsWith(".csv"));
        if (csvFiles == null || csvFiles.length == 0) {
            System.err.println("No CSV files found in the selected directory!");
            return;
        }

        for (File file : csvFiles) {
            String fileName = file.getName().toLowerCase();

            if (fileName.contains("scenario") && !fileName.contains("station")) {
                scenarioFiles.add(file);
            } else if (fileName.contains("station")) {
                stationsFiles.add(file);
            }
        }

        scenarioFiles.sort((f1, f2) -> {
            int num1 = extractNumberFromFilename(f1.getName());
            int num2 = extractNumberFromFilename(f2.getName());
            return Integer.compare(num1, num2);
        });

        stationsFiles.sort((f1, f2) -> {
            int num1 = extractNumberFromFilename(f1.getName());
            int num2 = extractNumberFromFilename(f2.getName());
            return Integer.compare(num1, num2);
        });

        System.out.println("Found " + scenarioFiles.size() + " scenario files and " + stationsFiles.size() + " station files\n");

        System.out.println();

        System.out.println("-".repeat(10) + "Testing US13 Performance" + "-".repeat(10));
        testUS13Implementation(scenarioFiles, stationsFiles);

        System.out.println("\n" + "=".repeat(63) + "\n");

        System.out.println("-".repeat(10) + "Testing US14 Performance" + "-".repeat(10));
        // inserir metodo de teste da us14
    }

    private static int extractNumberFromFilename(String fileName) {
        java.util.regex.Pattern pattern = java.util.regex.Pattern.compile("\\d+");
        java.util.regex.Matcher matcher = pattern.matcher(fileName);

        if (matcher.find()) {
            return Integer.parseInt(matcher.group());
        }
        return 0;
    }

    private static void testUS13Implementation(List<File> scenarioFiles, List<File> stationFiles) throws IOException {
        List<PerformanceResult> resultsUS13 = new ArrayList<>();

        for (File file : scenarioFiles) {
            File matchingFile = findMatchingFile(file, stationFiles);

            if (matchingFile == null) {
                System.out.println("There's no matching file for : " + file.getName());
                continue;
            }

            if (!file.exists() || !matchingFile.exists()) {
                System.out.println("One of the files was not found.");
                continue;
            }

            List<String[]> edges = getScenarioLines(file);
            List<String> stations = getScenarioStations(matchingFile);

            String[] trainTypesNumbers = {"1", "2", "3"};   // Steam, Diesel, Electric
            String[] trainTypesNames = {"Steam", "Diesel", "Electric"};

            for (int i = 0; i < trainTypesNumbers.length; i++) {
                List<Long> executionTimes = new ArrayList<>();
                int testCases = Math.min(10, Math.max(1, stations.size() / 2));

                for (int test = 0; test < testCases; test++) {
                    String departure = stations.get(test * 2 % stations.size());
                    String arrival = stations.get((test * 2 + 1) % stations.size());

                    long startTime = System.nanoTime();

                    Map<String, List<String>> graphUS13 = buildGraphUS13(edges, trainTypesNumbers[i]);
                    boolean connectedUS13 = isConnectedUS13(graphUS13, departure, arrival);

                    long endTime = System.nanoTime();
                    long executionTime = endTime - startTime;
                    executionTimes.add(executionTime);
                }

                double averageTime = executionTimes.stream().mapToLong(Long::longValue).average().orElse(0.0);
                resultsUS13.add(new PerformanceResult("US13-" + trainTypesNames[i] + "-" + edges.size(), stations.size(), edges.size(), averageTime));
            }
        }
        printPerformanceSummary("User Story 13", resultsUS13);
    }

    private static File findMatchingFile(File scenarioFile, List<File> stationFiles) {
        String scenario = scenarioFile.getName();

        int scenarioNumber = extractNumberFromFilename(scenario);

        for (File file : stationFiles) {
            int stationNumber = extractNumberFromFilename(file.getName());

            if (stationNumber == scenarioNumber) {
                return file;
            }
        }

        String scenarioBaseName = scenario.toLowerCase().replace("scenario_", "").replace("_lines.csv", "").replace(".csv", "");

        for (File file : stationFiles) {
            String stationName = file.getName().toLowerCase();
            if (stationName.contains(scenarioBaseName)) {
                return file;
            }
        }

        return null;
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

    private static List<String> getScenarioStations(File stationsFile) throws FileNotFoundException {
        Scanner fileSC = new Scanner(stationsFile);
        List<String> stations = new ArrayList<>();

        if (fileSC.hasNextLine()) {
            String line = fileSC.nextLine().trim();

            String[] stationArray = line.split(";");
            for (String station : stationArray) {
                stations.add(station.trim());
            }
        }
        fileSC.close();

        return stations;
    }

    private static Map<String, List<String>> buildGraphUS13(List<String[]> scenarioEdges, String trainType) {
        Map<String, List<String>> graph = new HashMap<>();
        for (String[] edge : scenarioEdges) {
            String departure = edge[0];
            String arrival = edge[1];
            String type = edge[2];

            if (trainType.equals("3") && type.equals("0")) continue;

            graph.putIfAbsent(departure, new ArrayList<>());
            graph.putIfAbsent(arrival, new ArrayList<>());
            graph.get(departure).add(arrival);
            graph.get(arrival).add(departure);
        }
        return graph;
    }

    private static boolean isConnectedUS13(Map<String, List<String>> graph, String departure, String arrival) {
        Set<String> visited = new HashSet<>();
        return dfsUS13(graph, departure, arrival, visited);
    }

    private static boolean dfsUS13(Map<String, List<String>> graph, String current, String arrival, Set<String> visited) {
        if (current.equals(arrival)) return true;
        visited.add(current);

        for (String neighbor : graph.getOrDefault(current, new ArrayList<>())) {
            if (!visited.contains(neighbor)) {
                if (dfsUS13(graph, neighbor, arrival, visited)) {
                    return true;
                }
            }
        }
        return false;
    }

    private static void printPerformanceSummary(String US, List<PerformanceResult> results) {
        System.out.println("\n-----" + US + " PERFORMANCE SUMMARY -----");
        System.out.printf("%-25s %10s %10s %15s%n",
                "Test Case", "Vertices", "Edges", "Avg Time (ms)");
        System.out.println("-".repeat(63));

        for (PerformanceResult result : results) {
            System.out.printf("%-25s %10d %10d %15.3f%n", result.testName, result.vertexCount, result.edgeCount, result.avgTime / 1_000_000.0);
        }
    }

    private static class PerformanceResult {
        String testName;
        int vertexCount;
        int edgeCount;
        double avgTime;

        PerformanceResult(String testName, int vertexCount, int edgeCount, double avgTime) {
            this.testName = testName;
            this.vertexCount = vertexCount;
            this.edgeCount = edgeCount;
            this.avgTime = avgTime;
        }
    }
}