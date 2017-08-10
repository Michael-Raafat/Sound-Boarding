package studios.kdc.soundboarding.media.mixer;


import android.content.Context;
import android.os.Handler;
import android.util.Log;

import java.io.File;
import java.util.List;

import studios.kdc.soundboarding.media.MediaPlayerHandler;
import studios.kdc.soundboarding.models.SelectedTrack;
import studios.kdc.soundboarding.models.imp.SelectedTrackContainerImp;
import studios.kdc.soundboarding.view.adapters.ViewContract;

public class Mixer {
    private Handler handler;
    private Context context;
    private Pauser pauser;
    private List<MediaPlayerHandler> handlers;
    private boolean flag;
    private int size;
    private ViewContract.mixerProgressChange progressListener;

    public Mixer(Context context) {
        this.handler =new Handler();
        this.context = context;
        this.pauser = new Pauser();
        this.handlers = new ArrayList<>();
        this.flag = false;
        this.size = 0;
        this.progressListener = progressListener;
    }

    public void mix() {
        List<SelectedTrack> selectedTrackList = SelectedTrackContainerImp.getInstance().getTracks();
        this.assignStartingPointsForPlaying(selectedTrackList);
        this.assignStartingPointForSlider(selectedTrackList);

    }

    private void assignStartingPointsForPlaying(List<SelectedTrack> selectedTrackList ) {
        for(final SelectedTrack selectedTrack : selectedTrackList) {
            handler.postDelayed(new Runnable() {
                public void run() {
                    MediaPlayerHandler mediaPlayerHandler = new MixerHandler(context);
                    mediaPlayerHandler.playSong(selectedTrack.getGroupName() + File.separator + selectedTrack.getName());
                }
            }, selectedTrack.getStratPoint() * 1000); // milliseconds
        }
    }
    private void assignStartingPointForSlider(List<SelectedTrack> selectedTrackList) {
        final int maximumEndPoint = this.getMaximumEndPoint(selectedTrackList);
       // final int startPoint = (int) progressListener.getCurrentProgress(); //seconds
        handler.postDelayed(new Runnable() {
            public void run() {
                double  currentDuration = progressListener.getCurrentProgress(); // in seconds
                progressListener.setProgressChange(currentDuration + 1);
                if(currentDuration <= maximumEndPoint) {
                    handler.postDelayed(this, 1000);
                } else {
                    progressListener.notifyTrackFinished();
                }

            }
        }, 1000); // milliseconds

    }
    private int getMaximumEndPoint(List<SelectedTrack> selectedTrackList ) {
        int maximumEndPoint = 0;
        for(SelectedTrack selectedTrack : selectedTrackList) {
            if(selectedTrack.getEndPoint() > maximumEndPoint)
                maximumEndPoint = selectedTrack.getEndPoint();
        }
        return maximumEndPoint;

    }

}
