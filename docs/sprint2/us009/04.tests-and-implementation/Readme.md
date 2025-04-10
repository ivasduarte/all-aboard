# US009 - Buy a Train

## 4. Tests

**_DO NOT COPY ALL DEVELOPED TESTS HERE_**

**Test 1:** Check that it is not possible to create a `Locomotive` with null or invalid parameters

```java
@Test(expected = IllegalArgumentException.class)
public void ensureNullIsNotAllowed() {
    Locomotive loco = new Locomotive(null, null, null, 0, 0, 0, 0, null);
}
```

**Test 2:** Check that a locomotive has a valid (non-null and non-empty) serial number

```java
@Test(expected = IllegalArgumentException.class)
public void ensureLocomotiveHasSerialNumber() {
    Locomotive loco = new Locomotive("Express", "", LocomotiveType.Steam, 4000, 80, 150, 1920, AcquisitionType.PURCHASED);
}
```

**Test 3:** Check that the player can select a locomotive from a list of available locomotives

```java
@Test
public void ensureLocomotiveCanBeSelected() {
    List<Locomotive> availableLocomotives = scenario.getAvailableLocomotives();
    Locomotive selected = availableLocomotives.get(0);
    assertNotNull(selected);
}
```

**Test 4:** Check that the selected locomotive has a valid entry into service period (1 year from purchase)

```java
@Test
public void ensureValidEntryIntoServicePeriod() {
    LocalDate purchaseDate = LocalDate.of(2025, 1, 1);
    Locomotive loco = new Locomotive("Alpha", "SN-001", LocomotiveType.Diesel, 5000, 90, 180, 2025, AcquisitionType.PURCHASED);
    
    LocalDate expectedEndOfService = purchaseDate.plusYears(1);
    assertEquals(expectedEndOfService, loco.getEntryIntoServiceEndDate(purchaseDate));
}
```


## 5. Construction (Implementation)

**Class BuyTrainController**

**Class TrainRepository**

**Class Locomotive**


## 6. Integration and Demo

_In this section, it is suggested to describe the efforts made to integrate this functionality with the other features of the system._


## 7. Observations

_In this section, it is suggested to present a critical perspective on the developed work, pointing, for example, to other alternatives and or future related work._
