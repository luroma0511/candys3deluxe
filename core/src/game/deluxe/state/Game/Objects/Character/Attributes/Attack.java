package game.deluxe.state.Game.Objects.Character.Attributes;

import java.util.Random;

import game.deluxe.state.Game.Objects.Player;
import game.engine.util.Engine;
import game.engine.util.SpriteObject;

public class Attack {
    private float killTimer;
    private int limit;
    private boolean moved;
    private float flashTime;
    private boolean skip;
    private float twitch;
    private boolean twitching;
    private float delayMovement;
    private float initialDelayMovement;
    private float attackTimer;
    private byte teleports;
    private float delayMoveFrame;
    private int moveFrame;
    private byte position;
    private String audio;
    private float twitchSpeed;
    private float moveSpeed;

    public void reset(float flashTime, float delayMovement, float attackTimer, byte teleports, byte position, float moveSpeed, float twitchSpeed) {
        killTimer = 3;
        this.flashTime = flashTime;
        this.delayMovement = delayMovement;
        this.initialDelayMovement = delayMovement;
        this.attackTimer = attackTimer;
        this.teleports = teleports;
        this.position = position;
        this.twitchSpeed = twitchSpeed;
        this.moveSpeed = moveSpeed;
        moveFrame = 0;
        twitch = 0;
        moved = true;
        audio = null;
    }

    public boolean input(Engine engine, Player player, Hitbox hitbox, SpriteObject object, float mx, float my, boolean twitch, int audioID){
        if (!player.isScared() && object.mouseOverWithPanning(mx, my)){
            player.setScared();
            player.setAttack();
            playAudio(engine, audioID);
            killTimer = 1;
        }

        if (hitbox.isHovered(mx, my)) {
            if (!twitching && skip) {
                flashTime = 0;
                skip = false;
                return twitch;
            }
            killTimer = engine.increaseTimeValue(killTimer, 2, 1);
            twitch = true;
            twitching = true;
            this.twitch = engine.increaseTimeValue(this.twitch, 2, twitchSpeed);
            if (this.twitch == 2) this.twitch = 0;
            flashTime = engine.decreaseTimeValue(flashTime, 0, 1);
            if (flashTime == 0) hitbox.setCoord(0, 0);
        } else {
            killTimer = engine.decreaseTimeValue(killTimer, 0, 1);
            this.twitch = 0;
            twitching = false;
        }
        return twitch;
    }

    public boolean update(Engine engine, Random random, boolean scared){
        if (scared){
            float pitch = engine.increaseTimeValue(engine.getSoundManager().getPitch(audio), 2, 0.01f);
            engine.getSoundManager().setPitch(audio, pitch);
            attackTimer = engine.decreaseTimeValue(attackTimer, 0, 1);
        }

        if (attackTimer == 0 && moveFrame == 0){
            if (flashTime != 0) return false;
            stopAudio(engine);
            return true;
        }

        if (moveFrame != 0){
            boolean positive = moveFrame > 0;
            delayMoveFrame = engine.decreaseTimeValue(delayMoveFrame, 0, moveSpeed);
            if (delayMoveFrame == 0) {
                if (positive) moveFrame--;
                else moveFrame++;
                delayMoveFrame = 1;
            }
            if (moveFrame != 0) return false;
            if (positive) {
                position++;
                if (position == 3) position = 0;
            } else {
                position--;
                if (position == -1) position = 2;
            }
            moved = true;
        } else if (delayMovement > 0) {
            delayMovement = engine.decreaseTimeValue(delayMovement, 0, 1);
        } else if (flashTime == 0){
            if (random.nextInt(2) == 0) moveFrame = 3;
            else moveFrame = -3;
            delayMovement = initialDelayMovement;
            engine.getSoundManager().stop("twitch");
            twitching = false;
            twitch = 0;
            engine.getSoundManager().play("dodge");
        }

        return false;
    }

    private void playAudio(Engine engine, int id){
        if (id == 0 || id == 1) engine.getSoundManager().play("attack_begin");
        if (id == 0) audio = "attack";

        if (audio == null) return;
        engine.getSoundManager().play(audio);
        engine.getSoundManager().setLoop(audio, true);
    }

    private void stopAudio(Engine engine){
        engine.getSoundManager().stop(audio);
        audio = null;
    }

    public float getKillTimer() {
        return killTimer;
    }

    public boolean isMoved() {
        return moved;
    }

    public void setMoved() {
        moved = !moved;
    }

    public int getMoveFrame() {
        return moveFrame;
    }

    public byte getPosition() {
        return position;
    }

    public float getFlashTime() {
        return flashTime;
    }

    public void setFlashTime(float flashTime) {
        this.flashTime = flashTime;
    }

    public void setSkip(){
        skip = !skip;
    }

    public int getLimit(){
        return limit;
    }

    public void setLimit(int limit) {
        this.limit = limit;
    }

    public boolean isTwitching() {
        return twitching;
    }

    public float getTwitch() {
        return twitch;
    }
}
