package game.deluxe.state.Game.Objects.Character.Attributes;

import game.deluxe.state.Game.Objects.Player;
import game.engine.util.Engine;

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

    public boolean input(Engine engine, Hitbox hitbox, Player player, float mx, float my, boolean twitch, float delayTime){
        if (flashTime == 0 || cooldown == 0) return twitch;
        if (hitbox.isHovered(mx, my)){
            if (!player.isScared()) player.setScared();
            twitch = true;
            twitching = true;
            flashTime = engine.decreaseTimeValue(flashTime, 0, 1);
            if (flashTime != 0) return true;
            hitbox.setCoord(0, 0);
            engine.getSoundManager().play("thunder");
            player.setBlacknessDelay(delayTime);
            player.setBlacknessTimes(2);
            player.setBlacknessSpeed(6);
        } else {
            if (player.isScared()) player.setScared();
            this.twitch = 0;
            twitching = false;
            cooldown = engine.decreaseTimeValue(cooldown, 0, 1);
        }
        return twitch;
    }

    public boolean update(Engine engine){
        if (twitching) twitch = engine.increaseTimeValue(twitch, 2, twitchSpeed);
        if (this.twitch == 2) this.twitch = 0;
        if (flashTime == 0) delay = engine.decreaseTimeValue(delay, 0, 1);
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
