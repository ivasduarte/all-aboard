package domain;

public enum CargoMode {
    FULL,    // the train only leaves when it's fully loaded
    HALF,    // the train only leaves once it's half-loaded
    AVAILABLE // the train only leaves when it's loaded with the available cargo from the station
}