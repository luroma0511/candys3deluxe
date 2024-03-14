package deluxe.state.Game.Objects.Character;

import java.util.Random;

import deluxe.Candys3Deluxe;
import deluxe.state.Game.Objects.Character.Attributes.Attack;
import deluxe.state.Game.Objects.Character.Attributes.Bed;
import deluxe.state.Game.Objects.Character.Attributes.BedSide;
import deluxe.state.Game.Objects.Character.Attributes.Hitbox;
import deluxe.state.Game.Objects.Character.Attributes.Leave;
import deluxe.state.Game.Objects.Character.Attributes.Peek;
import deluxe.state.Game.Objects.Flashlight;
import deluxe.state.Game.Objects.Player;
import deluxe.state.Game.Objects.Room;
import util.Request;
import util.SpriteObject;
import util.Time;

public class Cat extends SpriteObject {
    private final Random random;

    private final Hitbox hitbox;
    private final Attack attack;
    private final BedSide bedSide;
    private final Bed bed;
    private final Peek peek;
    private final Leave leave;

    private byte ai;
    private byte roomState;
    private byte side;
    private boolean catPlay;

    public Cat(byte ai){
        super();
        this.ai = ai;
        random = new Random();
        hitbox = new Hitbox(100);
        bedSide = new BedSide(10);
        bedSide.reset();
        attack = new Attack();
        bed = new Bed();
        peek = new Peek();
        leave = new Leave(0);
        catPlay = false;
    }

    public boolean input(Player player, Room room, float mx, float my, boolean twitch){
        switch (roomState){
            case 0:
                if (bedSide.getFrame() > 0){
                    if (!catPlay){
                        Candys3Deluxe.soundManager.play("cat");
                        Candys3Deluxe.soundManager.setVolume("cat", 0);
                        Candys3Deluxe.soundManager.setLoop("cat", true);
                        catPlay = true;
                    }
                    float catVolume = Time.increaseTimeValue(Candys3Deluxe.soundManager.getVolume("cat"), 0.1f, 0.25f);
                    Candys3Deluxe.soundManager.setVolume("cat", catVolume);
                }
                if (!player.isFreeze() && bedSide.input(hitbox, mx, my)){
                    if (!bedSide.getSoundLock()){
                        Candys3Deluxe.soundManager.play("catPulse");
                        Candys3Deluxe.soundManager.setLoop("catPulse", true);
                        bedSide.setSoundLock();
                    }
                } else if (bedSide.getSoundLock()) {
                    Candys3Deluxe.soundManager.stop("catPulse");
                    bedSide.setSoundLock();
                }
                break;
            case 1:
                twitch = attack.input(player, hitbox, this, mx, my, twitch, 1);
                break;
            case 2:
                bed.input(player, room, this, mx, my);
                break;
            case 3:
                twitch = peek.input(hitbox, player, mx, my, twitch, 0.75f);
                break;
        }
        return twitch;
    }

