package studios.kdc.soundboarding.media;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.media.AudioManager;
import android.media.MediaPlayer;


/**
 * Created by Michael on 8/8/2017.
 */

public abstract class MediaPlayerHandler {

    protected MediaPlayer mediaPlayer;
    protected Context context;
    private AudioManager audioManager;
    private int maxVolume;
    private int curVolume;
    private String trackName;
    private boolean flag;
    private int currentPosition;


    public MediaPlayerHandler(Context context) {
        this.mediaPlayer = new MediaPlayer();
        this.context = context;
        this.setMediaPlayerListener();
        this.audioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
        this.maxVolume = audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
        this.curVolume = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
        this.trackName = null;
        this.flag = false;

    }

    public void seekTo(int i) {
        this.mediaPlayer.seekTo(i);
    }
    public int getDuration(){
        return this.mediaPlayer.getDuration();
    }
    public int getCurrentPosition(){
        return this.mediaPlayer.getCurrentPosition();
    }
    public boolean isPlaying() {return this.mediaPlayer.isPlaying ();}
    public void start() {mediaPlayer.start ();}
    public void pause() {mediaPlayer.pause ();}
    public void stop() {mediaPlayer.stop ();}
    public void setVolume(int vol) {
        this.audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, vol, 0);

    }


    protected abstract void setMediaPlayerListener();

    @SuppressLint("NewApi")
    public void playSong(String name) {
        if (mediaPlayer.getDuration() != -1) {
            if(mediaPlayer.isPlaying() && this.trackName.equals(name)) {
                mediaPlayer.pause();
                this.currentPosition = mediaPlayer.getCurrentPosition();
                flag = true;
                return;
            }
        }
        if (trackName != null && trackName.equals(name) && flag) {
            this.seekTo(currentPosition);
            this.start();
            flag = false;
            return;
        }
        this.mediaPlayer.reset();
        try {  //todo call to playing strategy
            AssetFileDescriptor afd = context.getAssets().openFd(name + ".mp3");
            this.mediaPlayer.setDataSource(afd.getFileDescriptor(),afd.getStartOffset(),afd.getLength());
            this.mediaPlayer.prepare();
            this.mediaPlayer.start();
            this.trackName = name;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public int getMaximumVolume() {
        return this.maxVolume;
    }

    public int getCurrentVolume() {
        return this.curVolume;
    }

}
