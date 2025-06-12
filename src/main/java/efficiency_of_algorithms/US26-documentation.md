  # US26 - Efficiency of the Algorithms Developed in US13 and US14

## 1. Introduction

This document addresses User Story 26 (US26), which requires an analysis of the efficiency of the algorithms implemented in **US13** and **US14** of the project.

Only algorithms developed using **primitive operations**, without relying on Java’s built-in graph-related libraries, are considered in this analysis. This includes procedures such as graph construction, depth-first search (DFS), and the analysis of node degrees to determine the possibility of an Eulerian path.

> **AC01**: The analysis report of the worst-case time complexity of the algorithms should be documented/detailed in the repository documentation(using markdown format). All the algorithms should be presented in pseudocode, where the complexity analysis should be made.

> **AC02**: As required by acceptance criterion AC02, all procedures related to **graphical visualization** (e.g., rendering with Graphviz) are **excluded from this analysis**, as they are handled by external libraries and not part of the algorithmic logic implemented by the developer.


## 2. Efficiency Analysis of US13

User Story 13 requires verifying whether a specific train — defined by its propulsion type (steam, diesel, or electric) — can travel between two selected stations on the railway network. This involves checking the connectivity of the network while considering constraints such as electrification of railway lines (in the case of electric trains) and ensuring that a valid path exists between the selected stations, using only permitted types of lines.

#### 2.1 Graph Representation

The railway network is modeled as an undirected graph \( G = (V, E) \), where:
- \( V \): set of vertices (stations)
- \( E \): set of edges (railway lines)

Each edge \( E \) indicates a line between two stations, which each one represents a vertex \( V \), with a different color representing whether it's a electrified line or not.

#### 2.2 Train Type Filtering

Let `T ∈ {Steam, Diesel, Electric}`.  
Only electric trains are restricted to electrified lines.  
The filtered graph for electric trains is:

E' = { (u, v) ∈ E | line(u, v) is electrified }

Resulting in a subgraph: 

G' = (V, E')

#### 2.3 Path Detection with DFS

To verify whether a train can travel from a starting station `s` to a destination station `t`, the algorithm performs a **Depth-First Search (DFS)**.

DFS is a classical graph traversal algorithm that explores the graph by visiting each node's neighbors recursively, going as deep as possible before backtracking.

In this context, the algorithm:

- Starts at the station `s`
- Recursively visits connected stations
- Stops if it reaches the destination `t`

If `t` is visited during the DFS traversal, it means there is a path between `s` and `t` in the graph.

This approach guarantees a correct answer because DFS explores **all reachable nodes** from `s`, and only fails if `t` is truly unreachable under the current train constraints (e.g., only electrified lines for electric trains).

#### 2.4 Worst-Case Time Complexity

Let:
- `V` be the number of stations (vertices)
- `E` be the number of railway lines (edges)

Then, the worst-case time complexity is:

- **Graph construction**:  
  Each edge is processed once to build adjacency lists  
  → `O(E)`

- **DFS traversal**:  
  In the worst case, all vertices and edges are visited  
  → `O(V + E)`

#### 2.5 Pseudocode

Below are the key algorithms implemented the US13, described in pseudocode.

#### a) Graph Construction (`buildGraph`)

This function reads the list of edges and constructs the adjacency list representation of the graph, applying the electrification filter if the train type is Electric.

    Function buildGraph(edgeList, trainType):
    graph <- empty map
    For each (stationA, stationB, isElectrified) in edgeList:
    If trainType = 'Electric' and isElectrified = 0:
        Skip this edge
    If stationA not in graph:
        graph[stationA] ← empty list
    If stationB not in graph:
        graph[stationB] ← empty list

    Add stationB to graph[stationA]
    Add stationA to graph[stationB]
    Return Graph

| Pseudocode                                               | Type    |
|----------------------------------------------------------|---------|
| graph <- empty map                                       | 1A      |       
| For each(stationA, stationB, isElectrified) in edgeList: | nA + nC |       
| if trainType = 'Electric' and isElectrified = 0          | nC      |       
| Skip this edge                                           | -       |       
| If stationA not in graph:                                | nC      |      
| graph[stationA] <- empty list                            | nA      |       
| If stationB not in graph:                                | nC      |       
| graph[stationB] <- empty list                            | nA      |       
| Add stationB to graph[stationA]                          | nOp     |       
| Add stationA to graph[stationB]                          | nOp     |       
| Return Graph                                             | 1R      |       



#### b) Path Search using dfs

This function determines if the destination station is reachable from the start station by using a depth-first search (DFS) traversal over the graph.


    Function isConnected(graph, start, end):
        visited ← empty set
    Return dfs(graph, start, end, visited)

    Function dfs(graph, current, end, visited):
    If current = end:
        Return True
    Add current to visited
    For each neighbor in graph[current]:
        If neighbor not in visited:
            If dfs(graph, neighbor, end, visited) = True:
                Return True
    Return False

| Pseudocode                                      | Type | 
|-------------------------------------------------|------|
| visited <- empty set                            | 1A   |       
| Return dfs(graph, start, end, visited)          | 1R   | 

