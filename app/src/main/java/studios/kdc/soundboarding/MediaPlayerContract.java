package studios.kdc.soundboarding;

import android.view.View;

/**
 * Created by Michael on 8/8/2017.
 */

public class MediaPlayerContract {

    public interface ControllerActions {

        void singlePlayAndPauseTrack(String groupName, String name);

        boolean checkTrackChanged(View view, int position, String name);


    }
}