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
import android.view.DragEvent;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.SearchView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import java.io.File;
import java.io.InputStream;
import java.util.Map;


import rm.com.audiowave.AudioWaveView;
import studios.kdc.soundboarding.view.adapters.GridViewAdapter;
import studios.kdc.soundboarding.view.adapters.HorizontalSlider;
import studios.kdc.soundboarding.view.adapters.MainAdapter;
import studios.kdc.soundboarding.view.adapters.TimeLineAdapter;

public class MainActivity extends AppCompatActivity {

    private DataController dataController;
    private MainAdapter mainAdapter;


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

        this.initializeTable();
        this.initializeTimeLineView();
        this.initializeSearchView();

    }
    private void initializeTable() {

        //TableLayout table = (TableLayout) findViewById(R.id.table);
        //this.addOnDragListenerOnTableTimeline(table);
      LinearLayout timeline_view = (LinearLayout) findViewById(R.id.timeline_view);
      this.addOnDragListenerOnTableTimeline(timeline_view);
     //   ScrollView scrollView = (ScrollView) findViewById(R.id.table_scroll);
      //  this.addOnDragListenerOnTableTimeline(scrollView);


    }
    private void initializeTimeLineView() {
       /* RecyclerView timeline_view = (RecyclerView) findViewById(R.id.timeline_view);
        timeline_view .setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        TimeLineAdapter timeLineAdapter = new TimeLineAdapter();
        timeline_view.setAdapter(timeLineAdapter);
       TableRow timelineView = (TableRow) findViewById(R.id.timeline_view);
        TextView tv = new TextView(this);
        tv.setTextColor(Color.WHITE);
        tv.setText("00:00");
        timelineView.addView(tv);

        TextView tv2= new TextView(this);
        tv2.setTextColor(Color.WHITE);
        tv2.setText("00:00");
        timelineView.addView(tv2);
        TextView tv3 = new TextView(this);
        tv3.setTextColor(Color.WHITE);
        tv3.setText("00:00");
        timelineView.addView(tv3);
        TextView tv4 = new TextView(this);
        tv4.setTextColor(Color.WHITE);
        tv4.setText("00:00");
        timelineView.addView(tv4);*/
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
                        String[] s = description.split("&&@");
                        Map<String, String> trackInfo = removeTrackFromView(view, s);
                        addTrackToTimeLine(v, trackInfo);
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
               // TableLayout tableLayout = (TableLayout) table;
              //  TableRow tb_row = new TableRow(getApplicationContext());
             //   TableLayout.LayoutParams rowParam = new TableLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT , ViewGroup.LayoutParams.WRAP_CONTENT);
             //   tb_row.setLayoutParams(rowParam);
                FrameLayout frameLayout = new FrameLayout(getApplicationContext());
                TextView name = new TextView(getApplicationContext());
              //  tb_row.setPadding(5, 5, 5, 5);  //TODO track extension
                byte[] soundBytes = getWaveFormByteArray(trackInfo.get("grpName") , trackInfo.get("name") , "mp3" );
                FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(Integer.parseInt(trackInfo.get("duration")) * 10, 100);
                LinearLayout.LayoutParams frameParam = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
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
           //     tb_row.addView(frameLayout);
           //     tableLayout.addView(tb_row);
                linearLayout.addView(frameLayout);
                waveForm.setOnTouchListener(new HorizontalSlider(waveForm.getX(), waveForm.getY()));
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

