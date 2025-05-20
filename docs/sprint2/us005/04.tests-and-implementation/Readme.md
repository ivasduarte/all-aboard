# US005 - Add a Station
## 4. Tests

**_DO NOT COPY ALL DEVELOPED TESTS HERE_**

**Test 1:** Check that if `Station` is not null.

```java
@Test
void checkNotNull() {
    Station station = new Station(0, 10, 10, "Depot");
    
    assertNotNull(station);
}
```

**Test 2:** Check if there are no other buildings on the position where the station is being added.

```java
@Test
void checkOverbuilding() {
    Station station = new Station(0, 10, 10, "Depot");

    assertTrue(station.checkOverbuilding());
}
```

## 5. Construction (Implementation)

**Class AddStationController**

```java
public boolean AddStation(Map map, Scenario scenario, int positionX, int positionY, String stationType)
{
    int mapID = -1;

    MapRepository mapRepository = Repositories.getInstance().getMapRepository();
    List<Map> maps = mapRepository.getMapList();

    for(int i = 0; i < maps.size(); i++)
    {
        if(maps.get(i).equals(map))
            mapID = i;
    }

    StationRepository stationRepository = Repositories.getInstance().getStationRepository();
    Station newStation = new Station(mapID, positionX, positionY, stationType);
    Map stationMap = newStation.getMap();

    if(scenario == null){
        System.out.println("No scenario found for the selected map.");
        return false;
    }

    boolean validStationType = false;
    int typeID = 0;
    for(int i = 0; i < Station.stationTypes.length; i++)
    {
        if(stationType.equalsIgnoreCase(Station.stationTypes[i]))
        {
            typeID = i;
            validStationType = true;
        }
    }

    if(!validStationType)
        return false;

    int size = Station.typesSizes[typeID] / 2;

    if(!newStation.ValidatePosition(stationMap, size))
        return false;

    if(!newStation.checkOverbuilding())
        return false;

    stationRepository.save(newStation);
    Station.IncreaseTotalStations();

    scenario.subtractBudget(Station.typesPrices[typeID]);
    return true;
}
```

**Class StationRepository**

```java
public void save(Station station) {
    stations.add(station);
}
```

**Class Station**

```java
public Station(int mapID, int positionX, int positionY, String stationType)
{
    this.mapID = mapID;
    this.positionX = positionX;
    this.positionY = positionY;
    this.stationType = stationType;
    stationID = totalStations;
    this.buildings = new ArrayList<>();
}
```