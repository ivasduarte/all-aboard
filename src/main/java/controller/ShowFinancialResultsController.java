package controller;

import domain.FinancialResults;
import repositories.FinancialResultsRepository;
import repositories.Repositories;

public class ShowFinancialResultsController {
    private FinancialResultsRepository financialResultsRepository;

    public void addFinancialResults(int trackMaintenance, int trainMaintenance, int fuelCost, int cargoDeliveries) {
        FinancialResultsRepository financialResultsRepository = Repositories.getInstance().getFinancialResultsRepository();

        FinancialResults financialResults = new FinancialResults(trackMaintenance, trainMaintenance, fuelCost, cargoDeliveries);

        // verify if year selected is valid

        financialResultsRepository.save(financialResults);
    }

    public FinancialResultsRepository getFinancialResultsRepository() {
        if (financialResultsRepository == null) {
            Repositories repositories = Repositories.getInstance();
            financialResultsRepository = repositories.getFinancialResultsRepository();
        }
        return financialResultsRepository;
    }
}
