# US26 - Efficiency of the Algorithms Developed in US13 and US14

## 1. Introduction

This document addresses User Story 26 (US26), which requires an analysis of the efficiency of the algorithms implemented in **US13** and **US14** of the project.

Only algorithms developed using **primitive operations**, without relying on Java’s built-in graph-related libraries, are considered in this analysis. This includes procedures such as graph construction, depth-first search (DFS), and the analysis of node degrees to determine the possibility of an Eulerian path.

> **AC01**: The analysis report of the worst-case time complexity of the algorithms should be documented/detailed in the repository documentation(using markdown format). All the algorithms should be presented in pseudocode, where the complexity analysis should be made.

> **AC02**: As required by acceptance criterion AC02, all procedures related to **graphical visualization** (e.g., rendering with Graphviz) are **excluded from this analysis**, as they are handled by external libraries and not part of the algorithmic logic implemented by the developer.


## 2. Efficiency Analysis of US13

User Story 13 requires checking whether a specific train can travel between two different stations belonging to the rail network.

#### 2.1 Graph Representation

The railway network is modeled as an undirected graph \( G = (V, E) \), where:
- \( V \): set of vertices (stations)
- \( E \): set of edges (railway lines)

Each edge \( V \) indicates a line between two stations, which each one represents a vertice \( V \), with a different color representing whether it's a electrified line or not.

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