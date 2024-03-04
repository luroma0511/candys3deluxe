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
import game.engine.util.CameraManager;
import game.engine.util.Engine;
import game.engine.util.InputManager;
import game.engine.util.RenderManager;
import game.engine.util.Request;

public class Menu {
    private final MenuCharacter rat;
    private final MenuCharacter cat;
    private final ChallengesData challengesData;
    private final Button optionButton;
    private final Button playButton;
    private final Button laserPointerButton;
    private final Button hardCassetteButton;
    private final Button freeScrollButton;
    private final Caption caption;

    private boolean shadow;
    private float staticAnimation;
    private float optionWindowAlpha;

    private boolean playMenu;
    private float menuPitch;

    public Menu(){
        rat = new MenuCharacter(null, 40, 44, 632, 632, 0, (short) 1);
        cat = new MenuCharacter(null, 688, 44, 428, 632,0, (short) 2);
        challengesData = new ChallengesData();
        optionButton = new Button("OPTIONS", 365, 24, 200, 100, 0);
        playButton = new Button("PLAY", 715, 24, 200, 100, 0);
        laserPointerButton = new Button("Laser Pointer", 236, 470, 84, 84, 0);
        hardCassetteButton = new Button("Hard Cassette", 236, 372, 84, 84, 0);
        freeScrollButton = new Button("Free Scroll", 236, 274, 84, 84, 0);
        caption = new Caption();
    }

    public void load(Request request){
        request.addImageRequest("Static/Static");
        request.addImageRequest("menu/button");
        request.addImageRequest("menu/window");
        request.addImageRequest("menu/option");
        request.addImageRequest("menu/scroll_bar");
        request.addImageRequest("menu/rat");
        request.addImageRequest("menu/cat");
        request.addImageRequest("menu/shadow_rat");
        request.addImageRequest("menu/shadow_cat");
    }

