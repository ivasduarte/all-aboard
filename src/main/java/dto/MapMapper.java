package dto;

import domain.Map;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class MapMapper {
    public static MapDTO toDTO(Map map) {
        if (map == null) {
            return null;
        }
        return new MapDTO(map.getName(), map.getCellSizeKm(), map.getHeight(), map.getWidth());
    }

    public static List<MapDTO> toDTO(List<Map> maps){
        if (maps == null) {
            return new ArrayList<>();
        }

        List<MapDTO> mapDTOs = new ArrayList<>();
        for (Map map : maps) {
            MapDTO mapDTO = toDTO(map);
            if (mapDTO != null) {
                mapDTOs.add(mapDTO);
            }
        }
        return mapDTOs;
    }

    public static Map toDomain(MapDTO mapDTO) {
        if (mapDTO == null) {
            return null;
        }
        return new Map(mapDTO.getName(), mapDTO.getWidth(), mapDTO.getHeight(), mapDTO.getCellSizeKm());
    }

    /**
     * Converts a list of MapDTO objects back to a list of Map domain objects.
     *
     * @param mapDTOs The list of MapDTO objects to convert
     * @return A list of Map domain objects, or empty list if input is null or empty
     */
    public static List<Map> toDomain(List<MapDTO> mapDTOs) {
        if (mapDTOs == null) {
            return new ArrayList<>();
        }

        List<Map> maps = new ArrayList<>();
        for (MapDTO mapDTO : mapDTOs) {
            Map map = toDomain(mapDTO);
            if (map != null) {
                maps.add(map);
            }
        }

        return maps;
    }
}
