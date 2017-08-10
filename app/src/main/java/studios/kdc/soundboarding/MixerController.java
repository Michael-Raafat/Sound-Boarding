package studios.kdc.soundboarding;

import android.content.Context;

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

}
