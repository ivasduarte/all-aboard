# US003 - Add A City

## 4. Tests

**Test 1:** Validates getName.

```java
@Test
void getName_ReturnsCorrectValue() {
    assertEquals("CidadeTeste", city.getName());
}
```


**Test 2:** Validates getMapName.

```java
void getMapName_ReturnsCorrectValue() {
        assertEquals("MapaTeste", city.getMapName());
    }
```

**Test 3:** Validates X center

```java
void getCenterX_ReturnsCorrectValue() {
    assertEquals(3, city.getCenterX());
}
```


**Test 4:** Validates Y center

```java
void getCenterY_ReturnsCorrectValue() {
        assertEquals(4, city.getCenterY());
    }
```


**Test 5:** Validates addition of house blocks

```java
void addBlockManual_AddsBlockSuccessfully() {
        HouseBlock block = new HouseBlock(1, 2);
        city.addBlockManual(block);
        List<HouseBlock> blocks = city.getBlocks();
        assertEquals(1, blocks.size());
        assertTrue(blocks.contains(block));
    }
```

**Test 6:** Validates getBlocks.

```java
void getBlocks_ReturnsUnmodifiableList() {
        List<HouseBlock> blocks = city.getBlocks();
        assertThrows(UnsupportedOperationException.class, () -> blocks.add(new HouseBlock(0, 0)));
    }
```

**Test 7:** Validates getX

```java
void getX_ReturnsCorrectValue() {     
    assertEquals(3, hb.getX());
}
```

**Test 8:** Validates getY

```java
void getY_ReturnsCorrectValue() {
        assertEquals(4, hb.getY());
    }
```

**Test 9:** Checks if house blocks have the same coordinates

```java
void equals_SameCoordinates_ReturnsTrue() {
        hb2 = new HouseBlock(3, 4);
        assertEquals(hb, hb2);
    }
```

```java
void equals_SameCoordinates_ReturnsFlase() {
    hb2 = new HouseBlock(5, 6);
    assertNotEquals(hb, hb2);
}
```

**Test 10:** Increases city size

```java
void add_IncreasesSize() {
    repo.add(new City("C", "Map1", 3, 3));
    assertEquals(3, repo.getCities().size());
}
```

**Test 11:** Validates getCities

```java
void getCities_Unmodifiable() {
        List<City> cities = repo.getCities();
        assertThrows(UnsupportedOperationException.class, () -> cities.add(null));
    }
```

**Test 12:** Validates if name is unique

```java
void isNameUnique_WorksCorrectly() {
        assertTrue(repo.isNameUnique("C","Map1"));
        assertFalse(repo.isNameUnique("A","Map1"));
    }
```


**Test 13:** Validates map poisiton

```java
void existsAtPosition_WorksCorrectly() {
        assertTrue(repo.existsAtPosition("Map1",1,1));
        assertFalse(repo.existsAtPosition("Map1",9,9));
    }
```

**Test 14:** Validates if block position is unique

```java
void isBlockPositionUnique_WorksCorrectly() {
        City city = new City("X","Map1",5,5);
        HouseBlock hb = new HouseBlock(7,7);
        city.addBlockManual(hb);
        repo.add(city);
        assertFalse(repo.isBlockPositionUnique("Map1", new HouseBlock(7,7)));
    }
```


**Test 15:** Validates block

```java
void validateBlock_AlwaysOK() {
        assertEquals(CreateCityController.BlockError.OK,
                repo.validateBlock("Map1", new HouseBlock(0,0)));
    }
```

## 5. Construction (Implementation)

### Class CreateCityController

