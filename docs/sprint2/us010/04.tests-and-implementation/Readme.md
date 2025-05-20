# US010 - Assign a Train to a Route
## 4. Tests

**_DO NOT COPY ALL DEVELOPED TESTS HERE_**

**Test 1:** Check that if route is not null.

```java
@Test
void checkNotNull() {
    locomotive.setRoute(route);
    
    assertNotNull(locomotive.getRoute());
}
```

## 5. Construction (Implementation)

**Class AssignRouteController**

```java
public void AssignRoute(Locomotive locomotive, List<Station> stations)
{
    locomotive.setRoute(stations);
}
```