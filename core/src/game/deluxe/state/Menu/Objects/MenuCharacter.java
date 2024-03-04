package game.deluxe.state.Menu.Objects;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import game.engine.Candys3Deluxe;
import game.engine.util.Engine;
import game.engine.util.InputManager;
import game.engine.util.RenderManager;
import game.engine.util.SpriteObject;

public class MenuCharacter extends SpriteObject {
    private byte ai;
    private final short captionID;
    private final int initX;
    private final int initY;

    public MenuCharacter(String path, int x, int y, int width, int height, float alpha, short captionID) {
        super(path, x, y, width, height, alpha);
        initX = x;
        initY = y;
        setX(x);
        setY(y);
        this.captionID = captionID;
    }

    public void update(Engine engine, Caption caption, InputManager inputManager, boolean focus){
        if (!Float.isNaN(inputManager.getX()) && !Float.isNaN(inputManager.getY())) {
            setX(getX() - characterPan(getX(), initX, inputManager.getX(), Candys3Deluxe.width));
            setY(getY() - characterPan(getY(), initY, inputManager.getY(), Candys3Deluxe.height));
        }
        boolean hovered = mouseOver(engine, true) && focus;
        if (!hovered) return;
        caption.setID(captionID);
        caption.setActive(true);
        if (engine.getInputManager().getScrolled() == 0) return;
        ai += engine.getInputManager().getScrolled();
        if (ai < 0) ai = 0;
        if (ai > 20) ai = 20;
    }

    public void debugRender(SpriteBatch batch, RenderManager renderManager){
        Color color = new Color(1, 0, 0, 0.5f);
        renderManager.getShapeManager().drawRect(batch, color,
                getX() + getWidth() / 4, getY() + getHeight(),
                (int) (getWidth() / 2), (int) (getHeight() / 1.5f));
    }

    private float characterPan(float value, float init, float mouseCoord, int length){
        float target = init - (mouseCoord - (float) length / 2) * 0.075f;
        return (value - target) / 4;
    }

    public byte getAi() {
        return ai;
    }
}
