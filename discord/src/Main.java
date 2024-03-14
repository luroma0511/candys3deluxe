import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class Main {
    public static void main(String[] args) {
        Discord discordRichPresence = new Discord();
        discordRichPresence.createInstance();
        discordRichPresence.fixRoot();
        ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();
        executor.scheduleAtFixedRate(discordRichPresence, 100, 100, TimeUnit.MILLISECONDS);
    }
}
