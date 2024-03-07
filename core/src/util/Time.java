package util;

import com.badlogic.gdx.Gdx;

public class Time {
    static float deltaTime;

    public static void update(Request request){
        if (request.resetDeltaTime) return;
        deltaTime = Gdx.graphics.getDeltaTime();
    }

    public static float increaseTimeValue(float value, float limit, float speed){
        if (value >= limit) return value;
        value += deltaTime * speed;
        if (value > limit) value = limit;
        return value;
    }

    public static float decreaseTimeValue(float value, float limit, float speed) {
        if (value <= limit) return value;
        value -= deltaTime * speed;
        if (value < limit) value = limit;
        return value;
    }

    public static float convertValue(float value){
        return value * deltaTime;
    }
}
