public class RequestLauncherConnection implements Runnable {

    @Override
    public void run(){
        long startTime = System.currentTimeMillis();
        while (System.currentTimeMillis() - startTime < 5_000){
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        Main.terminate = true;
    }
}
