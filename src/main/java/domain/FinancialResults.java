package domain;

public class FinancialResults {
    private int trackMaintenance;
    private int trainMaintenance;
    private int fuelCost;
    private int cargoDeliveries;

    /**
     * This method is the constructor of FinancialResults.
     *
     * @param trackMaintenance cost of track maintenance
     * @param trainMaintenance cost of train maintenance
     * @param fuelCost cost of fuel
     * @param cargoDeliveries cargo delivered
     */
    public FinancialResults(int trackMaintenance, int trainMaintenance, int fuelCost, int cargoDeliveries) {
        this.trackMaintenance = trackMaintenance;
        this.trainMaintenance = trainMaintenance;
        this.fuelCost = fuelCost;
        this.cargoDeliveries = cargoDeliveries;
    }

    /**
     * Returns the amount the player has spent in track maintenance, during the year.
     *
     * @return fuel cost
     */
    public int getTrackMaintenance() {
        return trackMaintenance;
    }

    /**
     * Returns the amount the player has spent in train maintenance, during the year.
     *
     * @return fuel cost
     */
    public int getTrainMaintenance() {
        return trainMaintenance;
    }

    /**
     * Returns the amount the player has spent in fuel, during the year.
     *
     * @return fuel cost
     */
    public int getFuelCost() {
        return fuelCost;
    }

    /**
     * Returns the amount of cargo delivered, during the year.
     *
     * @return cargo deliveries
     */
    public int getCargoDeliveries() {
        return cargoDeliveries;
    }

    @Override
    /**
     * Returns a string representation of the FinancialResults object.
     *
     * @return train maintenance cost, train fuel cost, cargo deliveries
     */
    public String toString() {
        return "\nTrain Maintenance Cost: " + getTrainMaintenance() + "€\nTrain Fuel Cost: " + getFuelCost() + "€\nCargo Deliveries: " + getCargoDeliveries();
    }
}
