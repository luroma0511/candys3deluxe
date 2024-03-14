import club.minnced.discord.rpc.DiscordEventHandlers;
import club.minnced.discord.rpc.DiscordRPC;
import club.minnced.discord.rpc.DiscordRichPresence;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;

public class Discord implements Runnable {
    File file;

    public void createInstance(){
        DiscordRPC lib = DiscordRPC.INSTANCE;
        String applicationId = "";
        String steamId = "";
        DiscordEventHandlers handlers = new DiscordEventHandlers();
        handlers.ready = (user) -> System.out.println("Ready!");
        lib.Discord_Initialize(applicationId, handlers, true, steamId);
        DiscordRichPresence presence = new DiscordRichPresence();
        presence.startTimestamp = System.currentTimeMillis() / 1000; // epoch second
        presence.details = "Testing RPC";
        System.out.println(presence.details);
        lib.Discord_UpdatePresence(presence);
        // in a worker thread
        new Thread(() -> {
            while (!Thread.currentThread().isInterrupted()) {
                lib.Discord_RunCallbacks();
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException ignored) {}
            }
        }, "RPC-Callback-Handler").start();
    }

    public void fixRoot(){
        Path root = Paths.get("").toAbsolutePath();
        while (!root.getFileName().toString().equals("candys3deluxe")) {
            root = root.getParent();
        }
        Path path = root.resolve("logs").resolve("launcher_log.json");
        file = new File(path.toUri());
    }

    @Override
    public void run(){
        if (!file.exists()) terminate();
        System.out.println("JSON detected");
    }

    private void terminate(){
        System.exit(0);
    }
}
