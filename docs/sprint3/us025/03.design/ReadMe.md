# US025 - See the Year Financial Results including Expenses

## 3. Design

### 3.1. Rationale

| Interaction ID | Question: Which class is responsible for...      | Answer                         | Justification (with patterns)                                                                               |
|:---------------|:-------------------------------------------------|:-------------------------------|:------------------------------------------------------------------------------------------------------------|
| Step 1         | ... interacting with the actor?                  | ShowFinancialResultsUI         | **Pure Fabrication**: UI initiation is not a responsibility of any domain class. It fabricates interaction. |
|                | ... coordinating the US?                         | ShowFinancialResultsController | **Controller**: Handles system operations initiated by the user (Player).                                   |
|                | ... instantiating the class that handles UI?     | ShowFinancialResultsUI         | **Pure Fabrication**: No domain object is responsible for UI instantiation.                                 |
| Step 2         | ... requesting the year from the user?           | ShowFinancialResultsController | **Controller**: Responsible for initiating communication to get user input.                                 |
| Step 3         | ... providing the data?                          | Player                         | **Role Assignment**: The actor (Player) provides input data as requested.                                   |
| Step 4         | ... validating and processing the provided data? | ShowFinancialResultsController | **Controller**: Takes user input, processes or delegates it further.                                        |
| Step 5         | ... saving the yearly data?                      | FinancialResultsRepository     | **Information Expert**: Specialized class for data persistence.                                             |
| Step 6         | ... displaying financial results?                | ShowFinancialResultsUI         | **Pure Fabrication**: UI handles output/display logic.                                                      |

### Systematization ##

According to the taken rationale, the conceptual classes promoted to software classes are:

* FinancialResults
* FinancialResultsRepository
* Player

Other software classes (i.e. Pure Fabrication) identified:

* ShowFinancialResultsUI
* ShowFinancialResultsController
* Repositories
* ApplicationSession
* UserSession

## 3.2. Sequence Diagram (SD)

![Sequence Diagram - Full](svg/US006-SD-full.svg)

## 3.3. Class Diagram (CD)

![Class Diagram](svg/US006-CD.svg)