package dto;

import domain.Locomotive;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class LocomotiveMapperTest {

    @Test
    void testToDTO() {
        Locomotive loco = new Locomotive("Model X", "Diesel", 1990, 2010, 500000, 20000, 300, 120);
        LocomotiveDTO dto = LocomotiveMapper.toDTO(loco);

        assertNotNull(dto);
        assertEquals("Model X", dto.getModel());
        assertEquals("Diesel", dto.getFuelType());
        assertEquals(1990, dto.getStartYear());
        assertEquals(2010, dto.getEndYear());
        assertEquals(500000, dto.getPrice());
        assertEquals(20000, dto.getMaintenanceCost());
        assertEquals(300, dto.getFuelCost());
        assertEquals(120, dto.getSpeed());
    }

    @Test
    void testToDomain() {
        LocomotiveDTO dto = new LocomotiveDTO("Model Y", "Electric", 150, 2000, 2025, 700000, 100, 15000);
        Locomotive loco = LocomotiveMapper.toDomain(dto);

        assertNotNull(loco);
        assertEquals("Model Y", loco.getLocomotiveModel());
        assertEquals("Electric", loco.getLocomotiveFuelType());
        assertEquals(2000, loco.getStartYear());
        assertEquals(2025, loco.getEndYear());
        assertEquals(700000, loco.getPrice());
        assertEquals(15000, loco.getMaintenanceCost());
        assertEquals(100, loco.getFuelCost());
        assertEquals(150, loco.getMaxSpeed());
    }

    @Test
    void testToDTO_Null() {
        assertNull(LocomotiveMapper.toDTO((Locomotive) null));
    }

    @Test
    void testToDomain_Null() {
        assertNull(LocomotiveMapper.toDomain((LocomotiveDTO) null));
    }

    @Test
    void testToDTO_List() {
        List<Locomotive> list = List.of(new Locomotive("A", "Steam", 1900, 1930, 10000, 1000, 50, 80));
        List<LocomotiveDTO> result = LocomotiveMapper.toDTO(list);
        assertEquals(1, result.size());
        assertEquals("A", result.get(0).getModel());
    }

    @Test
    void testToDomain_List() {
        List<LocomotiveDTO> list = List.of(new LocomotiveDTO("B", "Nuclear", 300, 2020, 2040, 1000000, 500, 40000));
        List<Locomotive> result = LocomotiveMapper.toDomain(list);
        assertEquals(1, result.size());
        assertEquals("B", result.get(0).getLocomotiveModel());
    }
}