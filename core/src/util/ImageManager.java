package util;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import java.util.HashMap;
import java.util.Map;

public class ImageManager {
    private final Map<String, TextureRegion> textures;

    public ImageManager() {
        textures = new HashMap<>();
    }

    public boolean loadingTextures(Request request){
        if (request.imagesIsEmpty()) return false;
        String path = request.getImage(0);
        FileHandle file = Gdx.files.absolute(JavaInfo.getPath() + path + ".png");
        Texture texture = new Texture(file);
        TextureRegion region = new TextureRegion(texture);
        textures.put(path, region);
        request.removeImage(0);
        return true;
    }

    public TextureRegion getRegion(String path, int width, int fileIndex){
        int x = fileIndex * width + fileIndex + 1;
        TextureRegion region = textures.get(path);
        region.setRegion(x, 0, width, region.getTexture().getHeight());
        return region;
    }

    public boolean isAlpha(Pixmap pixmap, short mx, short my){
        Color color = new Color(pixmap.getPixel(mx, my));
        return color.a != 0;
    }

    public Pixmap loadPixmap(Texture texture){
        if (!texture.getTextureData().isPrepared()) {
            texture.getTextureData().prepare();
        }
        return texture.getTextureData().consumePixmap();
    }

    public void add(String path, TextureRegion texture){
        textures.put(path, texture);
    }

    public TextureRegion get(String path){
        return textures.get(path);
    }

    public void dispose(){
        if (textures.isEmpty()) return;
        for (TextureRegion texture: textures.values()) texture.getTexture().dispose();
        textures.clear();
    }
}
