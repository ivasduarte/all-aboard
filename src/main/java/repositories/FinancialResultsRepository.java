package repositories;

import domain.FinancialResults;

import java.util.ArrayList;
import java.util.List;

public class FinancialResultsRepository {
    private List<FinancialResults> financial = new ArrayList<>();

    public void save(FinancialResults financialResults) {
        financial.add(financialResults);
    }

    public List<FinancialResults> getFinancialResultsList() {
        return financial;
    }
}
