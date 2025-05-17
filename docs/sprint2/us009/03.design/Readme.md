# US009 - Buy a train

## 3. Design

### 3.1. Rationale

**The rationale grounds on the SSD interactions and the identified input/output data.**

| Interaction ID | Question: Which class is responsible for...                             | Answer                 | Justification (with patterns)                                                          |
|:---------------|:------------------------------------------------------------------------|:-----------------------|:---------------------------------------------------------------------------------------|
| Step 1         | ...requesting to buy a locomotive?                                      | BuyLocomotiveUI        | **Pure Fabrication**: UI layer handles initiating user interaction.                    |
| Step 2         | ...displaying list of available scenarios?                              | BuyLocomotiveUI        | **Pure Fabrication**: UI is responsible for presenting available options to the user.  |
| Step 3         | ...selecting a scenario?                                                | Player                 | **Role Assignment**: The actor (Player) makes the selection based on displayed data.   |
| Step 4         | ...displaying list of available locomotives?                             | BuyLocomotiveUI        | **Pure Fabrication**: UI is responsible for presenting available options to the user.  |
| Step 5         | ...selecting a locomotive?                                               | Player                 | **Role Assignment**: The actor makes the selection based on displayed data.            |
| Step 6         | ...processing the locomotive purchase?                                   | BuyLocomotiveController| **Controller**: Coordinates the purchase process.                                      |
| Step 7         | ...checking if the scenario has enough budget?                           | Scenario               | **Information Expert**: Scenario knows its budget information.                         |
| Step 8         | ...updating the scenario's budget?                                       | Scenario               | **Information Expert**: Scenario knows how to update its own budget.                   |
| Step 9         | ...storing the purchased locomotive?                                     | LocomotiveRepository   | **Information Expert**: Repository knows how to store locomotive data.                 |
| Step 10        | ...confirming the purchase?                                              | BuyLocomotiveUI        | **Pure Fabrication**: UI confirms to the player that the purchase was successful.      |


### Systematization ##

**According to the taken rationale, the conceptual classes promoted to software classes are:**

* Locomotive
* Scenario
* Budget
* Map

**Other software classes (i.e., Pure Fabrication) identified:**

* BuyLocomotiveUI
* BuyLocomotiveController
* LocomotiveRepository
* ScenarioRepository


## 3.2. Sequence Diagram (SD)

![](svg/us009-sequence-diagram-full.svg)

## 3.3. Class Diagram (CD)

![](svg/us009-class-diagram.svg)





