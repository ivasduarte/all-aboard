# US006 - Upgrade a selected station with a building

## 1. Requirements Engineering

### 1.1. User Story Description

As a Player, I want to upgrade a selected station with a building

### 1.2. Customer Specifications and Clarifications

**From the specifications document:**

> When built, a station consists of just one building, which can be upgraded
with other buildings that can increase the station’s performance, improve
train operation, facilitate train manoeuvrers, and increase the lifespan of
cargo stored at the station.
> Some of the buildings that can be used to upgrade stations are: telegraph (later telephone), café (small or large), customs, post
office, hotel (small or large), silo, liquid storage.
> Each type of station improvement has a date from which it is available.
> Some equipments are mutually exclusive and some equipments/buildings replace others (the telegraph was initially used to facilitate the operation of trains at stations, and was later
replaced by the telephone, so after the advent of telephone, telegraph
is no more available).

    
**From the client clarifications:**

> **Question:** Is there a limit on the number of buildings a station can be upgraded with?

> **Answer:** No.

> **Question:** Does the upgrade from telegraph to telephone occur automatically once a certain year is reached?

> **Answer:** There are no automatic updates; When a new building is available, the player can buy it and then the previous building is replaced. 

### 1.3. Acceptance Criteria

**AC1**: The player must select an existing station to upgrade.
**AC2**: The system must display only buildings available for the selected station type (Depot/Station/Terminal) and current scenario date (e.g., telegraph unavailable after telephone introduction).
**AC3**: Mutually exclusive buildings (e.g., small vs. grand hotel) cannot be added together.
**AC4**: The system must prevent adding duplicate buildings to the same station.

### 1.4. Found out Dependencies

* There is a dependency with US005 - "Build a Station", since in order to upgrade a station, it needs to be built.

### 1.5 Input and Output Data

**Input Data:**

- Map
- Station
- Building

**Output Data:**

- Display's the map, with all current stations
- Display's the station, with the buildings available
- Message of success indicating that the building has been added
- Message of failure indicating that the building hasn't been added.


### 1.6. System Sequence Diagram (SSD)

![System Sequence Diagram](svg/us006-system-sequence-diagram.svg)



### 1.7 Other Relevant Remarks

* The price list of the upgrades must be established in advance by the system, which the player can request the values of each building to add in the station.