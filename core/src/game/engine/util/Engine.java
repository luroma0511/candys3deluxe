package game.engine.util;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;

import game.api.DiscordRichPresenceAPI;
import game.deluxe.state.StateManager;

public class Engine {
    private final DiscordRichPresenceAPI discordRichPresenceAPI;

    private final StateManager stateManager;
    private InputManager inputManager;
    private SoundManager soundManager;
    private final Request request;
    private float deltaTime;
    private long previousTime;

    public Engine(){
        stateManager = new StateManager();
        request = new Request();
        discordRichPresenceAPI = new DiscordRichPresenceAPI();
        discordRichPresenceAPI.startThread();
    }

    public void update(SoundManager soundManager){
        if (getSoundManager() == null) this.soundManager = soundManager;
        deltaTime = (float) (System.currentTimeMillis() - previousTime) / 1_000;
        inputManager.f2 = Gdx.input.isKeyJustPressed(Input.Keys.F2);
        stateManager.update(this);
        previousTime = System.currentTimeMillis();
        if (inputManager.pressed) inputManager.pressed = false;
        inputManager.scrolled = false;
    }

    public float increaseTimeValue(float value, float limit, float speed){
        if (value >= limit) return value;
        value += deltaTime * speed;
        if (value > limit) value = limit;
        return value;
    }

    public float decreaseTimeValue(float value, float limit, float speed) {
        if (value <= limit) return value;
        value -= deltaTime * speed;
        if (value < limit) value = limit;
        return value;
    }

    public float convertValue(float value){
        return value * deltaTime;
    }

    public StateManager getStateManager() {
        return stateManager;
    }

    public InputManager getInputManager() {
        return inputManager;
    }

    public void setInputManager(InputManager inputManager) {
        this.inputManager = inputManager;
    }

    public SoundManager getSoundManager() {
        return soundManager;
    }

    public Request getRequest() {
        return request;
    }
}
