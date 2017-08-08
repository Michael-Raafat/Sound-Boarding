package studios.kdc.soundboarding.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.HorizontalScrollView;


public class CustomHorizontalScrollView extends HorizontalScrollView{

    private boolean mCurrentlyTouching;
    private boolean mCurrentlyFling;


    private ScrollViewListener scrollViewListener = null;

    public CustomHorizontalScrollView(Context context) {
        super(context);
    }

    public CustomHorizontalScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CustomHorizontalScrollView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public void setScrollViewListener(ScrollViewListener scrollViewListener) {
        this.scrollViewListener = scrollViewListener;
    }

    @Override
    public void fling(int velocityX) {
        super.fling(velocityX);
        mCurrentlyFling = true;
    }

    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        super.onScrollChanged(l, t, oldl, oldt);
        if (scrollViewListener != null) {
            scrollViewListener.onScrollChanged(this, l, t, oldl, oldt);
        }

        if (Math.abs(t - oldt) < 2 || t >= getMeasuredHeight() || t == 0) {

            mCurrentlyFling = false;
        }

    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mCurrentlyTouching = true;
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                mCurrentlyTouching = false;
                break;

            default:
                break;
        }
        return super.onTouchEvent(ev);
    }


    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mCurrentlyTouching = true;
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                mCurrentlyTouching = false;

                break;

            default:
                break;
        }
        return super.onInterceptTouchEvent(ev);
    }
}