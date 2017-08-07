package studios.kdc.soundboarding.view.adapters;

import android.app.Activity;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.HorizontalScrollView;

import studios.kdc.soundboarding.Utils;

public class HorizontalSlider implements View.OnTouchListener {

    private boolean isDragging = false;
    private float deltaX;
    private HorizontalScrollView scrollView;
    private Activity activity;

    public HorizontalSlider(HorizontalScrollView scrollView , Activity activity) {
        this.scrollView = scrollView;
        this.activity = activity;

    }

    @Override
    public boolean onTouch(View view, MotionEvent event) {
        int action = event.getAction();
        View row_layout = (View) view.getParent().getParent();
        View parent = (View) view.getParent();
        if (action == MotionEvent.ACTION_DOWN) {
            isDragging = true;
            deltaX = event.getX();
            parent.setX(parent.getX() + event.getX() - deltaX);
            this.scrollView.requestDisallowInterceptTouchEvent(true);
            return true;
        } else if (isDragging) {
            if (action == MotionEvent.ACTION_MOVE) {
              if (parent.getX() + event.getX() - deltaX >= row_layout.getX() && (parent.getX() + event.getX() - deltaX + view.getWidth()) <= parent.getWidth()){
                    parent.setX(parent.getX() + event.getX() - deltaX);
                    parent.setY(parent.getY());

                  if((parent.getX() + event.getX() - deltaX + view.getWidth())>= Utils.getScreenWidth(this.activity)) {

                  }

              }
                return true;
            } else if (action == MotionEvent.ACTION_UP) {
                isDragging = false;
                return true;
            } else if (action == MotionEvent.ACTION_CANCEL) {

                isDragging = false;
                return true;
            }
        }
        this.scrollView.requestDisallowInterceptTouchEvent(false);
        return false;
    }
}

