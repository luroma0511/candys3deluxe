package deluxe.state.Menu;

import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import deluxe.data.ChallengesData;
import deluxe.data.GameData;
import deluxe.state.Menu.Objects.Button;
import deluxe.state.Menu.Objects.Caption;
import deluxe.state.Menu.Objects.MenuCharacter;
import deluxe.Candys3Deluxe;
import util.CameraManager;
import util.ImageManager;
import util.InputManager;
import util.RenderManager;
import util.Request;
import util.Time;

public class Menu {
    private final MenuCharacter rat;
    private final MenuCharacter cat;
    private final ChallengesData challengesData;
    private final Button optionButton;
    private final Button playButton;
    private final Button laserPointerButton;
    private final Button hardCassetteButton;
    private final Button freeScrollButton;
    private final Button shadowChallengeButton;
    private final Caption caption;

    private boolean shadow;
    private float thunderAlpha;
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
        shadowChallengeButton = new Button("Shadow Challenge", 236, 176, 84, 84, 0);
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

    public void update(GameData gameData) {
        InputManager inputManager = Candys3Deluxe.inputManager;
        if (Candys3Deluxe.request.resetDeltaTime && !playMenu) {
            Candys3Deluxe.soundManager.play("menu");
            Candys3Deluxe.soundManager.setLoop("menu", true);
            Candys3Deluxe.soundManager.setVolume("menu",0.25f);
            playMenu = true;
        }
        if (shadow && menuPitch != 0.75f) menuPitch = 0.75f;
        else if (!shadow && menuPitch != 1) menuPitch = 1;
        if (menuPitch != Candys3Deluxe.soundManager.getPitch("menu")) Candys3Deluxe.soundManager.setPitch("menu", menuPitch);

        thunderAlpha = Time.decreaseTimeValue(thunderAlpha, 0, 4);
        staticAnimation = Time.increaseTimeValue(staticAnimation, 8, 30);
        if (staticAnimation == 8) staticAnimation = 0;

        boolean focus = !optionButton.isSelected() && !playButton.isSelected();
        caption.setActive(false);
        rat.update(caption, inputManager, focus);
        cat.update(caption, inputManager, focus);
        caption.update();

        optionButton.update(inputManager, true);
        if (optionButton.isSelected()) {
            optionWindowAlpha = Time.increaseTimeValue(optionWindowAlpha, 1, 4);
            laserPointerButton.update(inputManager, false);
            hardCassetteButton.update(inputManager, false);
            freeScrollButton.update(inputManager, false);
            shadowChallengeButton.update(inputManager, false);
        }
        else if ((shadowChallengeButton.isSelected() && !shadow) || (!shadowChallengeButton.isSelected() && shadow)) {
            shadow = !shadow;
            optionWindowAlpha = 0;
            thunderAlpha = 1;
            Candys3Deluxe.soundManager.play("thunder");
        } else optionWindowAlpha = Time.decreaseTimeValue(optionWindowAlpha, 0, 4);

        playButton.update(inputManager, true);
        if (!playButton.isSelected()) return;
        playButton.setSelected(false);
        if (optionWindowAlpha > 0) return;
        gameData.writeData((byte) 0, rat.getAi(), cat.getAi(), (byte) 0, (byte) 0, (byte) 0);
        Candys3Deluxe.stateManager.setState((byte) 1);
        playMenu = false;
        Candys3Deluxe.soundManager.stop("menu");
    }

    public void render(SpriteBatch batch){
        BitmapFont candysFont = Candys3Deluxe.fontManager.getCandysFont();
        BitmapFont aiFont = Candys3Deluxe.fontManager.getAiFont();
        BitmapFont debugFont = Candys3Deluxe.fontManager.getDebugFont();
        BitmapFont captionFont = Candys3Deluxe.fontManager.getCaptionFont();
        renderNightMenu(batch, candysFont, aiFont, captionFont);
        debugRender(batch, debugFont);
    }

