package studios.kdc.soundboarding.view;


import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.HorizontalScrollView;

import studios.kdc.soundboarding.view.adapters.ViewContract;

public class CustomHorizontalSlider implements View.OnTouchListener {

    private boolean isDragging = false;
    private float deltaX;
    private HorizontalScrollView scrollView;
    private View v;
    private View parentLimit;
    private ViewContract.SliderListener listener;
    private boolean enabled;

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public CustomHorizontalSlider(HorizontalScrollView scrollView , View v , View parentLimit, ViewContract.SliderListener listener) {
        this.scrollView = scrollView;
        this.v = v;
        this.parentLimit = parentLimit;
        this.listener = listener;
        this.enabled = true;
    }

    @Override
    public boolean onTouch(View view, MotionEvent event) {
        if(this.enabled) {
            int action = event.getAction();
            if (action == MotionEvent.ACTION_DOWN) {
                isDragging = true;
                deltaX = event.getX();
                v.setX(v.getX() + event.getX() - deltaX);
                this.scrollView.requestDisallowInterceptTouchEvent(true);
                return true;
            } else if (isDragging) {
                if (action == MotionEvent.ACTION_MOVE) {
                    if (v.getX() + event.getX() - deltaX >= parentLimit.getX() && (v.getX() + event.getX() - deltaX + view.getWidth()) <= parentLimit.getWidth()) {
                        v.setX(v.getX() + event.getX() - deltaX);
                        v.setY(v.getY());

                    }
                    return true;
                } else if (action == MotionEvent.ACTION_UP || action == MotionEvent.ACTION_CANCEL) {
                    isDragging = false;
                    if (listener != null) {
                        ViewGroup parent = (ViewGroup) v.getParent();
                        listener.onSlideChanged((int) (v.getX() / 10), parent.indexOfChild(v));
                    }
                    return true;
                }
            }
            this.scrollView.requestDisallowInterceptTouchEvent(false);
        }
        return false;
    }
}

