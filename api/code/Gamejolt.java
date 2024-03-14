public class Gamejolt implements Runnable {

    @Override
    public void run(){
        while (!Main.terminate){
            try {
                System.out.println("Gamejolt Response");
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}