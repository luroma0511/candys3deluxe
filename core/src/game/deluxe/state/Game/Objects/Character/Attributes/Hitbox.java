package game.deluxe.state.Game.Objects.Character.Attributes;

import game.engine.util.SpriteObject;

public class Hitbox extends SpriteObject {
    public float size;

    public Hitbox(float size){
        super();
        this.size = size;
    }

    public void setCoord(int x, int y){
        setX(x);
        setY(y);
    }

    public boolean isHovered(float mx, float my){
        float lineA = Math.abs(mx - getX());
        float lineB = Math.abs(my - getY());
        return Math.hypot(lineA, lineB) <= size;
    }
}