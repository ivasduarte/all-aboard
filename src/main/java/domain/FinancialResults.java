package domain;

public class FinancialResults {
    private int trackMaintenance;
    private int trainMaintenance;
    private int fuelCost;
    private int cargoDeliveries;

    public FinancialResults(int trackMaintenance, int trainMaintenance, int fuelCost, int cargoDeliveries) {
        this.trackMaintenance = trackMaintenance;
        this.trainMaintenance = trainMaintenance;
        this.fuelCost = fuelCost;
        this.cargoDeliveries = cargoDeliveries;
    }

    public int getTrackMaintenance() {
        return trackMaintenance;
    }

    public int getTrainMaintenance() {
        return trainMaintenance;
    }

    public int getFuelCost() {
        return fuelCost;
    }

    public int getCargoDeliveries() {
        return cargoDeliveries;
    }

    @Override
    public String toString() {
        return "\nTrain Maintenance Cost: " + getTrainMaintenance() + "€\nTrain Fuel Cost: " + getFuelCost() + "€\nCargo Deliveries: " + getCargoDeliveries();
    }
}
