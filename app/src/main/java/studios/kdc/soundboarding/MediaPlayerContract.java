package studios.kdc.soundboarding;

import android.view.View;

import studios.kdc.soundboarding.view.adapters.GridViewAdapter;

/**
 * Created by Michael on 8/8/2017.
 */

public class MediaPlayerContract {

    public interface ControllerActions {

        void singlePlayAndPauseTrack(String groupName, String name);

        boolean checkTrackChanged(GridViewAdapter gridViewAdapter, View view, int position, String name);


    }
}
