package studios.kdc.soundboarding.media.mixer;


import android.content.Context;
import android.os.Handler;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import studios.kdc.soundboarding.media.mixer.runnable.CustomRunnable;
import studios.kdc.soundboarding.media.mixer.runnable.SeekBarRunnable;
import studios.kdc.soundboarding.models.SelectedTrack;
import studios.kdc.soundboarding.models.imp.SelectedTrackContainerImp;
import studios.kdc.soundboarding.view.adapters.ViewContract;

public class Mixer {
    private Handler handler;
    private Context context;
    private ViewContract.mixerProgressChange progressListener;
    private SeekBarRunnable seekBarRunnable;
    private List<CustomRunnable> runList;

    public Mixer(Context context, ViewContract.mixerProgressChange progressListener) {
        this.handler =new Handler();
        this.context = context;
        this.progressListener = progressListener;
        this.runList = Collections.synchronizedList(new ArrayList<CustomRunnable>());
        this.seekBarRunnable = new SeekBarRunnable(this.progressListener , this.handler);
    }

    public void mix() {
        List<SelectedTrack> selectedTrackList = SelectedTrackContainerImp.getInstance().getTracks();
        this.assignStartingPointsForPlaying(selectedTrackList,  progressListener.getCurrentProgress());
        this.assignStartingPointForSlider(selectedTrackList);

    }

    private void assignStartingPointsForPlaying(List<SelectedTrack> selectedTrackList, final int seekBarPosition ) {
        int count = 0;
        int size = this.runList.size();
        for(final SelectedTrack selectedTrack : selectedTrackList) {
            if ((selectedTrack.getEndPoint() - seekBarPosition) >= 0) {
                CustomRunnable temp;
                if (count < size) {
                    temp = runList.get(count);
                    temp.setGroupName(selectedTrack.getGroupName());
                    temp.setTrackName(selectedTrack.getName());
                    temp.setSeekPosition(((seekBarPosition - selectedTrack.getStartPoint()) * 1000));
                    count++;
                } else {

                    temp = new CustomRunnable(selectedTrack.getName(), selectedTrack.getGroupName(), ((seekBarPosition - selectedTrack.getStartPoint()) * 1000), context);
                    runList.add(temp);
                }
                handler.postDelayed(temp, (selectedTrack.getStartPoint() - seekBarPosition) * 1000);
            }
        }
    }
    private void assignStartingPointForSlider(List<SelectedTrack> selectedTrackList) {
        this.progressListener.resumeSeekBar();
        this.seekBarRunnable.setMaxEndPoint(this.getMaximumEndPoint(selectedTrackList));
        this.handler.postDelayed(seekBarRunnable, 0); // milliseconds
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
        progressListener.pauseSeekBar();
        for (int i = 0; i < runList.size(); i++) {
            handler.removeCallbacks(runList.get(i));
            runList.get(i).stopTrack();
        }

    }

    public void resume() {

        List<SelectedTrack> selectedTrackList = SelectedTrackContainerImp.getInstance().getTracks();
        this.seekBarRunnable.setMaxEndPoint(this.getMaximumEndPoint(selectedTrackList));
        this.assignStartingPointsForPlaying(selectedTrackList , progressListener.getCurrentProgress());
        progressListener.resumeSeekBar();

    }

}
