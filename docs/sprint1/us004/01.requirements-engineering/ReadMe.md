# US004 - Create a scenario

## 1. Requirements Engineering

### 1.1. User Story Description

As an Editor, I want to create a scenario for a selected map.

### 1.2. Customer Specifications and Clarifications

**From the specifications document:**

> The Scenario Editor must allow the user to define the restrictions and
conditions for using a given map, namely:
• Time Restrictions - the period in which the simulation will take place;
• Technological Restrictions - for example, which locomotives or industries are available in the scenario;
• Historical Restrictions - historical events that make sense in the scenario and change the operating conditions of the simulator. For instance, a war increases the steel demand, and a vaccination campaign
or a schooling program encourages mail and/or passenger traffic. In
this context, the goods that the Ports import and export are also defined.
 

### 1.3. Acceptance Criteria


### 1.4. Found out Dependencies

* There is a dependency with US001 - "Create map", given that to create a scenario, there needs to be a map first.

### 1.5 Input and Output Data

**Input Data:**

- Time restrictions
- Technological restrictions
- Historical restrictions

**Output Data:**

- Message of success indicating that the city has been added


### 1.6. System Sequence Diagram (SSD)


![USXXX-SSD](svg/USXXX-SSD.svg)

### 1.7 Other Relevant Remarks
