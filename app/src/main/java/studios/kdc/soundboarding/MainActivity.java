package studios.kdc.soundboarding;

import android.content.SharedPreferences;
import android.content.res.AssetManager;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.DragEvent;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.SearchView;
import android.widget.TextView;

import java.io.File;
import java.io.InputStream;
import java.util.Map;


import rm.com.audiowave.AudioWaveView;
import studios.kdc.soundboarding.view.ScrollViewListener;
import studios.kdc.soundboarding.view.CustomHorizontalScrollView;
import studios.kdc.soundboarding.view.adapters.GridViewAdapter;
import studios.kdc.soundboarding.view.adapters.HorizontalSlider;
import studios.kdc.soundboarding.view.adapters.MainAdapter;
import studios.kdc.soundboarding.view.adapters.CustomTimelineView;

public class MainActivity extends AppCompatActivity implements ScrollViewListener{

    private DataController dataController;
    private MainAdapter mainAdapter;
    private CustomHorizontalScrollView horizontalScrollView;
    private CustomTimelineView timelineView;
    private int i;

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
        this.i = 0;
        this.initializeTable();
        this.initializeTimeLineView();
        this.initializeSearchView();

    }
    private void initializeTable() {
        ScrollView scrollView = (ScrollView) findViewById(R.id.table_scroll);
        horizontalScrollView = (CustomHorizontalScrollView) findViewById(R.id.sc);
        horizontalScrollView.setScrollViewListener(this);
        this.addOnDragListenerOnTableTimeline(scrollView);
        ImageView seeker = (ImageView) findViewById(R.id.seeker);
        seeker.setOnTouchListener(new HorizontalSlider(horizontalScrollView, seeker , (View) seeker.getParent()));
        this.addOnscrollChangeListener();

    }

    private void addOnscrollChangeListener() {

     /*   horizontalScrollView.getViewTreeObserver().addOnScrollChangedListener(new ViewTreeObserver.OnScrollChangedListener() {
            @Override
            public void onScrollChanged() {

        });*/
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
                        Map<String, String> trackInfo = removeTrackFromView(view, s);
                        View viewGroup = ((ViewGroup)(((ViewGroup)((ViewGroup)v).getChildAt(0)).getChildAt(0))).getChildAt(1);
                        addTrackToTimeLine(viewGroup, trackInfo);
                        break;
                    case DragEvent.ACTION_DRAG_ENDED:
                        View view2 = (View) event.getLocalState();
                        view2.setVisibility(View.VISIBLE);
                        break;
                    default:
                        break;
                }

                return true;
            }

        });
    }

    private Map<String, String> removeTrackFromView(View view, String[] params) {
        GridView gridView = (GridView) view.getParent();
        GridViewAdapter adapter = (GridViewAdapter) gridView.getAdapter();
        Map<String, String> trackInfo = dataController.selectTrackToMix(params[1], Integer.parseInt(params[0]));
        adapter.notifyDataSetChanged();
        mainAdapter.notifyDataSetChanged();
        view.setVisibility(View.VISIBLE);
        return trackInfo;
    }

    private void addTrackToTimeLine(View table, Map<String, String> trackInfo) {
        try {
            LinearLayout linearLayout = (LinearLayout) table;
            FrameLayout frameLayout = new FrameLayout(getApplicationContext());
            TextView name = new TextView(getApplicationContext());
            //TODO track extension
            byte[] soundBytes = getWaveFormByteArray(trackInfo.get("grpName") , trackInfo.get("name") , "mp3" );
            FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(Integer.parseInt(trackInfo.get("duration")) * Utils.SECOND_PIXEL_RATIO, 100);
            LinearLayout.LayoutParams frameParam = new LinearLayout.LayoutParams(2400, ViewGroup.LayoutParams.WRAP_CONTENT);
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
            frameLayout.addView(waveForm);
            frameLayout.addView(name);
            name.setGravity(Gravity.CENTER_HORIZONTAL);
            linearLayout.addView(frameLayout);
            waveForm.setOnTouchListener(new HorizontalSlider(horizontalScrollView, frameLayout , (View) frameLayout.getParent().getParent()));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private byte[] getWaveFormByteArray(String grpName , String trackName , String extension) {
        AssetManager am = getAssets(); //TODO l 7ta deh msh htnf3 lw l path msh assets
        try {
            InputStream inputStream = am.open(grpName + File.separator + trackName + "." + extension);
            return   Utils.toByteArray(inputStream);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void onScrollChanged(CustomHorizontalScrollView scrollView, int x, int y, int oldX, int oldY) {

        int delta = x - oldX;
        if((delta > 0) && ((i + 1 ) * 150 < x) ) {
            i++;
            timelineView.IncreaseTimelineView();
        } else if((delta < 0) &&  ((i - 1 ) * 150 >= x)) {
            i--;
            timelineView.decreaseTimelineView();

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