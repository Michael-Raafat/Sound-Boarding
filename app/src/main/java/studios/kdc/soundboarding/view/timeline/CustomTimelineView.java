package studios.kdc.soundboarding.view.timeline;


import android.app.Activity;
import android.graphics.Color;
import android.support.v7.widget.PopupMenu;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.HorizontalScrollView;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import rm.com.audiowave.AudioWaveView;
import studios.kdc.soundboarding.R;
import studios.kdc.soundboarding.Utils;
import studios.kdc.soundboarding.playerStrategy.PlayerStrategyFactory;
import studios.kdc.soundboarding.view.CustomHorizontalSlider;
import studios.kdc.soundboarding.view.ViewContract;


public class CustomTimelineView  {

    private MinutesSecondsView minutesSecondsView;
    private Activity activity;
    private LinearLayout timelineWaves;
    private List<CustomHorizontalSlider> waveFormsListeners;
    private HorizontalScrollView horizontalScrollView;
    private List<ImageButton> optionButtons;
    private PlayerStrategyFactory factory;

    public CustomTimelineView(Activity activity , LinearLayout timelineWaves ,LinearLayout minSecView, HorizontalScrollView horizontalScrollView){
        this.minutesSecondsView = new MinutesSecondsView(activity);
        this.timelineWaves = timelineWaves;
        this.activity = activity;
        this.waveFormsListeners = new ArrayList<>();
        this.optionButtons = new ArrayList<>();
        this.horizontalScrollView = horizontalScrollView;
        this.factory = new PlayerStrategyFactory(activity);
        minSecView.addView(minutesSecondsView);
    }

    public void decreaseTimelineView(){
        this.minutesSecondsView.decreaseTimelineView();

    }

    public void increaseTimelineView(){
        this.minutesSecondsView.increaseTimelineView();
    }

    public int getChildCount(){
        return this.timelineWaves.getChildCount();
    }

    public int getTextViewWidth() {
        return this.minutesSecondsView.getTextViewWidth();
    }
    public void controlSlidingOfWaveForms(Boolean enabled) {
        for (CustomHorizontalSlider slider : waveFormsListeners){
            slider.setEnabled(enabled);
        }
        for(ImageButton button: this.optionButtons){
            button.setEnabled(enabled);
        }
    }

