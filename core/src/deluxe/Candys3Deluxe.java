package deluxe;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Graphics;

import deluxe.state.StateManager;
import util.CameraManager;
import util.FontManager;
import util.FrameBufferManager;
import util.ImageManager;
import util.InputManager;
import util.JavaInfo;
import util.RenderManager;
import util.Request;
import util.SoundManager;
import util.Time;
import util.VideoManager;

public class Candys3Deluxe extends ApplicationAdapter {
	public static InputManager inputManager;
	public static Request request;
	public static RenderManager renderManager;
	public static SoundManager soundManager;
	public static FontManager fontManager;
	public static ImageManager imageManager;
	public static VideoManager videoManager;
	public static FrameBufferManager frameBufferManager;
	public static CameraManager cameraManager;

	public static StateManager stateManager;

	private Graphics.DisplayMode displayMode;
	public static int width;
	public static int height;

	public Candys3Deluxe(int width, int height){
		Candys3Deluxe.width = width;
		Candys3Deluxe.height = height;
	}

	@Override
	public void create(){
		JavaInfo javaInfo = new JavaInfo();
		request = new Request();
		inputManager = new InputManager();
		stateManager = new StateManager();
		soundManager = new SoundManager();
		fontManager = new FontManager();
		imageManager = new ImageManager();
		videoManager = new VideoManager();
		frameBufferManager = new FrameBufferManager();
		cameraManager = new CameraManager(width, height);
		renderManager = new RenderManager();
		Gdx.input.setInputProcessor(inputManager);
		request.addSoundRequest("menu");
		displayMode = Gdx.graphics.getDisplayMode();
	}

	public void fullscreen(){
		Gdx.graphics.setFullscreenMode(displayMode);
	}

	@Override
	public void resize(int width, int height){
		if (cameraManager.getViewport() != null){
			cameraManager.getViewport().update(width, height);
			cameraManager.getViewport().apply();
		}
	}

	@Override
	public void render () {
		Time.update(request);
		inputManager.update();
		stateManager.update(request);
		renderManager.requests();
		renderManager.viewportAdjust();
		renderManager.render();
		inputManager.reset();
	}
	
	@Override
	public void dispose () {
		fontManager.dispose();
		imageManager.dispose();
		videoManager.dispose();
		frameBufferManager.dispose();
		soundManager.dispose();
		renderManager.dispose();
	}
}
