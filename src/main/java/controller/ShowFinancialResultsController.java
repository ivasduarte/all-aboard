package controller;

import domain.FinancialResults;
import domain.Scenario;
import repositories.FinancialResultsRepository;
import repositories.Repositories;

public class ShowFinancialResultsController {
    private FinancialResultsRepository financialResultsRepository;

    public void addFinancialResults(int trackMaintenance, int trainMaintenance, int fuelCost, int cargoDeliveries) {
        FinancialResultsRepository financialResultsRepository = Repositories.getInstance().getFinancialResultsRepository();

        FinancialResults financialResults = new FinancialResults(trackMaintenance, trainMaintenance, fuelCost, cargoDeliveries);

        financialResultsRepository.save(financialResults);
    }

    public FinancialResults getFinancialResultsByYear(int yearSelected) {
        return getFinancialResultsRepository().getFinancialResultsList().get(yearSelected);
    }

    public FinancialResultsRepository getFinancialResultsRepository() {
        if (financialResultsRepository == null) {
            Repositories repositories = Repositories.getInstance();
            financialResultsRepository = repositories.getFinancialResultsRepository();
        }
        return financialResultsRepository;
    }

    public boolean isYearValid(int yearSelected, Scenario currentScenario) {
        if (yearSelected < 0 || yearSelected >= (currentScenario.getCurrentYear() - currentScenario.getRestrictions().getStartDate().getYear())) {
            return false;
        }

        return true;
    }

    public boolean checkFinancialResultsExistence() {
        if (getFinancialResultsRepository().getFinancialResultsList().isEmpty()) {
            return false;
        }
        return true;
    }
}
