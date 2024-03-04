package game.engine.util;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.ScreenUtils;

import game.engine.Candys3Deluxe;

public class FrameBufferManager {
    private final FrameBuffer frameBuffer;
    private final FrameBuffer gameFrameBuffer;

    public FrameBufferManager(){
        frameBuffer = newFrameBuffer();
        gameFrameBuffer = newFrameBuffer();
    }

    public void begin(boolean game){
        if (game) gameFrameBuffer.begin();
        else frameBuffer.begin();
    }

    public void end(SpriteBatch batch, boolean game, boolean captureScreen){
        FrameBuffer frameBuffer;
        if (game) frameBuffer = gameFrameBuffer;
        else frameBuffer = this.frameBuffer;
        batch.flush();
        if (captureScreen){
            frameBuffer.end(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        } else {
            frameBuffer.end(0, 0, 1280, 720);
        }
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);
    }

    public void render(SpriteBatch batch, CameraManager cameraManager, boolean game){
        FrameBuffer frameBuffer;
        if (game) frameBuffer = gameFrameBuffer;
        else frameBuffer = this.frameBuffer;
        Texture texture = frameBuffer.getColorBufferTexture();
        texture.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        TextureRegion region = new TextureRegion(texture);
        region.flip(false, true);
        batch.draw(region, cameraManager.getX(), cameraManager.getY());
    }

    public FrameBuffer newFrameBuffer(){
        return new FrameBuffer(Pixmap.Format.RGBA8888, 1280, 720, true);
    }

    public void dispose(){
        frameBuffer.dispose();
        gameFrameBuffer.dispose();
    }
}
