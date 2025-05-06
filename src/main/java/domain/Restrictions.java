package domain;

import java.time.LocalDate;
import java.util.List;

public class Restrictions {

    //time restrictions
    private LocalDate startDate;
    private LocalDate endDate;

    //tech restrictions
    private List<String> locomotiveTypes;

    //historical restrictions (event descriptions)
    private List<String> historicalEvents;

    public Restrictions(LocalDate startDate, LocalDate endDate, List<String> locomotiveTypes, List<String> historicalEvents) {
        this.startDate = startDate;
        this.endDate = endDate;
        this.locomotiveTypes = locomotiveTypes;
        this.historicalEvents = historicalEvents;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public List<String> getLocomotiveTypes() {
        return locomotiveTypes;
    }

    public List<String> getHistoricalEvents() {
        return historicalEvents;
    }

    public boolean validate(){
        return validateTimeRestrictions() && validateLocomotiveTypes() &&& validateHistoricalRestrictions();
    }

    private boolean validateTimeRestrictions(){
        if(startDate == null || endDate == null){
            return false;
        }

        if(startDate.isAfter(endDate)){
            return false;
        }
        return true;
    }

    private boolean validateLocomotiveTypes(){
        if(locomotiveTypes == null || locomotionTypes.isEmpty()){
            return false;
        }

        for(String type : locomotionTypes){
            if(!type.equalsIgnoreCase("steam") &&
            !type.equalsIgnoreCase("diesel") &&
            !type.equalsIgnoreCase("electric")){
                return false;
            }
        }
        return true;
    }

    private boolean validateHistoricalRestrictions(){
        return historicalEvents != null && !historicalEvents.isEmpty();
    }
}
