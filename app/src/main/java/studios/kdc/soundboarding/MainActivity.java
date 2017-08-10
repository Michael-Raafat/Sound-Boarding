package studios.kdc.soundboarding;

import android.content.SharedPreferences;
import android.content.res.AssetManager;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.view.DragEvent;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.SearchView;
import android.widget.TextView;

import java.io.File;
import java.io.InputStream;
import java.util.Map;


import rm.com.audiowave.AudioWaveView;
import studios.kdc.soundboarding.mediaPlayer.MediaPlayerController;
import studios.kdc.soundboarding.view.CustomHorizontalScrollView;
import studios.kdc.soundboarding.view.HorizontalSlider;
import studios.kdc.soundboarding.view.adapters.MainAdapter;
import studios.kdc.soundboarding.view.CustomTimelineView;
import studios.kdc.soundboarding.view.adapters.ViewContract;

public class MainActivity extends AppCompatActivity implements ViewContract.ScrollViewListener , ViewContract.SliderListener{

    private DataController dataController;
    private MainAdapter mainAdapter;
    private CustomHorizontalScrollView horizontalScrollView;
    private CustomTimelineView timelineView;
    private ImageView seekbar;
    private int scrollFactor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


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
        this.initializeTable();
        this.initializeTimeLineView();
        this.initializeSearchView();


        Button mixer = (Button) findViewById(R.id.mix);
        mixer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MixerController.getInstance(getApplicationContext()).mix();
            }
        });








    }

    @Override
    protected  void onDestroy() {
        super.onDestroy();
        this.dataController.deleteReferences();
        this.dataController = null;
        MediaPlayerController.deleteInstance();
        Runtime.getRuntime().gc();
    }



    private void initializeTable() {
        ScrollView scrollView = (ScrollView) findViewById(R.id.table_scroll);
        horizontalScrollView = (CustomHorizontalScrollView) findViewById(R.id.sc);
        horizontalScrollView.setScrollViewListener(this);
        this.addOnDragListenerOnTableTimeline(scrollView);
        seekbar= (ImageView) findViewById(R.id.seeker);
        seekbar.setOnTouchListener(new HorizontalSlider(horizontalScrollView, seekbar , (View) seekbar.getParent(), null));
    }

    private void initializeTimeLineView() {
        LinearLayout linearLayout = (LinearLayout) findViewById(R.id.timeline);
        timelineView = new CustomTimelineView(this);
        linearLayout.addView(timelineView);
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
                        View view = (View) event.getLocalState();
                        String description = event.getClipData().getDescription().getLabel().toString();
                        String[] s = description.split(getResources().getString(R.string.separator));
                        Map<String, String> trackInfo = dataController.selectTrackToMix(s[1], Integer.parseInt(s[0]));
                        View viewGroup = ((ViewGroup)(((ViewGroup)((ViewGroup)v).getChildAt(0)).getChildAt(0))).getChildAt(1);
                        addTrackToTimeLine(viewGroup, trackInfo);
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

    private void addTrackToTimeLine(View table, Map<String, String> trackInfo) {
        try {
            LinearLayout linearLayout = (LinearLayout) table;
            FrameLayout frameLayout = new FrameLayout(getApplicationContext());
            TextView name = new TextView(getApplicationContext());
            //TODO track extension
            byte[] soundBytes = getWaveFormByteArray(trackInfo.get("grpName") , trackInfo.get("name") , ".mp3" );
            FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(Integer.parseInt(trackInfo.get("duration")) * Utils.SECOND_PIXEL_RATIO, 100);
            LinearLayout.LayoutParams frameParam = new LinearLayout.LayoutParams(Utils.TIMELINE_LENGTH_LIMIT , ViewGroup.LayoutParams.WRAP_CONTENT);
            frameParam.setMargins(10,10,10,10);
            frameLayout.setLayoutParams(frameParam);
            name.setLayoutParams(params);
            AudioWaveView waveForm = new AudioWaveView(getApplicationContext());
            waveForm.setWaveColor(Color.WHITE);
            waveForm.setExpansionAnimated(false);
            waveForm.setLayoutParams(params);
            waveForm.setRawData(soundBytes);
            name.setText(trackInfo.get("grpName") + " - " + trackInfo.get("name"));
            name.setTextColor(Color.WHITE);
            ImageButton optionsButton = new ImageButton(this);
            optionsButton.setBackgroundColor(Color.CYAN);
            FrameLayout.LayoutParams optionsParams = new FrameLayout.LayoutParams(30 , 30);
            optionsButton.setLayoutParams(optionsParams);
            frameLayout.addView(waveForm);
            frameLayout.addView(name);
            frameLayout.addView(optionsButton);
            name.setGravity(Gravity.CENTER_HORIZONTAL);
            linearLayout.addView(frameLayout);
            setOnClickListenerToOptionsButton(optionsButton);
            waveForm.setOnTouchListener(new HorizontalSlider(horizontalScrollView,
                    frameLayout , (View) frameLayout.getParent().getParent(), this));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setOnClickListenerToOptionsButton(final ImageButton options) {

       options.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               final FrameLayout frameLayout = (FrameLayout) view.getParent();
               final LinearLayout linearLayout = (LinearLayout) frameLayout.getParent();
               final PopupMenu popup = new PopupMenu(MainActivity.this, options);
               popup.getMenuInflater().inflate(R.menu.delete_menu, popup.getMenu());
               popup.show();
               popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                   public boolean onMenuItemClick(MenuItem item) {
                      switch (item.getItemId()){
                          case R.id.remove:
                              dataController.removeTrack(linearLayout.indexOfChild(frameLayout));
                              linearLayout.removeView(frameLayout);
                              break;
                      }
                       return true;
                   }
               });
           }

        });
    }


    private byte[] getWaveFormByteArray(String grpName , String trackName , String extension) {
        AssetManager am = getAssets(); //TODO l 7ta deh msh htnf3 lw l path msh assets
        try {
            InputStream inputStream = am.open(grpName + File.separator + trackName + extension);
            return  Utils.toByteArray(inputStream);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void onScrollChanged(CustomHorizontalScrollView scrollView, int x, int y, int oldX, int oldY) {

        int delta = x - oldX;
        if((delta > 0) && ((scrollFactor + 1 ) * 150 < x) ) {
            scrollFactor++;
            timelineView.IncreaseTimelineView();
        } else if((delta < 0) &&  ((scrollFactor - 1 ) * 150 >= x)) {
            scrollFactor--;
            timelineView.decreaseTimelineView();
        }
    }

    @Override
    public void onSlideChanged(int startSeconds, int position) {
      this.dataController.setStartPointTrack(position , startSeconds);
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