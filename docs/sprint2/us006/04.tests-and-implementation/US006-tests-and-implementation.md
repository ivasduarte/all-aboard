# US006 - Upgrade a selected station with a building

## 4. Tests

**Test 1:** Check that it is not possible to create a building with a null type.

```java
@Test(expected = IllegalArgumentException.class)
public void ensureBuildingIsNotCreatedWithNullType() {
    Building building = new Building(null);
}
```


**Test 2:** Check mutual exclusivity between Small Hotel and Large Hotel.

```java
@Test
public void ensureMaxSevenBuildingsLimit() {
    Station station = new Station(1, 5, 5, "Medium");
    for (int i = 0; i < 7; i++) {
        station.addBuilding(new Building("Type" + i));
    }

    assertFalse(station.addBuilding(new Building("Extra"))); // Exceeds limit
}

```

**Test 3:** Check that a valid building is successfully added.

```java

@Test
public void ensureValidBuildingUpgrade() {
    Station station = new Station(1, 20, 20, "Large");
    StationUpgradeService service = new StationUpgradeService();

    boolean result = service.upgradeStation(station, new Building("Fuel"), 2025);
    assertTrue(result);
}

```

## 5. Construction (Implementation)

**Class Building**

```java
public class Building {
    private String buildingType;

    public Building(String buildingType) {
        if (buildingType == null || buildingType.isBlank())
            throw new IllegalArgumentException("Building type cannot be null or blank.");
        this.buildingType = buildingType;
    }

    public String getBuildingType() {
        return buildingType;
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

**Class StationUpgradeService**
```java

public class StationUpgradeService {

    public boolean upgradeStation(Station station, Building building, int currentYear) {
        List<Building> existingBuildings = station.getBuildings();

        if (!checkMutualExclusivity(existingBuildings, building.getBuildingType())) {
            return false;
        }

        return station.addBuilding(building);
    }

    private boolean checkMutualExclusivity(List<Building> existingBuildings, String newType) {
        for (Building b : existingBuildings) {
            String existingType = b.getBuildingType();
            if ((existingType.equalsIgnoreCase("Small Hotel") && newType.equalsIgnoreCase("Large Hotel")) ||
                    (existingType.equalsIgnoreCase("Large Hotel") && newType.equalsIgnoreCase("Small Hotel"))) {
                return false;
            }
        }
        return true;
    }
}
```

**Class StationUpgradeController**
```java

public class UpgradeAStationController {
    private final StationUpgradeService upgradeService = new StationUpgradeService();

    public boolean upgradeStation(Station station, String buildingType, int currentYear) {
        return upgradeService.upgradeStation(station, new Building(buildingType), currentYear);
    }
}
```



## 6. Integration and Demo

* A new action is available in the simulation interface to allow the player to upgrade an existing station by adding a building.

* For demo purposes, some stations are preloaded with buildings and users can test the upgrade behavior interactively through the UI.


## 7. Observations

* Only one hotel type (Small or Large) can exist in a station — they are mutually exclusive.

* The maximum number of buildings allowed per station is 7.


