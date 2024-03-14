package deluxe.state.Game.Objects.Character.Attributes;

import deluxe.state.Game.Objects.Player;
import util.Time;

public class Leave {
    private float frame;
    private float delay;
    private final float initialDelay;

    public Leave(float delay) {
        initialDelay = delay;
    }

    public void reset(){
        frame = 0;
        delay = initialDelay;
    }

    public boolean update(Player player){
        frame = Time.increaseTimeValue(frame, 18, 26);
        if (frame != 18) return false;
        else if (delay == 0) return true;
        else if (delay == initialDelay){
            player.setBlacknessDelay(initialDelay);
            player.setBlacknessSpeed(6);
            player.setBlacknessTimes(2);
            player.setScared();
        }
        delay = Time.decreaseTimeValue(this.delay, 0, 1);
        return false;
    }

    public int getFrame(){
        return (int) frame;
    }
}
