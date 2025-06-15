package dto;

import domain.Map;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class MapMapperTest {

    @Test
    void toDTO_shouldMapCorrectly() {
        Map map = new Map("Europe", 10, 100, 200);
        MapDTO dto = MapMapper.toDTO(map);

        assertNotNull(dto);
        assertEquals("Europe", dto.getName());
        assertEquals(10, dto.getWidth());
        assertEquals(100, dto.getHeight());
        assertEquals(200, dto.getCellSizeKm());
    }

    @Test
    void toDTO_shouldReturnNullForNullInput() {
        assertNull(MapMapper.toDTO((Map) null));
    }

    @Test
    void toDTOList_shouldHandleNullAndEmpty() {
        assertNotNull(MapMapper.toDTO((List<Map>) null));
        assertTrue(MapMapper.toDTO((List<Map>) null).isEmpty());
        assertTrue(MapMapper.toDTO(List.of()).isEmpty());
    }

    @Test
    void toDTOList_shouldMapListCorrectly() {
        List<Map> maps = List.of(
                new Map("Map1", 5, 50, 50),
                new Map("Map2", 10, 100, 100)
        );

        List<MapDTO> dtos = MapMapper.toDTO(maps);
        assertEquals(2, dtos.size());
        assertEquals("Map1", dtos.get(0).getName());
        assertEquals("Map2", dtos.get(1).getName());
    }
}