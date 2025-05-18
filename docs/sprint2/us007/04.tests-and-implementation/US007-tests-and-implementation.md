# US007 - List all Stations

## 4. Tests

**Test 1:** Ensure that the controller correctly retrieves all stations.

```java
@Test
public void ensureControllerRetrievesAllStations() {
    StationRepository repo = new StationRepository();
    repo.save(new Station(1, 10, 10, "Medium"));
    repo.save(new Station(1, 20, 20, "Large"));

    Repositories.getInstance().setStationRepository(repo);

    ListStationsController controller = new ListStationsController();
    List<Station> stations = controller.getStationsByMapName("default");

    assertEquals(2, stations.size());
}
```


**Test 2:** Ensure the repository returns an empty list when no stations exist.

```java
@Test
public void ensureRepositoryReturnsEmptyWhenNoStations() {
    StationRepository repo = new StationRepository();
    Repositories.getInstance().setStationRepository(repo);

    ListStationsController controller = new ListStationsController();
    List<Station> stations = controller.getStationsByMapName("default");

    assertTrue(stations.isEmpty());
}

```

**Test 3:**  Ensure cargo value is initially zero.

```java

@Test
public void ensureStationReturnsCorrectBuildingInfo() {
    Station station = new Station(1, 5, 5, "Small");
    station.addBuilding(new Building("Storage"));
    station.addBuilding(new Building("Fuel"));

    List<Building> buildings = station.getBuildings();
    assertEquals(2, buildings.size());
    assertEquals("Storage", buildings.get(0).getBuildingType());
    assertEquals("Fuel", buildings.get(1).getBuildingType());
}

```

## 5. Construction (Implementation)

**Class ListStationsController**

```java
public class ListStationsController {
    private final StationRepository stationRepo;

    public ListStationsController() {
        this.stationRepo = Repositories.getInstance().getStationRepository();
    }

    public List<Station> getStationsByMapName(String mapName) {
        return stationRepo.getAvailableStationsByMapName(mapName);
    }
}
```

**Class Station**

```java
public boolean addBuilding(Building building) {
if (building == null)
throw new IllegalArgumentException("Building cannot be null.");

    for (Building b : buildings) {
        if (b.getBuildingType().equalsIgnoreCase(building.getBuildingType()))
            return false;
    }

    if (buildings.size() >= 7)
        return false;

    buildings.add(building);
    return true;
}
```




## 6. Integration and Demo

* Each station displays its ID, type, coordinates, stored cargo, and list of constructed buildings.

* Data is retrieved live from the repository, and presented interactively on the console.

## 7. Observations

* The system allows players to see and audit the current structure and cargo of each station.

* Building types and cargo values are retrieved directly from the station objects stored in memory.


