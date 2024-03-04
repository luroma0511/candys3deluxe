package game.engine.util;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;

public class FontManager {
    private final BitmapFont candysFont;
    private final BitmapFont aiFont;
    private final BitmapFont debugFont;
    private final BitmapFont captionFont;
    private final GlyphLayout layout;

    public FontManager(){
        layout = new GlyphLayout();
        FreeTypeFontGenerator generator1 = new FreeTypeFontGenerator(Gdx.files.absolute(DirectoryPath.getPath() + "fonts\\candysFont.ttf"));
        FreeTypeFontGenerator generator2 = new FreeTypeFontGenerator(Gdx.files.absolute(DirectoryPath.getPath() + "fonts\\captionFont.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();

        debugFont = initializeFont(generator2, parameter, 20);
        captionFont = initializeFont(generator2, parameter, 15);
        candysFont = initializeFont(generator1, parameter, 40);
        aiFont = initializeFont(generator1, parameter, 80);

        generator1.dispose();
        generator2.dispose();
    }

    private BitmapFont initializeFont(FreeTypeFontGenerator generator, FreeTypeFontGenerator.FreeTypeFontParameter parameter, int size){
        parameter.size = size;
        BitmapFont font = generator.generateFont(parameter);
        font.getRegion().getTexture().setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        font.setUseIntegerPositions(false);
        return font;
    }

    public void dispose(){
        candysFont.dispose();
        aiFont.dispose();
        debugFont.dispose();
        captionFont.dispose();
    }

    public GlyphLayout getLayout() {
        return layout;
    }

    public BitmapFont getCandysFont() {
        return candysFont;
    }

    public BitmapFont getAiFont() {
        return aiFont;
    }

    public BitmapFont getDebugFont() {
        return debugFont;
    }

    public BitmapFont getCaptionFont() {
        return captionFont;
    }
}
