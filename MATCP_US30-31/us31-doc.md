## US31 – Revenue Prediction Based on Cargo Type (Simple Linear Regression)

### User Story Details
As a Player, using the same data file as in the previous Sprint  (Sprint 2), I want to perform a statistical analysis to determine which type of cargo contributes most significantly to the revenue of a given station, in a specific year, both defined by the user. The goal for the specified station/year is to identify the cargo (independent variable) that shows the highest correlation with revenue (dependent variable). Based on the selected cargo, a linear regression model should be applied, presenting the error associated with the respective fit. Assuming the amount of cargo increased by 10% in January of the following year, estimate the expected revenue for January with a 95% confidence level.

AC01: Programming Language: Python

AC02: Development environment: Jupyter Notebook

AC03: Work delivery format: A single Jupyter Notebook file, containing all the work carried out for all US.

AC04: Documented in Markdown format, for each US, you must present:
1. Detailed description of the objectives and theoretical concepts on which the answers were based.
2. Thorough analysis and interpretation of the obtained results, extrapolated to the PI topic.

---

### Objective
To identify the cargo type that most strongly correlates with the revenue of a specific railway station in a given year. Once identified, apply a simple linear regression model to that cargo type and estimate the revenue for January of the following year assuming a 10% increase in that cargo. The result must include a 95% prediction interval.

---

### Theoretical Concepts

This analysis is based on **Simple Linear Regression**, as covered in Chapter 6 of *Matemática Computacional*.

- The regression model has the form:
-
$$
E(Y \mid x) = \beta_0 + \beta_1 x
$$

where:
- \( Y \) is the revenue (dependent variable)
- \( X \) is the quantity of a cargo type (independent variable)
- \( beta_0 \), \( beta_1 \) are the regression coefficients
- \( E \) is the random error term


- The **Pearson correlation coefficient** \( r \) is used to measure the strength and direction of the linear relationship between each cargo type and the revenue.

- The **least squares method** is used to estimate the regression parameters \( beta_0 \) and \( beta_1 \), minimizing the sum of squared residuals:

$$
\text{SQE} = \sum_{i=1}^{n} (y_i - \hat{y}_i)^2 = S_{yy} - \frac{S_{xy}^2}{S_{xx}}
$$


- A **95% prediction interval** is calculated for a future observation \( Y_0 \), using the following formula:

$$
\hat{y}_p \pm t_{1 - \frac{\alpha}{2}} \cdot s \cdot \sqrt{\frac{1}{n} + \frac{(x_p - \bar{x})^2}{S_{xx}}}
$$


where:
- \( s² \) is the estimated variance of residuals
- \( t_{1-\alpha/2} \) is the critical value from the t-distribution
- $$
  S_{xx} = \sum_{i=1}^{n} (x_i - \bar{x})^2
  $$

---

### Results and Interpretation

- **Best cargo type**: `{best_cargo}`
- **Correlation coefficient**: `{correlations[best_cargo]:.4f}`
- **Regression equation**:
  `y = {beta0:.2f} + {beta1:.2f}x`

- **January prediction with 10% increase**:
    - Estimated revenue: `{y_hat:.2f}`
    - 95% prediction interval: [`{lower:.2f}`, `{upper:.2f}`]

---

### Interpretation in the PI context

- The correlation analysis reveals the cargo type with the highest absolute correlation coefficient, representing the strongest revenue predictor. This relationship suggests either direct revenue impact or shared market influences. The linear regression model quantifies this relationship: the slope (β₁) represents revenue increase per cargo unit, while the intercept (β₀) indicates baseline revenue.
  The forecasting component uses historical January data as baseline, applying a realistic 10% increase scenario. The 95% confidence interval accounts for both model uncertainty and natural variation, providing a practical range for business planning.

- **Business Implications:** The analysis supports strategic decisions by identifying which cargo type to prioritize for resource allocation and capacity planning. However, the single-variable linear model has limitations - real revenue patterns may be non-linear and influenced by multiple factors not captured in this analysis.