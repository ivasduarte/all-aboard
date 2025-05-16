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

import static guru.nidi.graphviz.attribute.Attributes.attr;
import static guru.nidi.graphviz.engine.Engine.DOT;
import static guru.nidi.graphviz.model.Factory.*;

public class Main {
    static Scanner sc = new Scanner(System.in);

    public static void main(String[] args) throws IOException {
        // getting input
        String linesFile = askFileName("Enter the name of the lines file: ");         // lines file name
        String stationsFile = askFileName("Enter the name of the stations file: ");   // stations file name

        File lines = new File(linesFile);
        File stations = new File(stationsFile);

        while (true) {
            System.out.println("\n=== Menu ===");
            System.out.println("1. Verify if a train can travel between two stations (US13)");
            System.out.println("2. Verify if a route that passes once, and only once, by each railway line exists (US14)");
            System.out.println("3. Exit");
            System.out.print("Choose an option: ");
            String op = sc.nextLine().trim();

            if (op.equals("1")) {
                us13Implementation(lines);
                break;
            } else if (op.equals("2")) {  // adicionar metodo implementacao us14
                break;
            } else if (op.equals("3")) {
                System.out.println("Closing...");
                break;
            } else {
                System.out.println("Invalid option. Please, try another one.");
            }
        }
        sc.close();
    }

    // utilities methods
    public static String askFileName(String prompt) {
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

    private static Map<String, List<String>> buildGraph(List<String[]> scenarioEdges, String trainType) {
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

    private static boolean isConnected(Map<String, List<String>> graph, String departure, String arrival) {
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

    // User stories implementation
    private static void us13Implementation(File linesFile) throws IOException {
        // getting railway data
        List<String[]> edges = getScenarioLines(linesFile);
        List<String> vertices = new ArrayList<>();

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

        for (String[] edge : edges) {
            if (!vertices.contains(edge[0])) vertices.add(edge[0]);
            if (!vertices.contains(edge[1])) vertices.add(edge[1]);
        }

        // building adjacency list
        Map<String, List<String>> graph = buildGraph(edges, trainType);
        List<String> route = new ArrayList<>(graph.keySet());

        // checking connectivity
        boolean canTravel = isConnected(graph, startStation, destinationStation);
        if (canTravel) {
            System.out.println("The train can travel between " + startStation + " and " + destinationStation + ".");
        } else {
            System.out.println("It isn't possible for the train to travel between " + startStation + " and " + destinationStation + ".");
        }

        // graphical representation
        Map<String, MutableNode> nodeMap = new HashMap<>();
        for (String v : vertices) nodeMap.put(v, mutNode(v).add(Color.BLACK));

        MutableGraph builtGraph = mutGraph("StationsMap").setDirected(false).add(nodeMap.values().toArray(new MutableNode[0]));
        for (String[] edge : edges) {
            String a = edge[0], b = edge[1];
            Color c = edge[2].equals("1") ? Color.YELLOW : Color.RED;
            builtGraph.add(nodeMap.get(a).addLink(to(nodeMap.get(b)).with(c)));
        }

        for (int i = 0; i < route.size() - 1; i++) {
            builtGraph.add(nodeMap.get(route.get(i)).addLink(to(nodeMap.get(route.get(i + 1))).with(attr("penwidth", "4"))));
        }

        Graphviz.fromGraph(builtGraph).engine(DOT).render(Format.SVG).toFile(new File("station-map.svg"));
        System.out.println("Graph saved as stationsMap.svg");
    }
}
