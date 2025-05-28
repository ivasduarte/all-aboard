package repositories;

import domain.FinancialResults;

import java.util.ArrayList;
import java.util.List;

public class FinancialResultsRepository {
    private List<FinancialResults> financial = new ArrayList<>();

    /**
     * This method is responsible to save the financial results from a specific year on the FinancialResultsRepository.
     *
     * @param financialResults financial results of a specific year
     */
    public void save(FinancialResults financialResults) {
        financial.add(financialResults);
    }

    /**
     * This method allows to get the financial results listed by years.
     * It is then used to get the results from a year selected by the player.
     *
     * @return list of all financial results
     */
    public List<FinancialResults> getFinancialResultsList() {
        return financial;
    }
}
