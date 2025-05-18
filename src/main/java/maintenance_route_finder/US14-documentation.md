# US14 - Verification if a maintenance route visiting each railway line exactly once is possible

## 1. Introduction
This User Story is responsible for determining whether it is possible to perform a maintenance route through the railway network that 
passes once and only once through each railway line (Eulerian path), 
considering all lines or only the electrified ones.
It also generates a graphical representation of the filtered rail network, saved as an .svg file.

## 2. Methods used in the Implementation

### 2.1. buildGraph()
This method constructs an undirected graph from the filtered list of edges (lines). 
Each station is represented as a node, and each railway connection as an edge. Electrification status is preserved to allow filtering.

### 2.2. dfsVisit()
A depth-first search traversal method used to check whether the graph is connected (i.e., all stations with at least one line are reachable from a starting station).

### 2.3. main()
This method coordinates the entire logic flow of the User Story. It:

Asks for the directory and selects the correct .csv file.

Parses the file and builds the graph.

Prompts the user for the type of maintenance (all lines or only electrified).

Filters the data accordingly.

Checks graph connectivity and Eulerian conditions (0 or 2 odd-degree vertices).

Informs the user whether a route is possible.

Builds and saves a Graphviz SVG file of the network in the /Graphs folder with a timestamp.


## 3. Execution Flow
* **Step 1:** Ask the user to input the folder containing the .csv files.
* **Step 2:** List available files and ask the user to choose one (with lines data).
* **Step 3:** Read and parse the connections from the selected .csv.
* **Step 4:** Ask the user whether to consider all lines or only electrified ones.
* **Step 5:** Filter the data according to the electrification selection.
* **Step 6:** Construct a graph and verify connectivity using dfsVisit().
* **Step 7:** Count the number of stations with odd degrees.
* **Step 8:** If the graph is disconnected → inform the user: no route possible. If the graph has 0 or 2 odd-degree vertices → inform the user: route is possible. If more than 2 odd-degree vertices → no Eulerian path possible.
* **Step 9:** Build and export a .svg image representing the filtered rail network to src/main/java/maintenance_route_finder/Graphs.
