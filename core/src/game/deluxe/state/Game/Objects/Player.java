package game.deluxe.state.Game.Objects;

import game.engine.Candys3Deluxe;
import game.engine.util.CameraManager;
import game.engine.util.Engine;
import game.engine.util.InputManager;

public class Player {
    private float x;
    private float y;
    private float shakeX;
    private boolean shakePositive;
    private boolean scared;
    private boolean attack;
    private boolean bedSpot;
    private float overlayAlpha;
    private float overlayFade;
    private float buttonFade;

    private float purpleAlpha;
    private float purpleSpeed;

    private float blacknessAlpha;
    private float blacknessDelay;
    private int blacknessTimes;
    private float blacknessSpeed;

    private final float shakeLimit;
    private final int initX;
    private final int initY;
    private final float lookSpeed;

    public Player(float lookSpeed, int initX, int initY){
        this.lookSpeed = lookSpeed;
        this.initX = initX;
        this.initY = initY;
        shakeLimit = 3.5f;
    }

    public void reset(){
        x = initX;
        y = initY;
        shakeX = 0;
        overlayFade = 0;
        overlayAlpha = 0;
        scared = false;
        purpleAlpha = 0;
        purpleSpeed = 0;
        blacknessAlpha = 0;
        blacknessSpeed = 2;
    }

    public void update(Engine engine, Room room, InputManager inputManager){
        if (room.getFrame() == 0) {
            x += lookMechanic(engine, x, inputManager.getX(), (float) Candys3Deluxe.width / 3, 1, room.getBoundsX());
            y += lookMechanic(engine, y, inputManager.getY(), (float) Candys3Deluxe.height / 3, 2, room.getBoundsY());
        }
        overlayAlpha = engine.decreaseTimeValue(overlayAlpha, 0, 1);
        if (scared && shakePositive) {
            shakeX = engine.increaseTimeValue(shakeX, shakeLimit, 60);
            if (shakeX == shakeLimit) shakePositive = false;
        } else if (scared) {
            shakeX = engine.decreaseTimeValue(shakeX, -shakeLimit, 60);
            if (shakeX == -shakeLimit) shakePositive = true;
        }  else if (shakeX != 0) shakeX = 0;

        if (!attack) overlayFade = engine.decreaseTimeValue(overlayFade, 0, 0.2f);
        else if (overlayFade != 1) overlayFade = 1;
        if (overlayAlpha <= 0.5f && overlayFade > 0) overlayAlpha = 1;

        blacknessDelay = engine.decreaseTimeValue(blacknessDelay, 0, 1);
        if (blacknessAlpha < 1 && (blacknessDelay == 0 || blacknessTimes > 0)) blacknessAlpha = engine.increaseTimeValue(blacknessAlpha, 1, blacknessSpeed);
        else if (blacknessTimes > 0){
            blacknessTimes--;
            blacknessAlpha = 0;
        }

        if (room.getFrame() == 0 && y == 0) buttonFade = engine.increaseTimeValue(buttonFade, 1, 4);
        else buttonFade = engine.decreaseTimeValue(buttonFade, 0, 4);

//        if (purpleAlpha < 1) purpleAlpha = engine.increaseTimeValue(purpleAlpha, 1, purpleSpeed);
//        purpleSpeed = engine.increaseTimeValue(purpleSpeed, 1, 0.0075f);
    }

    public void adjustCamera(Flashlight flashlight, InputManager inputManager, CameraManager cameraManager){
        flashlight.setCoord(inputManager.getX() + x, inputManager.getY() + y);
        if (getX() == cameraManager.getX() && y == cameraManager.getY()) return;
        cameraManager.translate(getX() - cameraManager.getX(), y - cameraManager.getY());
    }

    private float lookMechanic(Engine engine, float position, float mouse, float offset, int multiplier, int bounds){
        float distance = 0;
        float limit = engine.convertValue(bounds * 0.75f * multiplier);
        if (mouse < offset * 1){
            distance = (mouse - offset * 1) * engine.convertValue(lookSpeed);
            if (position + distance < 0) distance = -position;
            else if (distance < -limit) distance = -limit;
        } else if (mouse > offset * 2){
            distance = (mouse - offset * 2) * engine.convertValue(lookSpeed);
            if (position + distance > bounds) distance = bounds - position;
            else if (distance > limit) distance = limit;
        }
        return distance;
    }

    public float getOverlayAlpha() {
        return overlayFade * overlayAlpha;
    }

    public void setBlacknessDelay(float blacknessDelay) {
        this.blacknessDelay = blacknessDelay;
    }

    public void setBlacknessSpeed(float blacknessSpeed) {
        this.blacknessSpeed = blacknessSpeed;
    }

    public void setBlacknessTimes(int blacknessTimes) {
        this.blacknessTimes = blacknessTimes;
    }

    public float getBlacknessAlpha() {
        return blacknessAlpha;
    }

    public float getPurpleAlpha() {
        return purpleAlpha;
    }

    public float getX() {
        return x + shakeX;
    }

    public float getY() {
        return y;
    }

    public boolean isBedSpot(){
        return bedSpot;
    }

    public void setBedSpot(){
        bedSpot = !bedSpot;
    }

    public boolean isScared() {
        return scared;
    }

    public void setScared() {
        scared = !scared;
    }

    public boolean isAttack() {
        return attack;
    }

    public void setAttack() {
        attack = !attack;
    }

    public float getButtonFade() {
        return buttonFade;
    }
}
