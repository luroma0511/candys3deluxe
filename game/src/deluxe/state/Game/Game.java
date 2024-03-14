package deluxe.state.Game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import deluxe.data.GameData;
import deluxe.state.Game.Objects.Character.Characters;
import deluxe.state.Game.Objects.Flashlight;
import deluxe.state.Game.Objects.Player;
import deluxe.state.Game.Objects.Room;
import deluxe.Candys3Deluxe;
import util.CameraManager;
import util.FrameBufferManager;
import util.ImageManager;
import util.InputManager;
import util.Request;
import util.RenderManager;

public class Game {
    private byte nightState;
    private boolean reset;
    private Characters characters;
    private final Player player;
    private final Flashlight flashlight;
    private final Room room;

    public Game(){
        player = new Player(2.5f, 896, 152);
        flashlight = new Flashlight();
        room = new Room();
        reset = false;
    }

    public void reset(byte ratAI, byte catAI, byte vinnieAI, byte shadowRatAI, byte shadowCatAI){
        characters = new Characters(ratAI, catAI, vinnieAI, shadowRatAI, shadowCatAI);
    }

    public void load(GameData gameData, Request request){
        reset(gameData);
        characters.load(request);
        StringBuilder sb = new StringBuilder("game/");
        String prefix = sb.append("Buttons/").toString();
        request.addImageRequest(prefix + "TapePlayer");
        request.addImageRequest(prefix + "TapePlayerBack");
        request.addImageRequest(prefix + "UnderBed");
        request.addImageRequest(prefix + "UnderBedBack");

        sb.delete(sb.indexOf("/") + 1, sb.length());
        prefix = sb.append("room/").toString();
        request.addImageRequest(prefix + "Room");
        request.addImageRequest(prefix + "FullRoomEffect");
        request.addImageRequest(prefix + "UnderBed");
        request.addImageRequest(prefix + "UnderBedEffect");

        sb.delete(sb.indexOf("/") + 1, sb.length());
        prefix = sb.append("Tape/").toString();
        request.addImageRequest(prefix + "Tape");
        prefix = sb.append("Buttons/").toString();
        request.addImageRequest(prefix + "Buttons");
        request.addImageRequest(prefix + "PlayButton");
        request.addImageRequest(prefix + "StopButton");
        request.addImageRequest(prefix + "RewindButton");

        sb.delete(sb.indexOf("/") + 1, sb.length());
        prefix = sb.append("Moving/").toString();

        for (int i = 1; i <= 11; i++){
            request.addImageRequest(prefix + "Turn Back/Moving" + i);
            if (i == 11) break;
            request.addImageRequest(prefix + "Turn Around/Moving" + i);
            request.addImageRequest(prefix + "Under Bed/Moving" + i);
        }

        prefix = sb.delete(sb.indexOf("/") + 1, sb.length()).toString();
        request.addImageRequest(prefix + "BattleOverlay");
        request.addImageRequest(prefix + "Clock");
        request.addImageRequest(prefix + "Flashlight");

        for (String sounds: gameSoundData) request.addSoundRequest(sounds);
    }

    public void setNightState(byte nightState){
        this.nightState = nightState;
    }

    public void update(GameData gameData){
        if (reset) {
            reset(gameData);
            Candys3Deluxe.soundManager.stopAllSounds();
            reset = false;
        }

        if (Candys3Deluxe.inputManager.isF2()) {
            Candys3Deluxe.stateManager.setState((byte) 0);
            Candys3Deluxe.soundManager.stopAllSounds();
            return;
        }
        player.update(room);
        if (player.getY() == 0) room.input(Candys3Deluxe.inputManager.getX(), Candys3Deluxe.inputManager.getY(), Candys3Deluxe.inputManager.isPressed());
        room.update(player);
        characters.update(player, room, flashlight);
    }

    public void reset(GameData gameData){
        reset(gameData.getRatAI(),
                gameData.getCatAI(),
                gameData.getVinnieAI(),
                gameData.getShadowCatAI(),
                gameData.getShadowCatAI());
        player.reset();
        room.reset();
    }

