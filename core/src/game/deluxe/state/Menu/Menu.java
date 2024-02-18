package game.deluxe.state.Menu;

import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import game.deluxe.data.ChallengesData;
import game.deluxe.data.GameData;
import game.deluxe.state.Menu.Objects.Button;
import game.deluxe.state.Menu.Objects.Caption;
import game.deluxe.state.Menu.Objects.MenuCharacter;
import game.engine.Candys3Deluxe;
import game.engine.util.Engine;
import game.engine.util.InputManager;
import game.engine.util.RenderManager;
import game.engine.util.Request;
import game.engine.util.SoundManager;

public class Menu {
    private byte nightSelection = 1;
    private float staticAnimation;

    private final MenuCharacter shadowRat;
    private final MenuCharacter shadowCat;
    private final ChallengesData challengesData;
    private final Button optionButton;
    private final Button playButton;
    private final Caption caption;

    private boolean playMenu;

    private float optionWindowAlpha;

    public Menu(){
        shadowRat = new MenuCharacter("menu/shadow_rat", 40, 44, 632, 632, 0, (short) 1);
        shadowCat = new MenuCharacter("menu/shadow_cat", 688, 44, 428, 632,0, (short) 2);
        challengesData = new ChallengesData();
        optionButton = new Button("OPTIONS", 365, 24, 200, 100, 0);
        playButton = new Button("PLAY", 715, 24, 200, 100, 0);
        caption = new Caption();
    }

    public void load(Request request){
        request.addImageRequest("Static/Static");
        request.addImageRequest("menu/button");
        request.addImageRequest("menu/window");
        request.addImageRequest("menu/option");
        request.addImageRequest("menu/scroll_bar");
        request.addImageRequest("menu/shadow_rat");
        request.addImageRequest("menu/shadow_cat");
    }

    public void update(Engine engine, GameData gameData, SoundManager soundManager) {
        if (engine.getRequest().isNow()) return;

        if (!playMenu){
            soundManager.play("menu");
            soundManager.setLoop("menu", true);
            soundManager.setPitch("menu", 0.75f);
            playMenu = true;
        }

        staticAnimation = engine.increaseTimeValue(staticAnimation, 8, 30);
        if (staticAnimation == 8) {
            staticAnimation = 0;
        }

        optionButton.update(engine);
        if (optionButton.isSelected()) {
            optionWindowAlpha = engine.increaseTimeValue(optionWindowAlpha, 1, 4);
        } else {
            optionWindowAlpha = engine.decreaseTimeValue(optionWindowAlpha, 0, 4);
        }

        playButton.update(engine);
        if (playButton.isSelected()){
            gameData.writeData(nightSelection, shadowRat.getAi(), (byte) 0, (byte) 0, (byte) 0, (byte) 0);
            engine.getStateManager().setGameState((byte) 1);
            playButton.setSelected(false);
            return;
        }

        caption.setActive(false);
        shadowRat.update(engine, caption, engine.getInputManager(), !optionButton.isHovered() && !optionButton.isSelected() && !playButton.isSelected());
        shadowCat.update(engine, caption, engine.getInputManager(), !playButton.isHovered() && !optionButton.isSelected() && !playButton.isSelected());

        caption.update(engine);
    }

    public void render(RenderManager renderManager){
        BitmapFont candysFont = renderManager.getFontManager().getCandysFont();
        BitmapFont aiFont = renderManager.getFontManager().getAiFont();
        BitmapFont debugFont = renderManager.getFontManager().getDebugFont();
        BitmapFont captionFont = renderManager.getFontManager().getCaptionFont();

        renderNightMenu(renderManager, candysFont, aiFont, captionFont);

        debugRender(renderManager, debugFont);
    }

