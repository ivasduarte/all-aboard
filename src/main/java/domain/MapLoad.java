package domain;

import repositories.MapRepository;
import repositories.Repositories;

import java.io.*;
import java.util.List;
import java.util.Scanner;

public class MapLoad implements Runnable, Serializable {
    public void load() throws IOException, ClassNotFoundException {
        Scanner sc = new Scanner(System.in);

        File dir = new File("maps");
        File[] mapFiles = dir.listFiles((d, name) -> name.endsWith(".map"));

        if (mapFiles == null || mapFiles.length == 0) {
            System.out.println("No map save files found.");
            return;
        }

        System.out.println("Available map files:");
        for (int i = 0; i < mapFiles.length; i++) {
            System.out.printf("%d - %s%n", i + 1, mapFiles[i].getName());
        }

        System.out.println("0 - Cancel");

        System.out.print("Select a file number to load: ");

        int option = Integer.parseInt(sc.nextLine());

        if (option == 0) {
            System.out.println("Operation cancelled.");
            return;
        }

        if (option < 1 || option > mapFiles.length) {
            System.out.println("Invalid option.");
            return;
        }

        File file = mapFiles[option-1];

        if(!file.exists() || file.isDirectory())
        {
            System.out.println("File path is not valid!");
            return;
        }

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
    }

    @Override
    public void run()
    {
        try {
            load();
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
}
