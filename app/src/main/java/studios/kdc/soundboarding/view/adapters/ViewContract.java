package studios.kdc.soundboarding.view.adapters;

import studios.kdc.soundboarding.view.CustomHorizontalScrollView;

public class ViewContract {

    public interface ScrollViewListener {
        void onScrollChanged(CustomHorizontalScrollView scrollView, int x, int y, int oldx, int oldy);

    }
    public interface SliderListener {
        void onSlideChanged(int startSeconds, int position);

    }
    public interface mixerProgressChange {

        void resumeSeekBar();
        void pauseSeekBar();
        int getCurrentProgress();
        void setProgressChange(double seconds);
        void notifyTrackFinished();

    }

}
