package studios.kdc.soundboarding.view;


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
        int getCurrentProgress();
        void setProgressChange(double seconds);
        void notifyTrackFinished();

    }
    public interface waveFormListener {
        void removeWaveForm(int position);

    }
    public interface dataChangedNotifier {
        void notifyDataChanged();
    }

}
