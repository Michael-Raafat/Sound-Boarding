package studios.kdc.soundboarding.media.mixer.runnable;


import android.content.Context;

import java.io.File;

import studios.kdc.soundboarding.media.MediaPlayerHandler;
import studios.kdc.soundboarding.media.mixer.MixerHandler;

public class CustomRunnable implements Runnable {

    private String trackName;
    private String groupName;
    private int seekPosition;
    private MediaPlayerHandler mediaPlayerHandler;

    public CustomRunnable(String trackName, String groupName, int seekPosition , Context context){
        this.groupName = groupName;
        this.trackName = trackName;
        this.seekPosition = seekPosition;
        this.mediaPlayerHandler = new MixerHandler(context);
    }


    @Override
    public void run() {
            mediaPlayerHandler.playSong(this.groupName + File.separator + this.trackName);
            mediaPlayerHandler.seekTo(seekPosition);
            mediaPlayerHandler.start();
    }


    public void stopTrack(){
        this.mediaPlayerHandler.stop();
    }

    public void setTrackName(String trackName) {
        this.trackName = trackName;
    }


    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }


    public void setSeekPosition(int seekPosition) {
        this.seekPosition = seekPosition;
    }


}
