package studios.kdc.soundboarding.media.mixer;


import android.content.Context;
import android.os.Handler;
import android.util.Log;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import studios.kdc.soundboarding.media.MediaPlayerHandler;
import studios.kdc.soundboarding.models.SelectedTrack;
import studios.kdc.soundboarding.models.imp.SelectedTrackContainerImp;
import studios.kdc.soundboarding.view.adapters.ViewContract;

public class Mixer {
    private Handler handler;
    private Context context;
    private List<MediaPlayerHandler> handlers;
    private ViewContract.mixerProgressChange progressListener;
    private List<CustomRunnable> runList;

    public Mixer(Context context, ViewContract.mixerProgressChange progressListener) {
        this.handler =new Handler();
        this.context = context;
        this.handlers = new ArrayList<>();
        this.progressListener = progressListener;
        runList = new ArrayList<>();
    }

    public void mix() {
        List<SelectedTrack> selectedTrackList = SelectedTrackContainerImp.getInstance().getTracks();
        this.assignStartingPointsForPlaying(selectedTrackList,  progressListener.getCurrentProgress());
        this.assignStartingPointForSlider(selectedTrackList);

    }

    boolean flag = true;
    private void assignStartingPointsForPlaying(List<SelectedTrack> selectedTrackList, final int seekBarPosition ) {
        for(final SelectedTrack selectedTrack : selectedTrackList) {
            CustomRunnable temp;
            if (selectedTrack.getEndPoint() - seekBarPosition <= 0) {

            }else if (selectedTrack.getStartPoint() - seekBarPosition >= 0 ) {
                temp = new CustomRunnable(selectedTrack.getName(), selectedTrack.getGroupName(),
                        0, context);
                runList.add(temp);
                handler.postDelayed(temp, (selectedTrack.getStartPoint() - seekBarPosition) * 1000);// milliseconds
            } else {
                temp = new CustomRunnable(selectedTrack.getName(),
                        selectedTrack.getGroupName(),
                        (seekBarPosition - selectedTrack.getStartPoint()) * 1000,
                        context);
                runList.add(temp);
                handler.postDelayed(temp, 0);

            }
        }
    }
    private void assignStartingPointForSlider(List<SelectedTrack> selectedTrackList) {
        final int maximumEndPoint = this.getMaximumEndPoint(selectedTrackList);
       // final int startPoint = (int) progressListener.getCurrentProgress(); //seconds

        handler.postDelayed(new Runnable() {
            public void run() {
                double  currentDuration = progressListener.getCurrentProgress(); // in seconds
                progressListener.setProgressChange(currentDuration + 1.5);
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
        handlers.clear();
        progressListener.pauseSeekBar();
        for (int i = 0; i < runList.size(); i++) {
            runList.get(i).stopTrack();
            handler.removeCallbacks(runList.get(i));
        }

    }


    public List<MediaPlayerHandler> getHandlers() {
        return handlers;
    }

    public void resume() {
        List<SelectedTrack> selectedTrackList = SelectedTrackContainerImp.getInstance().getTracks();
        handlers.clear();
        this.assignStartingPointsForPlaying(selectedTrackList, progressListener.getCurrentProgress());
        progressListener.resumeSeekBar();
    }

    private void trackToResume(final SelectedTrack selectedTrack, final int seekBarPosition, final MediaPlayerHandler mediaPlayerHandler) {
        if (selectedTrack.getEndPoint() - seekBarPosition <= 0) {
            return;
        } else if (selectedTrack.getStartPoint() - seekBarPosition >= 0) {
            Runnable temp = new Runnable() {
                public void run() {
                    mediaPlayerHandler.playSong(selectedTrack.getGroupName() + File.separator + selectedTrack.getName());
                }
            };
           // runList.add(temp);
            handler.postDelayed(temp, (selectedTrack.getStartPoint() - seekBarPosition) * 1000);// milliseconds
        } else {
            mediaPlayerHandler.seekTo((seekBarPosition - selectedTrack.getStartPoint()) *1000 );
            mediaPlayerHandler.start();
        }

    }

}
