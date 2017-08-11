package studios.kdc.soundboarding;

import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.AsyncTask;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.DragEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.SearchView;
import android.widget.TextView;

import java.util.Map;

import studios.kdc.soundboarding.media.mixer.MixerController;
import studios.kdc.soundboarding.media.singlePlayer.MediaPlayerController;
import studios.kdc.soundboarding.view.CustomHorizontalScrollView;
import studios.kdc.soundboarding.view.CustomHorizontalSlider;
import studios.kdc.soundboarding.view.adapters.MainAdapter;
import studios.kdc.soundboarding.view.timeline.CustomTimelineView;
import studios.kdc.soundboarding.view.adapters.ViewContract;

public class MainActivity extends AppCompatActivity implements ViewContract.ScrollViewListener
        , ViewContract.SliderListener , ViewContract.mixerProgressChange , ViewContract.waveFormListener{

    private DataController dataController;
    private MainAdapter mainAdapter;
    private CustomTimelineView timelineView;
    private ImageView seekBar;
    private FloatingActionButton mixer;
    private FloatingActionButton pause_resume;
    private int scrollFactor;
    private boolean pauseResume;
    private boolean seekBarFlag;
    private CustomHorizontalSlider seekBarSlider;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        this.pauseResume = false;
        this.seekBarFlag = false;


        //////////////////////////////////MO2KATAN//////////////
        SharedPreferences  sharedPreferences = this.getSharedPreferences("studios.kdc.soundboarding", MODE_PRIVATE);
        SQLiteDatabase  tracksDatabase = this.openOrCreateDatabase("Data", MODE_PRIVATE, null);
        DataServiceSingleton.getInstance(tracksDatabase);
        if (!sharedPreferences.getBoolean("Start", false)) {
            DataServiceSingleton.getInstance(tracksDatabase).loadDefaultDatabase();
            sharedPreferences.edit().putBoolean("Start", true).apply();
        }
        ///////////////////////////////////////////////////////
        dataController = new DataController();
        new DatabaseGetter().execute();
        this.scrollFactor = 0;
        this.initializeTimeLineView();
        this.initializeSearchView();
        this.initializeMixerButton();
        this.initializePauseButton();
    }

    @Override
    protected  void onDestroy() {
        super.onDestroy();
        this.dataController.deleteReferences();
        this.dataController = null;
        MediaPlayerController.deleteInstance();
        MixerController.deleteInstance();
        Runtime.getRuntime().gc();
    }


    private void initializePauseButton(){
        this.pause_resume = (FloatingActionButton) findViewById(R.id.pause_resume);
        this.pause_resume.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!pauseResume) {
                    MixerController.getInstance(getApplicationContext(), MainActivity.this).pause();
                    pause_resume.setImageResource((R.drawable.paused));
                    seekBarSlider.setEnabled(true);
                    timelineView.controlSlidingOfWaveForms(true);
                    pauseResume = true;
                } else {
                    MixerController.getInstance(getApplicationContext(), MainActivity.this).resume();
                    pause_resume.setImageResource((R.drawable.played));
                    seekBarSlider.setEnabled(false);
                    timelineView.controlSlidingOfWaveForms(false);
                    pauseResume = false;
                }
            }
        });

    }
    private void initializeMixerButton(){
        this.mixer = (FloatingActionButton) findViewById(R.id.mix);
        this.mixer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MixerController.getInstance(getApplicationContext() , MainActivity.this).mix();
                mixer.setVisibility(View.GONE);
                pause_resume.setVisibility(View.VISIBLE);
                pause_resume.setImageResource(R.drawable.played);
                seekBarSlider.setEnabled(false);
                timelineView.controlSlidingOfWaveForms(false);
                pauseResume = false;
            }
        });

    }

    private void initializeTimeLineView() {
        ScrollView scrollView = (ScrollView) findViewById(R.id.table_scroll);
        CustomHorizontalScrollView  horizontalScrollView = (CustomHorizontalScrollView) findViewById(R.id.sc);
        horizontalScrollView.setScrollViewListener(this);
        this.addOnDragListenerOnTableTimeline(scrollView);
        seekBar= (ImageView) findViewById(R.id.seeker);
        seekBarSlider = new CustomHorizontalSlider(horizontalScrollView, seekBar , (View) seekBar.getParent(), null);
        seekBar.setOnTouchListener(seekBarSlider);
        LinearLayout linearLayout = (LinearLayout) findViewById(R.id.timeline);
        LinearLayout linearLayout1 = (LinearLayout) findViewById(R.id.timeline_view);
        timelineView = new CustomTimelineView(this, linearLayout1 , horizontalScrollView);
        linearLayout.addView(timelineView.getMinutesSecondsView());
    }

    private void initializeSearchView() {
        SearchView searchView = (SearchView) findViewById(R.id.search_view);
        int id = searchView.getContext().getResources().getIdentifier("android:id/search_src_text", null, null);
        TextView textView = searchView.findViewById(id);
        textView.setTextColor(Color.WHITE);
        setSearchBoxClickListener(searchView);

    }
    private void setSearchBoxClickListener(SearchView searchView) {
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                dataController.searchTracksInGroups(s);
                if(mainAdapter != null)
                    mainAdapter.notifyDataSetChanged();
                return true;
            }
        });
    }

    private void addOnDragListenerOnTableTimeline(View root){
        root.setOnDragListener(new View.OnDragListener() {
            @Override
            public boolean onDrag(View v, DragEvent event) {
                switch (event.getAction()) {
                    case DragEvent.ACTION_DRAG_STARTED:
                        break;
                    case DragEvent.ACTION_DRAG_ENTERED:
                        break;
                    case DragEvent.ACTION_DRAG_EXITED:
                        break;
                    case DragEvent.ACTION_DROP:
                        String description = event.getClipData().getDescription().getLabel().toString();
                        String[] s = description.split(getResources().getString(R.string.separator));
                        Map<String, String> trackInfo = dataController.selectTrackToMix(s[1], Integer.parseInt(s[0]));
                        timelineView.addWaveFormsToTimeline(trackInfo);
                        if(timelineView.getChildCount() > 0){
                            if(pause_resume.getVisibility() == View.GONE)
                                mixer.setVisibility(View.VISIBLE);
                        }
                        break;
                    case DragEvent.ACTION_DRAG_ENDED:
                        break;
                    default:
                        break;
                }

                return true;
            }

        });
    }



    @Override
    public void onScrollChanged(CustomHorizontalScrollView scrollView, int x, int y, int oldX, int oldY) {

        int delta = x - oldX;
        if((delta > 0) && ((scrollFactor + 1 ) * 150 < x) ) {
            scrollFactor++;
            timelineView.increaseTimelineView();
        } else if((delta < 0) &&  ((scrollFactor - 1 ) * 150 >= x)) {
            scrollFactor--;
            timelineView.decreaseTimelineView();
        }
    }

    @Override
    public void onSlideChanged(int startSeconds, int position) {
      this.dataController.setStartPointTrack(position , startSeconds);
    }

    @Override
    public void pauseSeekBar() {
        seekBarFlag = true;
    }

    @Override
    public void resumeSeekBar() {
        seekBarFlag = false;
    }

    @Override
    public int getCurrentProgress() {
        return ((int) this.seekBar.getX() / Utils.SECOND_PIXEL_RATIO);
    }

    @Override
    public void setProgressChange(double seconds) {
        if (!seekBarFlag) {
            this.seekBar.setX((float) (seconds * Utils.SECOND_PIXEL_RATIO));
        }
    }

    @Override
    public void notifyTrackFinished() {
        this.seekBar.setX(10);
        this.mixer.setVisibility(View.VISIBLE);
        this.pause_resume.setVisibility(View.GONE);
        this.pauseResume = true;
        this.seekBarSlider.setEnabled(true);
        this.timelineView.controlSlidingOfWaveForms(true);
        this.seekBarFlag = false;
    }

    @Override
    public void removeWaveForm(int position) {
            dataController.removeTrack(position);
            MixerController.getInstance(getApplicationContext(), MainActivity.this).removeHandler(position);
            this.timelineView.removeWave(position);
            if (this.timelineView.getChildCount() < 1) {
                this.mixer.setVisibility(View.GONE);
                this.pause_resume.setVisibility(View.GONE);
            }

    }
    private class DatabaseGetter extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... voids) {
            dataController.importDatabase();
            return null;
        }
        @Override
        protected void onPostExecute(Void v) {
            RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
            mainAdapter = new MainAdapter(getApplicationContext());
            dataController.setMainAdapterList(mainAdapter);
            RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
            recyclerView.setLayoutManager(layoutManager);
            recyclerView.setAdapter(mainAdapter);

        }
    }
}