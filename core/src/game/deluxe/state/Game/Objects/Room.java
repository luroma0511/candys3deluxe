package game.deluxe.state.Game.Objects;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import game.deluxe.state.Game.Objects.Character.Characters;
import game.engine.util.CameraManager;
import game.engine.util.Engine;
import game.engine.util.FrameBufferManager;
import game.engine.util.ImageManager;
import game.engine.util.RenderManager;
import game.engine.util.SpriteObject;

public class Room extends SpriteObject {
    private byte state;
    private float frame;
    private boolean framePositive;
    private int boundsX;
    private int boundsY;

    public Room(){
        super();
    }

    public void reset(){
        state = 0;
        frame = 0;
    }

    public void input(){
        if (!framePositive && false){

        }
    }

    public void update(Engine engine){
        switch (state){
            case 0:
                boundsX = 1792;
                boundsY = 304;
                break;
            case 1:
                boundsX = 768;
                boundsY = 0;
                break;
            case 2:
                boundsX = 0;
                boundsY = 0;
                break;
        }

        int limit = 10;
        if (state == 2) limit++;

        if (framePositive) frame = engine.increaseTimeValue(frame, limit, 30);
        else frame = engine.decreaseTimeValue(frame, 0, 30);

        if ((int) frame == limit + 1 && framePositive){
            limit = 10;
            if (state == 2) limit++;
            frame = limit + 0.99f;
        }
    }

    public void render(RenderManager renderManager, Characters characters, Room room, Flashlight flashlight){
        SpriteBatch batch = renderManager.getBatch();
        ImageManager imageManager = renderManager.getImageManager();
        FrameBufferManager frameBufferManager = renderManager.getFrameBufferManager();
        CameraManager cameraManager = renderManager.getCameraManager();
        TextureRegion region;
        if ((int) frame == 0) {
            if (state != 2){
                frameBufferManager.begin(false);
                if (state == 0){
                    Color color = new Color(0, 0, 0, 1);
                    renderManager.getShapeManager().drawRect(batch, color,
                            0, 0, 1280, 720);
                    batch.draw(imageManager.get("game/room/FullRoomEffect"), 0, 0);
                    region = imageManager.get("game/Flashlight");
                    batch.draw(region,
                            flashlight.getX() - (float) region.getRegionWidth() / 2,
                            flashlight.getY() - (float) region.getRegionHeight() / 2);
                }

                frameBufferManager.end(batch, false, true);
            }

            if (state == 0) region = imageManager.getRegion("game/room/Room", 3072, 0);
            else if (state == 1) region = imageManager.get("game/room/UnderBed");
            else region = imageManager.get("game/Tape/Tape");
        } else {
            if (state == 0) region = imageManager.get("game/Moving/Turn Around/Moving" + (int) frame);
            else if (state == 1) region = imageManager.get("game/Moving/Under Bed/Moving" + (int) frame);
            else region = imageManager.get("game/Moving/Turn Back/Moving" + (int) frame);
        }

        frameBufferManager.begin(true);
        batch.draw(region, 0, 0);
        characters.render(batch, imageManager, room);

        if (state != 2 && (int) frame == 0) {
            int srcFunc = batch.getBlendSrcFunc();
            int dstFunc = batch.getBlendDstFunc();
            //DON'T CHANGE THIS!!!
            batch.setBlendFunction(GL20.GL_ONE_MINUS_DST_ALPHA, GL20.GL_SRC_COLOR);
            frameBufferManager.render(batch, cameraManager, false);
            batch.setBlendFunction(srcFunc, dstFunc);
            batch.flush();
        }

        frameBufferManager.end(batch, true, true);
    }

    public byte getState() {
        return state;
    }

    public void setState(byte state) {
        this.state = state;
    }

    public int getFrame() {
        return (int) frame;
    }

    public void setFrame(float frame) {
        this.frame = frame;
    }

    public int getBoundsX() {
        return boundsX;
    }

    public int getBoundsY() {
        return boundsY;
    }
}
