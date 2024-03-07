package deluxe.state.Game.Objects.Character.Attributes;

import deluxe.state.Game.Objects.Player;
import deluxe.state.Game.Objects.Room;
import deluxe.Candys3Deluxe;
import util.SpriteObject;
import util.Time;

public class Bed {
    private float cooldown;
    private float killTimer;

    public void reset(float cooldown, float killTimer){
        this.cooldown = cooldown;
        this.killTimer = killTimer;
    }

    public void input(Player player, Room room, SpriteObject object, float mx, float my){
        if (!object.mouseOverWithPanning(mx, my) || room.getState() != 1 || player.isBedSpot()) return;
        player.setBedSpot();
        Candys3Deluxe.soundManager.play("bed");
    }

    public boolean update(Player player){
        if (player.isBedSpot()) killTimer = Time.decreaseTimeValue(killTimer,0, 1);
        cooldown = Time.decreaseTimeValue(cooldown, 0, 1);
        return cooldown == 0;
    }

    public boolean killTime(){
        return killTimer == 0;
    }
}
