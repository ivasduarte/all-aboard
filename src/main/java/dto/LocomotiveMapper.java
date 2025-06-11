package dto;

import domain.Locomotive;

import java.util.ArrayList;
import java.util.List;

public class LocomotiveMapper {

    public static LocomotiveDTO toDTO(Locomotive loco) {
        if (loco == null) {
            return null;
        }

        return new LocomotiveDTO(loco.getLocomotiveModel(), loco.getLocomotiveFuelType(), loco.getMaxSpeed(), loco.getStartYear(),
                loco.getEndYear(), loco.getPrice(), loco.getFuelCost(), loco.getMaintenanceCost());
    }

    public static List<LocomotiveDTO> toDTO(List<Locomotive> locomotives) {
        if (locomotives == null) {
            return new ArrayList<>();
        }

        List<LocomotiveDTO> locomotiveDTOS = new ArrayList<>();
        for (Locomotive loco : locomotives) {
            LocomotiveDTO locomotiveDTO = toDTO(loco);

            if (locomotiveDTO != null) {
                locomotiveDTOS.add(locomotiveDTO);
            }
        }

        return locomotiveDTOS;
    }

    public static Locomotive toDomain(LocomotiveDTO locomotiveDTO) {
        if (locomotiveDTO == null) {
            return null;
        }

        return new Locomotive(locomotiveDTO.getModel(), locomotiveDTO.getFuelType(), locomotiveDTO.getStartYear(), locomotiveDTO.getEndYear(),
                locomotiveDTO.getPrice(), locomotiveDTO.getMaintenanceCost(), locomotiveDTO.getFuelCost(), locomotiveDTO.getSpeed());
    }

    public static List<Locomotive> toDomain(List<LocomotiveDTO> locomotiveDTOS) {
        if (locomotiveDTOS == null) {
            return new ArrayList<>();
        }

        List<Locomotive> locomotives = new ArrayList<>();
        for (LocomotiveDTO locomotiveDTO : locomotiveDTOS) {
            Locomotive loco = toDomain(locomotiveDTO);

            if (loco != null) {
                locomotives.add(loco);
            }
        }

        return locomotives;
    }
}
