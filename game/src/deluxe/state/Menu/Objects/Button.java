package deluxe.state.Menu.Objects;

import util.InputManager;
import util.SpriteObject;
import util.Time;

public class Button extends SpriteObject {
    private boolean selected;
    private boolean hovered;

    public Button(String path, int x, int y, int width, int height, float alpha) {
        super(path, x, y, width, height, alpha);
    }

    public void update(InputManager inputManager, boolean alphaConfig){
        hovered = mouseOver(false);
        if (hovered && inputManager.isPressed()) selected = !selected;
        if (!alphaConfig) return;
        if (hovered || selected) setAlpha(Time.increaseTimeValue(getAlpha(), 1, 8));
        else setAlpha(Time.decreaseTimeValue(getAlpha(), 0, 8));
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
