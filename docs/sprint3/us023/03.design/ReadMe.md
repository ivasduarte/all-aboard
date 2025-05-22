# US023 - Save Simulation in File

## 3. Design

### 3.1. Rationale

**The rationale grounds on the SSD interactions and the identified input/output data.**

| Interaction ID | Question: Which class is responsible for...      | Answer                   | Justification (with patterns)                                                                               |
|:---------------|:-------------------------------------------------|:-------------------------|:------------------------------------------------------------------------------------------------------------|
| Step 1         | ... asking to save a simulation in file?         | SaveSimulationUI         | **Pure Fabrication**: UI initiation is not a responsibility of any domain class. It fabricates interaction. |
|                | ... coordinating the US?                         | SaveSimulationController | **Controller**: Handles system operations initiated by the user (Player).                                   |
|                | ... instantiating the class that handles the UI? | SaveSimulationUI         | **Pure Fabrication**: No domain object is responsible for UI instantiation.                                 |
| Step 2         | ... saving the simulation to a file?             | SimulationRepository     | **Information Expert**: Repository has the knowledge to save simulations to files.                          |
| Step 3         | ... showing the operation success?               | SaveSimulationUI         | **Pure Fabrication**: UI class presents success messages to the user.                                       |
| Step 4         | ... coordinating between UI and Domain for flow? | SaveSimulationController | **Controller**: Central coordinator for user-system interaction.                                            |

### Systematization ##

According to the taken rationale, the conceptual classes promoted to software classes are:

* Simulation
* SimulationRepository
* Player

Other software classes (i.e. Pure Fabrication) identified:

* SaveSimulationUI
* SaveSimulationController
* Repositories
* ApplicationSession
* UserSession

## 3.2. Sequence Diagram (SD)

### Full Diagram

This diagram shows the full sequence of interactions between the classes involved in the realization of this user story.

![Sequence Diagram - Full](svg/US006-SD-full.svg)

### Split Diagrams

The following diagram shows the same sequence of interactions between the classes involved in the realization of this
user story, but it is split in partial diagrams to better illustrate the interactions between the classes.

It uses Interaction Occurrence (a.k.a. Interaction Use).

![Sequence Diagram - split](svg/US006-SD-split.svg)

**Get Task Category List Partial SD**

![Sequence Diagram - Partial - Get Task Category List](svg/US006-SD-partial-get-task-category-list.svg)

**Get Task Category Object**

![Sequence Diagram - Partial - Get Task Category Object](svg/US006-SD-partial-get-task-category.svg)

**Get Employee**

![Sequence Diagram - Partial - Get Employee](svg/US006-SD-partial-get-employee.svg)

**Create Task**

![Sequence Diagram - Partial - Create Task](svg/US006-SD-partial-create-task.svg)

## 3.3. Class Diagram (CD)

![Class Diagram](svg/US006-CD.svg)