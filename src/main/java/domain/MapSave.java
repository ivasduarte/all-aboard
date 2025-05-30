package domain;

import repositories.CityRepository;
import repositories.IndustryRepository;
import repositories.Repositories;
import repositories.StationRepository;

import java.io.*;
import java.util.List;

public class MapSave implements Runnable, Serializable {

    private final String filePath;
    private final Map map;

    public MapSave(String fileName, Map map)
    {
        this.filePath = fileName;
        this.map = map;
    }

    public void save(String filePath, Map map) throws IOException {

        File file = new File(filePath);

        StationRepository stationRepository = Repositories.getInstance().getStationRepository();
        CityRepository cityRepository = Repositories.getInstance().getCityRepository();
        IndustryRepository industryRepository = Repositories.getInstance().getIndustryRepository();

        List<Station> stations = stationRepository.getAvailableStationsByMapName(map.getName());
        List<City> cities = cityRepository.getCitiesByMap(map.getName());
        List<Industry> industries = industryRepository.getIndustriesByMap(map);

        file.createNewFile();
        
        try {
            ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(file));

            System.out.println("Out made");
            out.writeObject(map);
            System.out.println("map added");
            out.writeObject(stations);
            System.out.println("stations added");
            out.writeObject(cities);
            System.out.println("cities added");
            out.writeObject(industries);
            System.out.println("industries added");
            out.close();
            System.out.println("closed");

            System.out.println(map.getName() + " has been saved!");
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    @Override
    public void run()
    {
        try {
            save(filePath, map);
        } catch (IOException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
}
