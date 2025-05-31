package domain;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;

/**
 * Represents a set of restrictions based on time, locomotive types, and historical events.
 * This class is designed to validate these restrictions based on defined criteria.
 */
public class Restrictions implements Serializable {
    //time restrictions
    private LocalDate startDate;
    private LocalDate endDate;

    //tech restrictions
    private List<String> locomotiveTypes;

    //historical restrictions (event descriptions)
    private List<String> historicalEvents;

    //port behavior restrictions
    private List<PortBehavior> portBehaviors;

    /**
     * Constructs a new instance of the Restrictions class with specified start date, end date,
     * locomotive types, and historical events.
     *
     * @param startDate             The start date of the restriction. Can be null if no specific start date is required.
     * @param endDate               The end date of the restriction. Can be null if no specific end date is required.
     * @param locomotiveTypes       A list of locomotive types associated with the restriction. Cannot be null or empty.
     * @param historicalEvents      A list of historical events associated with the restriction. Cannot be null or empty.
     */
    public Restrictions(LocalDate startDate, LocalDate endDate, List<String> locomotiveTypes, List<String> historicalEvents, List<PortBehavior> portBehaviors) {
        this.startDate = startDate;
        this.endDate = endDate;
        this.locomotiveTypes = locomotiveTypes;
        this.historicalEvents = historicalEvents;
        this.portBehaviors = portBehaviors;
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
     * Validates the time restrictions by checking the validity and consistency
     * of the start and end dates.
     *
     * This method ensures:
     * - Neither startDate nor endDate is null.
     * - The startDate does not occur after the endDate.
     * - Both startDate and endDate are valid according to custom validation rules
     *   defined in the isValidDate method.
     *
     * @return true if both startDate and endDate are valid and satisfy the conditions,
     *         false otherwise.
     */
    private boolean validateTimeRestrictions(){
        if (startDate == null || endDate == null) {
            return false;
        }

        if (startDate.isAfter(endDate)) {
            return false;
        }

        if (!isValidDate(startDate) || !isValidDate(endDate)) {
            return false;
        }

        return true;
    }

    /**
     * Checks if the provided date is valid based on specific constraints.
     * A valid date must have a year greater than or equal to 1770, a month
     * value between 1 and 12 (inclusive), and a day value between 1 and 31 (inclusive).
     *
     * @param date the {@code LocalDate} to validate
     * @return true if the date is valid according to the defined constraints, false otherwise
     */
    private boolean isValidDate(LocalDate date) {
        int year = date.getYear();
        int month = date.getMonthValue();
        int day = date.getDayOfMonth();

        return year >= 1770 && month >= 1 && month <= 12 && day >= 1 && day <= 31;
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
        if(historicalEvents == null || historicalEvents.isEmpty())
        {
            return false;
        }

        for(String event : historicalEvents){
            if(!event.trim().equalsIgnoreCase("none") &&
                    !event.trim().equalsIgnoreCase("pandemic") &&
                    !event.trim().equalsIgnoreCase("great war") && !event.trim().equalsIgnoreCase("war")){
                return false;
            }
        }
        return true;
    }

    public List<PortBehavior> getPorts(){
        return portBehaviors;
    }
}
