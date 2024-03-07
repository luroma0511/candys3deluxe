package util;

import java.util.ArrayList;
import java.util.List;

public class Request {
    private final List<String> imageRequests;
    private final List<String> soundRequests;
    private boolean imagesEmptyLock;
    private boolean soundsEmptyLock;
    boolean resetDeltaTime;

    public Request(){
        imageRequests = new ArrayList<>();
        soundRequests = new ArrayList<>();
    }

    public void addImageRequest(String path){
        imageRequests.add(path);
        imagesEmptyLock = false;
    }

    public String getImage(int index){
        return imageRequests.get(index);
    }

    public void removeImage(int index){
        imageRequests.remove(index);
    }

    public boolean imagesIsEmpty(){
        if (!imagesEmptyLock && imageRequests.isEmpty()) imagesEmptyLock = true;
        return imagesEmptyLock;
    }

    public void addSoundRequest(String path){
        soundRequests.add(path);
        soundsEmptyLock = false;
    }

    public String getSound(int index){
        return soundRequests.get(index);
    }

    public void removeSound(int index){
        soundRequests.remove(index);
    }

    public boolean soundsIsEmpty(){
        if (!soundsEmptyLock && soundRequests.isEmpty()) soundsEmptyLock = true;
        return soundsEmptyLock;
    }
}
