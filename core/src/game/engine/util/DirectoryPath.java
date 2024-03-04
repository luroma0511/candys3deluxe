package game.engine.util;

public class DirectoryPath {

    private static final String path = System.getProperty("user.home") + "\\AppData\\Roaming\\.candys3deluxe\\assets\\";

    public static String getPath(){
        return path;
    }
}
