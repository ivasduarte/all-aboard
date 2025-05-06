package domain;

public class Scenario {
    private String name;
    private Restrictions restrictions;
    private Map map;

    public Scenario(String name, Restrictions restrictions, Map map){
        this.name = name;
        this.restrictions = restrictions;
        this.map = map;
        validateName();
    }


    //getters
    public String getScenarioName() {
        return name;
    }

    public Restrictions getRestrictions() {
        return restrictions;
    }

    public Map getMap() {
        return map;
    }


    //validations
    public boolean validateName(){
        if(name == null || name.isEmpty()){
            return false;
        } else if(name.length() > 15){
            return false;
        } else if(!name.matches(".*[A-Z].*")){
            return false;
        } else if(!name.matches("[A-Za-z0-9]+")){
            return false;
        }
        return true;
    }

    public boolean validateRestrictions(){
        return restrictions != null && restrictions.validate();
    }

    public boolean hasValidMap(){
        return map != null;
    }

    public boolean isValid(){
        return validateName() && validateRestrictions() && hasValidMap();
    }


    //toString

    @Override
    public String toString() {
        return "" + name + " (" + restrictions + ", belongs to map '" + map.getMapName() + ")";
    }
}
