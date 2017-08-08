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
    private MediaPlayerContract.AdapterActions gridViewAdapter;
    private MediaPlayerHandler mediaPlayerHandler;
    private Context context;

    public MediaPlayerController(MediaPlayerContract.AdapterActions gridViewAdapter,
                                 Context context,
                                 List<Track> media,
                                 int color ,
                                 int cardPosition,
                                 String groupName) {
        name = "";
        position = 0;
        mediaPlayerHandler = new MediaPlayerHandler(context);
        this.gridViewAdapter = gridViewAdapter;
        this.context = context;
    }

    @Override
    public void singlePlayAndPauseTrack(View v, int position, String name) {
        mediaPlayerHandler.playSong(name);
    }

    @Override
    public void checkTrackChanged(View view, int position, String name) {
        if (!this.name.equals(name)) {
            mediaPlayerHandler = new MediaPlayerHandler(context);
            gridViewAdapter.setOnFirstClickListener(view, position, name);
            this.name = name;
        } else {
            gridViewAdapter.setOnSecondClickListener(view, position, name);
        }
    }
}
