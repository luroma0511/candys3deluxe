package deluxe.state.Game.Objects.Character.Attributes;

import java.util.Random;

import deluxe.state.Game.Objects.Player;
import deluxe.Candys3Deluxe;
import util.SpriteObject;
import util.Time;

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

    public boolean input(Player player, Hitbox hitbox, SpriteObject object, float mx, float my, boolean twitch, int audioID){
        if (!player.isScared() && object.mouseOverWithPanning(mx, my)){
            player.setScared();
            player.setAttack();
            playAudio(audioID);
            killTimer = 1;
        }

        if (hitbox.isHovered(mx, my)) {
            if (!twitching && skip) {
                flashTime = 0;
                skip = false;
                return twitch;
            }
            killTimer = Time.increaseTimeValue(killTimer, 2, 1);
            twitch = true;
            twitching = true;
            this.twitch = Time.increaseTimeValue(this.twitch, 2, twitchSpeed);
            if (this.twitch == 2) this.twitch = 0;
            flashTime = Time.decreaseTimeValue(flashTime, 0, 1);
            if (flashTime == 0) hitbox.setCoord(0, 0);
        } else {
            killTimer = Time.decreaseTimeValue(killTimer, 0, 1);
            this.twitch = 0;
            twitching = false;
        }
        return twitch;
    }

    public boolean update(Random random, boolean scared){
        if (scared){
            float pitch = Time.increaseTimeValue(Candys3Deluxe.soundManager.getPitch(audio), 2, 0.01f);
            Candys3Deluxe.soundManager.setPitch(audio, pitch);
            attackTimer = Time.decreaseTimeValue(attackTimer, 0, 1);
        }

        if (attackTimer == 0 && moveFrame == 0){
            if (flashTime != 0) return false;
            stopAudio();
            return true;
        }

        if (moveFrame != 0){
            boolean positive = moveFrame > 0;
            delayMoveFrame = Time.decreaseTimeValue(delayMoveFrame, 0, moveSpeed);
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
            delayMovement = Time.decreaseTimeValue(delayMovement, 0, 1);
        } else if (flashTime == 0){
            if (random.nextInt(2) == 0) moveFrame = 3;
            else moveFrame = -3;
            delayMovement = initialDelayMovement;
            Candys3Deluxe.soundManager.stop("twitch");
            twitching = false;
            twitch = 0;
            Candys3Deluxe.soundManager.play("dodge");
        }

        return false;
    }

    private void playAudio(int id){
        if (id == 0 || id == 1) Candys3Deluxe.soundManager.play("attack_begin");
        if (id == 0) audio = "attack";

        if (audio == null) return;
        Candys3Deluxe.soundManager.play(audio);
        Candys3Deluxe.soundManager.setLoop(audio, true);
    }

    private void stopAudio(){
        Candys3Deluxe.soundManager.stop(audio);
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
