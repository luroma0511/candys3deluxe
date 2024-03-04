package game.engine.util;

public abstract class SpriteObject {
    private float x;
    private float y;
    private float width;
    private float height;
    private float alpha;
    private final StringBuilder sb;

    public SpriteObject(){
        this(null, 0, 0, 0, 0, 0);
    }

    public SpriteObject(String path, float x, float y, float width, float height, float alpha){
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.alpha = alpha;
        sb = new StringBuilder();
        if (path == null) return;
        sb.append(path);
    }

    public boolean mouseOver(Engine engine, boolean smaller){
        if (smaller) return engine.getInputManager().mouseOver(x + width / 4, y + height / 4,
                width / 2, height / 1.5f);
        return engine.getInputManager().mouseOver(x, y, width, height);
    }

    public boolean mouseOverWithPanning(float mx, float my){
        return mx >= x && mx <= x + width && my >= y && my <= y + height;
    }

    public String getPath() {
        return sb.toString();
    }

    public void setPath(String path) {
        sb.delete(0, sb.length());
        sb.append(path);
    }

    public float getX() {
        return x;
    }

    public void setX(float x) {
        this.x = x;
    }

    public float getY() {
        return y;
    }

    public void setY(float y) {
        this.y = y;
    }

    public float getWidth() {
        return width;
    }

    public float getHeight() {
        return height;
    }

    public void setWidth(float width) {
        this.width = width;
    }

    public void setHeight(float height) {
        this.height = height;
    }

    public float getAlpha() {
        return alpha;
    }

    public void setAlpha(float alpha) {
        this.alpha = alpha;
    }
}
