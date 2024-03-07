package util;

import java.nio.file.Path;
import java.nio.file.Paths;

public class JavaInfo {
    private static final Path projectDir = Paths.get("candys3deluxe").toAbsolutePath();
    private static final String path = projectDir.resolveSibling("assets") + "\\";
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
