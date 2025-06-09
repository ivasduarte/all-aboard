package dto;

import domain.Map;

import java.util.ArrayList;
import java.util.List;

/**
 * Utility class that provides methods to map between Map domain objects and MapDTO objects.
 *
 * This class includes static methods for converting Map to MapDTO, MapDTO to Map,
 * and their respective list conversions.
 */
public class MapMapper {
    /**
     * Converts a Map domain object into a MapDTO object.
     *
     * @param map The Map domain object to be converted. If the input is null, the method returns null.
     * @return A MapDTO object that represents the converted Map domain object or null if the input is null.
     */
    public static MapDTO toDTO(Map map) {
        if (map == null) {
            return null;
        }
        return new MapDTO(map.getName(), map.getCellSizeKm(), map.getHeight(), map.getWidth());
    }

    /**
     * Converts a list of Map domain objects into a list of MapDTO objects.
     *
     * @param maps The list of Map domain objects to be converted. If null, an empty list is returned.
     * @return A list of MapDTO objects representing the converted domain objects. If input is null or empty,
     * an empty list is returned.
     */
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

    /**
     * Converts a MapDTO object into a Map domain object.
     *
     * @param mapDTO The MapDTO instance to be converted
     * @return The converted Map domain object, or null if the input is null
     */
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
