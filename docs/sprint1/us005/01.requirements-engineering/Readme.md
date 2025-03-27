# US005 - Build a Station

## 1. Requirements Engineering

### 1.1. User Story Description

As a Player, I want to build a station.

### 1.2. Customer Specifications and Clarifications

**From the specifications document:**

> A station consists of just one building, which can be upgraded
with other buildings that can increase the station’s performance, improve
train operation, facilitate train manoeuvrers, and increase the lifespan of
cargo stored at the station.

**From the client clarifications:**

> 
### 1.3. Acceptance Criteria

* **AC1:** Overbuilding is not possible

### 1.4. Found out Dependencies

* There is a dependency with US001 - "Create map", given that to create a station, there needs to be a map first. 

### 1.5 Input and Output Data

**Input Data:**

- Station type
- Location
- Centre

**Output Data:**

- Message of success indicating that the station has been added 


### 1.6. System Sequence Diagram (SSD)

![System Sequence Diagram](svg\us005-system-sequence-diagram.svg)



### 1.7 Other Relevant Remarks

* 