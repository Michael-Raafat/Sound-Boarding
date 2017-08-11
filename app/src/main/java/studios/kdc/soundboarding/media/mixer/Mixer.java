package studios.kdc.soundboarding.media.mixer;


import android.content.Context;
import android.os.Handler;

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
    private int maximumEndPoint;

    public Mixer(Context context, ViewContract.mixerProgressChange progressListener) {
        this.handler =new Handler();
        this.context = context;
        this.handlers = new ArrayList<>();
        this.progressListener = progressListener;
        runList = Collections.synchronizedList(new ArrayList<CustomRunnable>());
    }

    public void mix() {
        List<SelectedTrack> selectedTrackList = SelectedTrackContainerImp.getInstance().getTracks();
        this.assignStartingPointsForPlaying(selectedTrackList,  progressListener.getCurrentProgress());
        this.assignStartingPointForSlider(selectedTrackList);

    }

    private void assignStartingPointsForPlaying(List<SelectedTrack> selectedTrackList, final int seekBarPosition ) {
        int count = 0;
        int size = runList.size();
        for(final SelectedTrack selectedTrack : selectedTrackList) {
            CustomRunnable temp;
             if(count < size){
                 temp = runList.get(count);
                 temp.setGroupName(selectedTrack.getGroupName());
                 temp.setTrackName(selectedTrack.getName());
                 temp.setSeekPosition( ((seekBarPosition - selectedTrack.getStartPoint()) * 1000));
                 count++;
             }
            else {

                temp= new CustomRunnable(selectedTrack.getName() , selectedTrack.getGroupName(), ((seekBarPosition - selectedTrack.getStartPoint()) * 1000) , context);
            /*Runnable temp;
            if (selectedTrack.getEndPoint() - seekBarPosition <= 0) {

            }else if (selectedTrack.getStartPoint() - seekBarPosition >= 0 ) {
                temp = new Runnable() {
                    public void run() {
                        MediaPlayerHandler mediaPlayerHandler = new MixerHandler(context);
                        mediaPlayerHandler.playSong(selectedTrack.getGroupName() + File.separator + selectedTrack.getName());
                        handlers.add(mediaPlayerHandler);
                    }
                };
                runList.add(temp);
                handler.postDelayed(temp, (selectedTrack.getStartPoint() - seekBarPosition) * 1000);// milliseconds
            } else {
                temp = new Runnable() {
                    public void run() {
                        MediaPlayerHandler mediaPlayerHandler = new MixerHandler(context);
                        mediaPlayerHandler.playSong(selectedTrack.getGroupName() + File.separator + selectedTrack.getName());
                        mediaPlayerHandler.seekTo((seekBarPosition - selectedTrack.getStartPoint()) * 1000);
                        mediaPlayerHandler.start();
                        handlers.add(mediaPlayerHandler);
                    }
                };


                */

                runList.add(temp);
        }
            handler.postDelayed(temp, (selectedTrack.getStartPoint() - seekBarPosition) * 1000);
        }
    }
    private void assignStartingPointForSlider(List<SelectedTrack> selectedTrackList) {

        this.maximumEndPoint = this.getMaximumEndPoint(selectedTrackList);
        handler.postDelayed(new Runnable() {
            public void run() {
                double  currentDuration = progressListener.getCurrentProgress(); // in seconds
                progressListener.setProgressChange(currentDuration + 1.5);
                if(currentDuration < maximumEndPoint) {
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
       /* for (int i = 0; i < handlers.size(); i++) {
            handlers.get(i).pause();
        }*/
        progressListener.pauseSeekBar();
        for (int i = 0; i < runList.size(); i++) {
            handler.removeCallbacks(runList.get(i));
            runList.get(i).stopTrack();
        }

    }


    public List<MediaPlayerHandler> getHandlers() {
        return handlers;
    }

    public void resume() {

        List<SelectedTrack> selectedTrackList = SelectedTrackContainerImp.getInstance().getTracks();
        this.maximumEndPoint = this.getMaximumEndPoint(selectedTrackList);
        this.assignStartingPointsForPlaying(selectedTrackList , progressListener.getCurrentProgress());
        progressListener.resumeSeekBar();
        /*if (selectedTrackList.size() == handlers.size()) {
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
        progressListener.resumeSeekBar();*/
    }

   /* private void trackToResume(final SelectedTrack selectedTrack, final int seekBarPosition, final MediaPlayerHandler mediaPlayerHandler) {
        if (selectedTrack.getEndPoint() - seekBarPosition <= 0) {
            return;
        } else if (selectedTrack.getStartPoint() - seekBarPosition >= 0) {
            Runnable temp = new Runnable() {
                public void run() {
                    mediaPlayerHandler.playSong(selectedTrack.getGroupName() + File.separator + selectedTrack.getName());
                }
            };
            runList.add(temp);
            handler.postDelayed(temp, (selectedTrack.getStartPoint() - seekBarPosition) * 1000);// milliseconds
        } else {
            mediaPlayerHandler.seekTo((seekBarPosition - selectedTrack.getStartPoint()) *1000 );
            mediaPlayerHandler.start();
        }

    }*/

}
