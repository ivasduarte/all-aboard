# US14 - Maintenance Route Visiting Each Railway Line Exactly Once

## 1. Introduction
This User Story determines whether it is possible to carry out a maintenance route across the railway network, traversing each railway line exactly once (Eulerian Path), 
either considering all lines or only electrified ones.
If such a route is possible, the system suggests a starting station. 
A graphical representation of the selected rail network is also generated and saved as an .svg file using Graphviz.

## 2. Methods used in the Implementation

### 2.1. main()
This method controls the full execution flow. It:

- Asks the user to input the path to the folder containing CSV files.
- Lists all CSV files in the folder and lets the user choose one.
- Prompts the user to choose between maintaining all lines or only electrified lines.
- Reads and filters the CSV data accordingly.
- Builds the graph by mapping each station to its connected neighbors.
- Identifies all nodes with an odd number of connections.
- Performs a connectivity check using "dfs()".
- Determines whether a maintenance route is possible (Eulerian path).
- Suggests a starting station if a valid route exists.
- Generates a ".dot" file representing the graph and converts it into an ".svg" file using Graphviz.

### 2.2. dfs(String node, Map<String, List<String>> graph, Set<String> visited)
This method performs a depth-first search starting at the given node. It recursively visits all reachable stations, marking them as visited in the "visited" set. It is used to check whether the graph is connected.

### 2.3. readAndFilterEdges(File selected, boolean onlyElectrified)
This block reads each line of the selected CSV file, splits it into parts, and filters based on the electrification column. If the user selected "only electrified", only lines with value "1" in the third column are added to the edge list.

### 2.4. buildGraph(List<String[]> edges)

For each valid edge, both stations are added to the graph if not already present. Then, each is added to the other’s adjacency list, since the graph is undirected.

### 2.5. countOddDegreeNodes(Map<String, List<String>> graph)
Iterates through all graph nodes and adds to a list those whose adjacency list size is odd. This list is used to determine whether an Eulerian path is possible (must be of size 0 or 2).

### 2.6. exportToGraphviz(List<String[]> edges)
This logic creates a ".dot" file containing all the edges and their color (yellow for electrified, red otherwise). It then invokes the "dot" command using a "ProcessBuilder" to export the file as "railway.svg", allowing the user to visually inspect the network.



## 3. Execution Flow
* **Step 1:** Ask the user to input the folder containing the ".csv" files.
* **Step 2:** List available ".csv" files and ask the user to choose one (containing railway lines).
* **Step 3:** Ask the user whether to consider all lines or only electrified ones.
* **Step 4:** Read and parse the connections from the selected ".csv", filtering based on the electrification choice.
* **Step 5:** Construct a graph where each station is a node and each line is an undirected edge.
* **Step 6:** Perform a connectivity check using "dfs()", starting from the first connected station.
* **Step 7:** Count the number of stations with an odd number of connections.
* **Step 8:** If the graph is disconnected → inform the user: no maintenance route possible.  
  If the graph has 0 or 2 odd-degree stations → inform the user: route is possible and suggest a starting station.  
  If more than 2 odd-degree stations → inform the user: no Eulerian path possible.
* **Step 9:** Build and export a ".svg" image ("railway.svg") representing the filtered rail network. Electrified lines appear in yellow; others in red.

