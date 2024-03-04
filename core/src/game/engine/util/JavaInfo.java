package game.engine.util;

public class JavaInfo {

    private static final String path = System.getProperty("user.home") + "\\AppData\\Roaming\\.candys3deluxe\\assets\\";
    private static final String javaPath = System.getProperty("java.home");
    private static final String jre = System.getProperty("java.version");

    public static String getPath(){
        return path;
    }

    public static String getJavaPath(){
        return javaPath;
    }

    public static String getJre(){
        return jre;
    }
}
