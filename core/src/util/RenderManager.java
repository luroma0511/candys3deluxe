package util;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.ScreenUtils;

import deluxe.Candys3Deluxe;
import space.earlygrey.shapedrawer.ShapeDrawer;

public class RenderManager {
    public static SpriteBatch batch;
    public static ShapeDrawer shapeDrawer;

    public RenderManager(){
        batch = new SpriteBatch();
        shapeDrawer = new ShapeDrawer(batch, null);
    }

    public void requests(){
        Candys3Deluxe.request.resetDeltaTime = !(Candys3Deluxe.request.imagesIsEmpty() && Candys3Deluxe.request.soundsIsEmpty());
        if (!Candys3Deluxe.request.resetDeltaTime) return;
        long time = System.currentTimeMillis();
        Candys3Deluxe.imageManager.dispose();
        while (Candys3Deluxe.soundManager.loadingSounds(Candys3Deluxe.request)){

        }

        while (Candys3Deluxe.imageManager.loadingTextures(Candys3Deluxe.request)){

        }
        System.out.println("Time: " + (System.currentTimeMillis() - time) + "ms");
    }

    public void viewportAdjust(){
        Vector3 v3 = Candys3Deluxe.cameraManager.viewport.unproject(new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0));
        Candys3Deluxe.inputManager.x = v3.x - Candys3Deluxe.cameraManager.getX();
        Candys3Deluxe.inputManager.y = v3.y - Candys3Deluxe.cameraManager.getY();
        Candys3Deluxe.inputManager.readjust();
    }

    public void render(){
        if (shapeDrawer.getRegion() == null){
            Candys3Deluxe.frameBufferManager.begin(2);
            ScreenUtils.clear(1, 1, 1, 1);
            Candys3Deluxe.frameBufferManager.end(batch, 2, 1280, 720);
            TextureRegion region = new TextureRegion(Candys3Deluxe.frameBufferManager.getFrameBuffer(2).getColorBufferTexture());
            shapeDrawer.setTextureRegion(region);
        }

        int state = Candys3Deluxe.stateManager.getPrevState();
        ScreenUtils.clear(0, 0, 0, 1);

        if (state == 0){
            Candys3Deluxe.frameBufferManager.begin(0);
            Candys3Deluxe.stateManager.getMenu().render(batch);
            Candys3Deluxe.frameBufferManager.end(batch, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
            Candys3Deluxe.frameBufferManager.render(batch, Candys3Deluxe.cameraManager, 0);
        } else {
            Candys3Deluxe.stateManager.getGame().render();
        }

        Candys3Deluxe.videoManager.updateRender(batch, Candys3Deluxe.width, Candys3Deluxe.height);
        batch.end();
    }

    public static void restoreColor(){
        batch.setColor(1, 1, 1, 1);
    }

    public void dispose(){
        batch.dispose();
    }
}
