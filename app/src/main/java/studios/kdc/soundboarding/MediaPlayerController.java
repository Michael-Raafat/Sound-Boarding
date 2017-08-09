package studios.kdc.soundboarding;

import android.content.Context;
import android.view.View;

import java.util.List;

import studios.kdc.soundboarding.models.Track;
import studios.kdc.soundboarding.view.adapters.GridViewAdapter;

/**
 * Created by Michael on 8/8/2017.
 */

public class MediaPlayerController implements  MediaPlayerContract.ControllerActions{

    private String name;
    private int position;
    private MediaPlayerHandler mediaPlayerHandler;
    private Context context;
    private static MediaPlayerController instance;

    private MediaPlayerController(Context context) {
        name = "";
        position = 0;
        mediaPlayerHandler = new MediaPlayerHandler(context);
        this.context = context;
    }

    public static MediaPlayerController getInstance(Context context) {
        if (instance == null) {
            instance = new MediaPlayerController(context);
        }
        return instance;
    }

    @Override
    public void singlePlayAndPauseTrack(String groupName, String name) {
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
}
