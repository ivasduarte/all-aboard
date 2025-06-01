# US001 - Create A Map

## 4. Tests

**Test 1:** Validates Map Contructor with valid input

```java
void testConstructorWithValidInput() {
    assertEquals("TestMap", testMap.getMapName());
    assertEquals(100, testMap.getWidth());
    assertEquals(100, testMap.getHeight());
    assertTrue(testMap.getScenarios().isEmpty());
}
```

**Test 2:** Validates map constructor with invalid map name

```java
void testConstructorWithInvalidName() {
    assertThrows(IllegalArgumentException.class, () -> new Map("", 10, 10));
    assertThrows(IllegalArgumentException.class, () -> new Map(null, 10, 10));
}
```


**Test 3:** Validates map contructor with invalid size

```java
void testConstructorWithInvalidSize() {
    assertThrows(IllegalArgumentException.class, () -> new Map("Map", 0, 10));
    assertThrows(IllegalArgumentException.class, () -> new Map("Map", 10, -1));
}
```


**Test 4:** Validates adding a new scenario to map

```java
void testAddNewScenario() {
    Scenario scenario = new Scenario("testScenario", validRestrictions, testMap, testBudget);
    testMap.addNewScenario(scenario);
    assertEquals(1, testMap.getScenarios().size());
    assertTrue(testMap.getScenarios().contains(scenario));
}
```


**Test 5:** Validates toString method

```java
void testToString() {
    assertEquals("TestMap (100x100)", testMap.toString());
}
```


**Test 6:** Validates adding a new map to map repository

```java
void testAddNewMapSuccess() {
    repository.add(map1);
    assertTrue(repository.getMapList().contains(map1));
}
```

**Test 7:** Validates adding a duplicate map to map repository

```java
void testAddDuplicateMapFails() {
    repository.add(map1);
    repository.add(map1); // Duplicate
    assertEquals(1, repository.getMapList().size());
}
```


**Test 8:** Validates map name already existing in map repository

```java
void testExistsByNameTrue() {
    repository.add(map1);
    assertTrue(repository.existsByName("mapone"));
}
```


**Test 9:** Validates map name not existing in map repository

```java
void testExistsByNameFalse() {
    assertFalse(repository.existsByName("nonexistent"));
}
```


**Test 10:** Validates getting list of maps

```java
void testGetMapList() {
    repository.add(map1);
    var mapList = repository.getMapList();
    assertEquals(1, mapList.size());
    assertThrows(UnsupportedOperationException.class, () -> mapList.add(map2));
}
```


**Test 11:** Validates getMap method

```java
void testGetMap() {
    repository.add(map1);
    Optional<Map> result = repository.getMap("MapOne");
    assertTrue(result.isPresent());
    assertEquals(map1, result.get());
}
```


**Test 12:** Validates getting a map that doesn't exist

```java
void testGetMapNotExists() {
    Optional<Map> result = repository.getMap("NonExistent");
    assertTrue(result.isEmpty());
}
```




## 5. Construction (Implementation)

### Class CreateMapController

```java
package controller;

import domain.Map;
import repositories.Repositories;

/**
 * The CreateMapController class provides functionality to create a new map with specified dimensions
 * and add it to a repository. This ensures that each map is unique by name and validates the dimensions
 * before adding the map.
 */
public class CreateMapController {
    /**
     * Creates a new map with the specified name, width, height, and adds it to the map repository.
     * Ensures that the map name is unique and throws an exception if a map with the same name already exists.
     *
     * @param name      The name of the map to be created. Must be non-null and non-empty.
     * @param width     The width of the map. Must be a positive integer.
     * @param height    The height of the map. Must be a positive integer.
     * @return          A newly created {@code Map} instance with the specified name and dimensions.
     * @throws          IllegalArgumentException if a map with the same name already exists or if the dimensions are invalid.
     */
    public Map createMap(String name, int width, int height, int size){
        if(Repositories.getInstance().getMapRepository().existsByName(name)){
            throw new IllegalArgumentException("Map with this name already exists.");
        }
        Map map = new Map(name, width, height, size);
        Repositories.getInstance().getMapRepository().add(map);
        return map;
    }
}
```

### Class MapRepository

```java
package repositories;

import domain.Map;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * The MapRepository class is used to manage a collection of Map instances.
 * It provides methods to add, retrieve, and perform operations on the stored maps.
 */
public class MapRepository implements Serializable {
    private List<Map> maps;

    /**
     * Constructs a new instance of the {@code MapRepository} class.
     * This initializes the internal collection used to store maps.
     */
    public MapRepository() {
        maps = new ArrayList<>();
    }

    /**
     * Adds a new map to the repository if it passes validation.
     *
     * @param map The Map instance to be added to the repository. It must not already exist in the collection.
     */
    public void add(Map map){
        if(validateMap(map)){
            maps.add(map);
        }
    }

    /**
     * Validates if the given map instance is not already present in the repository's collection.
     *
     * @param map   The Map instance to validate. It must not be null.
     * @return      True if the map is not already contained in the repository's collection; false otherwise.
     */
    private boolean validateMap(Map map){
        return !maps.contains(map);
    }

    /**
     * Checks if a map with the specified name exists in the repository.
     *
     * @param name The name of the map to check for existence. Must not be null.
     * @return {@code true} if a map with the given name exists in the repository;
     *         {@code false} otherwise.
     */
    public boolean existsByName(String name) {
        for (int i = 0; i < maps.size(); i++) {
            if (maps.get(i).getName().equalsIgnoreCase(name)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Retrieves an unmodifiable list of maps stored in the repository.
     * The returned list cannot be modified and reflects the current state
     * of the internal collection of maps.
     *
     * @return An unmodifiable list containing all maps currently stored in the repository.
     */
    public List<Map> getMapList() {
        return List.copyOf(maps);
    }

    /**
     * Retrieves a map from the repository by its name.
     *
     * This method searches through the collection of maps in the repository and
     * attempts to locate a map instance that matches the specified name. If a
     * matching map is found, it is returned wrapped in an {@code Optional}. If no
     * map is found with the given name, an empty {@code Optional} is returned.
     *
     * @param name  The name of the map to retrieve. Must not be null.
     * @return      An {@code Optional} containing the matching {@code Map} if found,
     *              or an empty {@code Optional} if no match is found.
     */
    public Optional<Map> getMap(String name) {
        return maps.stream().filter(m -> m.getName().equals(name)).findFirst();
    }

}
```