package dto;

import domain.BuildingType;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public  class BuildingTypeMapper {

    private BuildingTypeMapper() {}

    public static BuildingTypeDTO toDTO(BuildingType bt) {
        if (bt == null) return null;
        return new BuildingTypeDTO(
                bt.getName(),
                bt.getCost(),
                bt.getEffect(),
                bt.getAvailableYear()
        );
    }

    public static List<BuildingTypeDTO> toDTO(List<BuildingType> list) {
        if (list == null) return new ArrayList<>();
        List<BuildingTypeDTO> result = new ArrayList<>();
        for (BuildingType item : list) {
            BuildingTypeDTO dto = BuildingTypeMapper.toDTO(item);
            if (dto != null) {
                result.add(dto);
            }
        }
        return result;
    }

    public static BuildingType toDomain(BuildingTypeDTO dto) {
        if (dto == null) return null;
        return new BuildingType(
                dto.getName(),
                dto.getCost(),
                dto.getEffect(),
                dto.getAvailableYear()
        );
    }
}
