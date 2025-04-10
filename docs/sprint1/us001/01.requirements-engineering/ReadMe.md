# US001 - Create a map

## 1. Requirements Engineering

### 1.1. User Story Description

As an Editor, I want to create a map with a size and a name.

### 1.2. Customer Specifications and Clarifications

**From the document specifications:**

> The Map Editor allows you to create rectangular maps and place static elements on the map. 
> Elements can be cities or industries.


**From the client clarifications:**

> **Question:** The map's size must be infinite or should it have a limit?
> **Answer:** The map should have a maximum size limit.
> 
>
> **Question:** Are there specific formats required (square, rectangular), or can any dimensions be used?
> **Answer:** It's up to the editor to decide.
>
>
> **Question:** Are there any naming conventions for maps (no special characters, character limit)?
> **Answer:** File name like restrictions.
>
> 
> **Question:** Can the map size be a real number, or should it only be an integer?
> **Answer:** The map is a set of cells, similar to a spreadsheet. The coordinates are integers (row and column, 
> or alternatively X and Y).
> 
> 
> **Question:** Is there a predefined list of sizes, or should users be able to input custom dimensions?
> **Answer:** Custom dimensions but suggesting predefined sizes could be a good idea.
> 
> 
> **Question:** Should map names be unique within the system?
> **Answer:** Yes.


### 1.3. Acceptance Criteria

> **AC1:** The system must allow the Editor to create a map by providing a name and size.
> **AC2:** The map name must be unique and cannot contain special characters or be left empy.
> **AC3:** The map size must be within a predefined range.
> **AC4:** Upon successful creation, the system must display a confirmation message.
> **AC5** If creation fails, the system must provide a descriptive error message indicating the cause.
> **AC6:** Names of scenarios and maps must be different.

### 1.4. Found out Dependencies


### 1.5 Input and Output Data

**Input Data:**

- Size
- Name

**Output Data:**

- Message of success indicating that the city has been added
- Error message, in case of failed map creation, indicating failure cause.


### 1.6. System Sequence Diagram (SSD)

_Insert here a SSD depicting the envisioned Actor-System interactions and throughout which data is inputted and outputted to fulfill the requirement. All interactions must be numbered._

![System Sequence Diagram](svg\us001-system-sequence-diagram.svg)

### 1.7 Other Relevant Remarks