    public void update(Engine engine, GameData gameData) {
        if (engine.getRequest().isNow()) return;

        if (!playMenu){
            engine.getSoundManager().play("menu");
            engine.getSoundManager().setLoop("menu", true);
            engine.getSoundManager().setVolume("menu", 0.25f);
            playMenu = true;
        }

        if (shadow && menuPitch != 0.75f){
            engine.getSoundManager().setPitch("menu", 0.75f);
            menuPitch = 0.75f;
        } else if (!shadow && menuPitch != 1) {
            engine.getSoundManager().setPitch("menu", 1);
            menuPitch = 1;
        }

        staticAnimation = engine.increaseTimeValue(staticAnimation, 8, 30);
        if (staticAnimation == 8) staticAnimation = 0;

        boolean focus = !optionButton.isSelected() && !playButton.isSelected();
        caption.setActive(false);
        rat.update(engine, caption, engine.getInputManager(), focus);
        cat.update(engine, caption, engine.getInputManager(), focus);
        caption.update(engine);

        optionButton.update(engine, true);
        if (optionButton.isSelected()) optionWindowAlpha = engine.increaseTimeValue(optionWindowAlpha, 1, 4);
        else optionWindowAlpha = engine.decreaseTimeValue(optionWindowAlpha, 0, 4);

        if (optionWindowAlpha > 0){
            laserPointerButton.update(engine, false);
            hardCassetteButton.update(engine, false);
            freeScrollButton.update(engine, false);
        }

        playButton.update(engine, true);
        if (!playButton.isSelected()) return;
        playButton.setSelected(false);
        if (optionWindowAlpha > 0) return;
        gameData.writeData((byte) 0, rat.getAi(), (byte) 0, (byte) 0, (byte) 0, (byte) 0);
        engine.getStateManager().setState((byte) 1);
        playMenu = false;
        engine.getSoundManager().stop("menu");
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
        CameraManager cameraManager = renderManager.getCameraManager();
        cameraManager.setOrigin();
        batch.setProjectionMatrix(cameraManager.getViewport().getCamera().combined);
        batch.enableBlending();
        batch.begin();

        TextureRegion region = renderManager.getImageManager().getRegion("Static/Static", 1024, (int) staticAnimation);
        GlyphLayout layout = renderManager.getFontManager().getLayout();
        nightSetColor(batch, 3);
        batch.draw(region, 0, 0, 1280, 720);
        renderManager.restoreColor(batch);

        int srcFunc = batch.getBlendSrcFunc();
        int dstFunc = batch.getBlendDstFunc();
        batch.setBlendFunction(GL20.GL_SRC_ALPHA, GL20.GL_DST_ALPHA);

        batch.setColor(1, 1, 1, 0.6f);
        if (shadow) region = renderManager.getImageManager().get("menu/shadow_rat");
        else region = renderManager.getImageManager().get("menu/rat");
        batch.draw(region, rat.getX(), rat.getY());
        if (shadow) region = renderManager.getImageManager().get("menu/shadow_cat");
        else region = renderManager.getImageManager().get("menu/cat");
        batch.draw(region, cat.getX(), cat.getY());

        batch.flush();
        batch.setBlendFunction(srcFunc, dstFunc);
        fontAlpha(aiFont, 1, true);
        layout.setText(aiFont, "AI: " + rat.getAi());
        aiFont.draw(batch, layout,
                rat.getX() + rat.getWidth() / 2 - layout.width / 2,
                rat.getY() + rat.getHeight() / 2 - layout.height / 2);

        fontAlpha(aiFont, 1, true);
        layout.setText(aiFont, "AI: " + cat.getAi());
        aiFont.draw(batch, layout,
                cat.getX() + cat.getWidth() / 2 - layout.width / 2,
                cat.getY() + cat.getHeight() / 2 - layout.height / 2);

        fontAlpha(candysFont, 1, true);
        if (!shadow) layout.setText(candysFont, "The Main Cast");
        else layout.setText(candysFont, "The Shadow Cast");
        candysFont.draw(batch, layout,
                (float) Candys3Deluxe.width / 2 - layout.width / 2, 696);

        region = renderManager.getImageManager().get("menu/button");
        for (byte i = 0; i < 2; i++) {
            Button button;
            if (i == 0) button = optionButton;
            else button = playButton;

            nightSetColor(batch, 2 - button.getAlpha());
            batch.draw(region, button.getX(), button.getY());
            fontAlpha(candysFont, button.getAlpha(), true);
            layout.setText(candysFont, button.getPath());
            candysFont.draw(batch, layout,
                    button.getX() + button.getWidth() / 2 - layout.width / 2,
                    90);
        }

        if (optionWindowAlpha > 0) {
            region = renderManager.getImageManager().get("menu/window");
            nightSetColor(batch, 1, optionWindowAlpha);
            batch.draw(region, 190, 144);
            renderManager.restoreColor(batch);

            fontAlpha(candysFont, optionWindowAlpha, false);
            layout.setText(candysFont, "Challenges");
            candysFont.draw(batch, layout,
                    (float) Candys3Deluxe.width / 2 - layout.width / 2, 629);

            byte fileIndex = -1;
            for (byte i = 0; i < 3; i++){
                Button button;
                if (i == 0) button = laserPointerButton;
                else if (i == 1) button = hardCassetteButton;
                else button = freeScrollButton;
                layout.setText(candysFont, button.getPath());
                candysFont.draw(batch, layout, 336, 525 - 98 * i);

                if (fileIndex != 1 && button.isSelected()) fileIndex = 1;
                else if (fileIndex != 0 && !button.isSelected()) fileIndex = 0;
                region = renderManager.getImageManager().getRegion("menu/option", 84, fileIndex);
                challengeButtonColor(batch, button, optionWindowAlpha);
                batch.draw(region, 236, 470 - 98 * i);
                renderManager.restoreColor(batch);
            }
        }

        if (optionWindowAlpha == 1){
            for (byte i = 0; i < 2; i++) {
                if ((i == 0 && challengesData.isLaserPointer()) || challengesData.isHardCassette()) continue;
                region = renderManager.getImageManager().get("menu/scroll_bar");
                batch.draw(region, 680, 496 - 98 * i);
                region = renderManager.getImageManager().getRegion("menu/option", 84, 2);

                float value;
                if (i == 0) value = (100 - challengesData.getLaserPointerValue()) * 3.4f;
                else value = (4 - challengesData.getHardCassetteValue()) * 170;

                batch.draw(region, 645 + value, 470 - 98 * i);
            }
        }

        caption.render(batch, renderManager, captionFont);
    }

    private void debugRender(RenderManager renderManager, BitmapFont debugFont){
        SpriteBatch batch = renderManager.getBatch();
        InputManager inputManager = renderManager.getInputManager();
        renderManager.restoreColor(batch);

//        rat.debugRender(batch, renderManager);
//        cat.debugRender(batch, renderManager);

        debugFont.draw(batch,
                "Mouse: " + (int) inputManager.getX() + " | " + (int) inputManager.getY(),
                24, 696);
    }

    private void fontAlpha(BitmapFont font, float alpha, boolean tweak){
        if (tweak) alpha = 0.5f + alpha / 2;
        if (!shadow) font.setColor(1, 0, 0, alpha);
        else font.setColor(0.5f, 0, 1, alpha);
    }

    private void challengeButtonColor(SpriteBatch batch, Button button, float alpha){
        if (button.isHovered()) batch.setColor(1, 1, 1, alpha);
        else if (shadow){
            if (button.isSelected()) batch.setColor(0.85f, 0.7f, 1, alpha);
            else batch.setColor(0.5f, 0, 1, alpha);
        } else {
            if (button.isSelected()) batch.setColor(1, 0.7f, 0.7f, alpha);
            else batch.setColor(0.5f, 0, 0, alpha);
        }
    }

    private void nightSetColor(SpriteBatch batch, float divider){
        nightSetColor(batch, divider, 1);
    }

    private void nightSetColor(SpriteBatch batch, float divider, float alpha){
        if (!shadow) batch.setColor((float) 1 / divider, 0, 0, alpha);
        else batch.setColor(0.5f / divider, 0, (float) 1 / divider, alpha);
    }
}
