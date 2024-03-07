package deluxe.state.Game.Objects.Character.Attributes;

import deluxe.state.Game.Objects.Player;
import deluxe.Candys3Deluxe;
import util.Time;

public class Peek {
    private float cooldown;
    private float flashTime;
    private float delay;
    private boolean twitching;
    private float twitch;
    private float twitchSpeed;

    public void reset(float cooldown, float flashTime, float delay, float twitchSpeed){
        this.cooldown = cooldown;
        this.flashTime = flashTime;
        this.delay = delay;
        this.twitchSpeed = twitchSpeed;
        twitching = false;
        twitch = 0;
    }

    public boolean input(Hitbox hitbox, Player player, float mx, float my, boolean twitch, float delayTime){
        if (flashTime == 0 || cooldown == 0) return twitch;
        if (hitbox.isHovered(mx, my)){
            if (!player.isScared()) player.setScared();
            twitch = true;
            twitching = true;
            flashTime = Time.decreaseTimeValue(flashTime, 0, 1);
            if (flashTime != 0) return true;
            hitbox.setCoord(0, 0);
            Candys3Deluxe.soundManager.play("thunder");
            player.setBlacknessDelay(delayTime);
            player.setBlacknessTimes(2);
            player.setBlacknessSpeed(6);
        } else {
            if (player.isScared()) player.setScared();
            this.twitch = 0;
            twitching = false;
            cooldown = Time.decreaseTimeValue(cooldown, 0, 1);
        }
        return twitch;
    }

    public boolean update(){
        if (twitching) twitch = Time.increaseTimeValue(twitch, 2, twitchSpeed);
        if (this.twitch == 2) this.twitch = 0;
        if (flashTime == 0) delay = Time.decreaseTimeValue(delay, 0, 1);
        return delay == 0;
    }

    public boolean isKillTime(){
        return cooldown == 0;
    }

    public boolean isTwitching() {
        return twitching;
    }

    public float getTwitch() {
        return twitch;
    }
}
