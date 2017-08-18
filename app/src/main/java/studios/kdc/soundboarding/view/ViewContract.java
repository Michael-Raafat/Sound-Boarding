package studios.kdc.soundboarding.view;


import android.widget.ImageButton;

import java.util.List;

public class ViewContract {

    public interface ScrollViewListener {
        void onScrollChanged(CustomHorizontalScrollView scrollView, int x, int y, int oldX, int oldY);

    }
    public interface SliderListener {
        void onSlideChanged(int startSeconds, int position);

    }
    public interface mixerProgressChange {

        void resumeSeekBar();
        void pauseSeekBar();
        int  getCurrentProgress();
        void setProgressChange(double seconds);
        void notifyTrackFinished();

    }
    public interface waveFormListener {
        void removeWaveForm(int position);
        void updateVolume(int position , int progress);
        int getCurrentVolume(int position);
    }
    public interface dataChangedNotifier {
        void notifyDataChanged();
        void notifySelectedWavesRemoved(List<Integer> positions);
    }

}
