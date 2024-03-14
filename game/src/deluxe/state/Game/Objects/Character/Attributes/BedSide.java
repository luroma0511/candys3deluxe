package deluxe.state.Game.Objects.Character.Attributes;

import java.util.Random;
import util.Time;

public class BedSide {
    private float frame;
    private int phase;
    private int targetPhase;
    private boolean retreat;
    private float delay;
    private float flashTime;
    private boolean signal;
    private boolean soundLock;

    private final float initialDelay;


    public BedSide(float delay){
        initialDelay = delay;
    }

    public void reset(){
        delay = initialDelay;
        retreat = false;
        phase = 0;
        targetPhase = 0;
        frame = 0;
        flashTime = 0;
    }

    public boolean input(Hitbox hitbox, float mx, float my){
        boolean sound = true;
        if (hitbox.isHovered(mx, my)) flashTime = Time.increaseTimeValue(flashTime, 1.25f, 1);
        else if (!retreat) {
            flashTime = Time.decreaseTimeValue(flashTime, 0, 2);
            sound = false;
        }
        return sound;
    }

    public int update(Random random, int side){
        signal = false;
        if (flashTime == 1.25f) retreat = true;
        else if (flashTime == 0) delay = Time.decreaseTimeValue(delay, 0, 1);
        if (delay == 0) {
            delay = 6;
            if (targetPhase == 0) side = random.nextInt(2) * 2;
            targetPhase++;
        }
        if (retreat) {
            frame = Time.decreaseTimeValue(frame, 0, 48);
            return side;
        }
        float limit = 0;
        if (targetPhase == 1) limit = 23;
        else if (targetPhase == 2) limit = 42;
        else if (targetPhase == 3) limit = 57;
        if (frame < limit) {
            frame = Time.increaseTimeValue(frame, limit, 30);
            if (frame == limit) signal = true;
        }
        return side;
    }

    public void setPhase(int phase){
        this.phase = phase;
    }

    public boolean isSignal() {
        return signal;
    }

    public int getPhase() {
        return phase;
    }

    public boolean endState(){
        return retreat && frame == 0;
    }

    public int getFrame(){
        return (int) frame;
    }

    public boolean getSoundLock(){
        return soundLock;
    }

    public void setSoundLock(){
        soundLock = !soundLock;
    }
}
