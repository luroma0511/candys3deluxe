package game.deluxe.state;

import game.deluxe.data.GameData;
import game.deluxe.state.Game.Game;
import game.deluxe.state.Menu.Menu;
import game.engine.util.Engine;

public class StateManager {
    private final Game game;
    private final Menu menu;
    private final GameData gameData;
    private int state;
    private int prevState;

    public StateManager(){
        game = new Game();
        menu = new Menu();
        gameData = new GameData();
        state = 0;
        prevState = -1;
    }

    public void update(Engine engine){
        if (state == 0) menu.update(engine, gameData);
        else game.update(engine, gameData);

        if (prevState == state) return;
        engine.getRequest().setNow(true);
        if (state == 0) menu.load(engine.getRequest());
        else if (state == 1) game.load(gameData, engine.getRequest());
        prevState = state;
        engine.getRequest().setStartLoading(true);
    }

    public Game getGame() {
        return game;
    }

    public Menu getMenu() {
        return menu;
    }

    public int getPrevState() {
        return prevState;
    }

    public int getState() {
        return state;
    }

    public void setState(byte state) {
        this.state = state;
    }
}
