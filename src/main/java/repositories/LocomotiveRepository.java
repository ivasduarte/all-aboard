package repositories;

import domain.Locomotive;
import domain.Restrictions;
import domain.Scenario;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class LocomotiveRepository {
    private List<Locomotive> locomotives = new ArrayList<>();
    private List<Locomotive> availableLocomotives = new ArrayList<>();
    private List<Locomotive> playerLocomotives = new ArrayList<>();

    public void saveLocomotive(Locomotive locomotive) {
        locomotives.add(locomotive);
    }

    public void save(Locomotive locomotive) {
        playerLocomotives.add(locomotive);
    }

    public List<Locomotive> getAvailableLocomotives(int scenarioId) {
        List<Scenario> scenarios = Repositories.getInstance().getScenarioRepository().getScenarioList();
        Scenario scenario = scenarios.get(scenarioId);
        Restrictions scenarioRestrictions = scenario.getRestrictions();
        List<String> availableLocomotiveTypes = scenarioRestrictions.getLocomotiveTypes();
        LocalDate scenarioStartDate = scenarioRestrictions.getStartDate();

        List<Locomotive> validTypeLocomotives = new ArrayList<>();

        for (int i = 0; i < locomotives.size(); i++) {
            for (int j = 0; j < availableLocomotiveTypes.size(); j++) {
                if (locomotives.get(i).getLocomotiveType().equals(Locomotive.LocomotiveType.valueOf(availableLocomotiveTypes.get(j)))) {
                    validTypeLocomotives.add(locomotives.get(i));
                }
            }
        }

        for (int i = 0; i < validTypeLocomotives.size(); i++) {
            if (validTypeLocomotives.get(i).getStartYear() <= scenarioStartDate.getYear()) {
                availableLocomotives.add(validTypeLocomotives.get(i));
            }
        }

        return availableLocomotives;
    }

    public Locomotive getLocomotiveBySerialNumber(int serialNumber) {
        return availableLocomotives.get(serialNumber);
    }
}