    public void clearTimeline() {
        for(int i = getChildCount() - 1; i >= 0; i--){
            this.removeWave(i);
        }
    }
    public void addWaveFormsToTimeline(Map<String, String> trackInfo, int position){
        try {
            int screenHeight = Utils.getScreenHeight(activity);
            int screenWidth = Utils.getScreenWidth(activity);
            FrameLayout frameLayout = new FrameLayout(activity.getApplicationContext());
            TextView name = new TextView(activity.getApplicationContext());
            FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(Integer.parseInt(trackInfo.get("duration")) * Utils.SECOND_PIXEL_RATIO, (int)(screenHeight / 19.2));
            LinearLayout.LayoutParams frameParam = new LinearLayout.LayoutParams(Utils.TIMELINE_LENGTH_LIMIT , ViewGroup.LayoutParams.WRAP_CONTENT);
            frameParam.setMargins((screenHeight / 192), 0 , 0, (screenHeight / 192));
            frameLayout.setLayoutParams(frameParam);
            name.setLayoutParams(params);
            AudioWaveView waveForm = new AudioWaveView(activity.getApplicationContext());
            waveForm.setWaveColor(Color.WHITE);
            waveForm.setExpansionAnimated(false);
            waveForm.setLayoutParams(params);
            byte[] soundBytes = getWaveFormByteArray(trackInfo.get("type") , trackInfo.get("path"));
            if(soundBytes != null)
              waveForm.setRawData(soundBytes);
            name.setText(trackInfo.get("grpName") + " - " + trackInfo.get("name"));
            name.setTextColor(Color.WHITE);
            ImageButton optionsButton = new ImageButton(activity.getApplicationContext());
            optionsButton.setBackgroundColor(activity.getResources().getColor(R.color.cyan));
            FrameLayout.LayoutParams optionsParams = new FrameLayout.LayoutParams((screenWidth / 36) ,(screenHeight / 64));
            optionsButton.setLayoutParams(optionsParams);
            frameLayout.addView(waveForm);
            frameLayout.addView(name);
            frameLayout.addView(optionsButton);
            name.setGravity(Gravity.CENTER_HORIZONTAL);
            frameLayout.setX(position);
            this.timelineWaves.addView(frameLayout);
            setOnClickListenerToOptionsButton(optionsButton);
            CustomHorizontalSlider slider = new CustomHorizontalSlider(horizontalScrollView,
                    frameLayout , (View) frameLayout.getParent().getParent(), (ViewContract.SliderListener)activity);
            waveForm.setOnTouchListener(slider);
            this.waveFormsListeners.add(slider);
            this.optionButtons.add(optionsButton);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void removeWave(int position){
        this.timelineWaves.removeViewAt(position);
        this.waveFormsListeners.remove(position);
        this.optionButtons.remove(position);
    }



    private void setOnClickListenerToOptionsButton(final ImageButton button) {
       button.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               final FrameLayout frameLayout = (FrameLayout) view.getParent();
               final PopupMenu popup = new PopupMenu(activity, button);
               popup.getMenuInflater().inflate(R.menu.delete_menu, popup.getMenu());
               popup.show();
               popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                   @Override
                   public boolean onMenuItemClick(MenuItem item) {
                       switch (item.getItemId()) {
                           case R.id.remove:
                               ((ViewContract.waveFormListener) activity).removeWaveForm(timelineWaves.indexOfChild(frameLayout));
                               break;
                       }
                       return true;
                   }
               });

           }
       });
   }
    private byte[] getWaveFormByteArray(String type , String path) {

        InputStream inputStream = factory.createPlayerStrategy(type).getInputStream(path);
        try {
            return  Utils.toByteArray(inputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
         return null;
    }



    private class MinutesSecondsView extends LinearLayout {
        private int minutes, seconds;
        private String finalMinSec;
        private int numberOfTextViews;
        private Activity context;
        private LinearLayout.LayoutParams params;
        private int textViewWidth;


        private MinutesSecondsView (Activity context) {
            super(context);
            this.minutes = 0;
            this.seconds = 0;
            this.textViewWidth = (Utils.getScreenWidth(context) / 7);
            this.context = context;
            this.setOrientation(HORIZONTAL);
            this.params = new LinearLayout.LayoutParams(textViewWidth, ViewGroup.LayoutParams.WRAP_CONTENT);
            this.params.setMargins(0, 0, (Utils.getScreenWidth(context) / 216), 0);
            this.numberOfTextViews = (Utils.getScreenWidth(context) + this.textViewWidth) / this.textViewWidth;
            this.initializeTextViews();
        }

        private void initializeTextViews() {
            for (int i = 0; i < this.numberOfTextViews; i++) {
                TextView tv = new TextView(context);
                tv.setTextColor(Color.WHITE);
                calculateMinSec();
                tv.setText(this.finalMinSec);
                tv.setLayoutParams(this.params);
                this.addView(tv);
            }
        }
        private int getTextViewWidth() {
            return textViewWidth;
        }
        private void calculateMinSec() {
            this.formatMinSec();
            this.increaseMinSec();
        }

        private void formatMinSec() {
            String min = String.valueOf(minutes).length() < 2 ? "0" + minutes : "" + minutes;
            String sec = String.valueOf(seconds).length() < 2 ? "0" + seconds : "" + seconds;
            this.finalMinSec = min + ":" + sec;
        }

        private void increaseMinSec() {
            this.seconds += 15;
            if (this.seconds >= 60) {
                this.seconds = 0;
                this.minutes++;
            }

        }

        private void increaseTimelineView() {
            TextView tv = (TextView) this.getChildAt(0);
            TextView last = (TextView) this.getChildAt(this.numberOfTextViews - 1);
            this.removeView(tv);
            tv.setText(this.calculateTimeAfter(last.getText().toString()));
            this.addView(tv);
        }

        private void decreaseTimelineView() {
            TextView tv = (TextView) this.getChildAt(this.numberOfTextViews - 1);
            TextView first = (TextView) this.getChildAt(0);
            if (!first.getText().toString().equals("00:00")) {
                this.removeView(tv);
                tv.setText(this.calculateTimeBefore(first.getText().toString()));
                this.addView(tv, 0);
            }
        }

        private String calculateTimeAfter(String textviewAfter) {
            String[] s = textviewAfter.split(":");
            this.seconds = Integer.parseInt(s[1]);
            this.minutes = Integer.parseInt(s[0]);
            this.increaseMinSec();
            this.formatMinSec();
            return this.finalMinSec;
        }


        private String calculateTimeBefore(String textviewAfter) {
            String[] s = textviewAfter.split(":");
            this.seconds = Integer.parseInt(s[1]);
            this.minutes = Integer.parseInt(s[0]);
            this.decreaseMinSec();
            this.formatMinSec();
            return this.finalMinSec;
        }

        private void decreaseMinSec() {
            this.seconds -= 15;
            if (this.seconds <= 0) {
                this.seconds = 45;
                this.minutes--;
                if (this.minutes < 0) {
                    this.minutes = 0;
                    this.seconds = 0;
                }
            }
        }

    }
}