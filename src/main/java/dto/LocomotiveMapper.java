package dto;

import domain.Locomotive;

import java.util.ArrayList;
import java.util.List;

/**
 * Utility class that provides methods to map between Locomotive domain objects and LocomotiveDTO objects.
 *
 * This class includes static methods for converting Locomotive to LocomotiveDTO, LocomotiveDTO to Locomotive,
 * and their respective list conversions.
 */
public class LocomotiveMapper {

    /**
     * Converts a Locomotive domain object into a LocomotiveDTO object.
     *
     * @param loco The Locomotive domain object to be converted. If the input is null, the method returns null.
     *
     * @return A LocomotiveDTO object that represents the converted Locomotive domain object or null if the input is null.
     */
    public static LocomotiveDTO toDTO(Locomotive loco) {
        if (loco == null) {
            return null;
        }

        return new LocomotiveDTO(loco.getLocomotiveModel(), loco.getLocomotiveFuelType(), loco.getMaxSpeed(), loco.getStartYear(),
                loco.getEndYear(), loco.getPrice(), loco.getFuelCost(), loco.getMaintenanceCost());
    }

    /**
     * Converts a list of Locomotive domain objects into a list of LocomotiveDTO objects.
     *
     * @param locomotives The list of Locomotive domain objects to be converted. If null, an empty list is returned.
     *
     * @return A list of LocomotiveDTO objects representing the converted domain objects. If input is null or empty,
     * an empty list is returned.
     */
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

    /**
     * Converts a LocomotiveDTO object into a Locomotive domain object.
     *
     * @param locomotiveDTO The LocomotiveDTO instance to be converted
     *
     * @return The converted Locomotive domain object, or null if the input is null
     */
    public static Locomotive toDomain(LocomotiveDTO locomotiveDTO) {
        if (locomotiveDTO == null) {
            return null;
        }

        return new Locomotive(locomotiveDTO.getModel(), locomotiveDTO.getFuelType(), locomotiveDTO.getStartYear(), locomotiveDTO.getEndYear(),
                locomotiveDTO.getPrice(), locomotiveDTO.getMaintenanceCost(), locomotiveDTO.getFuelCost(), locomotiveDTO.getSpeed());
    }

    /**
     * Converts a list of LocomotiveDTO objects back to a list of Locomotive domain objects.
     *
     * @param locomotiveDTOS The list of LocomotiveDTO objects to convert
     *
     * @return A list of Locomotive domain objects, or empty list if input is null or empty
     */
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
