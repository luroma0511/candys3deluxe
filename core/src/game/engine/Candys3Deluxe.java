package game.engine;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Graphics;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import game.engine.util.Engine;
import game.engine.util.InputManager;
import game.engine.util.RenderManager;
import game.engine.util.Request;
import game.engine.util.SoundManager;

public class Candys3Deluxe extends ApplicationAdapter {
	private RenderManager renderManager;
	private SoundManager soundManager;
	private ScheduledExecutorService engineTimer;
	private Engine engine;
	private Graphics.DisplayMode displayMode;
	public static int width;
	public static int height;

	public Candys3Deluxe(int width, int height){
		Candys3Deluxe.width = width;
		Candys3Deluxe.height = height;
	}

	public void requestSounds(Request request){
		request.addSoundRequest("menu");
	}

	@Override
	public void create(){
		InputManager inputManager = new InputManager();
		Gdx.input.setInputProcessor(inputManager);
		renderManager = new RenderManager(width, height);
		renderManager.setInputManager(inputManager);
		soundManager = new SoundManager();
		engine = new Engine();
		requestSounds(engine.getRequest());
		engine.setInputManager(inputManager);
		engineTimer = Executors.newSingleThreadScheduledExecutor();
		engineTimer.scheduleAtFixedRate(() -> engine.update(soundManager), 0, 16, TimeUnit.MILLISECONDS);
		displayMode = Gdx.graphics.getDisplayMode();
		Gdx.graphics.setFullscreenMode(displayMode);
	}

	@Override
	public void resize(int width, int height){
		if (renderManager.getCameraManager().getViewport() != null){
			renderManager.getCameraManager().getViewport().update(width, height);
			renderManager.getCameraManager().getViewport().apply();
		}
	}

	@Override
	public void render () {
		renderManager.requests(soundManager, engine.getRequest());
		renderManager.viewportAdjust();
		renderManager.render(engine);
	}
	
	@Override
	public void dispose () {
		engineTimer.shutdown();
		soundManager.dispose();
		renderManager.dispose();
	}
}
