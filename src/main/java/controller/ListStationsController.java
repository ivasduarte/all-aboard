package controller;

import domain.Station;
import repositories.Repositories;
import repositories.StationRepository;

import java.util.List;


public class ListStationsController {

    private final StationRepository StationRepo;

    public ListStationsController() {

        this.StationRepo = Repositories.getInstance().getStationRepository();

    }

    public List<Station> getAllStations() {

        return StationRepo.getStations();

    }
}