| Pseudocode                                   | Type    | 
|----------------------------------------------|---------|
| If current = end                             | nC      |       
| Return true                                  | 1R      |
| Add current to visited                       | nOp     |       
| For each neighbor in graph[current]          | nA + nC |
| If neighbor not in visited                   | nC      |       
| If dfs(graph, neighbor, end, visited) = True | nC      |
| Return true                                  | 1R      |
| Return false                                 | 1R      |


## 3. Efficiency Analysis of US14

User Story 14 requires that, given a railway network (represented as a graph with stations and railway lines), the system should verify whether it is possible to find a maintenance route that traverses each railway line exactly once.

This corresponds to detecting the existence of an Eulerian Path (or Eulerian Circuit) in an undirected graph.

An Eulerian Path exists in a connected undirected graph if:

- Exactly two vertices have an odd degree (start and end points);
- All vertices have an even degree, in which case an Eulerian Circuit is possible (start and end at the same station).

If the graph is disconnected, or has more than two vertices with odd degree, no Eulerian path is possible.

#### 3.1 Graph Representation

The railway network is modeled as an undirected graph \( G = (V, E) \), where:
- \( V \): set of vertices (stations)
- \( E \): set of edges (railway lines), which can be electrified or non-electrified.

Depending on the user's selection, the algorithm may consider:

- All the railway lines (Electrified ones and non-Electrified);
- Only the electrified ones.

#### 3.2 Worst-Case Time complexity

The algorithm performs the next following steps:

1. Graph construction - `O(E)`: each edge is added to the adjacency list of both connected vertices;
2. Connectivity using DFS - `O(V + E)`: verifies that all non-isolated vertices belong to a single connected component;
3. Counting odd-degree vertices - `O(V)`: iterates through each vertex to compute its degree.

Then, the worst-case time complexity is:

`O(V + E)`

#### 3.3 Pseudocode

#### a) Connectivity check ('isConnected')

This function performs a depth-first search (DFS) starting from a vertex with neighbors, and verifies if all reachable vertices are part of the same connected component. Only non-isolated vertices are considered.

    Function isConnected(graph):
        visited ← empty set
        start ← any vertex in graph with neighbors
        DFS(start, graph, visited)
    For each vertex in graph:
        If vertex has neighbors and vertex not in visited:
            Return False
    Return True

| Pseudocode                                         | Type |
|----------------------------------------------------|------|
| visited <- empty set                               | 1A   |       
| start <- any vertex in graph with neighbors        | 1A   |       
| DFS                                                | nOP  |       
| For each vertex in graph:                          | nA   |       
| If vertex has neighbors and vertex not in visited: | nC   |      
| Return False                                       | 1R   |       
| Return True                                        | 1R   |       



#### b) Odd-degree Vertices Count ('countOddDegreeVertices')

This function iterates through all vertices in the graph and counts how many have an odd number of connections (degree). This is used to determine the feasibility of an Eulerian path or circuit.

    Function countOddDegreeVertices(graph):
        odd ← 0
        For each vertex in graph:
            If degree(vertex) mod 2 ≠ 0:
                odd ← odd + 1
    Return odd

| Pseudocode                  | Type |
|-----------------------------|------|
| odd <- 0                    | 1A   |        
| For each vertex in graph:   | nA   |       
| If degree(vertex) mod 2 ≠ 0 | nC   |      
| odd <- odd + 1              | nOP  |       
| Return odd                  | 1R   |       


#### c) Eulerian Path Verification ('hasEulerianPath')

This function combines the two previous ones. It first checks if the graph is connected. If it is, it analyzes the number of odd-degree vertices to determine whether an Eulerian circuit or path exists.

    Function hasEulerianPath(graph):
        If not isConnected(graph):
        Return "No Eulerian path – graph is disconnected"
    odd ← countOddDegreeVertices(graph)
    If odd == 0:
        Return "Eulerian circuit possible"
    Else if odd == 2:
        Return "Eulerian path possible"
    Else:
        Return "No Eulerian path – more than 2 vertices with odd degree"

| Pseudocode                                                        | Type |
|-------------------------------------------------------------------|------|
| If not isConnected(graph):                                        | 1C   |        
| Return "No Eulerian path - graph is disconnected"                 | 1R   |       
| odd <- countOddDegreeVertices(graph)                              | 1A   |      
| If odd == 0                                                       | 1C   |       
| Return "Eulerian circuit possible"                                | 1R   |
| Else if odd == 2:                                                 | 1C   |        
| Return "Eulerian path possible"                                   | 1R   |       
| Else:                                                             | -    |      
| Return "No Eulerian path - more than 2 vertices with odd degree"  | 1R   |       



## 4. Summary Table

| Algorithm                        | Description                           | Time Complexity |
|----------------------------------|---------------------------------------|-----------------|
| Graph construction (US13 & US14) | Create adjacency list from edge list  | O(E)            |
| DFS traversal (US13 & US14)      | Explore network to check connectivity | O(V + E)        |
| Odd-degree count (US14)          | Count vertices with odd degrees       | O(V)            |
| **Overall (US13 & US14)**        | -                                     | **O(V + E)**    |






