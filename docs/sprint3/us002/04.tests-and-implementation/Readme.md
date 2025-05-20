# US002 - Add an industry

## 4. Tests

**_DO NOT COPY ALL DEVELOPED TESTS HERE_**

**Test 1:** Check that if the position where the `Industry` is trying to be added is valid.

```java
void validatePosition_Invalid() {
    Industry invalidIndustry = new Industry(0, 0, 150, 75, "Invalid industry") {
        @Override
        public boolean validatePosition() {
            int width = 100;
            int height = 100;

            return getPositionX() >= 0 && getPositionX() <= width &&
                    getPositionY() >= 0 && getPositionY() <= height;
        }
    };

    assertFalse(invalidIndustry.validatePosition());
}
```

**Test 2:** Check if there are no other buildings on the position where the industry is being added.

```java
void checkOverbuilding() {
    Industry conflictIndustry = new Industry(0, 0, 25, 75, "Primary sector industry") {
        @Override
        public boolean checkOverbuilding() {
            int otherX = 25;
            int otherY = 75;

            return !(getPositionX() == otherX && getPositionY() == otherY);
        }
    };

    assertFalse(conflictIndustry.checkOverbuilding());
}
```

**Test 3:** Check if the list that is being returned by the `IndustryRepository` is correct.

```java
void getIndustryList() {
    Industry testIndustry2 = new Industry(0, 0, 25, 25, "Transforming industry");
    indRepo.save(testIndustry);
    indRepo.save(testIndustry2);

    List<Industry> industries = indRepo.getIndustryList();

    assertEquals(2, industries.size());
    assertTrue(industries.contains(testIndustry));
    assertTrue(industries.contains(testIndustry2));
}
```

## 5. Construction (Implementation)

**Class AddIndustryController**

```java
public String addIndustry(int mapID, int scenarioID, int positionX, int positionY, String industryType) {
    IndustryRepository industryRepository = Repositories.getInstance().getIndustryRepository();

    Industry industry = new Industry(mapID, scenarioID, positionX, positionY, industryType);

    if (!industry.validatePosition()) {
        return "ERROR: Invalid position";
    } else if (!industry.checkOverbuilding()) {
        return "ERROR: Position is already occupied.";
    }

    industryRepository.save(industry);
    return "Industry [" + positionX + ", " + positionY + "] saved successfully.";
}
```

**Class IndustryRepository**

```java
public void save(Industry industry) {
    industries.add(industry);
}
```

**Class Industry**

```java
public Industry(int mapID, int scenarioID, int positionX, int positionY, String industryType) {
    this.mapID = mapID;
    this.scenarioID = scenarioID;
    this.positionX = positionX;
    this.positionY = positionY;
    this.industryType = industryType;
}
```