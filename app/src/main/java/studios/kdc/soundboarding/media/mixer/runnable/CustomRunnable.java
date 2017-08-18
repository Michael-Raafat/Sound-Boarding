package studios.kdc.soundboarding.media.mixer.runnable;


import android.content.Context;


import studios.kdc.soundboarding.media.MediaPlayerHandler;
import studios.kdc.soundboarding.media.mixer.MixerHandler;

public class CustomRunnable implements Runnable {


    private String trackPath;
    private String trackType;
    private int seekPosition;
    private MediaPlayerHandler mediaPlayerHandler;
    private int volume;

    public CustomRunnable(String trackPath, String trackType, int seekPosition , int volume, Context context){
        this.seekPosition = seekPosition;
        this.mediaPlayerHandler = new MixerHandler(context);
        this.trackType = trackType;
        this.trackPath = trackPath;
        this.volume = volume;
    }


    @Override
    public void run() {
            this.mediaPlayerHandler.playSong(this.trackType, this.trackPath);
            this.mediaPlayerHandler.setVolume(this.volume);
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

    public void setVolume(int volume) {
        this.volume = volume;
        this.mediaPlayerHandler.setVolume(this.volume);
    }

}
