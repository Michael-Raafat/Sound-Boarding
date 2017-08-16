package studios.kdc.soundboarding.media.singlePlayer;

import android.content.Context;

import studios.kdc.soundboarding.media.MediaPlayerHandler;


/**
 * Created by Michael on 8/8/2017.
 */

public class MediaPlayerController implements  MediaPlayerContract.ControllerActions {


    private MediaPlayerHandler mediaPlayerHandler;
    private MediaPlayerContract.OnCompletionListener listener;
    private static MediaPlayerController instance;

    private MediaPlayerController(Context context) {
        this.mediaPlayerHandler = new PlayerHandler(context);
    }

    public static MediaPlayerController getInstance(Context context) {
        if (instance == null) {
            instance = new MediaPlayerController(context);
        }
        return instance;
    }
    public static  void deleteInstance(){
        instance = null;
    }

    @Override
    public void singlePlayAndPauseTrack(String type, String path, MediaPlayerContract.OnCompletionListener listener) {
       if(this.listener != null && this.listener != listener ) {
           this.listener.notifyOnTrackCompletion();
       }
        this.listener = listener;
        this.mediaPlayerHandler.playSong(type, path);
    }


    public void onCompletion(){
        if(this.listener != null)
           this.listener.notifyOnTrackCompletion();
    }
}
