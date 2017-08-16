package studios.kdc.soundboarding.media.singlePlayer;


/**
 * Created by Michael on 8/8/2017.
 */

public class MediaPlayerContract {

    public interface ControllerActions {

        void singlePlayAndPauseTrack(String groupName, String name, MediaPlayerContract.OnCompletionListener listener);

    }

    public interface OnCompletionListener {

        void notifyOnTrackCompletion();
    }
}
