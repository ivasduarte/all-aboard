package controller;

import domain.*;
import repositories.BuildingRepository;
import repositories.Repositories;
import repositories.ScenarioRepository;
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

    public BuildingType getBuildingTypeByName(String name) {
        return buildingRepository.getBuildingType(name);
    }

    public Scenario getScenarioByMapName(String mapName) {
        ScenarioRepository scenarioRepository = Repositories.getInstance().getScenarioRepository();
        for (Scenario scenario : scenarioRepository.getScenarioList()) {
            if (scenario.getMap().getMapName().equalsIgnoreCase(mapName)) {
                return scenario;
            }
        }
        return null;
    }

    public void removeObsoleteBuildings(Station station, int currentYear) {
        boolean hadTelegraph = false;

        if (currentYear >= 1920) {
            hadTelegraph = station.getBuildings().removeIf(b ->
                    b.getBuildingType().equalsIgnoreCase("Telegraph")
            );

            if (hadTelegraph) {
                BuildingType telephone = Repositories.getInstance()
                        .getBuildingRepository()
                        .getBuildingType("Telephone");

                if (telephone != null) {
                    station.addBuilding(new Building(telephone));
                    System.out.println("INFO: Telegraph was replaced by Telephone at station " + station.getStationID());
                }
            }
        }
    }
}
