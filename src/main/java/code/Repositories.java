package code;

public class Repositories {
    private static Repositories instance = new Repositories();
    private MapRepository mapRepository = new MapRepository();
    private IndustryRepository industryRepository = new IndustryRepository();

    private Repositories() {}

    public static Repositories getInstance() {
        return instance;
    }

    public MapRepository getMapRepository() {
        return mapRepository;
    }

    public IndustryRepository getIndustryRepository() {
        return industryRepository;
    }
}
