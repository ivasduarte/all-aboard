package maintenance_route_finder;

import java.io.*;
import java.util.*;

public class Main {
    static Scanner read = new Scanner(System.in);

    public static void main(String[] args) throws IOException, InterruptedException {

        System.out.print("Enter the path to the folder containing the CSV files: ");
        String directoryPath = read.nextLine().trim();
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
        int fileNum = read.nextInt();
        read.nextLine();
        File selected = files[fileNum - 1];

        System.out.println("Choose maintenance type:\n[1] All Lines\n[2] Only Electrified Lines");
        String option = read.nextLine().trim();
        boolean onlyElectrified = option.equals("2");

        List<String[]> edges = new ArrayList<>();
        Scanner sc = new Scanner(selected);
        while (sc.hasNextLine()) {
            String[] parts = sc.nextLine().trim().split(";");
            if (parts.length >= 3) {
                if (!onlyElectrified || parts[2].equals("1")) {
                    edges.add(parts);
                }
            }
        }
        sc.close();

        Set<String> allStations = new HashSet<>();
        for (String[] edge : edges) {
            allStations.add(edge[0]);
            allStations.add(edge[1]);
        }
        Map<String, List<String>> graph = new HashMap<>();
        for (String station : allStations) {
            graph.put(station, new ArrayList<>());
        }
        for (String[] edge : edges) {
            graph.get(edge[0]).add(edge[1]);
            graph.get(edge[1]).add(edge[0]);
        }

        List<String> odd = new ArrayList<>();
        for (String node : graph.keySet()) {
            if (graph.get(node).size() % 2 != 0) {
                odd.add(node);
            }
        }

        Set<String> visited = new HashSet<>();
        String start = graph.keySet().stream().filter(k -> !graph.get(k).isEmpty()).findFirst().orElse(null);
        if (start != null) dfs(start, graph, visited);

        boolean connected = graph.entrySet().stream()
                .filter(e -> !e.getValue().isEmpty())
                .allMatch(e -> visited.contains(e.getKey()));

        if (!connected) {
            System.out.println("No maintenance route is possible due to disconnection.");
        } else {
            if (odd.size() == 0 || odd.size() == 2) {
                System.out.println("A maintenance route is possible.");
                if (!odd.isEmpty()) {
                    System.out.println("Start at: " + odd.get(0));
                } else {
                    System.out.println("Start at: " + start);
                }
            } else {
                System.out.println("No Eulerian path possible (vertices with odd degree: " + odd.size() + ").");
            }
        }


        File dotFile = new File("railway.dot");
        try (PrintWriter pw = new PrintWriter(dotFile)) {
            pw.println("graph G {");
            for (String[] edge : edges) {
                String color = edge[2].equals("1") ? "yellow" : "red";
                pw.println("  \"" + edge[0] + "\" -- \"" + edge[1] + "\" [color=" + color + "];");
            }
            for(String station: graph.keySet()) {
                if(graph.get(station).isEmpty()) {
                    pw.println("  \"" + station + "\";");
                }
            }
            pw.println("}");
        }

        ProcessBuilder pb = new ProcessBuilder("dot", "-Tsvg", "railway.dot", "-o", "railway.svg");
        pb.redirectErrorStream(true);
        Process p = pb.start();
        p.waitFor();
        System.out.println("Graph exported to railway.svg");
    }

    public static void dfs(String node, Map<String, List<String>> graph, Set<String> visited) {
        visited.add(node);
        for (String neighbor : graph.get(node)) {
            if (!visited.contains(neighbor)) {
                dfs(neighbor, graph, visited);
            }
        }
    }
}
