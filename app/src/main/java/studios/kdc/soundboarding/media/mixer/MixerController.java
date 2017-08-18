package studios.kdc.soundboarding.media.mixer;

import android.content.Context;
import studios.kdc.soundboarding.view.ViewContract;

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
        if (instance == null)
            instance = new MixerController(context ,progressListener);
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

    public void resume() {
        this.mixer.resume();
    }

    public int getCurrentVolumeOf(int position){
        return this.mixer.getCurrentVolumeOf(position);
    }

    public void setCurrentVolumeOf(int position , int progress) {
        this.mixer.setCurrentVolumeOf(position , progress);
    }
}