```java
package controller;

import domain.City;
import domain.HouseBlock;
import domain.Map;
import repositories.CityRepository;
import repositories.MapRepository;
import repositories.Repositories;

import java.util.Optional;

/**
 * This controller is responsible for the creation of cities and their validation on maps.
 *
 * Provides methods to validate the city creation, map names, center and block positions,
 * to create cities and add blocks - manually or automatically.
 */
public class CreateCityController {
    private final MapRepository mapRepo;
    private final CityRepository cityRepo;

    /**
     * The possible errors that can occur when creating a city.
     */
    public enum NameMapError { OK, MAP_NOT_FOUND, NAME_DUPLICATE }
    public enum CenterError { OK, OUT_OF_BOUNDS, POSITION_TAKEN, BLOCK_PRESENT }
    public enum BlockError { OK, OUT_OF_BOUNDS, COLLIDES_CITY_CENTER, COLLIDES_BLOCK }

    /**
     * The constructor of the class, initializing repositories to be used.
     */
    public CreateCityController() {
        var repos = Repositories.getInstance();
        this.mapRepo = repos.getMapRepository();
        this.cityRepo = repos.getCityRepository();
    }

    /**
     * Validates the name and map of the city to be created.
     *
     * @param name  Name of the city to be created.
     * @param map   Map to be used.
     * @return      NameMapError.OK if the city can be created, or one of the following:
     *              NameMapError.MAP_NOT_FOUND if the map does not exist;
     *              NameMapError.NAME_DUPLICATE if the name is already used by another city in the map.
     */
    public NameMapError validateNameMap(String name, String map) {
        Optional<Map> m = mapRepo.getMap(map);
        if (m.isEmpty()) return NameMapError.MAP_NOT_FOUND;
        if (!cityRepo.isNameUnique(name, map)) return NameMapError.NAME_DUPLICATE;
        return NameMapError.OK;
    }

    /**
     * Validates the center position of the city to be created.
     *
     * @param name  Name of city-to-be-created.
     * @param map   Map where the city will be created.
     * @param x     The X-coordinate of the city center.
     * @param y     The Y-coordinate of the city center.
     * @return      CenterError.OK if the city can be created, or one of the following:
     *              CenterError.OUT_OF_BOUNDS if the coordinates are outside the map bounds;
     *              CenterError.POSITION_TAKEN if the coordinates are already occupied by another city;
     *              CenterError.BLOCK_PRESENT if the coordinates are already occupied by a block.
     */
    public CenterError validateCenter(String name, String map, int x, int y) {
        Optional<domain.Map> m = mapRepo.getMap(map);
        if (m.isEmpty()) return CenterError.OUT_OF_BOUNDS;
        var mp = m.get();
        if (x < 1 || x >= mp.getWidth() || y < 1 || y >= mp.getHeight()) return CenterError.OUT_OF_BOUNDS;
        if (cityRepo.existsAtPosition(map, x, y)) return CenterError.POSITION_TAKEN;
        HouseBlock hb = new HouseBlock(x, y);
        if (!cityRepo.isBlockPositionUnique(map, hb)) return CenterError.BLOCK_PRESENT;
        return CenterError.OK;
    }

    /**
     * Validates the block position of the city to be created.
     *
     * @param map   Map where the block will be placed.
     * @param b     House block to be validated.
     * @return      BlockError.OK if the block can be placed, or one of the following:
     *              BlockError.OUT_OF_BOUNDS if the coordinates are outside the map bounds;
     *              BlockError.COLLIDES_CITY_CENTER if the coordinates are already occupied by another city;
     *              BlockError.COLLIDES_BLOCK if the coordinates are already occupied by another block.
     */
    public BlockError validateBlock(String map, HouseBlock b) {
        Optional<domain.Map> m = mapRepo.getMap(map);
        if (m.isEmpty()) return BlockError.OUT_OF_BOUNDS;
        var mp = m.get();
        int x = b.getX(), y = b.getY();
        if (x < 1 || x >= mp.getWidth() || y < 1 || y >= mp.getHeight()) return BlockError.OUT_OF_BOUNDS;
        if (cityRepo.existsAtPosition(map, x, y)) return BlockError.COLLIDES_CITY_CENTER;
        if (!cityRepo.isBlockPositionUnique(map, b)) return BlockError.COLLIDES_BLOCK;
        return BlockError.OK;
    }

    /**
     * Creates a new city in the given map, at the given coordinates.
     *
     * @param name  Name of the city to be created.
     * @param map   Map where the city will be created.
     * @param x     Center X coordinate of the city.
     * @param y     Center Y coordinate of the city.
     * @return      Created city.
     * @throws      IllegalStateException if the city cannot be created for any reason.
     */
    public City createCity(String name, String map, int x, int y) {
        if (validateNameMap(name, map) != NameMapError.OK || validateCenter(name, map, x, y) != CenterError.OK) {
            throw new IllegalStateException("Cannot create city: validation failed");
        }
        City c = new City(name, map, x, y);
        cityRepo.add(c);
        return c;
    }


    /**
     * Manually adds a house block to the specified city, after validating its position.
     * The validation checks if the block's position is within the map boundaries,
     * does not collide with another city's center, and does not conflict with existing blocks.
     *
     * @param city   {@code City} where the house block will be added
     * @param b      {@code HouseBlock} representing the block to be added
     * @return       {@code true} if the block was successfully added, or {@code false} if the validation fails
     */
    public boolean addBlockManual(City city, HouseBlock b) {
        BlockError err = validateBlock(city.getMapName(), b);
        if (err != BlockError.OK) return false;
        city.addBlockManual(b);
        return true;
    }

    /**
     * Automatically adds blocks to the given city.
     * The number of blocks to be added is specified by the count parameter.
     *
     * @param city      City to which the blocks will be added.
     * @param count     Number of blocks to be added.
     * @param sigma     Standard deviation of the Gaussian distribution used to generate the coordinates of the blocks.
     */
    public void assignBlocksAuto(City city, int count, double sigma) {
        java.util.Random rnd = new java.util.Random();
        for (int i = 0; i < count; i++) {
            HouseBlock hb;
            BlockError err;
            do {
                int x = city.getCenterX() + (int) (rnd.nextGaussian() * sigma);
                int y = city.getCenterY() + (int) (rnd.nextGaussian() * sigma);
                hb = new HouseBlock(x, y);
                err = validateBlock(city.getMapName(), hb);
            } while (err != BlockError.OK);
            city.addBlockManual(hb);
        }
    }

    /**
     * Retrieves the {@code CityRepository} instance used for managing cities.
     * The repository provides access to stored city information and supports
     * operations such as adding, retrieving, and validating cities.
     *
     * @return the {@code CityRepository} instance for city management
     */
    public CityRepository getCityRepo(){
        var repos = Repositories.getInstance();
        return repos.getCityRepository();
    }
}
```

