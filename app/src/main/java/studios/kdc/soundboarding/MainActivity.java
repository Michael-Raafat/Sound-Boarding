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
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.SearchView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import java.io.InputStream;
import java.util.Map;


import rm.com.audiowave.AudioWaveView;
import studios.kdc.soundboarding.view.adapters.GridViewAdapter;
import studios.kdc.soundboarding.view.adapters.MainAdapter;
import studios.kdc.soundboarding.view.adapters.TimeLineAdapter;

public class MainActivity extends AppCompatActivity {

    private DataController dataController;
    private RecyclerView recyclerView;
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
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        dataController = new DataController();
        new DatabaseGetter().execute();


        TableLayout table = (TableLayout) findViewById(R.id.table);
        table.setOnDragListener(new Dragger());


        RecyclerView timeline_view = (RecyclerView) findViewById(R.id.timeline_view);
        timeline_view .setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        TimeLineAdapter timeLineAdapter = new TimeLineAdapter();
        timeline_view.setAdapter(timeLineAdapter);


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


    private class DatabaseGetter extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... voids) {
            dataController.importDatabase();
            return null;
        }
        @Override
        protected void onPostExecute(Void v) {

            mainAdapter = new MainAdapter(getApplicationContext());
            dataController.setMainAdapterList(mainAdapter);
            RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
            recyclerView.setLayoutManager(layoutManager);
            recyclerView.setAdapter(mainAdapter);

        }

    }

    private class Dragger implements View.OnDragListener {

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

        private  Map<String, String> removeTrackFromView(View view, String[] params) {
            GridView gridView = (GridView) view.getParent();
            GridViewAdapter adapter = (GridViewAdapter) gridView.getAdapter();
            Map<String, String> trackInfo = dataController.selectTrackToMix(params[1] , Integer.parseInt(params[0]));
            adapter.notifyDataSetChanged();
            mainAdapter.notifyDataSetChanged();
            view.setVisibility(View.VISIBLE);
            return trackInfo;
        }

        private void addTrackToTimeLine(View table , Map<String, String> trackInfo) {
            try {
                TableLayout tableLayout = (TableLayout) table;
                TableRow tb_row = new TableRow(getApplicationContext());
                TextView name = new TextView(getApplicationContext());
                FrameLayout frameLayout = new FrameLayout(getApplicationContext());
                tb_row.setPadding(5, 5, 5, 5);
                AssetManager am = getAssets();
                InputStream inputStream  = am.open(trackInfo.get("grpName") + "/" + trackInfo.get("name")+ ".mp3");
                byte[] soundBytes = Utils.toByteArray(inputStream);
                AudioWaveView waveForm = new AudioWaveView(getApplicationContext());
                //TODO track duration
                TableRow.LayoutParams params = new TableRow.LayoutParams(Integer.parseInt(trackInfo.get("duration")) * 10 , 100);
                frameLayout.setLayoutParams(params);
                waveForm.setWaveColor(Color.WHITE);
                waveForm.setExpansionAnimated(false);
                waveForm.setLayoutParams(params);
                waveForm.setRawData(soundBytes);
                name.setText(trackInfo.get("grpName") + " - " + trackInfo.get("name"));
                name.setTextColor(Color.WHITE);
                frameLayout.addView(waveForm);
                frameLayout.addView(name);
                tb_row.addView(frameLayout);
                tableLayout.addView(tb_row);
                waveForm.setOnTouchListener(new HorizontalDragger( waveForm.getX(),  waveForm.getY()));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }


    private class HorizontalDragger implements View.OnTouchListener {

            private boolean isDragging = false;
            private float lastX;
            private float lastY;
            private float deltaX;

            private HorizontalDragger(float initialX, float initialY) {
                lastX = initialX;
                lastY = initialY;
            }

            @Override
            public boolean onTouch(View view, MotionEvent event) {
                int action = event.getAction();
                View row_layout = (View) view.getParent().getParent();
                View parent = (View) view.getParent();
                if (action == MotionEvent.ACTION_DOWN && !isDragging) {
                    isDragging = true;
                    deltaX = event.getX();
                    return true;
                } else if (isDragging) {
                    if (action == MotionEvent.ACTION_MOVE) {
                        if (parent.getX() + event.getX() - deltaX >= row_layout.getX() &&
                                parent.getX() + event.getX() + parent.getWidth() - deltaX
                                        < row_layout.getWidth()) {

                            parent.setX(parent.getX() + event.getX() - deltaX);
                            parent.setY(parent.getY());
                        }
                        return true;
                    } else if (action == MotionEvent.ACTION_UP) {
                        isDragging = false;
                        lastX = parent.getX();
                        lastY = parent.getY();
                        return true;
                    } else if (action == MotionEvent.ACTION_CANCEL) {
                        parent.setX(lastX);
                        parent.setY(lastY);
                        isDragging = false;
                        return true;
                    }
                }

                return false;
            }
        }

    }
}
