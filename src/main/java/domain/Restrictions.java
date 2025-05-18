package domain;

import java.time.LocalDate;
import java.util.List;

/**
 * Represents a set of restrictions based on time, locomotive types, and historical events.
 * This class is designed to validate these restrictions based on defined criteria.
 */
public class Restrictions {
    //time restrictions
    private LocalDate startDate;
    private LocalDate endDate;

    //tech restrictions
    private List<String> locomotiveTypes;

    //historical restrictions (event descriptions)
    private List<String> historicalEvents;

    /**
     * Constructs a new instance of the Restrictions class with specified start date, end date,
     * locomotive types, and historical events.
     *
     * @param startDate             The start date of the restriction. Can be null if no specific start date is required.
     * @param endDate               The end date of the restriction. Can be null if no specific end date is required.
     * @param locomotiveTypes       A list of locomotive types associated with the restriction. Cannot be null or empty.
     * @param historicalEvents      A list of historical events associated with the restriction. Cannot be null or empty.
     */
    public Restrictions(LocalDate startDate, LocalDate endDate, List<String> locomotiveTypes, List<String> historicalEvents) {
        this.startDate = startDate;
        this.endDate = endDate;
        this.locomotiveTypes = locomotiveTypes;
        this.historicalEvents = historicalEvents;
    }

    /**
     * Retrieves the start date of the restriction.
     *
     * @return  The start date as a {@code LocalDate}. Returns null if no start date is specified.
     */
    public LocalDate getStartDate() {
        return startDate;
    }

    /**
     * Retrieves the end date of the restriction.
     *
     * @return  The end date as a {@code LocalDate}. Returns null if no end date is specified.
     */
    public LocalDate getEndDate() {
        return endDate;
    }

    /**
     * Retrieves the list of locomotive types associated with the restriction.
     *
     * @return  A list of strings representing the locomotive types. Cannot be null or empty.
     */
    public List<String> getLocomotiveTypes() {
        return locomotiveTypes;
    }

    /**
     * Retrieves the list of historical events associated with the restrictions.
     *
     * @return  A list of strings representing the historical events. The list cannot be null or empty.
     */
    public List<String> getHistoricalEvents() {
        return historicalEvents;
    }

    /**
     * Validates the current state of the restrictions by verifying
     * the time restrictions, locomotive types, and historical restrictions.
     *
     * This method ensures that all required validations from the associated
     * internal methods are satisfied. The state is considered valid only
     * if all individual validation methods return true.
     *
     * @return  true if all restrictions (time, locomotive types, historical events)
     *          are valid; false otherwise.
     */
    public boolean validate(){
        return validateTimeRestrictions() && validateLocomotiveTypes() && validateHistoricalRestrictions();
    }

    /**
     * Validates the time restrictions by checking if the start date and end date
     * are non-null and ensuring that the start date is not after the end date.
     *
     * @return  true if the time restrictions are valid (both dates are non-null
     *          and start date is not after end date); false otherwise.
     */
    private boolean validateTimeRestrictions(){
        if(startDate == null || endDate == null){
            return false;
        }

        if(startDate.isAfter(endDate)){
            return false;
        }
        return true;
    }

    /**
     * Validates the list of locomotive types associated with the instance.
     *
     * This method checks whether the locomotive types list is non-null, non-empty,
     * and contains only valid types (case-insensitive) such as "steam", "diesel", and "electric".
     *
     * @return  true if the locomotive types list is valid; false otherwise.
     */
    private boolean validateLocomotiveTypes(){
        if(locomotiveTypes == null || locomotiveTypes.isEmpty()){
            return false;
        }

        for(String type : locomotiveTypes){
            if(!type.equalsIgnoreCase("steam") &&
            !type.equalsIgnoreCase("diesel") &&
            !type.equalsIgnoreCase("electric")){
                return false;
            }
        }
        return true;
    }

    /**
     * Validates the historical restrictions by checking if the list of historical events
     * is not null and is not empty.
     *
     * @return  true if the list of historical events is non-null and contains at least one element;
     *          false otherwise.
     */
    private boolean validateHistoricalRestrictions(){
        return historicalEvents != null && !historicalEvents.isEmpty();
    }
}
