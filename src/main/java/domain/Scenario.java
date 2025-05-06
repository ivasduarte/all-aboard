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

    private void validateName(){
        if(name == null || name.isEmpty()){
            throw new IllegalArgumentException("Scenario name cannot be empty.");
        }
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return "" + name + " (" + restrictions + ", belongs to map '" + map.getMapName() + ")";
    }
}
