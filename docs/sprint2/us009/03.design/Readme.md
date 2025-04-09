# US009 - Buy a train

## 3. Design

### 3.1. Rationale

**The rationale grounds on the SSD interactions and the identified input/output data.**

| Interaction ID | Question: Which class is responsible for...                             | Answer             | Justification (with patterns)                                                          |
|:---------------|:------------------------------------------------------------------------|:-------------------|:---------------------------------------------------------------------------------------|
| Step 1         | ...requesting to buy a train?                                           | BuyTrainUI         | **Pure Fabrication**: UI layer handles initiating user interaction.                    |
| Step 2         | ...displaying list of available locomotives?                            | BuyTrainUI         | **Pure Fabrication**: UI is responsible for presenting available options to the user.  |
| Step 3         | ...selecting a locomotive?                                              | Player             | **Role Assignment**: The actor (Player) makes the selection based on displayed data.   |
| Step 4         | ...asking if they want to add another locomotive?                       | BuyTrainUI         | **Pure Fabrication**: UI prompts the user with further interaction options.            |
| Step 5         | ...selecting another locomotive (if user chooses to)?                   | Player             | **Role Assignment**: The actor continues the selection process.                        |
| Step 6         | ...finalizing the selection?                                            | BuyTrainController | **Controller**: Coordinates the completion of the selection process.                   |
| Step 7         | ...confirming the selection?                                            | BuyTrainUI         | **Pure Fabrication**: UI confirms to the player that their selection is acknowledged.  |
| Step 8         | ...coordinating the whole flow (buy train use case)?                    | BuyTrainController | **Controller**: Orchestrates the interaction between UI, domain classes, and logic.    |
| Step 9         | ...showing purchase details (selected locomotives + current date)?      | BuyTrainUI         | **Pure Fabrication**: Responsible for displaying the final summary of the transaction. |
| Step 10        | ...storing the final train purchase or assembling the train from parts? | Train / TrainOrder | **Information Expert**: Domain class knows how to store/manage train composition data. |


### Systematization ##

**According to the taken rationale, the conceptual classes promoted to software classes are:**

* Train
* **Locomotive
* Player

**Other software classes (i.e., Pure Fabrication) identified:**

* BuyTrainUI
* BuyTrainController


## 3.2. Sequence Diagram (SD)

![](svg/us009-sequence-diagram-full.svg)

## 3.3. Class Diagram (CD)

![](svg/us009-class-diagram.svg)