    private void renderNightMenu(RenderManager renderManager, BitmapFont candysFont, BitmapFont aiFont, BitmapFont captionFont){
        SpriteBatch batch = renderManager.getBatch();
        batch.setProjectionMatrix(renderManager.getCameraManager().getViewport().getCamera().combined);
        batch.enableBlending();
        batch.begin();

        TextureRegion region = renderManager.getImageManager().getRegion("Static/Static", 1024, (int) staticAnimation);
        GlyphLayout layout = renderManager.getFontManager().getLayout();
        nightSetColor(batch, 4);
        batch.draw(region, 0, 0, 1280, 720);
        renderManager.restoreColor(batch);

        int srcFunc = batch.getBlendSrcFunc();
        int dstFunc = batch.getBlendDstFunc();
        batch.setBlendFunction(GL20.GL_SRC_ALPHA, GL20.GL_DST_ALPHA);

        batch.setColor(1, 1, 1, 0.6f);
        region = renderManager.getImageManager().get(shadowRat.getPath());
        batch.draw(region, shadowRat.getX(), shadowRat.getY());

        batch.setColor(1, 1, 1, 0.6f);
        region = renderManager.getImageManager().get(shadowCat.getPath());
        batch.draw(region, shadowCat.getX(), shadowCat.getY());

        batch.flush();
        batch.setBlendFunction(srcFunc, dstFunc);

        batch.setColor(1, 1, 1, shadowRat.getAlpha());
        region = renderManager.getImageManager().get(shadowRat.getPath());
        batch.draw(region, shadowRat.getX(), shadowRat.getY());

        batch.setColor(1, 1, 1, shadowCat.getAlpha());
        region = renderManager.getImageManager().get(shadowCat.getPath());
        batch.draw(region, shadowCat.getX(), shadowCat.getY());
        renderManager.restoreColor(batch);

        fontAlpha(aiFont, shadowRat.getAlpha(), true);
        layout.setText(aiFont, "AI: " + shadowRat.getAi());
        aiFont.draw(batch, layout,
                shadowRat.getX() + shadowRat.getWidth() / 2 - layout.width / 2,
                shadowRat.getY() + shadowRat.getHeight() / 2 - layout.height / 2);

        fontAlpha(aiFont, shadowCat.getAlpha(), true);
        layout.setText(aiFont, "AI: " + shadowCat.getAi());
        aiFont.draw(batch, layout,
                shadowCat.getX() + shadowCat.getWidth() / 2 - layout.width / 2,
                shadowCat.getY() + shadowCat.getHeight() / 2 - layout.height / 2);


        fontAlpha(candysFont, 1, true);
        layout.setText(candysFont, "Shadow Night");
        candysFont.draw(batch, layout,
                (float) Candys3Deluxe.width / 2 - layout.width / 2, 696);

        region = renderManager.getImageManager().get("menu/button");
        nightSetColor(batch, 2 - optionButton.getAlpha());
        batch.draw(region, optionButton.getX(), optionButton.getY());

        nightSetColor(batch, 2 - playButton.getAlpha());
        batch.draw(region, playButton.getX(), playButton.getY());

        fontAlpha(candysFont, optionButton.getAlpha(), true);
        layout.setText(candysFont, "OPTIONS");
        float x = optionButton.getX() + optionButton.getWidth() / 2 - layout.width / 2;
        candysFont.draw(batch, layout, x, 90);

        fontAlpha(candysFont, playButton.getAlpha(), true);
        layout.setText(candysFont, "PLAY");
        x = playButton.getX() + playButton.getWidth() / 2 - layout.width / 2;
        candysFont.draw(batch, layout, x, 90);


        if (optionWindowAlpha > 0) {
            region = renderManager.getImageManager().get("menu/window");
            nightSetColor(batch, 1, optionWindowAlpha);
            batch.draw(region, 190, 144);
            renderManager.restoreColor(batch);

            fontAlpha(candysFont, optionWindowAlpha, false);
            layout.setText(candysFont, "Challenges");
            candysFont.draw(batch, layout,
                    (float) Candys3Deluxe.width / 2 - layout.width / 2, 629);

            region = renderManager.getImageManager().getRegion("menu/option", 84, 0);
            nightSetColor(batch, 1, optionWindowAlpha);
            for (byte i = 0; i < 4; i++) {
                batch.draw(region, 236, 470 - 98 * i);
            }
            renderManager.restoreColor(batch);

            for (byte i = 0; i < 4; i++){
                if (i == 0) {
                    layout.setText(candysFont, "Laser Pointer");
                } else if (i == 1) {
                    layout.setText(candysFont, "Hard Cassette");
                } else if (i == 2) {
                    layout.setText(candysFont, "Classic Cat");
                } else {
                    layout.setText(candysFont, "Free Scroll");
                }
                candysFont.draw(batch, layout, 336, 525 - 98 * i);
            }
        }

        if (optionWindowAlpha == 1){
            for (byte i = 0; i < 2; i++) {
                if ((i == 0 && challengesData.isLaserPointer()) || challengesData.isHardCassette()) continue;

                region = renderManager.getImageManager().get("menu/scroll_bar");
                batch.draw(region, 680, 496 - 98 * i);

                region = renderManager.getImageManager().getRegion("menu/option", 84, 2);

                float value;
                if (i == 0) {
                    value = (100 - challengesData.getLaserPointerValue()) * 3.4f;
                } else {
                    value = (4 - challengesData.getHardCassetteValue()) * 170;
                }

                batch.draw(region, 645 + value, 470 - 98 * i);
            }
        }

        caption.render(batch, renderManager, captionFont);
    }

    private void debugRender(RenderManager renderManager, BitmapFont debugFont){
        SpriteBatch batch = renderManager.getBatch();
        InputManager inputManager = renderManager.getInputManager();
        renderManager.restoreColor(batch);

//        shadowRat.debugRender(batch, renderManager);
//        shadowCat.debugRender(batch, renderManager);

        debugFont.draw(batch,
                "Mouse: " + (int) inputManager.getX() + " | " + (int) inputManager.getY(),
                24, 696);

        debugFont.draw(batch,
                "Panning: " + characterPanX + " | " + characterPanY,
                24, 666);
    }

    private void fontAlpha(BitmapFont font, float alpha, boolean tweak){
        if (tweak){
            alpha = 0.5f + alpha / 2;
        }
        if (nightSelection == 0) {
            font.setColor(1, 0, 0, alpha);
        } else {
            font.setColor(0.5f, 0, 1, alpha);
        }
    }

    private void nightSetColor(SpriteBatch batch, float divider){
        nightSetColor(batch, divider, 1);
    }

    private void nightSetColor(SpriteBatch batch, float divider, float alpha){
        if (nightSelection == 0){
            batch.setColor((float) 1 / divider, 0, 0, alpha);
        } else {
            batch.setColor(0.5f / divider, 0, (float) 1 / divider, alpha);
        }
    }
}