    public void update(Rat rat, Player player, Room room){
        if (roomState != 0 && roomState != 4){
            float catVolume = Time.increaseTimeValue(Candys3Deluxe.soundManager.getVolume("cat"), 0.75f, 0.5f);
            Candys3Deluxe.soundManager.setVolume("cat", catVolume);
            float catPitch = Time.increaseTimeValue(Candys3Deluxe.soundManager.getPitch("cat"), 2, 0.0075f);
            Candys3Deluxe.soundManager.setPitch("cat", catPitch);
        }
        switch (roomState){
            case 0:
                side = (byte) bedSide.update(random, side);
                if (bedSide.getFrame() != 23 && bedSide.getFrame() != 42 && bedSide.getFrame() != 57) hitbox.setCoord(0, 0);
                if (bedSide.getFrame() > 0){
                    boolean change = true;
                    float bedPhase = bedSide.getPhase();
                    if (bedSide.getFrame() <= 23) bedSide.setPhase(1);
                    else if (bedSide.getFrame() <= 42) bedSide.setPhase(2);
                    else bedSide.setPhase(3);
                    if (bedPhase == bedSide.getPhase()) change = false;
                    if (change) changePath();
                }
                if (bedSide.isSignal()) {
                    if (side == 0) {
                        if (bedSide.getFrame() <= 23) hitbox.setCoord(192, 548);
                        else if (bedSide.getFrame() <= 42) hitbox.setCoord(186, 720);
                        else hitbox.setCoord(187, 660);
                    } else {
                        if (bedSide.getFrame() <= 23) hitbox.setCoord(2910, 448);
                        else if (bedSide.getFrame() <= 42) hitbox.setCoord(2838, 547);
                        else hitbox.setCoord(2805, 619);
                    }
                }
                if (!bedSide.endState()) return;
                if (rat == null || rat.getRoomState() != 2) side = (byte) (random.nextInt(2) * 2);
                else if (rat.getSide() == 0) side = 2;
                else side = 0;
                transitionRoomState((byte) 2);
                Candys3Deluxe.soundManager.stop("catPulse");
                bedSide.setSoundLock();
                Candys3Deluxe.soundManager.play("crawl");
                break;
            case 1:
                if (roomUpdate(rat, player) || !attack.isMoved()) return;
                if (side == 0) {
                    if (attack.getPosition() == 0) hitbox.setCoord(366, 741);
                    else if (attack.getPosition() == 1) hitbox.setCoord(290, 632);
                    else hitbox.setCoord(466, 616);
                } else if (side == 1) {
                    if (attack.getPosition() == 0) hitbox.setCoord(1529, 724);
                    else if (attack.getPosition() == 1) hitbox.setCoord(1341, 583);
                    else hitbox.setCoord(1703, 605);
                } else {
                    if (attack.getPosition() == 0) hitbox.setCoord(2559, 890);
                    else if (attack.getPosition() == 1) hitbox.setCoord(2395, 714);
                    else hitbox.setCoord(2748, 669);
                }
                attack.setMoved();
                if (attack.getLimit() != 0) {
                    int chance = random.nextInt(10);
                    if (chance == 7) attack.setSkip();
                    if (chance == 3 || chance == 7) {
                        attack.setLimit(attack.getLimit() - 1);
                        break;
                    }
                }
                attack.setLimit(3);
                attack.setFlashTime(0.25f + random.nextInt(4) * 0.125f);
                break;
            case 2:
                if (bed.killTime()) return;
                if (!bed.update(player, room)) return;
                transitionRoomState((byte) 3);
                Candys3Deluxe.soundManager.play("peek");
                break;
            case 3:
                if (!peek.update()) return;
                player.setScared();
                player.setFreeze();
                if (side == 0) {
                    if (rat == null || rat.getDoor().getFrame() == 13) side = (byte) (1 + random.nextInt(2));
                    else if (rat.getSide() == 1) side = 2;
                    else side = 1;
                } else {
                    if (rat == null || rat.getDoor().getFrame() == 13) side = (byte) (random.nextInt(2));
                    else if (rat.getSide() == 1) side = 0;
                    else side = 1;
                }
                transitionRoomState((byte) 1);
                break;
            case 4:
                if (!leave.update(player)) return;
                transitionRoomState((byte) 0);
                break;
        }
    }

    public void changePath(){
        if (roomState == 0) {
            if (side == 0) {
                setPath("game/Cat/Retreat/Left");
                append(String.valueOf(bedSide.getPhase()));
                setX(0);
                if (bedSide.getPhase() == 1){
                    setY(339);
                    setWidth(371);
                } else if (bedSide.getPhase() == 2){
                    setY(274);
                    setWidth(400);
                } else {
                    setY(233);
                    setWidth(378);
                }
            } else {
                setPath("game/Cat/Retreat/Right");
                append(String.valueOf(bedSide.getPhase()));
                setY(218);
                if (bedSide.getPhase() == 1){
                    setX(2761);
                    setWidth(274);
                } else if (bedSide.getPhase() == 2){
                    setX(2603);
                    setWidth(433);
                } else {
                    setX(2537);
                    setWidth(497);
                }
            }
        } else if (roomState == 1){
            if (side == 0) {
                setPath("game/Cat/Battle/Left");
                setX(171);
                setY(322);
                setWidth(406);
                setHeight(515);
            } else if (side == 1) {
                setPath("game/Cat/Battle/Middle");
                setX(1215);
                setY(78);
                setWidth(664);
                setHeight(791);
            } else {
                setPath("game/Cat/Battle/Right");
                setX(2229);
                setY(196);
                setWidth(711);
                setHeight(828);
            }
        } else if (roomState == 2){
            if (side == 0) {
                setPath("game/Cat/Bed/LeftUnder");
                setX(0);
                setY(170);
                setWidth(784);
                setHeight(509);
            } else {
                setPath("game/Cat/Bed/RightUnder");
                setX(1100);
                setY(141);
                setWidth(948);
                setHeight(538);
            }
        } else if (roomState == 3){
            if (side == 0) {
                hitbox.setCoord(652, 440);
                setPath("game/Cat/Bed/LeftPeek");
                setX(515);
                setY(231);
                setWidth(274);
                setHeight(345);
            } else {
                hitbox.setCoord(2349, 512);
                setPath("game/Cat/Bed/RightPeek");
                setX(2156);
                setY(194);
                setWidth(557);
                setHeight(525);
            }
        } else {
            if (side == 0){
                setPath("game/Cat/Leaving/Left");
                setX(104);
                setY(323);
                setWidth(386);
                setHeight(459);
            } else if (side == 2){
                setPath("game/Cat/Leaving/Right");
                setX(2491);
                setY(259);
                setWidth(459);
                setHeight(589);
            } else {
                setPath("game/Cat/Leaving/Middle");
                setX(1414);
                setY(384);
                setWidth(226);
                setHeight(401);
            }
        }
    }