    public void render(){
        SpriteBatch batch = RenderManager.batch;
        CameraManager cameraManager = Candys3Deluxe.cameraManager;
        ImageManager imageManager = Candys3Deluxe.imageManager;
        FrameBufferManager frameBufferManager = Candys3Deluxe.frameBufferManager;
        player.adjustCamera(flashlight);

        batch.setProjectionMatrix(cameraManager.getViewport().getCamera().combined);
        RenderManager.shapeDrawer.update();
        batch.enableBlending();
        batch.begin();

        room.render(batch, characters, room, flashlight);

        frameBufferManager.render(batch, cameraManager, 1);
        frameBufferManager.begin(1);

        if (player.getOverlayAlpha() > 0) {
            int srcFunc = batch.getBlendSrcFunc();
            int dstFunc = batch.getBlendDstFunc();
            //DON'T CHANGE THIS!!!
            batch.setBlendFunction(GL20.GL_SRC_COLOR, GL20.GL_DST_ALPHA);
            TextureRegion region = imageManager.get("game/BattleOverlay");
            batch.setColor(player.getOverlayAlpha(), player.getOverlayAlpha(), player.getOverlayAlpha(), 1);
            batch.draw(region, cameraManager.getX(), cameraManager.getY(), 1280, 720);
            RenderManager.restoreColor();
            batch.setBlendFunction(srcFunc, dstFunc);
        }

        frameBufferManager.end(batch, 1, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        frameBufferManager.render(batch, cameraManager, 1);

        Color color = new Color(0, 0, 0, 1 - player.getBlacknessAlpha());
        RenderManager.shapeDrawer.setColor(color);
        RenderManager.shapeDrawer.filledRectangle(cameraManager.getX(), cameraManager.getY(), 1280, 720);

        color = new Color(0.2f, 0, 0.2f, player.getPurpleAlpha());
        RenderManager.shapeDrawer.setColor(color);
        RenderManager.shapeDrawer.filledRectangle(cameraManager.getX(), cameraManager.getY(), 1280, 720);

        if (player.getButtonFade() > 0) {
            batch.setColor(1, 1, 1, player.getButtonFade());
            switch (room.getState()) {
                case 0:
                    TextureRegion region = imageManager.get("game/Buttons/TapePlayer");
                    batch.draw(region, cameraManager.getX() + 132, cameraManager.getY());

                    region = imageManager.get("game/Buttons/UnderBed");
                    batch.draw(region, cameraManager.getX() + 732, cameraManager.getY());
                    break;
                case 1:
                    region = imageManager.get("game/Buttons/UnderBedBack");
                    batch.draw(region, cameraManager.getX() + 430, cameraManager.getY());
                    break;
                case 2:
                    region = imageManager.get("game/Buttons/TapePlayerBack");
                    batch.draw(region, cameraManager.getX() + 430, cameraManager.getY());
                    break;
            }
            RenderManager.restoreColor();
        }

        debugRender(batch, cameraManager);
    }

    private void debugRender(SpriteBatch batch, CameraManager cameraManager){
        InputManager inputManager = Candys3Deluxe.inputManager;

        Candys3Deluxe.fontManager.getDebugFont().draw(batch,
                "Mouse: " + (int) inputManager.getX() + " | " + (int) inputManager.getY(),
                24 + cameraManager.getX(), 696 + cameraManager.getY());

        if (characters.getRat() != null){
            Candys3Deluxe.fontManager.getDebugFont().draw(batch,
                    "Rat = " + characters.getRat().getPath() + " | " + characters.getRat().getX(),
                    24 + cameraManager.getX(), 666 + cameraManager.getY());
        }

        if (characters.getCat() != null){

            Color color = new Color(0.2f, 0.2f, 0.2f, 1);
            RenderManager.shapeDrawer.setColor(color);
            RenderManager.shapeDrawer.filledRectangle(
                    (float) Candys3Deluxe.width / 4 + cameraManager.getX(),
                    50 + cameraManager.getY(),
                    640,
                    50);

            color = new Color(0.75f, 0, 0, 1);
            RenderManager.shapeDrawer.setColor(color);
            RenderManager.shapeDrawer.filledRectangle(
                    (float) Candys3Deluxe.width / 4 + cameraManager.getX(),
                    50 + cameraManager.getY(),
                    (int) (characters.getCat().getAttackHealth() * 640),
                    50);

            color = new Color(0.75f, 0.5f, 0.5f, 0.5f);
            RenderManager.shapeDrawer.setColor(color);
            RenderManager.shapeDrawer.filledCircle(
                    characters.getCat().getHitbox().getX(),
                    characters.getCat().getHitbox().getY(),
                    characters.getCat().getHitbox().size);
        }
    }

    public void setReset() {
        reset = !reset;
    }

    private final String[] gameSoundData = new String[] {
            "attack_begin",
            "attack",
            "bed",
            "cat",
            "catPulse",
            "ambience",
            "crawl",
            "spotted",
            "knock",
            "hard_knock",
            "knockLeft",
            "hard_knockLeft",
            "knockRight",
            "hard_knockRight",
            "peek",
            "get_in",
            "twitch",
            "dodge",
            "thunder",
            "leave",
            "tapeButton",
            "tapeStop",
            "tapeRewind"
    };
}
