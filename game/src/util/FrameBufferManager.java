package util;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;

public class FrameBufferManager {
    private final FrameBuffer frameBuffer;
    private final FrameBuffer gameFrameBuffer;
    private final FrameBuffer shapeFrameBuffer;

    public FrameBufferManager(){
        frameBuffer = newFrameBuffer();
        gameFrameBuffer = newFrameBuffer();
        shapeFrameBuffer = newFrameBuffer();
    }

    public void begin(int id){
        FrameBuffer frameBuffer = getFrameBuffer(id);
        frameBuffer.begin();
    }

    public void end(SpriteBatch batch, int id, int width, int height){
        FrameBuffer frameBuffer = getFrameBuffer(id);
        batch.flush();
        frameBuffer.end(0, 0, width, height);
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);
    }

    public void render(SpriteBatch batch, CameraManager cameraManager, int id){
        FrameBuffer frameBuffer = getFrameBuffer(id);
        Texture texture = frameBuffer.getColorBufferTexture();
        texture.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        TextureRegion region = new TextureRegion(texture);
        region.flip(false, true);
        batch.draw(region, cameraManager.getX(), cameraManager.getY());
    }

    public FrameBuffer getFrameBuffer(int id){
        if (id == 0) return frameBuffer;
        else if (id == 1) return gameFrameBuffer;
        return shapeFrameBuffer;
    }

    public FrameBuffer newFrameBuffer(){
        return new FrameBuffer(Pixmap.Format.RGBA8888, 1280, 720, true);
    }

    public void dispose(){
        frameBuffer.dispose();
        gameFrameBuffer.dispose();
        shapeFrameBuffer.dispose();
    }
}