    private void renderNightMenu(SpriteBatch batch, BitmapFont candysFont, BitmapFont aiFont, BitmapFont captionFont){
        CameraManager cameraManager = Candys3Deluxe.cameraManager;
        ImageManager imageManager = Candys3Deluxe.imageManager;
        cameraManager.setOrigin();
        batch.setProjectionMatrix(cameraManager.getViewport().getCamera().combined);
        RenderManager.shapeDrawer.update();
        batch.enableBlending();
        batch.begin();

        TextureRegion region = imageManager.getRegion("Static/Static", 1024, (int) staticAnimation);
        GlyphLayout layout = Candys3Deluxe.fontManager.getLayout();
        nightSetColor(batch, 3);
        batch.draw(region, 0, 0, 1280, 720);
        RenderManager.restoreColor();

        int srcFunc = batch.getBlendSrcFunc();
        int dstFunc = batch.getBlendDstFunc();
        batch.setBlendFunction(GL20.GL_SRC_ALPHA, GL20.GL_DST_ALPHA);

        batch.setColor(1, 1, 1, 0.6f);
        if (shadow) region = imageManager.get("menu/shadow_rat");
        else region = imageManager.get("menu/rat");
        batch.draw(region, rat.getX(), rat.getY());
        if (shadow) region = imageManager.get("menu/shadow_cat");
        else region = imageManager.get("menu/cat");
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

        region = imageManager.get("menu/button");
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
            region = imageManager.get("menu/window");
            nightSetColor(batch, 1, optionWindowAlpha);
            batch.draw(region, 190, 144);
            RenderManager.restoreColor();

            fontAlpha(candysFont, optionWindowAlpha, false);
            layout.setText(candysFont, "Challenges");
            candysFont.draw(batch, layout,
                    (float) Candys3Deluxe.width / 2 - layout.width / 2, 629);

            byte fileIndex = -1;
            for (byte i = 0; i < 4; i++){
                Button button;
                if (i == 0) button = laserPointerButton;
                else if (i == 1) button = hardCassetteButton;
                else if (i == 2) button = freeScrollButton;
                else button = shadowChallengeButton;
                layout.setText(candysFont, button.getPath());
                candysFont.draw(batch, layout, 336, 525 - 98 * i);

                if (fileIndex != 1 && button.isSelected()) fileIndex = 1;
                else if (fileIndex != 0 && !button.isSelected()) fileIndex = 0;
                region = imageManager.getRegion("menu/option", 84, fileIndex);
                challengeButtonColor(batch, button, optionWindowAlpha);
                batch.draw(region, 236, 470 - 98 * i);
                RenderManager.restoreColor();
            }
        }

        if (optionWindowAlpha == 1){
            for (byte i = 0; i < 2; i++) {
                if ((i == 0 && challengesData.isLaserPointer()) || challengesData.isHardCassette()) continue;
                region = imageManager.get("menu/scroll_bar");
                batch.draw(region, 680, 496 - 98 * i);
                region = imageManager.getRegion("menu/option", 84, 2);

                float value;
                if (i == 0) value = (100 - challengesData.getLaserPointerValue()) * 3.4f;
                else value = (4 - challengesData.getHardCassetteValue()) * 170;

                batch.draw(region, 645 + value, 470 - 98 * i);
            }
        }

        caption.render(batch, captionFont);

        RenderManager.shapeDrawer.setColor(1, 1, 1, thunderAlpha);
        RenderManager.shapeDrawer.filledRectangle(0, 0, 1280, 720);
    }

    private void debugRender(SpriteBatch batch, BitmapFont debugFont){
        InputManager inputManager = Candys3Deluxe.inputManager;
        RenderManager.restoreColor();
        debugFont.draw(batch,
                "Mouse: " + (int) inputManager.getX() + " | " + (int) inputManager.getY(),
                24, 696);

//        rat.debugRender(batch, renderManager);
//        cat.debugRender(batch, renderManager);
//
//        debugFont.draw(batch,
//                "Java path: " + JavaInfo.getJavaPath(),
//                24, 666);
//
//        debugFont.draw(batch,
//                "Java version: " + JavaInfo.getJre(),
//                24, 636);
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
