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

    /**
     * This method is responsible to add locomotives to the list of all locomotives available in the simulator.
     *
     * @param locomotive predefined locomotives
     */
    public void saveLocomotive(Locomotive locomotive) {
        locomotives.add(locomotive);
    }

    /**
     * This method saves the locomotives to the list of bought locomotives.
     *
     * @param locomotive bought locomotive
     */
    public void save(Locomotive locomotive) {
        playerLocomotives.add(locomotive);
    }

    /**
     * This method is responsible to get a list of the available locomotives based on the scenario restrictions.
     *
     * @param scenarioId The id of the scenario that is being played.
     *
     * @return list of available locomotives
     */
    public List<Locomotive> getAvailableLocomotives(int scenarioId) {
        List<Scenario> scenarios = Repositories.getInstance().getScenarioRepository().getScenarioList();
        Scenario scenario = scenarios.get(scenarioId);
        Restrictions scenarioRestrictions = scenario.getRestrictions();
        List<String> availableLocomotiveTypes = scenarioRestrictions.getLocomotiveTypes();
        LocalDate scenarioStartDate = scenarioRestrictions.getStartDate();

        List<Locomotive> validTypeLocomotives = new ArrayList<>();

        for (int i = 0; i < locomotives.size(); i++) {
            for (int j = 0; j < availableLocomotiveTypes.size(); j++) {
                if (locomotives.get(i).getLocomotiveFuelType().equals(availableLocomotiveTypes.get(j))) {   // this line has a high chance of being wrong
                    validTypeLocomotives.add(locomotives.get(i));
                }
            }
        }   // method needs further review

        for (int i = 0; i < validTypeLocomotives.size(); i++) {
            if (validTypeLocomotives.get(i).getStartYear() <= scenarioStartDate.getYear()) {
                availableLocomotives.add(validTypeLocomotives.get(i));
            }
        }

        return availableLocomotives;
    }

    /**
     * This method is responsible to get a locomotive by its serial number.
     *
     * @param serialNumber The locomotive serial number
     *
     * @return locomotive
     */
    public Locomotive getLocomotiveBySerialNumber(int serialNumber) {
        return availableLocomotives.get(serialNumber);
    }

    /**
     * This method is responsible to show the locomotive list in the form of a list.
     */
    public void displayAvailableLocomotives() {
        for (int i = 0; i < availableLocomotives.size(); i++) {
            System.out.println((i+1) + ". " + availableLocomotives.get(i));
        }
    }
}
