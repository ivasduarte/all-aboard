package domain;

import java.io.*;

/**
 * Class responsible for saving map data to a file.
 * Implements Runnable to allow for asynchronous saving operations.
 */
public class MapSave implements Runnable, Serializable {

    private final Map map;

    /**
     * Creates a new MapSave instance for the specified map.
     *
     * @param map The map to be saved
     */
    public MapSave(Map map) {
        this.map = map;
    }

    /**
     * Saves the specified map to a file in the 'maps' directory.
     * The file will be named after the map and have a .map extension.
     *
     * @param map The map to be saved
     * @throws IOException if there is an error during file operations
     */
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

    /**
     * Executes the save operation when this class is run as a thread.
     * This method is called automatically when the class is executed as a Runnable.
     */
    @Override
    public void run() {
        try {
            save(map);
        } catch (IOException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
}
