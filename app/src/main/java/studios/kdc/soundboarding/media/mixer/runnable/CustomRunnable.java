package studios.kdc.soundboarding.media.mixer.runnable;


import android.content.Context;

import java.io.File;

import studios.kdc.soundboarding.media.MediaPlayerHandler;
import studios.kdc.soundboarding.media.mixer.MixerHandler;

public class CustomRunnable implements Runnable {


    private String trackPath;
    private String trackType;
    private int seekPosition;
    private MediaPlayerHandler mediaPlayerHandler;

    public CustomRunnable(String trackPath, String trackType, int seekPosition , Context context){
        this.seekPosition = seekPosition;
        this.mediaPlayerHandler = new MixerHandler(context);
        this.trackType = trackType;
        this.trackPath = trackPath;
    }


    @Override
    public void run() {
            this.mediaPlayerHandler.playSong(this.trackType, this.trackPath);
            this.mediaPlayerHandler.seekTo(seekPosition);
            this.mediaPlayerHandler.start();
    }


    public void stopTrack(){
        this.mediaPlayerHandler.stop();
    }

    public void setSeekPosition(int seekPosition) {
        this.seekPosition = seekPosition;
    }


    public void setTrackType(String trackType) {
        this.trackType = trackType;
    }

    public void setTrackPath(String trackPath) {
        this.trackPath = trackPath;
    }

}
