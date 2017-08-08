package studios.kdc.soundboarding.view.adapters;

import android.view.MotionEvent;
import android.view.View;
import android.widget.HorizontalScrollView;

public class HorizontalSlider implements View.OnTouchListener {

    private boolean isDragging = false;
    private float deltaX;
    private HorizontalScrollView scrollView;
    private View v;
    private View parentLimit;

    public HorizontalSlider(HorizontalScrollView scrollView , View v , View parentLimit) {
        this.scrollView = scrollView;
        this.v = v;
        this.parentLimit = parentLimit;

    }

    @Override
    public boolean onTouch(View view, MotionEvent event) {
        int action = event.getAction();
        if (action == MotionEvent.ACTION_DOWN) {
            isDragging = true;
            deltaX = event.getX();
            v.setX(v.getX() + event.getX() - deltaX);
            this.scrollView.requestDisallowInterceptTouchEvent(true);
            return true;
        } else if (isDragging) {
            if (action == MotionEvent.ACTION_MOVE) {
              if (v.getX() + event.getX() - deltaX >= parentLimit.getX() && (v.getX() + event.getX() - deltaX + view.getWidth()) <= parentLimit.getWidth()){
                    v.setX(v.getX() + event.getX() - deltaX);
                    v.setY(v.getY());

                  //if((parent.getX() + event.getX() - deltaX + view.getWidth())>= Utils.getScreenWidth(this.activity)) {

                 // }

              }
                return true;
            } else if (action == MotionEvent.ACTION_UP || action == MotionEvent.ACTION_CANCEL) {
                isDragging = false;
                return true;
            }
        }
        this.scrollView.requestDisallowInterceptTouchEvent(false);
        return false;
    }
}

