package game.deluxe.state.Menu.Objects;

import game.engine.util.Engine;
import game.engine.util.SpriteObject;

public class Button extends SpriteObject {
    private boolean selected;
    private boolean hovered;

    public Button(String path, int x, int y, int width, int height, float alpha) {
        super(path, x, y, width, height, alpha);
    }

    public void update(Engine engine, boolean alphaConfig){
        hovered = mouseOver(engine, false);
        if (hovered && engine.getInputManager().isPressed()) selected = !selected;
        if (!alphaConfig) return;
        if (hovered || selected) setAlpha(engine.increaseTimeValue(getAlpha(), 1, 8));
        else setAlpha(engine.decreaseTimeValue(getAlpha(), 0, 8));
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public boolean isHovered() {
        return hovered;
    }

    public void setHovered(boolean hovered) {
        this.hovered = hovered;
    }
}
