package maintenance_route_finder;

import guru.nidi.graphviz.attribute.Color;
import guru.nidi.graphviz.engine.Format;
import guru.nidi.graphviz.engine.Graphviz;
import static guru.nidi.graphviz.engine.Engine.DOT;
import static guru.nidi.graphviz.model.Factory.*;
import guru.nidi.graphviz.engine.GraphvizCmdLineEngine;
import guru.nidi.graphviz.model.MutableGraph;
import guru.nidi.graphviz.model.MutableNode;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class Main {

    static Scanner read = new Scanner(System.in);

    public static void main(String[] args) throws IOException {

        String directoryPath;

        if (args.length > 0) {
            directoryPath = args[0];
        } else {
            System.out.print("Enter the path to the folder containing the CSV files: ");
            directoryPath = read.nextLine().trim();
        }

        File Directory = new File(directoryPath);

        if (!Directory.exists() || !Directory.isDirectory()) {
            System.out.println("Directory does not exist or is not a directory.");
            System.exit(1);
        }

        File[] files = Directory.listFiles((directory, name) -> name.toLowerCase().endsWith(".csv"));

        if (files == null || files.length < 2) {
            System.out.println("There must be at least 2 CSV files.");
            System.exit(1);
        }

        for (int i = 0; i < files.length; i++) {
            System.out.println("[" + (i + 1) + "] " + files[i].getName());
        }

        File linesCSV = null;
        boolean Choice = false;

        while (!Choice) {
            System.out.println("Please enter the number of lines you wish to read: ");
            if (read.hasNextInt()) {
                int number = read.nextInt();
                read.nextLine();
                if (number >= 1 && number <= files.length) {
                    linesCSV = files[number - 1];
                    Choice = true;
                } else {
                    System.out.println("Please enter a number between 1 and " + files.length + ".");
                }
            } else {
                System.out.println("Please enter a number between 1 and " + files.length + ".");
                read.nextLine();
            }
        }

        List<String[]> ligacoes = new ArrayList<>();
        Scanner fileScanner = new Scanner(linesCSV);

        while (fileScanner.hasNextLine()) {
            String linha = fileScanner.nextLine().trim();
            if (!linha.isEmpty()) {
                String[] dadosOriginais = linha.split(";");
                if (dadosOriginais.length >= 3) {
                    String[] dados = Arrays.copyOfRange(dadosOriginais, 0, 3);
                    ligacoes.add(dados);
                }
            }
        }

        fileScanner.close();

        Map<String, List<Edge>> graph = buildGraph(ligacoes);

        boolean onlyElectrified = false;
        boolean maintenanceChoiceMade = false;

        while (!maintenanceChoiceMade) {
            System.out.println("Choose maintenance type:");
            System.out.println("[1] All Lines");
            System.out.println("[2] Only Electrified Lines");
            String option = read.nextLine().trim();
            if (option.equals("1")) {
                maintenanceChoiceMade = true;
            } else if (option.equals("2")) {
                onlyElectrified = true;
                maintenanceChoiceMade = true;
            } else {
                System.out.println("Invalid option. Please choose 1 or 2.");
            }
        }

        List<String[]> filteredData = new ArrayList<>();
        for (String[] data : ligacoes) {
            boolean electrified = data[2].equals("1");
            if (!onlyElectrified || electrified) {
                filteredData.add(data);
            }
        }

        Map<String, List<Edge>> filteredGraph = buildGraph(filteredData);
        List<String> oddVertices = new ArrayList<>();

        for (Map.Entry<String, List<Edge>> entry : filteredGraph.entrySet()) {
            String station = entry.getKey();
            int degree = entry.getValue().size();
            if (degree % 2 != 0) {
                oddVertices.add(station);
            }
        }

        Set<String> visitedNodes = new HashSet<>();
        String start = null;

        for (Map.Entry<String, List<Edge>> entry : filteredGraph.entrySet()) {
            if (!entry.getValue().isEmpty()) {
                start = entry.getKey();
                break;
            }
        }

        if (start != null) {
            dfsVisit(start, filteredGraph, visitedNodes);
        }

        boolean isConnected = true;
        for (Map.Entry<String, List<Edge>> entry : filteredGraph.entrySet()) {
            if (!entry.getValue().isEmpty() && !visitedNodes.contains(entry.getKey())) {
                isConnected = false;
            }
        }

        if (!isConnected) {
            System.out.println("The network is disconnected. No maintenance route is possible.");
        } else if (oddVertices.size() == 0 || oddVertices.size() == 2) {
            System.out.println("A maintenance route is possible.");
            System.out.println("Start at: " + (oddVertices.isEmpty() ? start : oddVertices.get(0)));
        } else {
            System.out.println("No Eulerian path possible (vertices with odd degree: " + oddVertices.size() + ").");
        }

        Map<String, MutableNode> nodeMap = new HashMap<>();
        for (String station : filteredGraph.keySet()) {
            nodeMap.put(station, mutNode(station).add(Color.BLACK));
        }

        MutableGraph graphViz = mutGraph("RailwayNetwork").setDirected(false);
        for (MutableNode node : nodeMap.values()) {
            graphViz.add(node);
        }

        Set<String> addedEdges = new HashSet<>();
        for (String[] dados : filteredData) {
            String origin = dados[0];
            String destination = dados[1];
            boolean electrified = dados[2].equals("1");

            String key1 = origin + "-" + destination;
            String key2 = destination + "-" + origin;

            if (addedEdges.contains(key1) || addedEdges.contains(key2)) continue;

            Color color = electrified ? Color.YELLOW : Color.RED;
            nodeMap.get(origin).addLink(to(nodeMap.get(destination)).with(color));
            addedEdges.add(key1);
        }

        System.setProperty("graphviz.dot", "C:\\Program Files\\Graphviz\\bin\\dot.exe");
        Graphviz.useEngine(new GraphvizCmdLineEngine());

        File outputDir = new File("src/main/java/maintenance_route_finder/Graphs");

        String timestamp = new java.text.SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        File outputFile = new File(outputDir, "railway-network_" + timestamp + ".svg");

        Graphviz.fromGraph(graphViz).engine(DOT).render(Format.SVG).toFile(outputFile);

        System.out.println("SVG generated at: " + outputFile.getAbsolutePath());

    }

    public static class Edge {
        public String destination;
        public boolean electrified;

        public Edge(String destination, boolean electrified) {
            this.destination = destination;
            this.electrified = electrified;
        }
    }

    public static void dfsVisit(String current, Map<String, List<Edge>> graph, Set<String> visited) {
        visited.add(current);
        for (Edge e : graph.get(current)) {
            if (!visited.contains(e.destination)) {
                dfsVisit(e.destination, graph, visited);
            }
        }
    }

    public static Map<String, List<Edge>> buildGraph(List<String[]> dados) {
        Map<String, List<Edge>> graph = new HashMap<>();
        for (String[] linha : dados) {
            String origin = linha[0];
            String destination = linha[1];
            boolean electrified = linha[2].equals("1");

            graph.putIfAbsent(origin, new ArrayList<>());
            graph.putIfAbsent(destination, new ArrayList<>());

            graph.get(origin).add(new Edge(destination, electrified));
            graph.get(destination).add(new Edge(origin, electrified));
        }
        return graph;
    }
}
