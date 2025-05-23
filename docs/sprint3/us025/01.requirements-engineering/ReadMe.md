# US025 - See the Year Financial Results including Expenses

## 1. Requirements Engineering

### 1.1. User Story Description

As a Player, I want to see the Year Financial Results including Expenses (Track Maintenance, Train Maintenance and Fuel)
and Revenues (Cargo Deliveries).

### 1.2. Customer Specifications and Clarifications

**From the specifications document:**

> Values, such as, locomotive costs and cargoes deliver values should be checked in the game documentation
> previously referred to.
> 
> The demand for a specific type of load at a station can vary between 1 and 9. If there is no demand, the value will
> be 0. For example, if the demand is 5, the delivery of a load is paid for at the base value and will be modified by 
> 20 percent for each value that goes up or down. For example, for a demand of 8, the load value will be paid at the
> Base value + 60% and for a demand of 3 the load value will be paid at the Base value - 40%. If 30 cargoes, or more,
> of a specific type are delivered in a station per year, the demand will drop 1 value. If 10 cargoes, or less, of a
> specific type are delivered in a station per year, the demand will raise 1 value.

**From the client clarifications:**

> **Question:**
>
> **Answer:**

### 1.3. Acceptance Criteria

* **AC01:** Track Maintenance is based on kms of line; the train maintenance is derived from locomotive properties
and Fuel is based on traveled kms.

### 1.4. Found out Dependencies

* There is a dependency with US008 - "Build a railway station between two stations", given that to get the maintenance
cost of the tracks there needs to be railway lines.
* There is a dependency with US009 - "Buy a locomotive", give that to get maintenance and fuel cost of a train we need
to know the locomotive model of the train.

### 1.5 Input and Output Data

**Output Data:**
* **Financial Results:**
    - A message showing how much the player spent during the year on track and train maintenance, train fuel cost and
    the profits of revenues.

### 1.6. System Sequence Diagram (SSD)

![System Sequence Diagram](svg/.svg)

### 1.7 Other Relevant Remarks

* No other relevant remarks 