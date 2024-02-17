package game.deluxe.state.Menu.Objects;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import game.engine.util.Engine;
import game.engine.util.ImageManager;
import game.engine.util.RenderManager;
import game.engine.util.SpriteObject;

public class MenuCharacter extends SpriteObject {
    private boolean hovered;
    private byte ai;
    private final short captionID;
    private final int initX;
    private final int initY;

    private TextureRegion boxRegion;

    public MenuCharacter(String path, int x, int y, int width, int height, float alpha, short captionID) {
        super(path, x, y, width, height, alpha);
        initX = x;
        initY = y;
        this.captionID = captionID;
    }

    public void update(Engine engine, Caption caption, float characterPanX, float characterPanY, boolean focus){
        setX(initX - characterPanX);
        setY(initY - characterPanY);

        hovered = mouseOver(engine, true) && focus;

        if (hovered || ai > 0){
            setAlpha(engine.increaseTimeValue(getAlpha(), 0.75f, 4));
        } else {
            setAlpha(engine.decreaseTimeValue(getAlpha(), 0, 4));
        }

        if (hovered){
            caption.setID(captionID);
            caption.setActive(true);
            if (engine.getInputManager().getScrolled() == 0) return;
            ai += engine.getInputManager().getScrolled();
            if (ai < 0) ai = 0;
            if (ai > 20) ai = 20;
        }
    }

    public void debugRender(SpriteBatch batch, RenderManager renderManager){
        if (boxRegion == null){
            boxRegion = new TextureRegion(renderManager.getFrameBufferManager().getTexture());
            boxRegion.setRegion(0, 0, (int) (getWidth() / 2), (int) (getHeight() / 1.5f));
        }
        batch.setColor(1, 0, 0, 0.5f);
        batch.draw(boxRegion, getX() + getWidth() / 4, getY() + getHeight() / 4);
        renderManager.restoreColor(batch);
    }

    public boolean isHovered() {
        return hovered;
    }

    public void setHovered(boolean hovered) {
        this.hovered = hovered;
    }

    public byte getAi() {
        return ai;
    }

    public short getCaptionID() {
        return captionID;
    }
}