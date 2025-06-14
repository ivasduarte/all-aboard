  # US28 - Efficiency of the Algorithms Developed in US27

## 1. Introduction

This document addresses User Story 28 (US28), which requires an analysis of the efficiency of the algorithms implemented in **US27** of the project.

Only algorithms developed using primitive operations, without relying on Java’s built-in graph libraries, are considered in this analysis.
This includes custom procedures such as graph construction, shortest-path search using Dijkstra’s algorithm, and waypoint route chaining.
The worst-case time complexity refers to the maximum runtime for the most demanding input. We use asymptotic notation — particularly Θ (tight bound) — to describe how performance scales with input size.

> **AC01**: The analysis report of the worst-case time complexity of the algorithms should be documented/detailed in the repository documentation(using markdown format). All the algorithms should be presented in pseudocode, where the complexity analysis should be made.

> **AC02**: As required by acceptance criterion AC02, all procedures related to **graphical visualization** (e.g., rendering with Graphviz) are **excluded from this analysis**, as they are handled by external libraries and not part of the algorithmic logic implemented by the developer.

## 2. Efficiency Analysis of US27

User Story 27 requires computing the shortest possible route that connects a predefined, ordered list of stations on the railway network. The list includes the start station, end station, and any number of intermediate waypoints that must be visited in the exact order given.

To achieve this, the algorithm verifies that all stations are connected, then uses a shortest-path procedure (based on Dijkstra’s algorithm) to compute the optimal segment between each consecutive pair of stations. The final route is constructed by chaining these shortest segments into a complete journey.

Dijkstra’s algorithm is a greedy single-source shortest-path algorithm that computes the minimum path cost from a given source vertex to all other vertices in a weighted graph with non-negative edge weights.

#### 2.1 Graph Representation

The railway network is modelled as an **undirected weighted graph** \( G = (V, E) \), where:
- \( V \) is the set of stations (vertices)
- \( E \) is the set of railway lines (edges), each with a **positive integer weight** representing distance in kilometers

Two different representations are used:
- A **lightweight adjacency list** (`Map<String, List<String>>`) is built initially to check whether all required stations are connected, using a simple depth-first search.
- A **weighted adjacency list** (`Map<String, List<Edge>>`) is constructed dynamically inside the `shortestPath()` method before each Dijkstra call. This version associates each edge with its numerical length, used to compute optimal routes.

The final graph structure ensures that all shortest-path computations consider actual distances between stations. Both representations have worst-case size **Θ(|V| + |E|)**.

#### 2.2 Station connectivity check

Before calculating shortest paths, the algorithm confirms that all required stations (waypoints) are in the same connected component. This is done using DFS from the first station.

The DFS traversal touches each reachable vertex and its neighbors, so in the worst case the traversal has time complexity Θ(V + E).

#### 2.3 Shortest Path with Dijkstra

To find the shortest path between each pair of consecutive stations in the waypoint list, the algorithm uses Dijkstra’s algorithm.

Dijkstra runs from a source vertex, using a priority queue to always expand the nearest unvisited vertex, updating distances accordingly. The implementation has time complexity Θ((V + E) log V) per run.

#### 2.4 Waypoint Route Chaining

Given an ordered list of k stations that the train must visit, the algorithm computes the shortest path between each consecutive pair of stations using Dijkstra’s algorithm. This results in a total of k - 1 Dijkstra executions. Since each execution has a time complexity of Θ((V + E) log V), the overall complexity of chaining all required segments is Θ(k × (V + E) log V).

#### 2.5 Pseudocode

Below are the key algorithms implemented the US27, described in pseudocode.
> **Legend for the "Type" column:**
> - `A`: Assignment
> - `C`: Comparison
> - `R`: Return
> - `Op`: Operation (e.g., arithmetic or function call)
> - `I`: Insertion into a structure (e.g., list, set)

---

#### a) DFS Connectivity Check

This function determines if the destination station is reachable from the start station by using a depth-first search (DFS) traversal over the graph.

V = number of vertices, E = number of edges.

    Function dfs(node, graph, visited):
    Add node to visited
    For each neighbor in graph[node]:
        If neighbor not in visited:
            If dfs(neighbor, graph, visited)

| Pseudocode                       | Type            | 
|----------------------------------|-----------------|
| Add node to visited              | VOp + VI        |       
| For each neighbor in graph[node] | A(E+1) + C(E+1) |
| If neighbor not in visited       | VC              |       
| If dfs(neighbor, graph, visited) | VC              |

