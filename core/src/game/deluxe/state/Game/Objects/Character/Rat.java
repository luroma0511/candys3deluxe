package game.deluxe.state.Game.Objects.Character;

import java.util.Random;

import game.deluxe.state.Game.Objects.Character.Attributes.Attack;
import game.deluxe.state.Game.Objects.Character.Attributes.Bed;
import game.deluxe.state.Game.Objects.Character.Attributes.Door;
import game.deluxe.state.Game.Objects.Character.Attributes.Hitbox;
import game.deluxe.state.Game.Objects.Character.Attributes.Peek;
import game.deluxe.state.Game.Objects.Player;
import game.deluxe.state.Game.Objects.Room;
import game.engine.util.*;

public class Rat extends SpriteObject {
    private final Random random;

    private final Hitbox hitbox;
    private final Attack attack;
    private final Door door;
    private final Bed bed;
    private final Peek peek;

    private float transitionCooldown;

    private byte ai;
    private byte roomState;
    private byte side;

    public Rat(byte ai){
        super();
        this.ai = ai;
        random = new Random();
        hitbox = new Hitbox(100);
        door = new Door(6);
        door.reset(5);
        attack = new Attack();
        bed = new Bed();
        peek = new Peek();
    }

    public boolean input(Engine engine, Player player, Room room, float mx, float my, boolean twitch){
        switch (roomState){
            case 0:
                door.input(engine, hitbox, mx, my);
                break;
            case 1:
                twitch = attack.input(engine, player, hitbox, this, mx, my, twitch, 0);
                break;
            case 2:
                bed.input(engine, player, room, this, mx, my);
                break;
            case 3:
                twitch = peek.input(engine, hitbox, player, mx, my, twitch, 1.25f);
                break;
        }
        return twitch;
    }

    public void update(Engine engine, Player player){
        switch (roomState){
            case 0:
                side = door.update(engine, random, side);
                if (door.isSignal()) {
                    if (side == 0) hitbox.setCoord(481, 663);
                    else if (side == 1) hitbox.setCoord(1531, 696);
                    else hitbox.setCoord(2525, 715);
                    changePath();
                }
                if (!door.isTimeUp()) return;
                transitionRoomState((byte) 1);
                engine.getSoundManager().play("get_in");
                player.setBlacknessTimes(2);
                player.setBlacknessSpeed(6);
                break;
            case 1:
                if (roomUpdate(engine, player) || !attack.isMoved()) return;
                if (side == 0) {
                    if (attack.getPosition() == 0) hitbox.setCoord(376, 765);
                    else if (attack.getPosition() == 1) hitbox.setCoord(289, 621);
                    else hitbox.setCoord(470, 595);
                } else if (side == 1) {
                    if (attack.getPosition() == 0) hitbox.setCoord(1521, 720);
                    else if (attack.getPosition() == 1) hitbox.setCoord(1315, 574);
                    else hitbox.setCoord(1686, 604);
                } else {
                    if (attack.getPosition() == 0) hitbox.setCoord(2560, 869);
                    else if (attack.getPosition() == 1) hitbox.setCoord(2402, 680);
                    else hitbox.setCoord(2734, 651);
                }
                attack.setMoved();
                if (attack.getFlashTime() != 0) {
                    attack.setLimit(3);
                    break;
                }
                if (attack.getLimit() != 0 && random.nextInt(5) == 2) {
                    attack.setLimit(attack.getLimit() - 1);
                    break;
                }
                attack.setLimit(3);
                attack.setFlashTime(0.3f + random.nextInt(5) * 0.125f);
//                    if (random.nextInt(5) == 2) attack.setSkip();
                break;
            case 2:
                if (bed.killTime()) return;
                if (!bed.update(engine, player)) return;
                transitionRoomState((byte) 3);
                engine.getSoundManager().play("peek");
                break;
            case 3:
                if (peek.update(engine)) transitionRoomState((byte) 4);
                break;
        }
    }

    public void changePath(){
        if (roomState == 0) {
            if (side == 0) {
                setPath("game/Rat/Looking Away/Left");
                setX(380);
                setY(372);
                setWidth(130);
            } else if (side == 1) {
                setPath("game/Rat/Looking Away/Middle");
                setX(1411);
                setY(382);
                setWidth(232);
            } else {
                setPath("game/Rat/Looking Away/Right");
                setX(2483);
                setY(321);
                setWidth(105);
            }
        } else if (roomState == 1){
            if (side == 0) {
                setPath("game/Rat/Battle/Left");
                setX(142);
                setY(327);
                setWidth(464);
                setHeight(512);
            } else if (side == 1) {
                setPath("game/Rat/Battle/Middle");
                setX(1144);
                setY(45);
                setWidth(736);
                setHeight(801);
            } else {
                setPath("game/Rat/Battle/Right");
                setX(2165);
                setY(195);
                setWidth(826);
                setHeight(829);
            }
        } else if (roomState == 2){
            if (side == 0) {
                setPath("game/Rat/Bed/LeftUnder");
                setX(0);
                setY(155);
                setWidth(915);
                setHeight(529);
            } else {
                setPath("game/Rat/Bed/RightUnder");
                setX(1438);
                setY(190);
                setWidth(610);
                setHeight(521);
            }
        } else if (roomState == 3){
            if (side == 0) {
                hitbox.setCoord(706, 450);
                setPath("game/Rat/Bed/LeftPeek");
                setX(414);
                setY(217);
                setWidth(425);
                setHeight(369);
            } else {
                hitbox.setCoord(2361, 470);
                setPath("game/Rat/Bed/RightPeek");
                setX(2127);
                setY(195);
                setWidth(589);
                setHeight(496);
            }
        } else {

        }
    }

    private boolean roomUpdate(Engine engine, Player player){
        if (transitionCooldown != 0) {
            transitionCooldown = engine.decreaseTimeValue(transitionCooldown, 0, 1);
            if (transitionCooldown == 0) {
                player.setScared();
                player.setAttack();
                transitionRoomState((byte) 2);
                side = (byte) (random.nextInt(2) * 2);
                engine.getSoundManager().play("crawl");
                player.setBlacknessSpeed(1);
            }
            return true;
        }

        if (attack.update(engine, random, player.isScared())){
            transitionCooldown = 0.5f;
            engine.getSoundManager().play("thunder");
            player.setBlacknessDelay(1.25f);
            player.setBlacknessSpeed(6);
            player.setBlacknessTimes(2);
            return true;
        }
        return false;
    }

    public void transitionRoomState(byte roomState){
        this.roomState = roomState;
        if (roomState == 0){
            door.reset(5);
        } else if (roomState == 1){
            attack.reset(1.15f, 0.05f, 12, (byte) 0, (byte) 0, 25, 26);
            changePath();
        } else if (roomState == 2){
            bed.reset(10, 1.15f);
            changePath();
        } else if (roomState == 3){
            peek.reset(3, 2.25f, 1.25f, 26);
            changePath();
        }
    }

    public void jumpscare(VideoManager videoManager){
        videoManager.setRequest("game/Rat/Jumpscare/room");
    }

    private final String[] textures = new String[]{
            "Battle/Left",
            "Battle/Middle",
            "Battle/Right",
            "Bed/LeftPeek",
            "Bed/RightPeek",
            "Leaving/Left",
            "Leaving/Right",
            "Tape/Tape",
            "Looking Away/Left",
            "Looking Away/Middle",
            "Looking Away/Right"
    };

    public void load(Request request){
        String prefix = "game/Rat/";
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

    public Door getDoor() {
        return door;
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
