package studios.kdc.soundboarding;


import android.content.Context;
import android.os.Handler;

import java.io.File;
import java.util.List;

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
                    MediaPlayerHandler mediaPlayerHandler = new MediaPlayerHandler(context);
                    mediaPlayerHandler.playSong(selectedTrack.getName() + File.separator + selectedTrack.getName() +".mp3");
                }
            }, selectedTrack.getStratPoint() * 1000); // milliseconds
        }
    }

}
