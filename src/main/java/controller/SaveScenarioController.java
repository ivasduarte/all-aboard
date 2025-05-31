package controller;

import domain.Map;
import domain.Scenario;
import repositories.MapRepository;
import repositories.Repositories;
import repositories.ScenarioRepository;

import java.io.FileInputStream;
import java.io.ObjectInputStream;
import java.util.List;

public class SaveScenarioController {

    private final ScenarioRepository scenarioRepository;

    private final MapRepository mapRepository;

    public SaveScenarioController() {
        this.scenarioRepository = Repositories.getInstance().getScenarioRepository();
        this.mapRepository = Repositories.getInstance().getMapRepository();
    }

    public void saveScenarioInFile(Scenario scenario) {
        if (scenario == null) {
            throw new IllegalArgumentException("Scenario is null");
        }
        scenarioRepository.saveScenarioToFile(scenario);
    }


    public List<Map> getMapList() {
        return mapRepository.getMapList();
    }


    public List<Scenario> getScenarioList() {
        return scenarioRepository.getScenarioList();
    }

    public Scenario loadScenarioFromFile(String fileName, Map selectedMap) {
        return scenarioRepository.loadScenarioFromFile(fileName, selectedMap);
    }
}