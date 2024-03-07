package util;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.video.VideoPlayer;
import com.badlogic.gdx.video.VideoPlayerCreator;

import java.io.FileNotFoundException;

public class VideoManager {
    private final VideoPlayer videoPlayer;
    private String request;

    public VideoManager(){
        videoPlayer = VideoPlayerCreator.createVideoPlayer();
        videoPlayer.setOnCompletionListener(file -> dispose());
    }

    public void setRequest(String path){
        request = path;
    }

    private void play(String path){
        try {
            videoPlayer.play(Gdx.files.absolute(JavaInfo.getPath() + path + ".webm"));
        } catch (FileNotFoundException e){
            Gdx.app.error("gdx-video", "Something is wrong!");
        }
    }

    public void updateRender(SpriteBatch batch, int width, int height){
        if (request != null){
            play(request);
            request = null;
        }
        if (!videoPlayer.isPlaying()) return;
        videoPlayer.update();
        Texture video = videoPlayer.getTexture();
        if (video != null){
            batch.draw(video, 0, 0, width, height);
        }
    }

    public void dispose(){
        videoPlayer.dispose();
    }
}
