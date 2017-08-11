package studios.kdc.soundboarding.media.mixer;


import android.content.Context;

import java.io.File;

import studios.kdc.soundboarding.media.MediaPlayerHandler;

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
    public String getTrackName() {
        return trackName;
    }

    public void setTrackName(String trackName) {
        this.trackName = trackName;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public int getSeekPosition() {
        return seekPosition;
    }

    public void setSeekPosition(int seekPosition) {
        this.seekPosition = seekPosition;
    }


}