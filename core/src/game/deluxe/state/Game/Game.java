package game.deluxe.state.Game;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import game.deluxe.data.GameData;
import game.deluxe.state.Game.Objects.Character.Characters;
import game.deluxe.state.Game.Objects.Flashlight;
import game.deluxe.state.Game.Objects.Player;
import game.deluxe.state.Game.Objects.Room;
import game.engine.Candys3Deluxe;
import game.engine.util.Engine;
import game.engine.util.InputManager;
import game.engine.util.Request;
import game.engine.util.RenderManager;

public class Game {
    private byte nightState;
    private boolean reset;
    private final Characters characters;
    private final Player player;
    private final Flashlight flashlight;
    private final Room room;

    public Game(){
        characters = new Characters();
        player = new Player(2.25f, 896, 152);
        flashlight = new Flashlight();
        room = new Room();
        reset = false;
    }

    public void reset(byte ratAI, byte catAI, byte vinnieAI, byte shadowRatAI, byte shadowCatAI){
        characters.reset(ratAI, catAI, vinnieAI, shadowRatAI, shadowCatAI);
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

    public void update(Engine engine, GameData gameData){
        if (reset) {
            reset(gameData);
            reset = false;
        }
        if (engine.getRequest().isNow()) return;

        if (engine.getInputManager().isF2()) {
            engine.getStateManager().setState((byte) 0);
            engine.getSoundManager().stopAllSounds();
            return;
        }
        player.update(engine, room, engine.getInputManager());
        room.update(engine);
        characters.update(engine, player, room, flashlight);
    }

    public void reset(GameData gameData){
        reset(gameData.getRatAI(),
                gameData.getShadowCatAI(),
                gameData.getVinnieAI(),
                gameData.getShadowCatAI(),
                gameData.getShadowCatAI());
        player.reset();
    }

    public void render(RenderManager renderManager){
        SpriteBatch batch = renderManager.getBatch();
        player.adjustCamera(flashlight, renderManager.getInputManager(), renderManager.getCameraManager());

        batch.setProjectionMatrix(renderManager.getCameraManager().getViewport().getCamera().combined);
        batch.enableBlending();
        batch.begin();

        room.render(renderManager, characters, room, flashlight);

        renderManager.getFrameBufferManager().render(batch, renderManager.getCameraManager(), true);
        renderManager.getFrameBufferManager().begin(true);

        if (player.getOverlayAlpha() > 0) {
            int srcFunc = batch.getBlendSrcFunc();
            int dstFunc = batch.getBlendDstFunc();
            //DON'T CHANGE THIS!!!
            batch.setBlendFunction(GL20.GL_SRC_COLOR, GL20.GL_DST_ALPHA);
            TextureRegion region = renderManager.getImageManager().get("game/BattleOverlay");
            batch.setColor(player.getOverlayAlpha(), player.getOverlayAlpha(), player.getOverlayAlpha(), 1);
            batch.draw(region, renderManager.getCameraManager().getX(), renderManager.getCameraManager().getY(), 1280, 720);
            renderManager.restoreColor(batch);
            batch.setBlendFunction(srcFunc, dstFunc);
        }

        renderManager.getFrameBufferManager().end(batch, true, true);
        renderManager.getFrameBufferManager().render(batch, renderManager.getCameraManager(), true);

        Color color = new Color(0, 0, 0, 1 - player.getBlacknessAlpha());
        renderManager.getShapeManager().drawRect(batch, color, 0, 0, 1280, 720);

        color = new Color(0.2f, 0, 0.2f, player.getPurpleAlpha());
        renderManager.getShapeManager().drawRect(batch, color, 0, 0, 1280, 720);

        if (player.getButtonFade() > 0) {
            batch.setColor(1, 1, 1, player.getButtonFade());
            switch (room.getState()) {
                case 0:
                    TextureRegion region = renderManager.getImageManager().get("game/Buttons/TapePlayer");
                    batch.draw(region, renderManager.getCameraManager().getX(), renderManager.getCameraManager().getY());
                    break;
                case 1:
                    break;
                case 2:
                    break;
            }
            renderManager.restoreColor(batch);
        }

        debugRender(renderManager, renderManager.getFontManager().getDebugFont());
    }

    private void debugRender(RenderManager renderManager, BitmapFont debugFont){
        SpriteBatch batch = renderManager.getBatch();
        InputManager inputManager = renderManager.getInputManager();

        debugFont.draw(batch,
                "Mouse: " + (int) inputManager.getX() + " | " + (int) inputManager.getY(),
                24 + renderManager.getCameraManager().getX(), 696 + renderManager.getCameraManager().getY());

        if (characters.getRat() != null && true){
            debugFont.draw(batch,
                    "Rat side: " + characters.getRat().getSide(),
                    24 + renderManager.getCameraManager().getX(), 666 + renderManager.getCameraManager().getY());

            Color color = new Color(0.2f, 0.2f, 0.2f, 1);
            renderManager.getShapeManager().drawRect(batch, color,
                    (float) Candys3Deluxe.width / 4,
                    50,
                    640,
                    50);

            color = new Color(0.75f, 0, 0, 1);
            renderManager.getShapeManager().drawRect(batch, color,
                    (float) Candys3Deluxe.width / 4,
                    50,
                    (int) (characters.getRat().getAttackHealth() * 640),
                    50);

            color = new Color(0.75f, 0.5f, 0.5f, 0.5f);
            renderManager.getShapeManager().drawCircle(batch, color,
                    characters.getRat().getHitbox().getX() - renderManager.getCameraManager().getX(),
                    characters.getRat().getHitbox().getY() - renderManager.getCameraManager().getY(),
                    characters.getRat().getHitbox().size);
        }
    }

    private final String[] gameSoundData = new String[] {
            "attack_begin",
            "attack",
            "bed",
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
            "thunder"
    };
}
