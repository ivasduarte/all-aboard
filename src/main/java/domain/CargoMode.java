package domain;

/**
 * Enum representing different modes of cargo loading for trains.
 * These modes determine when a train should depart based on its cargo load.
 */
public enum CargoMode {
    /** The train only departs when it reaches full cargo capacity */
    FULL,
    /** The train departs when it reaches 50% of its cargo capacity */
    HALF,
    /** The train departs with whatever cargo is currently available at the station */
    AVAILABLE
}