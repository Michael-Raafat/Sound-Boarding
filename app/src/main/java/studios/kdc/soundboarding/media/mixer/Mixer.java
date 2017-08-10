package studios.kdc.soundboarding.media.mixer;


import android.content.Context;
import android.os.Handler;
import android.util.Log;

import java.io.File;
import java.util.List;

import studios.kdc.soundboarding.media.MediaPlayerHandler;
import studios.kdc.soundboarding.models.SelectedTrack;
import studios.kdc.soundboarding.models.imp.SelectedTrackContainerImp;

public class Mixer {
    private Handler handler;
    private Context context;

    public Mixer(Context context) {
        this.handler =new Handler();
        this.context = context;
    }

    public void mix() {
        List<SelectedTrack> selectedTrackList = SelectedTrackContainerImp.getInstance().getTracks();
        for(final SelectedTrack selectedTrack : selectedTrackList) {
            handler.postDelayed(new Runnable() {
                public void run() {
                    MediaPlayerHandler mediaPlayerHandler = new MixerHandler(context);
                    mediaPlayerHandler.playSong(selectedTrack.getGroupName() + File.separator + selectedTrack.getName());
                }
            }, selectedTrack.getStratPoint() * 1000); // milliseconds
        }
    }

}
