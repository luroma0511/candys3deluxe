package game.deluxe.state.Menu.Objects;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import java.util.ArrayList;
import java.util.List;

import game.deluxe.data.CaptionData;
import game.engine.Candys3Deluxe;
import game.engine.util.Engine;
import game.engine.util.RenderManager;
import game.engine.util.SpriteObject;

public class Caption extends SpriteObject {
    private final CaptionData captionData;
    private boolean active;
    private short previousID;
    private short ID;
    private boolean change;
    private final List<String> captions;

    public Caption(){
        super(null, 0, 0, 0, 24, 0);
        captions = new ArrayList<>();
        captionData = new CaptionData();
    }

    public void update(Engine engine){
        if (!active && getAlpha() > 0) {
            setAlpha(engine.decreaseTimeValue(getAlpha(), 0, 4));
            if (getAlpha() == 0) return;
        } else if (!active) return;

        if (getAlpha() < 1 && active) {
            setAlpha(engine.increaseTimeValue(getAlpha(), 1, 4));
        }
        setX(engine.getInputManager().getX() + 16);
        if (previousID == ID) return;
        captions.clear();
        String text = retrieveText();
        wordWrap(text, 40);
        change = true;
        previousID = ID;
    }

    public void render(SpriteBatch batch, RenderManager renderManager, BitmapFont captionFont){
        if (getAlpha() == 0) return;
        if (change) {
            float width = 0;
            for (String caption: captions) {
                renderManager.getFontManager().getLayout().setText(captionFont, caption);
                width = Math.max(width, renderManager.getFontManager().getLayout().width);
            }
            setWidth(width);
            change = false;
        }
        boolean leftSide = renderManager.getInputManager().getX() < (float) Candys3Deluxe.width / 2;
        float height = Math.max(0, renderManager.getInputManager().getY() - 24 * captions.size() - 4);
        float x;
        if (leftSide) x = getX() + 8;
        else x = getX() - getWidth() - 32;

        Color color = new Color(0, 0, 0, getAlpha());
        renderManager.getShapeManager().drawRect(batch, color,
                x, height,
                (int) getWidth() + 16, (int) getHeight() * captions.size() + 8);

        captionFont.setColor(1, 1, 1, getAlpha());
        for (byte i = 0; i < captions.size(); i++) {
            if (!leftSide) {
                captionFont.draw(renderManager.getBatch(), captions.get(i),
                        getX() - getWidth() - 24, height + getHeight() * (captions.size() - i) - 4);
            } else {
                captionFont.draw(renderManager.getBatch(), captions.get(i),
                        getX() + 16, height + getHeight() * (captions.size() - i) - 4);
            }
        }
    }

    public String retrieveText(){
        if (ID == 1) return captionData.shadowNightRatText;
        else if (ID == 2) return captionData.shadowNightCatText;
        return "";
    }

    public void wordWrap(String text, int characterLimit) {
        StringBuilder sb = new StringBuilder(text);
        while (characterLimit < sb.length()){
            short index = (short) sb.lastIndexOf(" ", characterLimit);
            if (index == -1) index = (short) sb.indexOf(" ");
            captions.add(sb.substring(0, index));
            sb.delete(0, index + 1);
        }
        captions.add(sb.toString());
    }

    public void setID(short ID) {
        this.ID = ID;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }
}
