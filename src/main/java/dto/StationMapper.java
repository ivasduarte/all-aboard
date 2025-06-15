package dto;

import domain.Building;
import domain.Station;

import java.util.ArrayList;
import java.util.List;


public class StationMapper {

    public static StationDTO toDTO(Station station) {
        if (station == null) return null;

        List<String> buildingTypes = new ArrayList<>();
        for (Building building : station.getBuildings()) {
            buildingTypes.add(building.getBuildingType());
        }

        return new StationDTO(
                station.getStationID(),
                station.getName(),
                station.getStationType().getDisplayName(),
                station.getPositionX(),
                station.getPositionY(),
                station.getCargo(),
                buildingTypes,
                station.getMapName(),
                station.getMapID(),
                station.getScenarioName(),
                station.getOrientation()
        );
    }

    public static List<StationDTO> toDTO(List<Station> stations) {
        if (stations == null) return new ArrayList<>();

        List<StationDTO> dtoList = new ArrayList<>();
        for (Station station : stations) {
            StationDTO dto = StationMapper.toDTO(station);
            if (dto != null) {
                dtoList.add(dto);
            }
        }
        return dtoList;

    }

    public static Station toDomain(StationDTO dto) {


        Station.StationType stationType = Station.StationType.fromString(dto.getStationType());

        return new Station(
                dto.getMapName(),
                dto.getMapId(),
                dto.getX(),
                dto.getY(),
                stationType,
                dto.getOrientation(),
                dto.getName(),
                dto.getScenarioName()
        );
    }

    public static List<Station> toDomain(List<StationDTO> dtoList) {
        if (dtoList == null) return new ArrayList<>();

        List<Station> domainList = new ArrayList<>();
        for (StationDTO dto : dtoList) {
            Station station = StationMapper.toDomain(dto);
            if (station != null) {
                domainList.add(station);
            }
        }
        return domainList;

    }
}
