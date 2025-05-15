# US003 - Add a city 

## 4. Tests

**_DO NOT COPY ALL DEVELOPED TESTS HERE_**

**Test 1:** Check that it is not possible to create an instance of the Example class with null values.

	@Test(expected = IllegalArgumentException.class)
		public void ensureNullIsNotAllowed() {
		Exemplo instance = new Exemplo(null, null);
	}

**Test 2:** Check if a city's name does not have any special characters

    @Test(expected = IllegalArgumentException.class)
        public void ensureCityNameHasNoSpecialCharacters() {
            City city = new City("São@Paulo", 10, 15, 12);
        }


**Test 3:** Check if a city's name does not contain any digits
    
    @Test(expected = IllegalArgumentException.class)
        public void ensureCityNameHasNoDigits() {
            City city = new City("City123", 5, 10, 20);
        }

**Test 4:** Check if the position where this city is located is valid 

    @Test(expected = IllegalArgumentException.class)
        public void ensureUniqueCityPosition() {
            CityRepository repo = new CityRepository();
            repo.addCity(new City("Porto", 10, 20, 5));
            repo.addCity(new City("Braga", 10, 20, 6)); 
        }





## 5. Construction (Implementation)

**Class CreateCityController**

```java
public City createCity(String name, String map, int x, int y) {
    if (validateNameMap(name, map) != NameMapError.OK || validateCenter(name, map, x, y) != CenterError.OK) {
        throw new IllegalStateException("Cannot create city: validation failed");
    }
    City c = new City(name, map, x, y);
    cityRepo.add(c);
    return c;
}
```

**Class CityRepository**

```java
public void add(City c) { 
    cities.add(c); 
}
```



## 6. Integration and Demo

_In this section, it is suggested to describe the efforts made to integrate this functionality with the other features of the system._


## 7. Observations

_In this section, it is suggested to present a critical perspective on the developed work, pointing, for example, to other alternatives and or future related work._

N/A