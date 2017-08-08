package studios.kdc.soundboarding.view.adapters;


import android.app.Activity;
import android.graphics.Color;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import studios.kdc.soundboarding.Utils;


public class CustomTimelineView extends LinearLayout{
    private int minutes , seconds;
    private String finalMinSec;
    private int numberOfTextViews;
    private Activity context;
    private LinearLayout.LayoutParams params;



    public CustomTimelineView(Activity context) {
        super(context);
        this.minutes = 0;
        this.seconds = 0;
        int textViewWidth = 150;
        this.context = context;
        this.setOrientation(HORIZONTAL);
        this.params = new LinearLayout.LayoutParams(textViewWidth, ViewGroup.LayoutParams.WRAP_CONTENT);
        this.params.setMargins(0,0,5,0);
        this.numberOfTextViews = (Utils.getScreenWidth(context) + textViewWidth) / textViewWidth;
        this.initializeTextViews();
    }
    private void initializeTextViews(){
         for(int i = 0; i< this.numberOfTextViews; i++){
             TextView tv = new TextView(context);
             tv.setTextColor(Color.WHITE);
             calculateMinSec();
             tv.setText(this.finalMinSec);
             tv.setLayoutParams(this.params);
             this.addView(tv);
         }
    }
    private void calculateMinSec() {
        formatMinSec();
        increaseMinSec();
    }
    private void formatMinSec() {
        String  min = String.valueOf(minutes).length() < 2 ? "0" + minutes : "" + minutes;
        String  sec = String.valueOf(seconds).length() < 2 ? "0" +seconds : "" + seconds;
        this.finalMinSec = min + ":" + sec;
    }
    private void increaseMinSec() {
        this.seconds += 15;
        if(seconds >= 60) {
            this.seconds = 0;
            this.minutes++;
        }

    }

    public void IncreaseTimelineView() {
        TextView tv = (TextView) this.getChildAt(0);
        TextView last = (TextView) this.getChildAt(this.numberOfTextViews - 1);
        this.removeView(tv);
        tv.setText(this.calculateTimeAfter(last.getText().toString()));
        this.addView(tv);
    }
    private String calculateTimeAfter(String textviewAfter) {
        String[] s = textviewAfter.split(":");
        this.seconds = Integer.parseInt(s[1]);
        this.minutes = Integer.parseInt(s[0]);
        increaseMinSec();
        formatMinSec();
        return this.finalMinSec;
    }

    public void decreaseTimelineView() {
        TextView tv = (TextView) this.getChildAt(this.numberOfTextViews - 1);
        TextView first = (TextView) this.getChildAt(0);
        if(!first.getText().toString().equals("00:00")) {
            this.removeView(tv);
            tv.setText(this.calculateTimeBefore(first.getText().toString()));
            this.addView(tv, 0);
        }
    }

    private String calculateTimeBefore(String textviewAfter) {
        String[] s = textviewAfter.split(":");
        this.seconds = Integer.parseInt(s[1]);
        this.minutes = Integer.parseInt(s[0]);
        decreaseMinSec();
        formatMinSec();
        return this.finalMinSec;
    }
    private void decreaseMinSec() {
        this.seconds -= 15;
        if(seconds <= 0) {
            this.seconds = 45;
            this.minutes--;
            if(this.minutes < 0){
                this.minutes = 0;
                this.seconds = 0;
            }
        }

    }

}
