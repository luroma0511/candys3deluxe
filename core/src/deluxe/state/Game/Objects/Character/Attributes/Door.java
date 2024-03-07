package deluxe.state.Game.Objects.Character.Attributes;

import java.util.Random;

import deluxe.Candys3Deluxe;
import util.Time;

public class Door {
    private float cooldown;
    private float timer;
    private float frame;
    private byte sameSide;
    private boolean signal;
    private byte knockTimes;
    private float knockDelay;
    private boolean knockHard;

    private final float intialCooldown;

    public Door(float cooldown){
        intialCooldown = cooldown;
    }

    public void reset(float timer){
        cooldown = intialCooldown;
        this.timer = timer;
        frame = 13;
        sameSide = 0;
        resetKnock();
    }

    public void resetKnock(){
        knockTimes = 0;
        knockDelay = 0;
    }

    public void input(Hitbox hitbox, float mx, float my){
        if (frame > 0 || !hitbox.isHovered(mx, my)) return;
        cooldown = intialCooldown;
        Candys3Deluxe.soundManager.play("spotted");
        resetKnock();
        hitbox.setCoord(0, 0);
    }

    public byte update(Random random, byte side){
        signal = cooldown == 0;
        cooldown = Time.decreaseTimeValue(cooldown, 0, 1);
        if (cooldown != 0) {
            frame = Time.increaseTimeValue(frame, 13, 30);
            return side;
        }
        frame = Time.decreaseTimeValue(frame, 0, 30);
        timer = Time.decreaseTimeValue(timer, 0, 1);
        if (!signal) {
            knockHard = timer <= 3;
            knockTimes = 3;
            if (sameSide == 3) {
                sameSide = 0;
                int rand = random.nextInt(2);
                if (side == 0) side = (byte) (1 + rand);
                else if (side == 1) side = (byte) (2 * rand);
                else side = (byte) rand;
            } else {
                byte previousSide = side;
                side = (byte) random.nextInt(3);
                if (previousSide == side) sameSide++;
            }
        }
        knockDelay = Time.decreaseTimeValue(knockDelay, 0, 1);
        if (knockDelay == 0 && knockTimes > 0){
            if (side == 0) playKnock("Left");
            else if (side == 1) playKnock("");
            else playKnock("Right");
            knockTimes--;
            knockDelay = 0.125f + timer * 0.05f;
        }
        return side;
    }

    public void playKnock(String path){
        if (knockHard) Candys3Deluxe.soundManager.play("hard_knock" + path);
        else Candys3Deluxe.soundManager.play("knock" + path);
    }

    public float getFrame() {
        return frame;
    }

    public boolean isSignal() {
        return !signal && cooldown == 0;
    }

    public boolean isTimeUp() {
        return timer == 0;
    }
}