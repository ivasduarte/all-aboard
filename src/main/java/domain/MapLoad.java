package domain;

import repositories.MapRepository;
import repositories.Repositories;

import java.io.*;
import java.util.List;
import java.util.Scanner;

public class MapLoad implements Runnable, Serializable {
    public void load() throws IOException
    {
        Scanner sc = new Scanner(System.in);

        System.out.print("\n\nEnter the path of the map file: ");
        String filePath = sc.next();

        File file = new File(filePath);

        if(!file.exists() || file.isDirectory())
        {
            System.out.println("File path is not valid!");
            return;
        }

        try {
            ObjectInputStream in = new ObjectInputStream(new FileInputStream(file));

            Map map = (Map) in.readObject();

            if(map != null)
            {
                MapRepository mapRepository = Repositories.getInstance().getMapRepository();
                mapRepository.add(map);

                List<Station> stations = map.getStations();
                List<City> cities = map.getCities();
                List<Industry> industries = map.getIndustries();

                Repositories.getInstance().getStationRepository().save(stations);
                Repositories.getInstance().getCityRepository().save(cities);
                Repositories.getInstance().getIndustryRepository().save(industries);

                System.out.println("\n" + map.getName() + " has been loaded!");
            }

        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    @Override
    public void run()
    {
        try {
            load();
        } catch (IOException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
}
