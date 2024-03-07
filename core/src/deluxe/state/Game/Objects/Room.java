package deluxe.state.Game.Objects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import deluxe.state.Game.Objects.Character.Characters;
import deluxe.Candys3Deluxe;
import util.CameraManager;
import util.FrameBufferManager;
import util.ImageManager;
import util.RenderManager;
import util.SpriteObject;
import util.Time;

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

    public void update(){
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

        if (framePositive) frame = Time.increaseTimeValue(frame, limit, 30);
        else frame = Time.decreaseTimeValue(frame, 0, 30);

        if ((int) frame == limit + 1 && framePositive){
            limit = 10;
            if (state == 2) limit++;
            frame = limit + 0.99f;
        }
    }

    public void render(SpriteBatch batch, Characters characters, Room room, Flashlight flashlight){
        ImageManager imageManager = Candys3Deluxe.imageManager;
        FrameBufferManager frameBufferManager = Candys3Deluxe.frameBufferManager;
        CameraManager cameraManager = Candys3Deluxe.cameraManager;
        TextureRegion region;
        if ((int) frame == 0) {
            if (state != 2){
                frameBufferManager.begin(0);
                if (state == 0){
                    Color color = new Color(0, 0, 0, 1);
                    RenderManager.shapeDrawer.setColor(color);
                    RenderManager.shapeDrawer.filledRectangle(cameraManager.getX(), cameraManager.getY(), 1280, 720);
                    batch.draw(imageManager.get("game/room/FullRoomEffect"), 0, 0);
                    region = imageManager.get("game/Flashlight");
                    batch.draw(region,
                            flashlight.getX() - (float) region.getRegionWidth() / 2,
                            flashlight.getY() - (float) region.getRegionHeight() / 2);
                }

                frameBufferManager.end(batch, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
            }

            if (state == 0) region = imageManager.getRegion("game/room/Room", 3072, 0);
            else if (state == 1) region = imageManager.get("game/room/UnderBed");
            else region = imageManager.get("game/Tape/Tape");
        } else {
            if (state == 0) region = imageManager.get("game/Moving/Turn Around/Moving" + (int) frame);
            else if (state == 1) region = imageManager.get("game/Moving/Under Bed/Moving" + (int) frame);
            else region = imageManager.get("game/Moving/Turn Back/Moving" + (int) frame);
        }

        frameBufferManager.begin(1);
        batch.draw(region, 0, 0);
        characters.render(batch, imageManager, room);

        if (state != 2 && (int) frame == 0) {
            int srcFunc = batch.getBlendSrcFunc();
            int dstFunc = batch.getBlendDstFunc();
            //DON'T CHANGE THIS!!!
            batch.setBlendFunction(GL20.GL_ONE_MINUS_DST_ALPHA, GL20.GL_SRC_COLOR);
            frameBufferManager.render(batch, cameraManager, 0);
            batch.setBlendFunction(srcFunc, dstFunc);
            batch.flush();
        }

        frameBufferManager.end(batch, 1, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
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
