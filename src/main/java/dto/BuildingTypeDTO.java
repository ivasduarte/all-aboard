package dto;

public class BuildingTypeDTO {

    private final String name;
    private final int cost;
    private final String effect;
    private final int availableYear;

    public BuildingTypeDTO(String name, int cost, String effect, int availableYear) {
        this.name = name;
        this.cost = cost;
        this.effect = effect;
        this.availableYear = availableYear;
    }

    public String getName()         { return name; }
    public int    getCost()         { return cost; }
    public String getEffect()       { return effect; }
    public int    getAvailableYear(){ return availableYear; }
}
