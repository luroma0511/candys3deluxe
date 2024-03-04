package game.engine.util;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.files.FileHandle;

import java.util.HashMap;
import java.util.Map;

public class SoundManager {
    private final Map<String, AudioClass> audios;

    public SoundManager(){
        audios = new HashMap<>();
    }

    public boolean loadingSounds(Request request){
        if (request.soundsIsEmpty()) return false;
        String path = request.getSound(0);
        if (!audios.containsKey(path)) {
            FileHandle file = Gdx.files.absolute(JavaInfo.getPath() + "sounds\\" + path + ".wav");
            Sound sound = Gdx.audio.newSound(file);
            AudioClass audioClass = new AudioClass(sound);
            audios.put(path, audioClass);
        }
        request.removeSound(0);
        return true;
    }

    public void play(String path){
        if (audios.containsKey(path)) audios.get(path).play();
    }

    public void stop(String path){
        if (audios.containsKey(path)) audios.get(path).stop();
    }

    public void stopAllSounds(){
        for (AudioClass audioClass: audios.values()){
            audioClass.stop();
        }
    }

    public void setLoop(String path, boolean loop){
        if (!audios.containsKey(path)) return;
        audios.get(path).setLoop(loop);
    }

    public float getVolume(String path){
        if (!audios.containsKey(path)) return -1;
        return audios.get(path).getVolume();
    }

    public void setVolume(String path, float volume){
        AudioClass audio = audios.get(path);
        if (audio != null && audio.getVolume() != volume) audio.setVolume(volume);
    }

    public float getPitch(String path){
        if (!audios.containsKey(path)) return -1;
        return audios.get(path).getPitch();
    }

    public void setPitch(String path, float pitch){
        AudioClass audio = audios.get(path);
        if (audio != null && audio.getPitch() != pitch) audio.setPitch(pitch);
    }

    public void dispose(){
        for (AudioClass audioClass: audios.values()) audioClass.dispose();
        audios.clear();
    }
}
