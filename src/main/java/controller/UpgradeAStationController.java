package controller;

import domain.Building;
import domain.Station;
import domain.StationUpgradeService;

public class UpgradeAStationController {

    private final StationUpgradeService upgradeService;

    public UpgradeAStationController() {
        this.upgradeService = new StationUpgradeService();
    }

    public boolean upgradeStation(Station station, Building building, int currentYear) {
        return upgradeService.upgradeStation(station, building, currentYear);
    }

}
