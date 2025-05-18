# US13 - Verification if a specific train can travel between two stations belonging to the rail network

## 1. Introduction
This User Story will be responsible to give the player insight whether a train can, or not, travel between two
different stations, given a specific locomotive type, the departure station and the station of arrival.


## 2. Methods used in the Implementation

### 2.1. askFileName() 
This method is responsible to collect the name of the .csv files that will have the data to proceed with the
analysis, verifying if what's being inputted is a file of the correct format.

### 2.2. getScenarioLines()
This method is responsible to collect the data present in the file given.

### 2.3. buildGraph()
This method will be responsible to build the graph that will after be used to check the existing lines connecting 
different stations. 

### 2.4. isConnected()
This method is responsible to verify the connectivity existent between two stations.

### 2.5. us13Implementation()
This method is responsible for making the operation work successfully. It gets together every other method and
displays a message based on the result, as well as, a graphical representation of the built graph.


## 3. Execution Flow
* **Step 1:** Get the name of the file that will have all the data needed.
* **Step 2:** Get the locomotive type, the departure station and the arrival station.
* **Step 3:** Sort the data present on the file.
* **Step 4:** Create a graph of the scenario, with all the connections between stations.
* **Step 5:** Verify if the two stations are connected.
* **Step 6:** Return to the player a message of the connectivity of the two stations and graphical represented graph of the scenario.