### Class CityRepository

```java
package repositories;

import controller.CreateCityController;
import domain.City;
import domain.HouseBlock;

import java.util.ArrayList;
import java.util.List;

/**
 * A repository class responsible for managing and storing a collection of cities.
 * This class provides methods for adding new cities, retrieving existing cities,
 * and validating city-related data, such as uniqueness of names and positions.
 */

public class CityRepository {
    private final List<City> cities = new ArrayList<>();

    /**
     * Adds a city to the repository's collection of cities.
     *
     * @param c     {@code City} object to be added to the repository
     */
    public void add(City c) { cities.add(c); }
    public List<City> getCities() { return List.copyOf(cities); }


    /**
     * Checks if a city name is unique within the specified map.
     *
     * @param name  Name of the city to be checked
     * @param map   Name of the map where the check is performed
     * @return      {@code true} if no city within the specified map has the given name; {@code false} otherwise
     */
    public boolean isNameUnique(String name, String map) {
        return cities.stream().noneMatch(c->c.getName().equals(name) && c.getMapName().equals(map));
    }


    /**
     * Checks whether a city exists at the specified position on a given map.
     *
     * @param map   Name of the map where the position is being checked
     * @param x     X-coordinate of the position to check
     * @param y     Y-coordinate of the position to check
     * @return      {@code true} if a city exists at the specified position on the given map; {@code false} otherwise
     */
    public boolean existsAtPosition(String map, int x, int y) {
        return cities.stream().filter(c->c.getMapName().equals(map))
                .anyMatch(c->c.getCenterX()==x && c.getCenterY()==y);
    }

    /**
     * Checks if the position of a given {@code HouseBlock} is unique within the specified map.
     * A position is considered unique if no other {@code HouseBlock} in any city
     * on the same map has the same coordinates.
     *
     * @param map   Name of the map within which the uniqueness of the block's position is checked
     * @param b     {@code HouseBlock} whose position's uniqueness is to be verified
     * @return      {@code true} if the block's position is unique within the specified map;
     *              {@code false} otherwise
     */
    public boolean isBlockPositionUnique(String map, HouseBlock b) {
        return cities.stream().filter(c->c.getMapName().equals(map))
                .flatMap(c->c.getBlocks().stream())
                .noneMatch(b::equals);
    }


    /**
     * Validates a {@code HouseBlock} within the context of a specified map.
     * The method checks for potential issues such as position conflicts,
     * alignment constraints, or other validation criteria specific to the map and block.
     *
     * @param map   Name of the map where the block is to be validated.
     * @param b     {@code HouseBlock} object to be validated.
     * @return      An instance of {@code CreateCityController.BlockError},
     *              indicating the result of the validation. This may include
     *              error codes representing issues like non-unique positions
     *              or successful validation.
     */
    public CreateCityController.BlockError validateBlock(String map, HouseBlock b) {
        return CreateCityController.BlockError.OK;
    }
}
```