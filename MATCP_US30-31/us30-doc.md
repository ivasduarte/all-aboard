# US30 - Obtain Best-Fitting Curve and Observe It Graphically

## User Story Details

US30- As a Product Owner, I want to obtain the best-fitting curve for the previous sample, and see it graphically.

AC01: The fitted curve should align with the reference function obtained from the theoretical analysis of the procedure’s complexity.

---

## Objective

To analyzes and compare the performance of two algorithms — **US13** and **US14** — using regression techniques. The goal is to estimate the best-fitting time complexity model for each algorithm based on experimental data.

---

## Data Description

- Source files:
    - `us13_results_us30.csv`
    - `us14_results_us30.csv`
- Each dataset includes:
    - `size`: Number of edges in a graph
    - `time`: Average time (ms) taken by the algorithm

---

### Theoretical Concepts

This analysis is based on **Simple Linear Regression**, as presented in Chapter 6 of *Matemática Computacional* (ISEP).

- The regression model is given by:

$$
E(Y \mid x) = \beta_0 + \beta_1 x
$$

where:
- \( Y \): average runtime (dependent variable)
- \( x \): number of edges (independent variable)
- \( \beta_0, \beta_1 \): regression coefficients
- \( E \): random error term

---

- The **least squares method** estimates the coefficients by minimizing the sum of squared residuals:

$$
\text{SQE} = \sum_{i=1}^{n} (y_i - \hat{y}_i)^2 = S_{yy} - \frac{S_{xy}^2}{S_{xx}}
$$

with:
- \( S_{xx} = \sum_{i=1}^n (x_i - \bar{x})^2 \)
- \( S_{xy} = \sum_{i=1}^n (x_i - \bar{x})(y_i - \bar{y}) \)

---

- The **coefficient of determination** \( R^2 \) measures the goodness of fit:

$$
R^2 = 1 - \frac{\text{SS}_{\text{res}}}{\text{SS}_{\text{tot}}}
$$

Where:
- \( \text{SS}_{\text{res}} = \sum (y_i - \hat{y}_i)^2 \): residual sum of squares
- \( \text{SS}_{\text{tot}} = \sum (y_i - \bar{y})^2 \): total variation

---

- Each algorithm is evaluated against common complexity classes:
    - \( O(n) \)
    - \( O(\log n) \)
    - \( O(n \log n) \)
    - \( O(n^2) \)
    - \( O(n^3) \)

For each class, we fit a transformed model and choose the best according to the highest \( R^2 \) score.

---

### Results and Interpretation

#### US13

- **Best-fitting model**: e.g., `O(n log n)`
- **Regression equation**:

  $$
  \hat{y} = \beta_0 + \beta_1 \cdot (x \log x)
  $$

- **Coefficient of determination**:

  $$
  R^2 = 0.98 \quad \text{(example)}
  $$

- **Interpretation**:
  The runtime of US13 grows nearly linearithmically, suggesting efficiency and possibly use of sorting or divide-and-conquer strategies.

---

#### US14

- **Best-fitting model**: e.g., `O(n^2)`
- **Regression equation**:

  $$
  \hat{y} = \beta_0 + \beta_1 \cdot x^2
  $$

- **Coefficient of determination**:

  $$
  R^2 = 0.95 \quad \text{(example)}
  $$

- **Interpretation**:
  US14 has a quadratic growth pattern, likely reflecting nested iterations or less efficient graph traversal.

---

### Interpretation in the Computational Performance Context

This regression-based complexity analysis enables:

- Comparing different algorithms empirically
- Estimating computational growth for larger graphs
- Making informed decisions about performance trade-offs

The model helps identify which algorithm scales better and supports future planning or optimization in systems relying on graph-based processing.