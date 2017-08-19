package studios.kdc.soundboarding;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.DragEvent;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import studios.kdc.soundboarding.media.mixer.MixerController;
import studios.kdc.soundboarding.media.singlePlayer.MediaPlayerController;
import studios.kdc.soundboarding.view.CustomHorizontalScrollView;
import studios.kdc.soundboarding.view.CustomHorizontalSlider;
import studios.kdc.soundboarding.view.adapters.CustomListViewAdapter;
import studios.kdc.soundboarding.view.adapters.MainAdapter;
import studios.kdc.soundboarding.view.timeline.CustomTimelineView;
import studios.kdc.soundboarding.view.ViewContract;

public class MainActivity extends AppCompatActivity implements ViewContract.ScrollViewListener
        , ViewContract.SliderListener , ViewContract.mixerProgressChange ,
        ViewContract.waveFormListener , ViewContract.dataChangedNotifier{

    private MainAdapter mainAdapter;
    private CustomTimelineView timelineView;
    private ImageView seekBar;
    private FloatingActionButton mix;
    private FloatingActionButton save;
    private FloatingActionButton pause_resume;
    private int scrollFactor;
    private boolean pauseResume;
    private boolean seekBarFlag;
    private CustomHorizontalSlider seekBarSlider;
    private ActionBarDrawerToggle mDrawerToggle;
    private DrawerLayout mDrawerLayout;
    private Handler handler;
    private float initialSeekBarPosition;
    private RelativeLayout relativeLayout;
    private boolean isDeleteEnabled;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        this.seekBarFlag = false;
        this.isDeleteEnabled = true;
        this.scrollFactor = 0;
        this.handler = new Handler();
        this.relativeLayout = (RelativeLayout) findViewById(R.id.main);
        SharedPreferences sharedPreferences = this.getSharedPreferences("studios.kdc.soundboarding", MODE_PRIVATE);
        SQLiteDatabase tracksDatabase = this.openOrCreateDatabase("Data", MODE_PRIVATE, null);
        DataServiceSingleton.getInstance(tracksDatabase);
        if (!sharedPreferences.getBoolean("Start", false)) {
            DataServiceSingleton.getInstance(tracksDatabase).loadDefaultDatabase();
            sharedPreferences.edit().putBoolean("Start", true).apply();
        }
        new DatabaseGetter().execute();
        this.initializeTimeLineView();
        this.initializeSearchView();
        this.initializeMixerButton();
        this.initializePauseButton();
        this.initializeSaveButton();
        this.initializeDrawer();
        this.initializeDeleteButton();
        DataController.getInstance().setNotifierListener(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        DataController.getInstance().deleteReferences();
        DataController.deleteInstance();
        MediaPlayerController.deleteInstance();
        MixerController.deleteInstance();
        Runtime.getRuntime().gc();
    }

    private void initializeDrawer() {

        ListView mDrawerList = (ListView) findViewById(R.id.left_drawer);
        String[] myResArray = getResources().getStringArray(R.array.options);
        mDrawerList.setAdapter(new CustomListViewAdapter(this, Arrays.asList(myResArray)));
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
        ActionBar ab = getSupportActionBar();
        if(ab != null) {
            ab.setDisplayHomeAsUpEnabled(true);
            ab.setHomeButtonEnabled(true);
        }
   }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return  (this.mDrawerToggle.onOptionsItemSelected(item)) || (super.onOptionsItemSelected(item));
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        this.mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        this.mDrawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public void notifyDataChanged() {
        this.mainAdapter.notifyDataSetChanged();
    }

    @Override
    public void notifySelectedWavesRemoved(List<Integer> positions) {
        Collections.sort(positions, Collections.reverseOrder());
        for(Integer i : positions)
            this.timelineView.removeWave(i);
        Log.i("hna" , "" + this.timelineView.getChildCount());
        if (this.timelineView.getChildCount() < 1)
            afterRemoveChanges();
    }

    private class DrawerItemClickListener implements ListView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            Intent i;
            switch (position){
                case 0: //create track
                    i = new Intent(getApplicationContext() , TrackUploader.class);
                    startActivity(i);
                    break;
                case 1: //create grp
                    i = new Intent(getApplicationContext() , GroupCreator.class);
                    startActivity(i);
                    break;
                case 2: //open saved
                    openSavedTracks();
                    break;
                case 3: // about
                    break;
            }
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    mDrawerLayout.closeDrawers();
                }
            }, 250);
        }
    }
    private void openSavedTracks() {
        final List<String> saved = DataController.getInstance().getSavedTracks();
        if(saved.isEmpty())
            Toast.makeText(getApplicationContext(),"There is no saved tracks" , Toast.LENGTH_LONG).show();
        else {
            LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
            View customView = inflater.inflate(R.layout.saved_list_layout, null);
            final PopupWindow  mPopupWindow = new PopupWindow(customView, RelativeLayout.LayoutParams.MATCH_PARENT,
                    RelativeLayout.LayoutParams.MATCH_PARENT);
            mPopupWindow.setFocusable(true);
            ListView savedList = customView.findViewById(R.id.saved);
            ImageButton close = customView.findViewById(R.id.close);
            close.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mPopupWindow.dismiss();
                }
            });
            savedList.setAdapter(new ArrayAdapter<>(getApplicationContext() , R.layout.saved_text_layout , saved));
            savedList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    DataController.getInstance().loadSavedTrack(saved.get(i));
                    mPopupWindow.dismiss();
                    fillTimelineWithSavedTracks();
                }
            });
            mPopupWindow.showAtLocation(relativeLayout, Gravity.CENTER ,0,0);
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
                    isDeleteEnabled = true;
                    pauseResume = true;
                } else {
                    MixerController.getInstance(getApplicationContext(), MainActivity.this).resume();
                    pause_resume.setImageResource((R.drawable.played));
                    seekBarSlider.setEnabled(false);
                    timelineView.controlSlidingOfWaveForms(false);
                    isDeleteEnabled = false;
                    pauseResume = false;
                }
            }
        });

    }
    private void fillTimelineWithSavedTracks(){
        this.timelineView.clearTimeline();
        for(int i = 0 ; i< DataController.getInstance().getNoOfSelectedTracks(); i++){
            Map<String, String> trackInfo = DataController.getInstance().getTrackInfo(i);
            this.timelineView.addWaveFormsToTimeline(trackInfo , Integer.parseInt(trackInfo.get("startPoint")) * Utils.getSecondPixelRatio(this));
        }
        this.mix.setVisibility(View.VISIBLE);
        this.save.setVisibility(View.VISIBLE);
    }
    private void initializeSaveButton(){
        this.save = (FloatingActionButton) findViewById(R.id.save);
        this.save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                save.setEnabled(false);
                LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
                View customView = inflater.inflate(R.layout.save_popup_layout, null);
                final PopupWindow  mPopupWindow = new PopupWindow(customView,
                        (int) (Utils.getScreenWidth(MainActivity.this) / 1.5) ,
                        RelativeLayout.LayoutParams.WRAP_CONTENT
                );
                mPopupWindow.setFocusable(true);
                mPopupWindow.showAtLocation(relativeLayout, Gravity.CENTER ,0,0);
                Button saveButton = customView.findViewById(R.id.save);
                final EditText name = customView.findViewById(R.id.saved_track_name);
                ImageButton close = customView.findViewById(R.id.close);
                close.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        mPopupWindow.dismiss();
                        save.setEnabled(true);
                    }
                });
                saveButton .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        mPopupWindow.dismiss();
                        save.setEnabled(true);
                        if(name.getText().toString().trim().isEmpty())
                            Toast.makeText(getApplicationContext(),"You must specify track name" , Toast.LENGTH_LONG).show();
                        else {
                            Toast.makeText(getApplicationContext(),"Track is saved Successfully" , Toast.LENGTH_LONG).show();
                            DataController.getInstance().saveMixedTrack(name.getText().toString().trim());
                        }
                    }
                });
            }
        });
    }

    private void initializeMixerButton(){
        this.mix = (FloatingActionButton) findViewById(R.id.mix);
        this.mix.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MixerController.getInstance(getApplicationContext() , MainActivity.this).mix();
                mix.setVisibility(View.GONE);
                pause_resume.setVisibility(View.VISIBLE);
                pause_resume.setImageResource(R.drawable.played);
                seekBarSlider.setEnabled(false);
                timelineView.controlSlidingOfWaveForms(false);
                isDeleteEnabled = false;
                pauseResume = false;
            }
        });

    }
    private void initializeDeleteButton(){
        final ImageButton deleteButton = (ImageButton) findViewById(R.id.delete);
        deleteButton .setOnDragListener(new View.OnDragListener() {
            @Override
            public boolean onDrag(View v, DragEvent event) {
                if(isDeleteEnabled) {
                    switch (event.getAction()) {
                        case DragEvent.ACTION_DRAG_LOCATION:
                           deleteButton.setImageResource(R.drawable.delete_red);
                            break;
                        case DragEvent.ACTION_DROP:
                            String tag = ((View) event.getLocalState()).getTag().toString();
                            String description = event.getClipData().getDescription().getLabel().toString();
                            String[] s = description.split(getResources().getString(R.string.separator));
                            if(tag .equals("group")){
                                if(!DataController.getInstance().deleteGroup(Integer.parseInt(s[0])))
                                    Toast.makeText(getApplicationContext() , "You cannot delete this group !!" , Toast.LENGTH_LONG).show();
                            } else if(tag.equals("track")) {
                                if(!DataController.getInstance().deleteTrack(Integer.parseInt(s[2]) , Integer.parseInt(s[0])))
                                    Toast.makeText(getApplicationContext() , "You cannot delete this track !!" , Toast.LENGTH_LONG).show();
                            }
                            notifyDataChanged();
                            break;
                        case DragEvent.ACTION_DRAG_EXITED:
                            deleteButton.setImageResource(R.drawable.delete_white);
                            break;
                        case DragEvent.ACTION_DRAG_ENDED:
                            deleteButton.setImageResource(R.drawable.delete_white);
                            break;
                    }
                }
                return true;
            }
        });
    }

    private void initializeTimeLineView() {
        ScrollView scrollView = (ScrollView) findViewById(R.id.table_scroll);
        CustomHorizontalScrollView  horizontalScrollView = (CustomHorizontalScrollView) findViewById(R.id.sc);
        horizontalScrollView.setScrollViewListener(this);
        this.addOnDragListenerOnTableTimeline(scrollView);
        this.seekBar = (ImageView) findViewById(R.id.seeker);
        this.initialSeekBarPosition = this.seekBar.getX();
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
                DataController.getInstance().searchTracksInGroups(s);
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
                    case DragEvent.ACTION_DROP:
                        String tag = ((View) event.getLocalState()).getTag().toString();
                        if(tag.equals("track")){
                            String description = event.getClipData().getDescription().getLabel().toString();
                            String[] s = description.split(getResources().getString(R.string.separator));
                            Map<String, String> trackInfo = DataController.getInstance().selectTrackToMix(s[1], Integer.parseInt(s[0]));
                            timelineView.addWaveFormsToTimeline(trackInfo , 0);
                            if(timelineView.getChildCount() > 0){
                                save.setVisibility(View.VISIBLE);
                                if(pause_resume.getVisibility() == View.GONE)
                                    mix.setVisibility(View.VISIBLE);
                            }
                        }
                        break;
                }
                return true;
            }
        });
    }

    @Override
    public void onScrollChanged(CustomHorizontalScrollView scrollView, int x, int y, int oldX, int oldY) {
        int delta = x - oldX;
        int textViewWidth = this.timelineView.getTextViewWidth();
        if((delta > 0) && ((this.scrollFactor + 1 ) *  textViewWidth < x) ) {
            this.scrollFactor++;
            this.timelineView.increaseTimelineView();
        } else if((delta < 0) &&  ((this.scrollFactor - 1 ) *  textViewWidth >= x)) {
            this.scrollFactor--;
            this.timelineView.decreaseTimelineView();
        }
    }

    @Override
    public void onSlideChanged(int startSeconds, int position) {
         DataController.getInstance().setStartPointTrack(position , startSeconds / Utils.getSecondPixelRatio(this));
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
        return ((int) this.seekBar.getX() / Utils.getSecondPixelRatio(this));
    }

    @Override
    public void setProgressChange(double seconds) {
       if (!this.seekBarFlag)
            this.seekBar.setX((float) (seconds * Utils.getSecondPixelRatio(this)));
    }

    @Override
    public void notifyTrackFinished() {
       if (this.timelineView.getChildCount() > 0) {
           this.mix.setVisibility(View.VISIBLE);
           this.save.setVisibility(View.VISIBLE);
       }
        this.seekBar.setX(this.initialSeekBarPosition);
        this.pause_resume.setVisibility(View.GONE);
        this.pauseResume = true;
        this.seekBarSlider.setEnabled(true);
        this.timelineView.controlSlidingOfWaveForms(true);
        this.isDeleteEnabled = true;
        this.seekBarFlag = false;
    }

    @Override
    public void removeWaveForm(int position) {
            DataController.getInstance().removeTrack(position);
            this.timelineView.removeWave(position);
            if (this.timelineView.getChildCount() < 1)
               afterRemoveChanges();
    }

    @Override
    public void updateVolume(int position , int progress) {
        MixerController.getInstance(getApplicationContext() , MainActivity.this).setCurrentVolumeOf(position, progress);
    }

    @Override
    public int getCurrentVolume(int position) {
       return MixerController.getInstance(getApplicationContext() , MainActivity.this).getCurrentVolumeOf(position);
    }

    private void afterRemoveChanges(){
        this.mix.setVisibility(View.GONE);
        this.save.setVisibility(View.GONE);
        this.pause_resume.setVisibility(View.GONE);
        this.seekBar.setX(this.initialSeekBarPosition);
    }
    private class DatabaseGetter extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... voids) {
            DataController.getInstance().importDatabase();
            return null;
        }
        @Override
        protected void onPostExecute(Void v) {
            RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
            mainAdapter = new MainAdapter(MainActivity.this);
            DataController.getInstance().setMainAdapterList(mainAdapter);
            RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
            recyclerView.setLayoutManager(layoutManager);
            recyclerView.setAdapter(mainAdapter);
            int screenHeight = Utils.getScreenHeight(MainActivity.this);
            RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT
                    , (int)( screenHeight / 1.8));
            params.setMargins(0,(screenHeight / 15), 0, 0);
            recyclerView.setLayoutParams(params);
        }
    }
}