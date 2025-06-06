package controller;

import domain.FinancialResults;
import domain.Map;
import domain.Scenario;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import repositories.FinancialResultsRepository;
import repositories.Repositories;

import java.io.IOException;
import java.net.URL;

public class ShowFinancialResultsController {
    private FinancialResultsRepository financialResultsRepository;
    private Scenario scenario;
    private Map currentMap;

    @FXML
    private ComboBox<FinancialResults> financialResultsComboBox;
    @FXML
    private Label budget;
    @FXML
    private Label trackLabel;
    @FXML
    private Label maintenanceLabel;
    @FXML
    private Label fuelLabel;
    @FXML
    private Label cargoLabel;

    private FinancialResults selectedYear;

    public void setData(Map currentMap, Scenario scenario) {
        this.currentMap = currentMap;
        this.scenario = scenario;

        initializeFinancialResultsData();
    }

    /**
     * This method is responsible to add the yearly financial results to the repository.
     *
     * @param trackMaintenance cost to maintain the tracks
     * @param trainMaintenance cost to maintain the trains
     * @param fuelCost         price spent on fuel
     * @param cargoDeliveries  amount of cargo delivery
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
     * @param yearSelected    year selected
     * @param currentScenario scenario that the player is playing
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

    @FXML
    public void initialize() {
        resetLabels();
    }

    private void initializeFinancialResultsData() {
        if (scenario != null && checkFinancialResultsExistence()) {
            financialResultsComboBox.getItems().clear();
            financialResultsComboBox.getItems().addAll(getFinancialResultsRepository().getFinancialResultsList());

            setupComboBoxCellFactory();
        }
        showBudget();
    }

    private void setupComboBoxCellFactory() {
        financialResultsComboBox.setCellFactory(listView -> new javafx.scene.control.ListCell<FinancialResults>() {
            @Override
            protected void updateItem(FinancialResults item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    int yearIndex = financialResultsComboBox.getItems().indexOf(item);
                    int startYear = scenario.getRestrictions().getStartDate().getYear();
                    int displayYear = startYear + yearIndex;
                    setText("Year " + displayYear);
                }
            }
        });

        financialResultsComboBox.setButtonCell(new javafx.scene.control.ListCell<FinancialResults>() {
            @Override
            protected void updateItem(FinancialResults item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText("Available years");
                } else {
                    int yearIndex = financialResultsComboBox.getItems().indexOf(item);
                    int startYear = scenario.getRestrictions().getStartDate().getYear();
                    int displayYear = startYear + yearIndex;
                    setText("Year " + displayYear);
                }
            }
        });
    }

    private void resetLabels() {
        trackLabel.setText("Track Maintenance Cost: ");
        maintenanceLabel.setText("Train Maintenance Cost: ");
        fuelLabel.setText("Train Fuel Cost: ");
        cargoLabel.setText("Cargo Deliveries: ");
    }

    public void showAlert(String msg) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setContentText(msg);
        alert.showAndWait();
    }

    public void showBudget() {
        if (scenario != null) {
            budget.setText("Current Budget: " + scenario.getBudget() + "€");
        } else {
            budget.setText("Current Budget: ");
        }
    }

    @FXML
    public void updateFinancialResultsYears() {
        selectedYear = financialResultsComboBox.getValue();

        if (selectedYear == null) {
            resetLabels();
            return;
        }

        trackLabel.setText("Track Maintenance Cost: " + selectedYear.getTrackMaintenance() + "€");
        maintenanceLabel.setText("Train Maintenance Cost: " + selectedYear.getTrainMaintenance() + "€");
        fuelLabel.setText("Train Fuel Cost: " + selectedYear.getFuelCost() + "€");
        cargoLabel.setText("Cargo Deliveries: " + selectedYear.getCargoDeliveries());
    }

    @FXML
    public void handleButton() {
        navigateToPlayerMenu();
    }

    private void navigateToPlayerMenu() {
        try {
            URL fxmlURL = getClass().getResource("/JavaFX/playerMenu.fxml");
            System.out.println("FXML URL: " + fxmlURL);

            if (fxmlURL == null) {
                showAlert("FXML URL not found.");
                return;
            }

            FXMLLoader loader = new FXMLLoader(fxmlURL);
            Parent root = loader.load();
            PlayerMenuController controller = loader.getController();

            controller.setData(scenario);

            Stage stage = new Stage();
            stage.setTitle("Player Menu");
            stage.setScene(new Scene(root));
            stage.show();

            Stage currentStage = (Stage) financialResultsComboBox.getScene().getWindow();
            currentStage.close();

        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Navigation error: " + e.getMessage());
        }
    }
}
