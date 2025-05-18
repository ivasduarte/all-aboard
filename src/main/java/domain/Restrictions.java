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
     * Constructs an instance of the Restrictions class with specified time restrictions,
     * locomotive type restrictions, and historical event restrictions.
     *
     * @param startDate         The start date of the restriction. It can be null if no start date is specified.
     * @param endDate           The end date of the restriction. It can be null if no end date is specified.
     * @param locomotiveTypes   A list of locomotive types allowed under the restriction. Cannot be null or empty.
     * @param historicalEvents  A list of historical events associated with the restriction. Cannot be null or empty.
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
     * @return The start date as a {@code LocalDate}. Returns null if no start date is specified.
     */
    public LocalDate getStartDate() {
        return startDate;
    }


    /**
     * Retrieves the end date of the restriction.
     *
     * @return The end date as a {@code LocalDate}. Returns null if no end date is specified.
     */
    public LocalDate getEndDate() {
        return endDate;
    }


    /**
     * Retrieves the list of locomotive types associated with the restriction.
     *
     * @return A list of strings representing the locomotive types. Cannot be null or empty.
     */
    public List<String> getLocomotiveTypes() {
        return locomotiveTypes;
    }


    /**
     * Retrieves the list of historical events associated with the restriction.
     *
     * @return A list of strings representing historical events. Cannot be null or empty.
     */
    public List<String> getHistoricalEvents() {
        return historicalEvents;
    }


    /**
     * Validates the restrictions by checking time restrictions, locomotive type restrictions,
     * and historical event restrictions.
     *
     * This method ensures all the following validations:
     * - The start and end dates are valid and follow the logical order.
     * - The list of locomotive types only contains valid types (e.g., steam, diesel, electric).
     * - Historical events are not null or empty.
     *
     * @return true if all the restrictions (time, locomotive types, and historical events) are valid;
     *         false otherwise.
     */
    public boolean validate(){
        return validateTimeRestrictions() && validateLocomotiveTypes() && validateHistoricalRestrictions();
    }


    /**
     * Validates the time restrictions by ensuring that the start and end dates are not null
     * and that the start date does not occur after the end date.
     *
     * @return true if both startDate and endDate are not null and startDate is not after endDate;
     *         false otherwise.
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
     * Validates the list of locomotive types to ensure it is non-null, non-empty,
     * and only contains valid types: "steam", "diesel", or "electric".
     *
     * @return true if the locomotive types list is valid and all entries are valid types;
     *         false otherwise.
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
     * Validates the historical restrictions associated with the instance.
     *
     * This method checks if the list of historical events is not null and not empty.
     *
     * @return true if the list of historical events is not null and contains at least one entry;
     *         false otherwise.
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
}
