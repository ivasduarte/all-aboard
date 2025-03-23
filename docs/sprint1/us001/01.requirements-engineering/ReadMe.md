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
> **Answer:** The map must have 
> **Question:** Are there specific formats required (square, rectangular), or can any dimensions be used?
> **Answer:**
>
>
> **Question:** Are there any naming conventions for maps (no special characters, character limit)?
> **Answer:**
>
>
> **Question:** Will there be predefined maps, or will all maps be created by editors?
> **Answer:**
> 
> **Question:** Should maps be stored in a specific format?
> **Answer:** 


### 1.3. Acceptance Criteria

> The system must allow the Editor to create a map by providing a name and size.
> The map name must be unique and cannot contain special characters or be left empy.
> The map size must be within a predefined range.
> Upon successful creation, the system mjust display a confirmation message.
> If creation fails, the system must provide a descriptive error message indicating the cause.

### 1.4. Found out Dependencies

### 1.5 Input and Output Data

**Input Data:**

- Size
- Name

**Output Data:**

- Message of success indicating that the city has been added


### 1.6. System Sequence Diagram (SSD)

_Insert here a SSD depicting the envisioned Actor-System interactions and throughout which data is inputted and outputted to fulfill the requirement. All interactions must be numbered._

![System Sequence Diagram](svg/us001-system-sequence-diagram.svg)

### 1.7 Other Relevant Remarks
