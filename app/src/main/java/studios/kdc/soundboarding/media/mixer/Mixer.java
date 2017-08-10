package studios.kdc.soundboarding.media.mixer;


import android.content.Context;
import android.os.Handler;
import android.util.Log;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import studios.kdc.soundboarding.media.MediaPlayerHandler;
import studios.kdc.soundboarding.models.SelectedTrack;
import studios.kdc.soundboarding.models.imp.SelectedTrackContainerImp;

public class Mixer {
    private Handler handler;
    private Context context;
    private Pauser pauser;
    private List<MediaPlayerHandler> handlers;
    private boolean flag;
    private int size;

    public Mixer(Context context) {
        this.handler =new Handler();
        this.context = context;
        this.pauser = new Pauser();
        this.handlers = new ArrayList<>();
        this.flag = false;
        this.size = 0;
    }


    public void mix() {
        List<SelectedTrack> selectedTrackList = SelectedTrackContainerImp.getInstance().getTracks();
        if (flag && size == selectedTrackList.size()) {
            for (int i = 0; i < handlers.size(); i++) {
                int pos = handlers.get(i).getCurrentPosition();
                handlers.get(i).seekTo(pos);
                handlers.get(i).start();
                Log.i(selectedTrackList.get(i).getName() + " continue", String.valueOf(i));

            }
            return;
        } else if (flag) {
            int length = handlers.size();
            int i = 0;
            while(i < length) {
                handlers.get(i).stop();
                Log.i(selectedTrackList.get(i).getName() + " clear and start track ", String.valueOf(0));
                i++;
            }
            handlers.clear();
        }
        flag = true;
        size = selectedTrackList.size();
        for(final SelectedTrack selectedTrack : selectedTrackList) {
            handler.postDelayed(new Runnable() {
                public void run() {
                    Log.i(selectedTrack.getName() + " start track", String.valueOf(0));
                    MediaPlayerHandler mediaPlayerHandler = new MixerHandler(context);
                    handlers.add(mediaPlayerHandler);
                    mediaPlayerHandler.playSong(selectedTrack.getGroupName() + File.separator + selectedTrack.getName());
                }
            }, selectedTrack.getStratPoint() * 1000); // milliseconds
        }
    }

    public void pause() {
        List<SelectedTrack> selectedTrackList = SelectedTrackContainerImp.getInstance().getTracks();
         // milliseconds
        for (int i = 0; i < handlers.size(); i++) {
            handlers.get(i).pause();
        }
    }

    public List<MediaPlayerHandler> getHandlers() {
        return handlers;
    }
}
