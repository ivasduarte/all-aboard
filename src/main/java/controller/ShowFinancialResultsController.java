package controller;

import domain.FinancialResults;
import domain.Scenario;
import repositories.FinancialResultsRepository;
import repositories.Repositories;

public class ShowFinancialResultsController {
    private FinancialResultsRepository financialResultsRepository;

    /**
     * This method is responsible to add the yearly financial results to the repository.
     *
     * @param trackMaintenance cost to maintain the tracks
     * @param trainMaintenance cost to maintain the trains
     * @param fuelCost price spent on fuel
     * @param cargoDeliveries amount of cargo delivery
     */
    public void addFinancialResults(int trackMaintenance, int trainMaintenance, int fuelCost, int cargoDeliveries) {
        FinancialResultsRepository financialResultsRepository = Repositories.getInstance().getFinancialResultsRepository();

        FinancialResults financialResults = new FinancialResults(trackMaintenance, trainMaintenance, fuelCost, cargoDeliveries);

        financialResultsRepository.save(financialResults);
    }

    /**
     * This method is responsible to get the financial results of a specific year.
     *
     * @param yearSelected year selected by the player
     *
     * @return financial results
     */
    public FinancialResults getFinancialResultsByYear(int yearSelected) {
        return getFinancialResultsRepository().getFinancialResultsList().get(yearSelected);
    }

    /**
     * This method allows the controller to get access to the FinancialResultsRepository.
     *
     * @return FinancialResultsRepository
     */
    public FinancialResultsRepository getFinancialResultsRepository() {
        if (financialResultsRepository == null) {
            Repositories repositories = Repositories.getInstance();
            financialResultsRepository = repositories.getFinancialResultsRepository();
        }
        return financialResultsRepository;
    }

    /**
     * This method verifies if the year selected is present on the list shown to the player.
     *
     * @param yearSelected year selected
     * @param currentScenario scenario that the player is playing
     *
     * @return True if the year selected is valid
     */
    public boolean isYearValid(int yearSelected, Scenario currentScenario) {
        if (yearSelected < 0 || yearSelected >= (currentScenario.getCurrentYear() - currentScenario.getRestrictions().getStartDate().getYear())) {
            return false;
        }

        return true;
    }

    /**
     * This method verifies if the FinancialResultsRepository has something.
     *
     * @return True if any FinancialResults Object has been added to the repository.
     */
    public boolean checkFinancialResultsExistence() {
        if (getFinancialResultsRepository().getFinancialResultsList().isEmpty()) {
            return false;
        }
        return true;
    }
}
