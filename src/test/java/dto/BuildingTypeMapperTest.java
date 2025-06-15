package dto;

import domain.BuildingType;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class BuildingTypeMapperTest {

    @Test
    void toDTO_shouldMapCorrectly() {
        BuildingType domain = new BuildingType("Depot", 1000, "Stores cargo", 1850);
        BuildingTypeDTO dto = BuildingTypeMapper.toDTO(domain);

        assertNotNull(dto);
        assertEquals("Depot", dto.getName());
        assertEquals(1000, dto.getCost());
        assertEquals("Stores cargo", dto.getEffect());
        assertEquals(1850, dto.getAvailableYear());
    }

    @Test
    void toDTO_shouldReturnNullForNullInput() {
        assertNull(BuildingTypeMapper.toDTO((BuildingType) null));
    }

    @Test
    void toDTOList_shouldHandleNullAndEmpty() {
        assertNotNull(BuildingTypeMapper.toDTO((List<BuildingType>) null));
        assertTrue(BuildingTypeMapper.toDTO((List<BuildingType>) null).isEmpty());

        assertTrue(BuildingTypeMapper.toDTO(List.of()).isEmpty());
    }

    @Test
    void toDTOList_shouldMapListCorrectly() {
        List<BuildingType> domainList = List.of(
                new BuildingType("Depot", 1000, "Stores cargo", 1850),
                new BuildingType("Warehouse", 5000, "Increases storage", 1900)
        );

        List<BuildingTypeDTO> dtoList = BuildingTypeMapper.toDTO(domainList);
        assertEquals(2, dtoList.size());
        assertEquals("Depot", dtoList.get(0).getName());
        assertEquals("Warehouse", dtoList.get(1).getName());
    }

    @Test
    void toDomain_shouldMapCorrectly() {
        BuildingTypeDTO dto = new BuildingTypeDTO("Depot", 1000, "Stores cargo", 1850);
        BuildingType domain = BuildingTypeMapper.toDomain(dto);

        assertNotNull(domain);
        assertEquals("Depot", domain.getName());
        assertEquals(1000, domain.getCost());
        assertEquals("Stores cargo", domain.getEffect());
        assertEquals(1850, domain.getAvailableYear());
    }

    @Test
    void toDomain_shouldReturnNullForNullInput() {
        assertNull(BuildingTypeMapper.toDomain((BuildingTypeDTO) null));
    }
}