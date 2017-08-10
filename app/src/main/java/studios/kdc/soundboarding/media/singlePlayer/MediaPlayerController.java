package studios.kdc.soundboarding.media.singlePlayer;

import android.content.Context;
import android.view.View;

import studios.kdc.soundboarding.media.MediaPlayerHandler;


/**
 * Created by Michael on 8/8/2017.
 */

public class MediaPlayerController implements  MediaPlayerContract.ControllerActions {

    private String name;
    private MediaPlayerHandler mediaPlayerHandler;
    private MediaPlayerContract.OnCompletionListener listener;
    private static MediaPlayerController instance;

    private MediaPlayerController(Context context) {
        name = "";
        mediaPlayerHandler = new PlayerHandler(context);
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
    public void singlePlayAndPauseTrack(String groupName, String name, MediaPlayerContract.OnCompletionListener listener) {
       if(this.listener != null && this.listener != listener ) {
           this.listener.notifyOnTrackCompletion();
       }
        this.listener = listener;
        mediaPlayerHandler.playSong(groupName + "/" + name);
    }

    @Override
    public boolean checkTrackChanged(View view, int position, String name) {
        if (!this.name.equals(name)) {
            mediaPlayerHandler.stop();
            this.name = name;
            return true;
        }
        return false;
    }

    public void onCompletion(){
        if(this.listener != null)
           this.listener.notifyOnTrackCompletion();
    }
}
