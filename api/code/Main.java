import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Main {
    static boolean terminate;

    public static void main(String[] args) throws InterruptedException {
        Discord discord = new Discord();
        Gamejolt gamejolt = new Gamejolt();
        RequestLauncherConnection requestLauncherConnection = new RequestLauncherConnection();
        ExecutorService executor = Executors.newFixedThreadPool(3);
        executor.execute(discord);
        executor.execute(gamejolt);
        executor.execute(requestLauncherConnection);

        while (!terminate) {
            Thread.sleep(100);
        }
        executor.shutdown();
    }
}