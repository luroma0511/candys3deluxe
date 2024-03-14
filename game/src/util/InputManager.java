package util;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Graphics;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;

import deluxe.Candys3Deluxe;

public class InputManager extends InputAdapter {
    float x;
    float y;
    boolean scrolled;
    float scrolledAmount;
    boolean pressed;
    boolean f2;

    public void update(){
        pressed = Gdx.input.isButtonJustPressed(Input.Buttons.LEFT);
        f2 = Gdx.input.isKeyJustPressed(Input.Keys.F2);
        if (Gdx.input.isKeyJustPressed(Input.Keys.R) && Candys3Deluxe.stateManager.getState() == 1) Candys3Deluxe.stateManager.getGame().setReset();
    }

    public boolean fullscreen(Graphics.DisplayMode displayMode, boolean fullscreen, int width, int height){
        if (!Gdx.input.isKeyJustPressed(Input.Keys.F11)) return fullscreen;
        if (fullscreen) Gdx.graphics.setWindowedMode(width, height);
        else Gdx.graphics.setFullscreenMode(displayMode);
        return !fullscreen;
    }

    public void reset(){
        scrolled = false;
    }

    public boolean mouseOver(float x, float y, float width, float height){
        return mouseOverExact(x, y, x + width, y + height);
    }

    public boolean mouseOverExact(float x, float y, float x2, float y2){
        return this.x >= x && this.x <= x2 && this.y >= y && this.y <= y2;
    }

    public void readjust(){
        if (x < 0) x = 0;
        if (x > 1280) x = 1280;
        if (y < 0) y = 0;
        if (y > 720) y = 720;
    }

    @Override
    public boolean scrolled(float amountX, float amountY){
        scrolledAmount = -amountY;
        scrolled = true;
        return false;
    }

    public float getScrolled() {
        if (scrolled) return scrolledAmount;
        return 0;
    }

    public boolean isPressed() {
        return pressed;
    }

    public boolean isF2() {
        return f2;
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }
}
