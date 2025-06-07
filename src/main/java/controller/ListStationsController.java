package controller;

import domain.Building;
import domain.Station;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import repositories.Repositories;
import repositories.StationRepository;

import java.util.List;

/**
 * Controller responsible for handling operations related to listing stations.
 */
public class ListStationsController {

    @FXML
    private ComboBox<Station> stationComboBox;

    @FXML
    private TextArea stationDetails;

    private final StationRepository StationRepo;

    /**
     * Constructs a ListStationsController and initializes the station repository.
     */


    public ListStationsController() {
        this.StationRepo = Repositories.getInstance().getStationRepository();
    }

    /**
     * Retrieves all stations from the repository.
     *
     * @return a list of all stations
     */
    public List<Station> getAllStations() {
        return StationRepo.getStations();
    }

    /**
     * Retrieves all stations from a specific map that still have available building slots.
     *
     * @param mapName the name of the map
     * @return a list of stations with available slots in the specified map
     */
    public List<Station> getStationsByMapName(String mapName) {
        return Repositories.getInstance().getStationRepository().getAvailableStationsByMapName(mapName);
    }

    @FXML public void setStationComboBox(){

        List<Station> stations = StationRepo.getStations();

        stationComboBox.getItems().setAll(stations);

    }

    @FXML
    private void initialize() {
        List<Station> stations = StationRepo.getStations();
        stationComboBox.getItems().setAll(stations);
        stationComboBox.setOnAction(e -> showStationDetails());
    }


    private void showStationDetails() {
        Station station = stationComboBox.getValue();

        if (station == null) {
            stationDetails.setText("Choose a station.");
            return;
        }

        StringBuilder details = new StringBuilder();
        details.append("Name: ").append(station.getName()).append("\n");
        details.append("ID: ").append(station.getStationID()).append("\n");
        details.append("Type: ").append(station.getStationType()).append("\n");
        details.append("Position: (").append(station.getPositionX()).append(", ").append(station.getPositionY()).append(")\n");
        details.append("Cargo: ").append(station.getCargo()).append("\n");

        List<Building> buildings = station.getBuildings();
        if (buildings.isEmpty()) {
            details.append("Buildings: none");
        } else {
            details.append("Buildings:\n");
            for (Building building : buildings) {
                details.append("  - ").append(building.getBuildingType()).append("\n");
            }
        }

        stationDetails.setText(details.toString());
    }


}
