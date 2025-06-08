package controller;

import domain.Station;
import domain.Train;

import java.util.List;

public class AssignRouteController {

    public void AssignRoute(Train train, List<Station> stations)
    {
        train.setRoute(stations);
    }

}
