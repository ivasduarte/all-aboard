package controller;

import domain.Building;
import domain.Station;
import domain.StationUpgradeService;
import repositories.Repositories;
import repositories.StationRepository;

import java.util.List;

public class UpgradeAStationController {
    private final StationUpgradeService upgradeService;
    private final StationRepository stationRepository;

    public UpgradeAStationController() {
        this.upgradeService = new StationUpgradeService();
        this.stationRepository = Repositories.getInstance().getStationRepository();
    }

    public List<Station> getAvailableStations() {
        return stationRepository.getAvailableStations();
    }

    public boolean upgradeStation(Station station, String buildingType, int currentYear) {
        if (station == null || buildingType == null || buildingType.isBlank()) {
            throw new IllegalArgumentException("Station and building type must be provided.");
        }

        Building building = new Building(buildingType);
        return upgradeService.upgradeStation(station, building, currentYear);
    }
}
