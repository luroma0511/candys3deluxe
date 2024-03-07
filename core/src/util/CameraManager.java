package util;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

public class CameraManager {
    final Viewport viewport;
    private float x;
    private float y;

    public CameraManager(int width, int height){
        final OrthographicCamera camera = new OrthographicCamera(width, height);
        camera.setToOrtho(false);
        viewport = new FitViewport(width, height, camera);
        viewport.getCamera().update();
        viewport.update(width, height);
        viewport.apply();
    }

    public void move(float x, float y){
        if (x == this.x && y == this.y) return;
        translate(x - this.x, y - this.y);
    }

    public void translate(float x, float y){
        viewport.getCamera().translate(x, y, 0);
        this.x += x;
        this.y += y;
        viewport.getCamera().update();
    }

    public void setOrigin(){
        if (x != 0 || y != 0) translate(-x, -y);
    }

    public Viewport getViewport() {
        return viewport;
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }
}
