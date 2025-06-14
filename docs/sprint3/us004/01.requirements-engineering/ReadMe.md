# US004 - Create a scenario

## 1. Requirements Engineering

### 1.1. User Story Description

As an Editor, I want to create a scenario for a selected map.

### 1.2. Customer Specifications and Clarifications

**From the specifications document:**

> The Scenario Editor must allow the user to define the restrictions and conditions for using a given map:
> 
> 1. Time Restrictions
>    - Period in which the simulation will take place
>    - Affects available technologies and historical events
> 
> 2. Technological Restrictions
>    - Available locomotives (steam, diesel, electric)
>    - Available industries and their capabilities
>    - Infrastructure limitations
> 
> 3. Historical Restrictions
>    - Historical events affecting simulation conditions
>    - Examples:
>      * Wars (increased steel demand)
>      * Vaccination campaigns (increased mail traffic)
>      * Schooling programs (increased passenger traffic)
>    - Port import/export configurations

**From the client clarifications:**

> **Question:** When we are inserting the dependencies for each US, do we have to refer to the indirect ones or only the direct ones?
> **Answer:** Direct dependencies only.

> **Question:** Regarding the generating factors, just to clarify, ports can either supply (exports), demand (imports) or both (transforms)?
> **Answer:** Ports can export, import and/or transform.

> **Question:** In acceptance criterion 3, it talks about "the factors that alter the generation (frequency) of 
> generating industries," what exactly are these factors? Are we talking about the rate at which these industries produce resources? Or is it something else? 
> Also, are we supposed to give the editor the freedom to edit the generation rate of each generating industry or is it one rate for all the generating industries?
> *Answer:** The generation rates of the industries can be defined in a configuration file, it is not expected that there will be any US to edit them.
Each entry must define the industry and the number of cargos generated per unit of time like year or month.
(e.g., CoalMine, 4) A factor allows you to change this rate as a percentage. (e.g., CoalMine, 50%) meaning that the coal mine will now produce 6 cargos per unit of time.

> **Question:** Are the rates of production and quantity produce from industries decided in the scenario's specifications?
> **Answer:** The rates are predefined values but can be redefined in the scenario.


### 1.3. Acceptance Criteria

> **AC1:** Definition of port behavior, including cargo import/export/transformation rules.
> **AC2:** Definition of available locomotion types (steam, diesel, and/or electric).
> **AC3:** Definition of factors affecting resource generation in industries.
> **AC4:** The system must allow the Editor to create a scenario for a selected map.
> **AC5:** A scenario cannot be created without an existing map.
> **AC6:** The scenario must require all restriction types (time, historical, technological).
> **AC7:** Each restriction type must include valid inputs (e.g., Time Restrictions must include valid dates within an acceptable range).
> **AC8:** Each scenario must have a unique name (avoid duplicates), and prevent special characters and empty values in the name.
> **AC9:** Each scenario must be linked to exactly one map.
> **AC10:** Upon successful creation, the system must display a confirmation message.
> **AC11:** If creation fails, the system must provide a descriptive error message indicating the cause.
> **AC12:** Only users with "Editor" privileges can create, modify, and manage scenarios.
> **AC13:** Each scenario must have an associated budget.
> **AC14:** Each scenario must track its current year and final year.
> **AC15:** Each scenario must have a modification rate and production rate.

### 1.4. Found out Dependencies

* US001—"Create map": A map must exist before creating a scenario.

### 1.5 Input and Output Data

**Input Data:**

*Typed data:*

- Scenario name;
- Scenario start and end date;
- Scenario restrictions;
- Scenario's port behaviors.

*Selected data:*
- Map;

**Output Data:**

*Success Response:*
- Success message

*Error Response:*
- Error message 

### 1.6. Output Data Messages Assumptions

> **Acceptance Message:**
> Scenario created successfully.
> 
> **Possible Error Cases:**
>
> - *Map not selected:*
>   ERROR: Must select a map for scenario.
>
> - *Invalid restrictions:*
>   ERROR: Restrictions are invalid. Please enter again.
>
> - *Duplicate name:*
>   ERROR: Scenario name already exists.
>
> - *Invalid name:*
>   ERROR: Invalid name. Must be under 15 characters, have at least one upper case character, and cannot be null.
>
> - *Invalid budget:*
>   ERROR: Budget cannot be null or negative.

### 1.7. System Sequence Diagram (SSD)

**Acceptance Sequence Diagram:**

![System Sequence Diagram](svg\us004-system-sequence-diagram-acceptance.svg)

**Map not selected:**

![System Sequence Diagram](svg\us004-system-sequence-diagram-map.svg)

**Invalid Restrictions:**

![System Sequence Diagram](svg\us004-system-sequence-diagram-invalid-restrictions.svg)

**Duplicate Name:**

![System Sequence Diagram](svg\us004-system-sequence-diagram-name.svg)

**Invalid name:**

![System Sequence Diagram](svg\us004-system-sequence-diagram-invalid-name.svg)

### 1.8 Other Relevant Remarks

* The system should validate that historical events are compatible with the time period.
* Technological restrictions should be validated against the time period.
* Port configurations should be validated against available industries and resources.
* The system should maintain a list of valid scenario names to ensure uniqueness.
* Scenario creation should consider future expansion possibilities.
* The budget system should allow for additions and subtractions during the simulation.
* The modification rate and production rate should be configurable for each scenario.
* The system should track the current year and final year of the simulation.



