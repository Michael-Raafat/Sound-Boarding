package studios.kdc.soundboarding.media.mixer;


import android.content.Context;
import android.os.Handler;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

import studios.kdc.soundboarding.media.MediaPlayerHandler;
import studios.kdc.soundboarding.models.SelectedTrack;
import studios.kdc.soundboarding.models.imp.SelectedTrackContainerImp;
import studios.kdc.soundboarding.view.adapters.ViewContract;

public class Mixer {
    private Handler handler;
    private Context context;
   // private Pauser pauser;
    private List<MediaPlayerHandler> handlers;
    private int size;
    private ViewContract.mixerProgressChange progressListener;

    public Mixer(Context context, ViewContract.mixerProgressChange progressListener) {
        this.handler =new Handler();
        this.context = context;
       // this.pauser = new Pauser();
        this.handlers = new ArrayList<>();
        this.size = 0;
        this.progressListener = progressListener;
    }

    public void mix() {
        List<SelectedTrack> selectedTrackList = SelectedTrackContainerImp.getInstance().getTracks();
        size = selectedTrackList.size();
        this.assignStartingPointsForPlaying(selectedTrackList,  (int) progressListener.getCurrentProgress());
        this.assignStartingPointForSlider(selectedTrackList);

    }

    private void assignStartingPointsForPlaying(List<SelectedTrack> selectedTrackList, final int seekBarPosition ) {
        for(final SelectedTrack selectedTrack : selectedTrackList) {
            if (selectedTrack.getEndPoint() - seekBarPosition <= 0) {

            }else if (selectedTrack.getStartPoint() - seekBarPosition >= 0 ) {
                handler.postDelayed(new Runnable() {
                    public void run() {
                        MediaPlayerHandler mediaPlayerHandler = new MixerHandler(context);
                        mediaPlayerHandler.playSong(selectedTrack.getGroupName() + File.separator + selectedTrack.getName());
                        handlers.add(mediaPlayerHandler);
                    }
                }, (selectedTrack.getStartPoint() - seekBarPosition) * 1000);// milliseconds
            } else {
                handler.postDelayed(new Runnable() {
                    public void run() {
                        MediaPlayerHandler mediaPlayerHandler = new MixerHandler(context);
                        mediaPlayerHandler.playSong(selectedTrack.getGroupName() + File.separator + selectedTrack.getName());
                        mediaPlayerHandler.seekTo((seekBarPosition - selectedTrack.getStartPoint()) * 1000);
                        mediaPlayerHandler.start();
                        handlers.add(mediaPlayerHandler);
                    }
                }, 0);
            }
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
        }, 0); // milliseconds
    }
    private int getMaximumEndPoint(List<SelectedTrack> selectedTrackList ) {
        int maximumEndPoint = 0;
        for(SelectedTrack selectedTrack : selectedTrackList) {
            if(selectedTrack.getEndPoint() > maximumEndPoint)
                maximumEndPoint = selectedTrack.getEndPoint();
        }
        return maximumEndPoint;

    }

    public void pause() {
        for (int i = 0; i < handlers.size(); i++) {
            handlers.get(i).pause();
        }
        progressListener.pauseSeekBar();
    }


    public List<MediaPlayerHandler> getHandlers() {
        return handlers;
    }

    public void resume() {
        List<SelectedTrack> selectedTrackList = SelectedTrackContainerImp.getInstance().getTracks();
        if (selectedTrackList.size() == handlers.size()) {
            for (int i = 0; i < handlers.size(); i++) {
                this.trackToResume(selectedTrackList.get(i), progressListener.getCurrentProgress(),handlers.get(i));
            }
        } else {
            int length = handlers.size();
            int i = 0;
            while(i < length) {
                handlers.get(i).stop();
                i++;
            }
            handlers.clear();
            this.assignStartingPointsForPlaying(selectedTrackList, progressListener.getCurrentProgress());
        }
        progressListener.resumeSeekBar();
    }

    private void trackToResume(final SelectedTrack selectedTrack, final int seekBarPosition, final MediaPlayerHandler mediaPlayerHandler) {
        if (selectedTrack.getEndPoint() - seekBarPosition <= 0) {
            return;
        } else if (selectedTrack.getStartPoint() - seekBarPosition >= 0) {
            handler.postDelayed(new Runnable() {
                public void run() {
                    mediaPlayerHandler.playSong(selectedTrack.getGroupName() + File.separator + selectedTrack.getName());
                }
            }, (selectedTrack.getStartPoint() - seekBarPosition) * 1000);// milliseconds
        } else {
            mediaPlayerHandler.seekTo((seekBarPosition - selectedTrack.getStartPoint()) * 1000 );
            mediaPlayerHandler.start();
        }

    }

}
