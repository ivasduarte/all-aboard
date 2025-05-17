package controller;

import domain.Building;
import domain.BuildingType;
import domain.Station;
import domain.StationUpgradeService;
import repositories.BuildingRepository;
import repositories.Repositories;
import repositories.StationRepository;

import java.util.List;

public class UpgradeAStationController {
    private final StationUpgradeService upgradeService;
    private final StationRepository stationRepository;
    private final BuildingRepository buildingRepository;

    public UpgradeAStationController() {
        this.upgradeService = new StationUpgradeService();
        this.stationRepository = Repositories.getInstance().getStationRepository();
        this.buildingRepository = Repositories.getInstance().getBuildingRepository();
    }

    public List<Station> getAvailableStations(String mapName) {
        return stationRepository.getAvailableStationsByMapName(mapName);
    }

    public List<BuildingType> getAvailableBuildingTypes() {
        return buildingRepository.getBuildingTypes();
    }

    public boolean upgradeStation(Station station, String buildingTypeName, int currentYear) {
        if (station == null || buildingTypeName == null || buildingTypeName.isBlank()) {
            throw new IllegalArgumentException("Station and building type must be provided.");
        }

        BuildingType type = buildingRepository.getBuildingType(buildingTypeName);
        if (type == null) {
            System.out.println("ERROR: Invalid building type '" + buildingTypeName + "'");
            return false;
        }

        Building building = new Building(type);
        return upgradeService.upgradeStation(station, building, currentYear);
    }
}
