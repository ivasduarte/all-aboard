# US27 - Find the shortest route based on a list given by the player

## 1. Introduction
This User Story will be responsible to give the player the shortest route possible, based on ordered list of 
stations he chooses. If a route is possible the player will get a presentation of the scenario, where the shortest
route will be in a different color and the selected stations will the outlined.

## 2. Methods used in the Implementation

### 2.1. main()
This method is responsible to control the full execution flow. It:

- Asks the user to input the path to the folder containing CSV files.
- Lists all CSV files in the folder and lets the user choose one.
- Reads and filters the CSV data accordingly.
- Builds the graph by mapping each station to its connected neighbors.
- Asks the player the stations that must be included in the route.
- Verifies if the specific route is possible or not.
- Returns to the player the path that was found, as well as the total distance of it.
- Generates a ".dot" file representing the graph and converts it into an ".svg" file using Graphviz.

### 2.2. dfs()
This method performs a depth-first search starting at the given node. It recursively visits all reachable stations,
marking them as visited in the "visited" set. It is used to check whether the graph is connected.

### 2.3. shortestPath()
This method is responsible to calculate the shortest route possible, considering the stations given by the player.

## 3. Execution Flow
* **Step 1:** Ask the user to input the folder containing the ".csv" files.
* **Step 2:** List available ".csv" files and ask the user to choose one (containing railway lines).
* **Step 3:** Read and parse the connections from the selected ".csv".
* **Step 4:** Asks the player the stations the train must pass.
* **Step 5:** Construct a graph where each station is a node and each line is an undirected edge.
* **Step 6:** Perform a connectivity check using "dfs()", starting from the first connected station.
* **Step 7:** Verifies if there's at least one possible route passing by all stations given to the player.
* **Step 8:** Finds the shortest route possible.
* **Step 9:** Build and export a ".svg" image ("shortestRoute.svg") representing the shortest route found.
The lines present in the route will be presented with a different color from the rest.