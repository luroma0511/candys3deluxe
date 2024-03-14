package deluxe.state.Game.Objects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Pixmap;
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
    private int limit;
    public boolean hoverButton;
    private boolean bed;
    private boolean tape;

    Pixmap button1;
    Pixmap button2;
    Pixmap play;
    Pixmap stop;
    Pixmap rewind;

    public Room(){
        super();
    }

    public void reset(){
        state = 0;
        frame = 0;
        limit = 10;
        boundsX = 1792;
        boundsY = 304;
    }

    public void input(float mx, float my, boolean clicked){
        initializePixmap();
        hoverButton = false;
        if (!framePositive && (int) frame == 0){
            if (state == 0){
                if (button1 != null && Candys3Deluxe.imageManager.isAlpha(button1, (int) mx - 128, (int) (128 - my))){
                    hoverButton = true;
                    tape = true;
                } else if (button2 != null && Candys3Deluxe.imageManager.isAlpha(button2, (int) mx - 732, (int) (128 - my))){
                    hoverButton = true;
                    bed = true;
                }
            } else if (state == 1) hoverButton = button1 != null && Candys3Deluxe.imageManager.isAlpha(button1, (int) mx - 430, (int) (128 - my));
            else {
                hoverButton = button2 != null && Candys3Deluxe.imageManager.isAlpha(button2, (int) mx - 430, (int) (128 - my));
                if (Candys3Deluxe.imageManager.isAlpha(play, (int) ((mx - 541) / 1.25f), (int) ((320 - my) / 1.25f)) && clicked){
                    Candys3Deluxe.soundManager.play("tapeButton");
                } else if (Candys3Deluxe.imageManager.isAlpha(play, (int) ((mx - 690) / 1.25f), (int) ((355 - my) / 1.25f)) && clicked){
                    Candys3Deluxe.soundManager.play("tapeStop");
                    Candys3Deluxe.soundManager.stop("tapeRewind");
                } else if (Candys3Deluxe.imageManager.isAlpha(play, (int) ((mx - 763) / 1.25f), (int) ((371 - my) / 1.25f)) && clicked){
                    Candys3Deluxe.soundManager.play("tapeButton");
                    Candys3Deluxe.soundManager.play("tapeRewind");
                    Candys3Deluxe.soundManager.setLoop("tapeRewind", true);
                }
            }

            if (hoverButton && clicked) {
                framePositive = true;
                frame = 1;
                hoverButton = false;
            }
        }
    }

    private void initializePixmap(){
        if (button1 == null) button1 = Candys3Deluxe.imageManager.loadPixmap("game/Buttons/TapePlayer");
        if (button2 == null) button2 = Candys3Deluxe.imageManager.loadPixmap("game/Buttons/UnderBed");
        if (play == null) play = Candys3Deluxe.imageManager.loadPixmap("game/Tape/Buttons/PlayButton");
        if (stop == null) stop = Candys3Deluxe.imageManager.loadPixmap("game/Tape/Buttons/StopButton");
        if (rewind == null) rewind = Candys3Deluxe.imageManager.loadPixmap("game/Tape/Buttons/RewindButton");
    }

    public void update(Player player){
        if (framePositive) frame = Time.increaseTimeValue(frame, limit + 1, 26);
        else frame = Time.decreaseTimeValue(frame, 0, 26);

        if ((int) frame == limit + 1 && framePositive){
            if (bed){
                if (state == 0) {
                    state = 1;
                    player.setX(player.getX() / 2.34f);
                } else {
                    state = 0;
                    if (player.isBedSpot()) player.setBedSpot();
                    player.setX(player.getX() * 2.33f);
                    bed = false;
                }
            } else if (tape){
                if (state == 0) state = 2;
                else {
                    state = 0;
                    tape = false;
                }
            }

            if (state == 2) limit++;
            else if (limit != 10) limit--;
            frame = limit + 0.99f;
            framePositive = false;
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
                Color color = new Color(0, 0, 0, 1);
                RenderManager.shapeDrawer.setColor(color);
                RenderManager.shapeDrawer.filledRectangle(cameraManager.getX(), cameraManager.getY(), 1280, 720);
                if (state == 0) batch.draw(imageManager.get("game/room/FullRoomEffect"), 0, 0);
                else batch.draw(imageManager.get("game/room/UnderBedEffect"), 0, 0);
                region = imageManager.get("game/Flashlight");
                batch.draw(region,
                        flashlight.getX() - (float) region.getRegionWidth() / 2,
                        flashlight.getY() - (float) region.getRegionHeight() / 2);

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
        if (state != 2) batch.draw(region, 0, 0);
        else batch.draw(region, cameraManager.getX(), cameraManager.getY(), 1280, 720);
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
