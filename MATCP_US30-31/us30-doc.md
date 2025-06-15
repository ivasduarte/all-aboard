# US30 - Find the Best fitting curve

## 1. Requirements Engineering

### 1.1. User Story Description

As a Product Owner, I want to obtain the best-fitting curve for the previous sample, and see it graphically.

### 1.2. Acceptance Criteria

* **AC01**: The fitted curve should align with the reference function obtained from the theoretical analysis of the procedure’s complexity.
* **AC02**: Programming Language: Python
* **AC03**: Development environment: Jupyter Notebook
* **AC04**: Work delivery format: A single Jupyter Notebook file, containing all the work carried out for all US.
* **AC05**: Documented in Markdown format, for each US, you must present: Detailed description of the objectives and theoretical concepts on
     which the answers were based. Thorough analysis and interpretation of the obtained results, extrapolated to the PI topic.

### 1.3. Input and Output data

**Input Data:**

- CSV file with results from US29 regarding US13
- CSV file with results from US29 regarding US14

**Output Data:**

- Best fitting curve graph for US13 results
- Correlation Coefficient ($r^2$) of US13 results
- Residual Sum of Squares (RSS) of US13 results
- Best fitting curve graph for US14 results
- Correlation Coefficient ($r^2$) of US14 results
- Residual Sum of Squares (RSS) of US14 results


## 2. Objectives

This user story has the objective of gathering the results of US29 from two separate files and create a graph with the values given in the files. Then find the best fitting curve using diverse functions such as Linear function, Quadratic function and Cubic function, among others.
By obtaining all curves, the correlation coefficient ($r^2$) is calculated to determine the best fitting curve, and the curve with the highest $r^2$ is displayed and given the title of "best fitting curve".

## 3. Theoretical Concepts

The theoretical concepts used in this user story are the ones from the contents of Chapter 6 of MATCP - Simple Linear Regression

Regression model:

$$E(Y|x) = \beta_0 + \beta_1x$$

where:

- Y - Time
- $x$ - Number of edges
- $\beta_0$ and $\beta_1$ - Regression Coefficients
- E - Random component or Error

The Correlation Coefficient ($r^2$) is a measure of direction and degree in which two variables associate linearly.
It is calculated like this:

$$ r^2 = (\frac{S_{xy}}{\sqrt{S_{xx} \times S_{yy}}}) ^2 $$

where:

- $S_{xx} = \sum_{i=1}^{n} (x_i - \bar{x})^2 $
- $S_{yy} = \sum_{i=1}^{n} (y_i - \bar{y})^2 $
- $S_{xy} = \sum_{i=1}^{n} (x_i - \bar{x})(y_i - \bar{y})$
- $\bar{x}$ - mean of all the $x$ values from the file
- $\bar{y}$ - mean of all the $y$ values from the file

Residual Sum of Squares:

$$ \text{SQE} = \sum_{i=1}^{n} (y_i - \hat{y}_i)^2 = S_{yy} - \frac{S_{xy}^2}{S_{xx}} $$

## 4. Results Interpretation

For each user story (13 and 14) it is presented the following results:

- **Best fitting curve function**: The name of the function whose curve fits the input data best (eg. Linear $O(n)$, Quadratic $O(n^2)$, Cubic $O(n^3)$, Logarithmic $O(log(n))$) and its respective curve.
- **Correlation Coefficient $(r^2)$**: The highest $r^2$ value out of all the tested function, i.e, the closest to 1.
- **Residual Sum of Squares (RSS)**: The RSS value of the best fitting curve function.