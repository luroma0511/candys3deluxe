package game.deluxe.state.Game.Objects.Character.Attributes;

import game.deluxe.state.Game.Objects.Player;
import game.deluxe.state.Game.Objects.Room;
import game.engine.util.Engine;
import game.engine.util.SpriteObject;

public class Bed {
    private float cooldown;
    private float killTimer;

    public void reset(float cooldown, float killTimer){
        this.cooldown = cooldown;
        this.killTimer = killTimer;
    }

    public void input(Engine engine, Player player, Room room, SpriteObject object, float mx, float my){
        if (!object.mouseOverWithPanning(mx, my) || room.getState() != 1 || player.isBedSpot()) return;
        player.setBedSpot();
        engine.getSoundManager().play("bed");
    }

    public boolean update(Engine engine, Player player){
        if (player.isBedSpot()) killTimer = engine.decreaseTimeValue(killTimer,0, 1);
        cooldown = engine.decreaseTimeValue(cooldown, 0, 1);
        return cooldown == 0;
    }

    public boolean killTime(){
        return killTimer == 0;
    }
}
