package studios.kdc.soundboarding;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.AsyncTask;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.DragEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
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
        , ViewContract.SliderListener , ViewContract.mixerProgressChange , ViewContract.waveFormListener {

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
    private ActionBarDrawerToggle mDrawerToggle;
    private DrawerLayout mDrawerLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        this.seekBarFlag = false;

        SharedPreferences sharedPreferences = this.getSharedPreferences("studios.kdc.soundboarding", MODE_PRIVATE);
        SQLiteDatabase tracksDatabase = this.openOrCreateDatabase("Data", MODE_PRIVATE, null);
        DataServiceSingleton.getInstance(tracksDatabase);
        if (!sharedPreferences.getBoolean("Start", false)) {
            DataServiceSingleton.getInstance(tracksDatabase).loadDefaultDatabase();
            sharedPreferences.edit().putBoolean("Start", true).apply();
        }
        this.dataController = new DataController();
        new DatabaseGetter().execute();
        this.scrollFactor = 0;
        this.initializeTimeLineView();
        this.initializeSearchView();
        this.initializeMixerButton();
        this.initializePauseButton();
        this.initializeDrawer();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        this.dataController.deleteReferences();
        this.dataController = null;
        MediaPlayerController.deleteInstance();
        MixerController.deleteInstance();
        Runtime.getRuntime().gc();
    }

    private void initializeDrawer() {

        ListView mDrawerList = (ListView) findViewById(R.id.left_drawer);
        String[] items = getResources().getStringArray(R.array.options);
        mDrawerList.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, items));
        mDrawerList.setOnItemClickListener(new DrawerItemClickListener());
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerToggle = new ActionBarDrawerToggle(
                this, mDrawerLayout, R.string.open, R.string.close
        ) {
            public void onDrawerClosed(View view) {
                super.onDrawerClosed(view);
            }

            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
            }
        };
        mDrawerLayout.addDrawerListener(mDrawerToggle);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

   }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return  (mDrawerToggle.onOptionsItemSelected(item)) || (super.onOptionsItemSelected(item));
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    private class DrawerItemClickListener implements ListView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            mDrawerLayout.closeDrawers();
            switch (position){
            case 0:
                Intent i = new Intent(getApplicationContext() , TrackUploader.class);
                startActivity(i);
                break;
            case 1:
                break;
                case 2:
                break;

        }
    }
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
        this.seekBar= (ImageView) findViewById(R.id.seeker);
        this.seekBarSlider = new CustomHorizontalSlider(horizontalScrollView, seekBar , (View) seekBar.getParent(), null);
        this.seekBar.setOnTouchListener(seekBarSlider);
        LinearLayout linearLayout = (LinearLayout) findViewById(R.id.timeline);
        LinearLayout linearLayout1 = (LinearLayout) findViewById(R.id.timeline_view);
        this.timelineView = new CustomTimelineView(this, linearLayout1 , linearLayout , horizontalScrollView);

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
        this.seekBarFlag = true;
    }

    @Override
    public void resumeSeekBar() {
        this.seekBarFlag = false;
    }

    @Override
    public int getCurrentProgress() {
        return ((int) this.seekBar.getX() / Utils.SECOND_PIXEL_RATIO);
    }

    @Override
    public void setProgressChange(double seconds) {
       if (!this.seekBarFlag) {
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
            this.dataController.removeTrack(position);
            this.timelineView.removeWave(position);
            if (this.timelineView.getChildCount() < 1) {
                this.mixer.setVisibility(View.GONE);
                this.pause_resume.setVisibility(View.GONE);
                this.seekBar.setX(10);
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
            int screenHeight = Utils.getScreenHeight(MainActivity.this);
            RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT
                    ,(int) ( screenHeight /1.6 ));
            params.setMargins(0,(screenHeight/15) ,0,0);
            recyclerView.setLayoutParams(params);

        }
    }
}