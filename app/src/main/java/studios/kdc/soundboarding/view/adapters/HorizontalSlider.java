package studios.kdc.soundboarding.view.adapters;

import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

public class HorizontalSlider implements View.OnTouchListener {

    private boolean isDragging = false;
    private float lastX;
    private float lastY;
    private float deltaX;

    public HorizontalSlider(float initialX, float initialY) {
        this.lastX = initialX;
        this.lastY = initialY;
    }

    @Override
    public boolean onTouch(View view, MotionEvent event) {  //view -->waveform // parent -->framelayout
        int action = event.getAction();
        View row_layout = (View) view.getParent().getParent();
        View parent = (View) view.getParent();
        if (action == MotionEvent.ACTION_DOWN && !isDragging) {
            isDragging = true;
            deltaX = event.getX();
            return true;
        } else if (isDragging) {
            if (action == MotionEvent.ACTION_MOVE) {
             //  if (parent.getX() + event.getX() - deltaX >= row_layout.getX()){

                    parent.setX(parent.getX() + event.getX() - deltaX);
                    parent.setY(parent.getY());
             //  }
                return true;
            } else if (action == MotionEvent.ACTION_UP) {
                isDragging = false;
                lastX = parent.getX();
                lastY = parent.getY();
                return true;
            } else if (action == MotionEvent.ACTION_CANCEL) {
                parent.setX(lastX);
                parent.setY(lastY);
                isDragging = false;
                return true;
            }
        }

        return false;
    }
}

