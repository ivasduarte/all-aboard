package domain;

import java.io.*;

public class MapSave implements Runnable, Serializable {

    private final Map map;

    public MapSave(Map map) {
        this.map = map;
    }

    public void save(Map map) throws IOException {

        String directory = "maps";
        File directoryFile = new File(directory);
        directoryFile.mkdirs();

        String fileName = directory + "/" + map.getName() + ".map";

        File file = new File(fileName);
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
    public void run() {
        try {
            save(map);
        } catch (IOException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
}
