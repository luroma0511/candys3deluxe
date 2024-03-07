package util;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

public class ShapeManager {
    private final ShapeRenderer shapeRenderer;

    public ShapeManager() {
        shapeRenderer = new ShapeRenderer();
    }

    public void drawRect(SpriteBatch batch, Color color, float x, float y, float width, float height){
        batch.end();
        batch.begin();
        Gdx.gl.glEnable(GL20.GL_BLEND);
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.setColor(color);
        shapeRenderer.rect(x, y, width, height);
        shapeRenderer.flush();
        shapeRenderer.end();
        batch.end();
        batch.begin();
    }

    public void drawCircle(SpriteBatch batch, Color color, float x, float y, float size){
        batch.end();
        batch.begin();
        Gdx.gl.glEnable(GL20.GL_BLEND);
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.setColor(color);
        shapeRenderer.circle(x, y, size);
        shapeRenderer.flush();
        shapeRenderer.end();
        batch.end();
        batch.begin();
    }

    public void dispose(){
        shapeRenderer.dispose();
    }
}
