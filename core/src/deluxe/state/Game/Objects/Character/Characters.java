package deluxe.state.Game.Objects.Character;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import deluxe.state.Game.Objects.Flashlight;
import deluxe.state.Game.Objects.Player;
import deluxe.state.Game.Objects.Room;
import deluxe.Candys3Deluxe;
import util.ImageManager;
import util.Request;

public class Characters {
    private Rat rat;
    private Cat cat;
    private Vinnie vinnie;
    private ShadowRat shadowRat;
    private ShadowCat shadowCat;

    private boolean twitchLock;

    public void reset(byte ratAI, byte catAI, byte vinnieAI, byte shadowRatAI, byte shadowCatAI) {
        if (ratAI != 0) rat = new Rat(ratAI);
        if (catAI != 0) cat = new Cat(catAI);
        if (vinnieAI != 0) vinnie = new Vinnie(vinnieAI);
        if (shadowRatAI != 0) shadowRat = new ShadowRat(shadowRatAI);
        if (shadowCatAI != 0) shadowCat = new ShadowCat(shadowCatAI);
    }

    public void load(Request request){
        if (rat != null) rat.load(request);
//        if (characters.getCat() != null) characters.getCat().load(engine);
//        if (characters.getVinnie() != null) characters.getVinnie().load(engine);
//        if (characters.getShadowRat() != null) characters.getShadowRat().load(engine);
//        if (characters.getShadowCat() != null) characters.getShadowCat().load(engine);
    }

    public void update(Player player, Room room, Flashlight flashlight){
        boolean twitch = false;
        if (rat != null) {
            twitch = rat.input(player, room, flashlight.getX(), flashlight.getY(), false);
            rat.update(player);
        }

        if (twitch && !twitchLock){
            Candys3Deluxe.soundManager.play("twitch");
            Candys3Deluxe.soundManager.setLoop("twitch", true);
        } else if (!twitch && twitchLock) Candys3Deluxe.soundManager.stop("twitch");
        twitchLock = twitch;
    }

    public void render(SpriteBatch batch, ImageManager imageManager, Room room){
        TextureRegion region = null;
        if (rat != null){
            switch (rat.getRoomState()){
                case 0:
                    if (rat.getDoor().getFrame() == 13 || room.getState() != 0 || room.getFrame() != 0) break;
                    region = imageManager.getRegion(rat.getPath(), (int) rat.getWidth(), (int) rat.getDoor().getFrame());
                    break;
                case 1:
                    if (room.getState() != 0 || room.getFrame() != 0) break;
                    region = imageManager.getRegion(rat.getPath(), (int) rat.getWidth(), rat.getAttackFrame());
                    break;
                case 2:
                    if (room.getState() != 1 || room.getFrame() != 0) break;
                    region = imageManager.get(rat.getPath());
                    break;
                case 3:
                    if (room.getState() != 0 || room.getFrame() != 0) break;
                    region = imageManager.getRegion(rat.getPath(), (int) rat.getWidth(), (int) rat.getPeek().getTwitch());
                    break;
            }
            if (region != null) batch.draw(region, rat.getX(), rat.getY());
        }
    }

    public Rat getRat() {
        return rat;
    }

    public Cat getCat() {
        return cat;
    }

    public Vinnie getVinnie() {
        return vinnie;
    }

    public ShadowRat getShadowRat() {
        return shadowRat;
    }

    public ShadowCat getShadowCat() {
        return shadowCat;
    }
}
