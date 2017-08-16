package studios.kdc.soundboarding.media;

import android.annotation.SuppressLint;
import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.util.Log;

import studios.kdc.soundboarding.media.singlePlayer.MediaPlayerStrategy;
import studios.kdc.soundboarding.playerStrategy.PlayerStrategyFactory;


/**
 * Created by Michael on 8/8/2017.
 */

public abstract class MediaPlayerHandler {

    protected MediaPlayer mediaPlayer;
    protected Context context;
    private AudioManager audioManager;
    private int maxVolume;
    private int curVolume;
    private String trackPath;
    private boolean flag;
    private int currentPosition;
    private PlayerStrategyFactory playerStrategyFactory ;

    public MediaPlayerHandler(Context context) {
        this.mediaPlayer = new MediaPlayer();
        this.context = context;
        this.setMediaPlayerListener();
        this.audioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
        this.maxVolume = audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
        this.curVolume = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
        this.trackPath = null;
        this.flag = false;
        playerStrategyFactory = new PlayerStrategyFactory(context);

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
    public void start() {this.mediaPlayer.start ();}
    public void pause() {this.mediaPlayer.pause ();}
    public void stop() {this.mediaPlayer.stop ();}
    public void setVolume(int vol) {
        this.audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, vol, 0);

    }


    protected abstract void setMediaPlayerListener();

    @SuppressLint("NewApi")
    public void playSong(String type, String path) {
        if (this.mediaPlayer.getDuration() != 0) {
            if(mediaPlayer.isPlaying() && this.trackPath.equals(path)) {
                this.mediaPlayer.pause();
                this.currentPosition = mediaPlayer.getCurrentPosition();
                this.flag = true;
                return;
            }
        }
        if (trackPath != null && trackPath.equals(path) && this.flag) {
            this.seekTo(currentPosition);
            this.start();
            this.flag = false;
            return;
        }
        this.mediaPlayer.reset();
        try {
            MediaPlayerStrategy mediaPlayerStrategy = playerStrategyFactory.createPlayerStrategy(type);
            mediaPlayerStrategy.playMedia(this.mediaPlayer, path);
            this.mediaPlayer.prepare();
            this.mediaPlayer.start();
            this.trackPath = path;
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
