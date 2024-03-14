
public class Discord implements Runnable {

    @Override
    public void run(){
        DiscordClient
        while (!Main.terminate){
            try {
                System.out.println("Discord Response");
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}