Time complexity: Θ(V + E)

#### b) Weighted Graph Construction

This function constructs a weighted adjacency list from a list of railway connections, each with an associated positive distance.

n = number of edges. (railway lines).

    Function buildWeightedGraph(edgeList):
    graph ← empty map
    If stationA not in graph:
            graph[stationA] ← empty list
        If stationB not in graph:
            graph[stationB] ← empty list
        Add (stationB, distance) to graph[stationA]
        Add (stationA, distance) to graph[stationB]
    Return graph

| Pseudocode                                       | Type            |
|--------------------------------------------------|-----------------|
| graph ← empty map                                | 1A              |
| For each (stationA, stationB, distance) in edges | A(n+1) + C(n+1) |
| If stationA not in graph                         | nC              |
| graph\[stationA] ← empty list                    | nA              |
| If stationB not in graph                         | nC              |
| graph\[stationB] ← empty list                    | nA              |
| Add (stationB, distance) to graph\[stationA]     | nOp + nI        |
| Add (stationA, distance) to graph\[stationB]     | nOp + nI        |
| Return graph                                     | 1R              |

Time complexity: Θ(E)

#### c) Dijkstra's Algorithm

This function computes the shortest path between a source and a destination station using Dijkstra’s algorithm with a min-priority queue.

V = number of vertices, E = number of edges.

    Function dijkstra(start, end, graph):
    dist ← map with all nodes set to ∞
    dist[start] ← 0
    prev ← empty map
    pq ← priority queue ordered by dist

    pq.add(start)

    While pq is not empty:
        current ← pq.removeMin()
        If current = end:
            Break
        For each neighbor in graph[current]:
            newDist ← dist[current] + weight(current, neighbor)
            If newDist < dist[neighbor]:
                dist[neighbor] ← newDist
                prev[neighbor] ← current
                pq.add(neighbor)

    path ← empty list
    node ← end
    While node is not null:
        path.add(node)
        node ← prev[node]
    Reverse path
    Return path

| Pseudocode                             | Type            |
|----------------------------------------|-----------------|
| dist ← ∞ for all nodes                 | VOp + VA        |
| dist\[start] ← 0                       | 1A              |
| prev ← empty map                       | 1A              |
| pq ← priority queue                    | 1A              |
| pq.add(start)                          | 1Op + 1I        |
| While pq is not empty                  | A(V) + C(V)     |
| current ← pq.removeMin()               | VOp             |
| If current = end                       | VOp + VC        |
| For each neighbor in graph\[current]   | A(E+1) + C(E+1) |
| newDist ← dist\[current] + weight(...) | EOp             |
| If newDist < dist\[neighbor]           | EC              |
| dist\[neighbor] ← newDist              | EA              |
| prev\[neighbor] ← current              | EA              |
| pq.add(neighbor)                       | EOp + EI        |
| path ← empty list                      | 1A              |
| While node is not null                 | A(V) + C(V)     |
| path.add(node)                         | VI              |
| node ← prev\[node]                     | VA              |
| Reverse path                           | 1Op             |
| Return path                            | 1R              |

Time complexity: Θ((V + E) log V)

#### 2.6 Worst-Case Time Complexity

Let:
- `V` be the number of stations (vertices)
- `E` be the number of railway lines (edges)
- `k` be the number of waypoints (including start and end)

Then, the worst-case time complexity for the full procedure is:

- **DFS Connectivity Check** → Θ(V + E)
- **Graph Construction (weighted)** → Θ(E)
- **Dijkstra Execution (k – 1 times)** → Θ(k × (V + E) log V)

**Total complexity:**  
→ Θ(V + E + k × (V + E) log V)

## 3. Summary Table

| Algorithm                   | Description                                     | Time Complexity                  |
|-----------------------------|-------------------------------------------------|----------------------------------|
| DFS Connectivity Check      | Verifies if all required stations are connected | Θ(V + E)                         |
| Weighted Graph Construction | Builds adjacency list with distances            | Θ(E)                             |
| Dijkstra’s Algorithm        | Shortest path for a pair of stations            | Θ((V + E) log V)                 |
| Waypoint Chaining           | Repeats Dijkstra for k – 1 segments             | Θ(k × (V + E) log V)             |
| **Overall (US27)**          | Full process                                    | **Θ(V + E + k × (V + E) log V)** |







