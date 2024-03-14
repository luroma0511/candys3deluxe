package deluxe.state;

import deluxe.data.GameData;
import deluxe.state.Game.Game;
import deluxe.state.Menu.Menu;
import util.Request;

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

    public void update(Request request){
        if (state == 0) menu.update(gameData);
        else game.update(gameData);

        if (prevState == state) return;
        if (state == 0) menu.load(request);
        else if (state == 1) game.load(gameData, request);
        prevState = state;
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
