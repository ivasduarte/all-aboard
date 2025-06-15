package dto;

import domain.Building;
import domain.Station;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public final class StationMapper {

    private StationMapper() {}

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
                buildingTypes
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
        if (dto == null) return null;

        Station.StationType type = Station.StationType.fromString(dto.getStationType());

        return new Station(
                "UNKNOWN_MAP",
                -1,
                dto.getX(),
                dto.getY(),
                type,
                Station.Orientation.NE,
                dto.getName(),
                "UNKNOWN_SCENARIO"
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
