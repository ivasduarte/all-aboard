package domain;

import java.io.*;

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

        file.createNewFile();

        try {
            ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(file, false));

            out.writeObject(map);
            out.close();

            System.out.println("\n" + map.getName() + " has been saved!");
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
