package railway_connectivity;

import guru.nidi.graphviz.attribute.Color;
import guru.nidi.graphviz.engine.Format;
import guru.nidi.graphviz.engine.Graphviz;
import guru.nidi.graphviz.model.MutableGraph;
import guru.nidi.graphviz.model.MutableNode;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.*;

import static guru.nidi.graphviz.engine.Engine.DOT;
import static guru.nidi.graphviz.model.Factory.*;

public class Main {
    static Scanner sc = new Scanner(System.in);

    public static void main(String[] args) throws IOException {
        System.out.print("Enter the path to the folder containing the CSV files: ");
        String directoryPath = sc.nextLine().trim();
        File dir = new File(directoryPath);

        if (!dir.exists() || !dir.isDirectory()) {
            System.out.println("Directory does not exist.");
            return;
        }

        File[] files = dir.listFiles((d, name) -> name.endsWith(".csv"));
        if (files == null || files.length < 1) {
            System.out.println("No CSV files found.");
            return;
        }

        for (int i = 0; i < files.length; i++) {
            System.out.println("[" + (i + 1) + "] " + files[i].getName());
        }

        System.out.print("Choose file number with railway lines: ");
        int fileNum = sc.nextInt();
        sc.nextLine();
        File lines = files[fileNum - 1];

        us13Implementation(lines);
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

    private static Map<String, List<String>> buildGraph(List<String[]> scenarioEdges, String trainType) {
        Map<String, List<String>> graph = new HashMap<>();

        for (String[] edge : scenarioEdges) {
            String departure = edge[0];
            String arrival = edge[1];
            String lineType = edge[2];

            if (!canTrainUseLineType(trainType, lineType)) {
                continue;
            }

            graph.putIfAbsent(departure, new ArrayList<>());
            graph.putIfAbsent(arrival, new ArrayList<>());
            graph.get(departure).add(arrival);
            graph.get(arrival).add(departure);
        }
        return graph;
    }

    private static boolean canTrainUseLineType(String trainType, String lineType) {
        switch (trainType) {
            case "1":
            case "2":
                return true;
            case "3":
                return lineType.equals("1");
            default:
                return false;
        }
    }

    private static boolean isConnected(Map<String, List<String>> graph, String departure, String arrival) {
        if (!graph.containsKey(departure) || !graph.containsKey(arrival)) {
            return false;
        }

        Set<String> visited = new HashSet<>();
        return dfs(graph, departure, arrival, visited);
    }

    private static boolean dfs(Map<String, List<String>> graph, String current, String arrival, Set<String> visited) {
        if (current.equals(arrival)) return true;
        visited.add(current);

        for (String neighbor : graph.getOrDefault(current, new ArrayList<>())) {
            if (!visited.contains(neighbor)) {
                if (dfs(graph, neighbor, arrival, visited)) {
                    return true;
                }
            }
        }
        return false;
    }

    private static void us13Implementation(File linesFile) throws IOException {
        List<String[]> edges = getScenarioLines(linesFile);

        Set<String> allStations = new HashSet<>();
        for (String[] edge : edges) {
            allStations.add(edge[0]);
            allStations.add(edge[1]);
        }
        List<String> vertices = new ArrayList<>(allStations);

        String trainType = "";
        while (!trainType.equals("1") && !trainType.equals("2") && !trainType.equals("3")) {
            System.out.println("\nSelect a train type from the following list.");
            System.out.println("1. Steam");
            System.out.println("2. Diesel");
            System.out.println("3. Electric");
            System.out.print("Train type: ");
            trainType = sc.nextLine().trim();

            if (!trainType.equals("1") && !trainType.equals("2") && !trainType.equals("3")) {
                System.out.println("Invalid selection. Please choose 1, 2, or 3.");
            }
        }

        String startStation = "";
        while (!allStations.contains(startStation)) {
            System.out.print("\nEnter the station where the route will start: ");
            startStation = sc.nextLine().trim();
            if (!allStations.contains(startStation)) {
                System.out.println("Station '" + startStation + "' not found. Available stations: " + vertices);
            }
        }

        String destinationStation = "";
        while (!allStations.contains(destinationStation)) {
            System.out.print("Enter the station where the route will end: ");
            destinationStation = sc.nextLine().trim();
            if (!allStations.contains(destinationStation)) {
                System.out.println("Station '" + destinationStation + "' not found. Available stations: " + vertices);
            }
        }

        Map<String, List<String>> graph = buildGraph(edges, trainType);

        boolean canTravel = isConnected(graph, startStation, destinationStation);
        if (canTravel) {
            System.out.println("The train can travel between " + startStation + " and " + destinationStation + ".");
        } else {
            System.out.println("It isn't possible for the train to travel between " + startStation + " and " + destinationStation + ".");
        }

        Map<String, MutableNode> nodeMap = new HashMap<>();
        for (String v : vertices) {
            nodeMap.put(v, mutNode(v).add(Color.BLACK));
        }

        MutableGraph builtGraph = mutGraph("StationsMap").setDirected(false).add(nodeMap.values().toArray(new MutableNode[0]));

        for (String[] edge : edges) {
            String a = edge[0], b = edge[1], lineType = edge[2];
            Color c = lineType.equals("1") ? Color.RED : Color.YELLOW;
            builtGraph.add(nodeMap.get(a).addLink(to(nodeMap.get(b)).with(c)));
        }

        Graphviz.fromGraph(builtGraph).engine(DOT).render(Format.SVG).toFile(new File("stationsMap.svg"));
        System.out.println("Graph saved as stationsMap.svg");
    }
}