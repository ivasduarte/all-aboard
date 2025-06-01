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
> 
> 
> **Question:** Should the map scale be user-specified or system retrieved? In case of the latter, are there any 
> guidelines in place to get that value?
> **Answer:** 
> 
> 
> **Question:**
Considering the map will need to accommodate multiple cities, industries and stations with their respective connections,
> would it be acceptable to establish a minimum map size like 20x20 or 50x50? A smaller size might not provide enough 
> space for meaningful simulation elements. Can we also suggest to the editor the default map size of 100x100 or 
> another one that you indicate to us?
> **Answer:** Yes, you can define a minimum and suggest a default size.



### 1.3. Acceptance Criteria

> **AC1:** The system must allow the Editor to create a map by providing a name and size.
> **AC2:** The map name must be unique and cannot contain special characters or be left empty.
> **AC3:** The map size must be within a predefined range.
> **AC4:** Upon successful creation, the system must display a confirmation message.
> **AC5** If creation fails, the system must provide a descriptive error message indicating the cause.
> **AC6:** Names of scenarios and maps must be different.
> **AC7:** Map name should be a valid file name.
> **AC8:** The maps should have a scale that states the size of the cell map in kms.

### 1.4. Found out Dependencies


### 1.5 Input and Output Data

**Input Data:**

*Type Data:*
- Map name;
- Map width;
- Map height;
- Cell block value in kms.


**Output Data:**

- Message of success indicating that the map has been created;
- Error message, in case of failed map creation, indicating failure cause.


### 1.6. Output Data Messages Assumptions

> **Possible Error Case:**
>
> - *Invalid name input:*
>
> ERROR. Invalid name.
> Name already exists. Please enter a different name:
> ERROR: Map name is too long. Max length is 15 characters.
> ERROR: Map name contains invalid characters.
>
>
> - *Invalid size input:*
> - 
> ERROR: Invalid width. Please enter a positive integer less than or equal to 256.
> ERROR: Invalid height. Please enter a positive integer less than or equal to 256.
> 
> 
> - *Invalid cell block input:*
> 
> ERROR: Invalid size. Please enter a positive integer.


### 1.7. System Sequence Diagram (SSD)

**Acceptance Sequence Diagram:**

![System Sequence Diagram](svg\us001-system-sequence-diagram-acceptance.svg)


**Invalid Name Input Sequence Diagram:**

![System Sequence Diagram](svg\us001-system-sequence-diagram-invalid-name.svg)

**Invalid Size Input Sequence Diagram:**

![System Sequence Diagram](svg\us001-system-sequence-diagram-invalid-size.svg)
