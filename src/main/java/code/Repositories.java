package code;

public class Repositories {
    private static Repositories instance = new Repositories();
    private MapRepository mapRepository = new MapRepository();

    private Repositories() {}

    public static Repositories getInstance() {
        return instance;
    }

    public MapRepository getMapRepository() {
        return mapRepository;
    }
}