    private boolean roomUpdate(Rat rat, Player player){
        if (attack.update(random, player.isScared())) {
            transitionRoomState((byte) 4);
            player.setBlacknessSpeed(6);
            player.setBlacknessTimes(2);
            Candys3Deluxe.soundManager.setVolume("cat", 0);
            Candys3Deluxe.soundManager.setPitch("cat", 1);
            Candys3Deluxe.soundManager.play("thunder");
            if (side != 1) Candys3Deluxe.soundManager.play("leave");
            player.setScared();
            player.setAttack();
            return true;
        } else if (rat != null && rat.getDoor().getFrame() != 13){
            attack.setKillTimer(Time.increaseTimeValue(attack.getKillTimer(), 2, 0.5f));
        }
        return false;
    }

    public void transitionRoomState(byte roomState){
        this.roomState = roomState;
        if (roomState == 0){
            bedSide.reset();
        } else if (roomState == 1){
            attack.reset(1.15f, 0.025f, 12, (byte) 0, (byte) 0, 26, 26);
            changePath();
        } else if (roomState == 2){
            bed.reset(16, 1.15f);
            changePath();
        } else if (roomState == 3){
            peek.reset(3, 2.25f, 0.75f, 26);
            changePath();
        } else if (roomState == 4){
            leave.reset();
            changePath();
        }
    }

    public void jumpscare(){
        Candys3Deluxe.videoManager.setRequest("game/Cat/Jumpscare/room");
    }

    private final String[] textures = new String[]{
            "Battle/Left",
            "Battle/Middle",
            "Battle/Right",
            "Bed/LeftUnder",
            "Bed/RightUnder",
            "Bed/LeftPeek",
            "Bed/RightPeek",
            "Leaving/Left",
            "Leaving/Middle",
            "Leaving/Right",
            "Tape/Tape",
            "Retreat/Left1",
            "Retreat/Left2",
            "Retreat/Left3",
            "Retreat/Right1",
            "Retreat/Right2",
            "Retreat/Right3"
    };

    public void load(Request request){
        String prefix = "game/Cat/";
        for (String file: textures){
            request.addImageRequest(prefix + file);
        }
    }

    public byte getRoomState() {
        return roomState;
    }

    public byte getSide() {
        return side;
    }

    public BedSide getBedSide() {
        return bedSide;
    }

    public Attack getAttack() {
        return attack;
    }

    public Peek getPeek() {
        return peek;
    }

    public Hitbox getHitbox() {
        return hitbox;
    }

    public float getAttackHealth(){
        return attack.getKillTimer() / 2;
    }

    public int getLeaveFrame(){
        return leave.getFrame();
    }

    public int getAttackFrame(){
        int number = attack.getPosition() * 4;
        if (attack.getMoveFrame() > 0){
            number += 2 + (2 - attack.getMoveFrame());
        } else if (attack.getMoveFrame() < 0){
            number += (-3 - attack.getMoveFrame());
            if (number < 0) number += 12;
        } else {
            if (attack.isTwitching()) number += (int) attack.getTwitch();
        }
        return number;
    }
}
