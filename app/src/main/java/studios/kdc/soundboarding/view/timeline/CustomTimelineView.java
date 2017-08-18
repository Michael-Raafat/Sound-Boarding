package studios.kdc.soundboarding.view.timeline;


import android.app.Activity;
import android.graphics.Color;
import android.os.Handler;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.HorizontalScrollView;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import rm.com.audiowave.AudioWaveView;
import studios.kdc.soundboarding.MainActivity;
import studios.kdc.soundboarding.R;
import studios.kdc.soundboarding.Utils;
import studios.kdc.soundboarding.playerStrategy.PlayerStrategyFactory;
import studios.kdc.soundboarding.view.CustomHorizontalSlider;
import studios.kdc.soundboarding.view.ViewContract;

import static android.content.Context.LAYOUT_INFLATER_SERVICE;


public class CustomTimelineView  {

    private MinutesSecondsView minutesSecondsView;
    private Activity activity;
    private LinearLayout timelineWaves;
    private List<CustomHorizontalSlider> waveFormsListeners;
    private HorizontalScrollView horizontalScrollView;
    private List<ImageButton> optionButtons;
    private PlayerStrategyFactory factory;
    private boolean isTouched;
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
    private int getTimelineLengthLimit(){
        return  Utils.getSecondPixelRatio(this.activity) * ((int)(Utils.getScreenWidth(this.activity) / 1.8));
    }
    public void addWaveFormsToTimeline(Map<String, String> trackInfo, int position){
        try {
            int screenHeight = Utils.getScreenHeight(this.activity);
            int screenWidth = Utils.getScreenWidth(this.activity);
            FrameLayout frameLayout = new FrameLayout(this.activity.getApplicationContext());
            TextView name = new TextView(this.activity.getApplicationContext());
            int waveWidth = Integer.parseInt(trackInfo.get("duration")) * Utils.getSecondPixelRatio(this.activity);
            int waveHeight = (int)(screenHeight / 19.2);
            FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(waveWidth, waveHeight);
            LinearLayout.LayoutParams frameParam = new LinearLayout.LayoutParams(getTimelineLengthLimit() , ViewGroup.LayoutParams.WRAP_CONTENT);
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
            ImageButton deleteButton = new ImageButton(activity.getApplicationContext());
            deleteButton.setImageResource(R.drawable.close_cyan);
            FrameLayout.LayoutParams optionsParams = new FrameLayout.LayoutParams((screenWidth / 36) ,(screenHeight / 64));
            deleteButton.setLayoutParams(optionsParams);
            ImageButton volumeButton = new ImageButton(activity.getApplicationContext());
            volumeButton .setImageResource(R.drawable.volume_cyan);
            FrameLayout.LayoutParams volumeParams = new FrameLayout.LayoutParams((screenWidth / 25) ,(screenHeight / 55));
            volumeParams.gravity = (Gravity.BOTTOM);
            volumeParams.setMargins(waveWidth, 0, 0, 0);
            volumeButton.setLayoutParams(volumeParams);
            frameLayout.addView(waveForm);
            frameLayout.addView(name);
            frameLayout.addView(deleteButton);
            frameLayout.addView(volumeButton);
            name.setGravity(Gravity.CENTER_HORIZONTAL);
            frameLayout.setX(position);
            this.timelineWaves.addView(frameLayout);
            setOnClickListenerToDeleteButton(deleteButton);
            setOnClickListenerToVolumeButton(volumeButton);
            CustomHorizontalSlider slider = new CustomHorizontalSlider(this.horizontalScrollView,
                    frameLayout , (View) frameLayout.getParent().getParent(), (ViewContract.SliderListener)activity);
            waveForm.setOnTouchListener(slider);
            this.waveFormsListeners.add(slider);
            this.optionButtons.add(deleteButton);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void removeWave(int position){
        this.timelineWaves.removeViewAt(position);
        this.waveFormsListeners.remove(position);
        this.optionButtons.remove(position);
    }

    private void setOnClickListenerToDeleteButton(final ImageButton button) {
       button.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               FrameLayout frameLayout = (FrameLayout) view.getParent();
               ((ViewContract.waveFormListener) activity).removeWaveForm(timelineWaves.indexOfChild(frameLayout));
           }
       });
   }

    private void setOnClickListenerToVolumeButton(final ImageButton button) {
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                button.setEnabled(false);
                FrameLayout frameLayout = (FrameLayout) view.getParent();
               // ((ViewContract.waveFormListener) activity).showVolumeSeekBar(button, timelineWaves.indexOfChild(frameLayout));
                Handler handler = new Handler();
                LayoutInflater inflater = (LayoutInflater) activity.getSystemService(LAYOUT_INFLATER_SERVICE);
                View customView = inflater.inflate(R.layout.volume_control, null);
                final PopupWindow mPopupWindow = new PopupWindow(customView,
                        FrameLayout.LayoutParams.WRAP_CONTENT ,
                        FrameLayout.LayoutParams.WRAP_CONTENT
                );
                mPopupWindow.showAsDropDown(button , 30 , -1 * (int)(button.getY()));
                int width = (int)(Utils.getScreenWidth(activity) / 10.8);
                final SeekBar volume = customView.findViewById(R.id.volume);
                isTouched = false;
                volume.setMax(width);
                //volume.setProgress();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (!isTouched){
                            mPopupWindow.dismiss();
                            button.setEnabled(true);
                    }
                    }
                } , 1000);
                volume.setOnSeekBarChangeListener (new SeekBar.OnSeekBarChangeListener ( ) {

                    @Override
                    public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                        //i
                    }

                    @Override
                    public void onStartTrackingTouch(SeekBar seekBar) {
                        isTouched = true;
                    }

                    @Override
                    public void onStopTrackingTouch(SeekBar seekBar) {
                        button.setEnabled(true);
                        mPopupWindow.dismiss();
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