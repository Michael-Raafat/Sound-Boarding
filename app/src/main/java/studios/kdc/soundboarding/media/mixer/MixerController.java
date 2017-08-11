package studios.kdc.soundboarding.media.mixer;

import android.content.Context;
import android.util.Log;

import studios.kdc.soundboarding.models.imp.SelectedTrackContainerImp;
import studios.kdc.soundboarding.view.adapters.ViewContract;

/**
 * Created by Michael on 8/10/2017.
 */

public class MixerController {

    private Mixer mixer;
    private static MixerController instance;

    private MixerController (Context context, ViewContract.mixerProgressChange progressListener) {

        this.mixer = new Mixer(context,progressListener );
    }

    public static MixerController getInstance(Context context, ViewContract.mixerProgressChange progressListener) {
        if (instance == null) {
            instance = new MixerController(context ,progressListener);
        }
        return instance;
    }
    public static void deleteInstance() {
        instance = null;
    }
    public void mix() {
      this.mixer.mix();
    }

    public void pause() {
        this.mixer.pause();
    }

    public void removeHandler(int position) {

        if (position < this.mixer.getHandlers().size()) {
            this.mixer.getHandlers().get(position).stop();
            this.mixer.getHandlers().remove(position);
        }
    }

    public void resume() {
        this.mixer.resume();
    }
}
