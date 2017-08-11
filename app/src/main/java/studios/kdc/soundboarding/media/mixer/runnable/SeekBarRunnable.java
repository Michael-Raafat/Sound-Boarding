package studios.kdc.soundboarding.media.mixer.runnable;


import android.os.Handler;

import studios.kdc.soundboarding.models.imp.SelectedTrackContainerImp;
import studios.kdc.soundboarding.view.adapters.ViewContract;

public class SeekBarRunnable implements Runnable{
    private int maximumEndPoint;
    private ViewContract.mixerProgressChange progressListener;
    private Handler handler;



    public SeekBarRunnable(ViewContract.mixerProgressChange progressListener , Handler handler){
        this.progressListener = progressListener;
        this.handler = handler;
    }


    public void setMaxEndPoint(int maxEndPoint) {
        this.maximumEndPoint = maxEndPoint;
    }




    @Override
    public void run() {
        double  currentDuration = progressListener.getCurrentProgress(); // in seconds
        progressListener.setProgressChange(currentDuration + 1.5);
        if(currentDuration < maximumEndPoint && !SelectedTrackContainerImp.getInstance().getTracks().isEmpty()) {
            handler.postDelayed(this, 1000);
        } else {
            progressListener.notifyTrackFinished();
        }

    }
}
