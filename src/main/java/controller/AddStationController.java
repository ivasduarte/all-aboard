package controller;

import domain.*;
import repositories.MapRepository;
import repositories.Repositories;
import repositories.ScenarioRepository;
import repositories.StationRepository;

import java.util.List;

public class AddStationController {

    /**
     * Add a station in a map
     *
     * @param map         map of the station
     * @param scenario    scenario in which the budget will be subtracted from
     * @param positionX   station's X position
     * @param positionY   station's Y position
     * @param stationType station type
     * @return return true if successfully added station, false otherwise
     */
    public String AddStation(Map map, Scenario scenario, int positionX, int positionY, int stationType, String rotation) {
        int mapID = -1;

        MapRepository mapRepository = Repositories.getInstance().getMapRepository();
        List<Map> maps = mapRepository.getMapList();

        for (int i = 0; i < maps.size(); i++) {
            if (maps.get(i).equals(map))
                mapID = i;
        }

        StationRepository stationRepository = Repositories.getInstance().getStationRepository();

        if (stationType < 0 || stationType > Station.StationType.values().length-1) {
            return "Invalid station type";
        }

        Station.StationType type = Station.StationType.values()[stationType];

        Station.Orientation orientation;
        try {
            orientation = Station.Orientation.fromString(rotation);
        } catch (IllegalArgumentException e) {
            return "Invalid orientation";
        }
        
        int size = Station.StationType.values().length / 2;

        int budget = scenario.getBudget().getBudget();

        if (type.getPrice() > budget) {
            return "Invalid budget";
        }

        String stationName = generateStationName(map.getName(), positionX, positionY, type.getDisplayName());
        Station newStation = new Station(map.getName(), mapID, positionX, positionY, type, orientation, stationName);

        if (!newStation.validatePosition(map, size)) {
            return "Invalid position";
        }

        if (!newStation.checkOverbuilding()) {
            return "Station overbuilding";
        }

        stationRepository.save(newStation);
        Station.increaseTotalStations();
        scenario.subtractBudget(type.getPrice());
        return "Station added successfully under the name " + stationName;
    }

    public String generateStationName(String mapName, int posX, int posY, String type)
    {
        List<City> cities = Repositories.getInstance().getCityRepository().getCitiesByMap(mapName);
        String stationName = type;

        if(!cities.isEmpty())
        {
            double smallestDistance = Math.sqrt(Math.pow(cities.get(0).getCenterX()-posX, 2) + Math.pow(cities.get(0).getCenterY()-posY, 2));
            City closestCity = cities.get(0);

            for(City city : cities)
            {
                int cityX = city.getCenterX();
                int cityY = city.getCenterX();
                double distance = Math.sqrt(Math.pow(cityX-posX, 2) + Math.pow(cityY-posY, 2));

                if(distance < smallestDistance)
                {
                    closestCity = city;
                    smallestDistance = distance;
                }
            }

            stationName = closestCity.getName() + " " + type;
        }

        return stationName;
    }
}
