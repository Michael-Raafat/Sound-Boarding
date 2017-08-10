package studios.kdc.soundboarding.media.mixer;

import android.content.Context;
import android.util.Log;

/**
 * Created by Michael on 8/10/2017.
 */

public class MixerController {

    private Mixer mixer;

    private Context context;
    private static MixerController instance;

    private MixerController (Context context) {
        this.context = context;
        this.mixer = new Mixer(context);
    }

    public static MixerController getInstance(Context context) {
        if (instance == null) {
            instance = new MixerController(context);
        }
        return instance;
    }

    public void mix() {
      this.mixer.mix();
    }

    public void pause() {
        this.mixer.pause();
    }

    public void removeHandler(int position) {
        Log.i("loong", String.valueOf(position));
        mixer.getHandlers().get(position).stop();
        mixer.getHandlers().remove(position);
    }
}
