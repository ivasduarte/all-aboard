package shortest_route_finder;

import java.io.*;
import java.util.*;
import java.util.stream.Collectors;

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

        List<Edge> edges = new ArrayList<>();
        Scanner sc = new Scanner(selected);
        while (sc.hasNextLine()) {
            String[] parts = sc.nextLine().trim().split(";");
            if (parts.length == 4) {
                edges.add(new Edge(parts[0], parts[1], Integer.parseInt(parts[2]), Integer.parseInt(parts[3])));
            }
        }
        sc.close();

        Map<String, List<String>> graph = new HashMap<>();
        for (Edge edge : edges) {
            graph.putIfAbsent(edge.from, new ArrayList<>());
            graph.putIfAbsent(edge.to, new ArrayList<>());
            graph.get(edge.from).add(edge.to);
            graph.get(edge.to).add(edge.from);
        }

        System.out.print("Enter station names the train must pass through (comma separated): ");
        String[] mustPassArray = read.nextLine().trim().split(",");
        List<String> mustPassList = Arrays.stream(mustPassArray)
                .map(String::trim)
                .filter(s -> !s.isEmpty())
                .collect(Collectors.toList());

        for (String station : mustPassList) {
            if (!graph.containsKey(station)) {
                System.out.println("Station \"" + station + "\" not found in the network.");
                return;
            }
        }

        Set<String> visited = new HashSet<>();
        String start = mustPassList.get(0);
        dfs(start, graph, visited);

        boolean connected = mustPassList.stream().allMatch(visited::contains);
        if (!connected) {
            System.out.println("Not all required stations are connected. No route is possible.");
            return;
        }

        System.out.println("A route through all specified stations is possible.");
        System.out.println("Start at: " + start);

        Set<String> routeEdges = new HashSet<>();
        int totalDistance = 0;

        for (int i = 0; i < mustPassList.size() - 1; i++) {
            String from = mustPassList.get(i);
            String to = mustPassList.get(i + 1);
            List<String> path = shortestPath(from, to, graph, edges);
            if (path.isEmpty()) {
                System.out.println("No path found between " + from + " and " + to);
                return;
            }
            System.out.println("Path from " + from + " to " + to + ": " + String.join(" -> ", path));

            for (int j = 0; j < path.size() - 1; j++) {
                String a = path.get(j);
                String b = path.get(j + 1);
                routeEdges.add(a + ";" + b);
                routeEdges.add(b + ";" + a);

                for (Edge edge : edges) {
                    if ((edge.from.equals(a) && edge.to.equals(b)) || (edge.from.equals(b) && edge.to.equals(a))) {
                        totalDistance += edge.length;
                        break;
                    }
                }
            }
        }

        System.out.println("Total distance: " + totalDistance + " km");

        File dotFile = new File("shortestRoute.dot");
        try (PrintWriter pw = new PrintWriter(dotFile)) {
            pw.println("graph G {");
            for (Edge edge : edges) {
                String key1 = edge.from + ";" + edge.to;
                String key2 = edge.to + ";" + edge.from;
                String color = routeEdges.contains(key1) || routeEdges.contains(key2) ? "yellow" : "black";
                pw.printf("  \"%s\" -- \"%s\" [color=%s, label=\"%dkm\"];\n", edge.from, edge.to, color, edge.length);
            }
            for (String station : mustPassList) {
                pw.printf("  \"%s\" [color=green, penwidth=3];\n", station);
            }
            pw.println("}");
        }

        ProcessBuilder pb = new ProcessBuilder("dot", "-Tsvg", "shortestRoute.dot", "-o", "shortestRoute.svg");
        pb.redirectErrorStream(true);
        Process p = pb.start();
        p.waitFor();
        System.out.println("Graph exported to shortestRoute.svg");
    }

    public static void dfs(String node, Map<String, List<String>> graph, Set<String> visited) {
        visited.add(node);
        for (String neighbor : graph.get(node)) {
            if (!visited.contains(neighbor)) {
                dfs(neighbor, graph, visited);
            }
        }
    }

    public static List<String> shortestPath(String start, String end, Map<String, List<String>> graph, List<Edge> edges) {
        Map<String, List<Edge>> weightedGraph = new HashMap<>();
        for (Edge edge : edges) {
            weightedGraph.putIfAbsent(edge.from, new ArrayList<>());
            weightedGraph.putIfAbsent(edge.to, new ArrayList<>());
            weightedGraph.get(edge.from).add(edge);
            weightedGraph.get(edge.to).add(new Edge(edge.to, edge.from, 0, edge.length)); // reverse
        }

        Map<String, Integer> distances = new HashMap<>();
        Map<String, String> previous = new HashMap<>();
        PriorityQueue<String> queue = new PriorityQueue<>(Comparator.comparingInt(distances::get));

        for (String node : weightedGraph.keySet()) {
            distances.put(node, Integer.MAX_VALUE);
        }
        distances.put(start, 0);
        queue.add(start);

        while (!queue.isEmpty()) {
            String current = queue.poll();
            if (current.equals(end)) break;

            for (Edge edge : weightedGraph.getOrDefault(current, Collections.emptyList())) {
                int newDist = distances.get(current) + edge.length;
                if (newDist < distances.get(edge.to)) {
                    distances.put(edge.to, newDist);
                    previous.put(edge.to, current);
                    queue.add(edge.to);
                }
            }
        }

        List<String> path = new ArrayList<>();
        for (String at = end; at != null; at = previous.get(at)) {
            path.add(at);
            if (at.equals(start)) break;
        }

        if (!path.contains(start)) return Collections.emptyList();
        Collections.reverse(path);
        return path;
    }

    static class Edge {
        String from;
        String to;
        int lineType;
        int length;

        Edge(String from, String to, int lineType, int length) {
            this.from = from;
            this.to = to;
            this.lineType = lineType;
            this.length = length;
        }
    }
}
