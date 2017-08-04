package studios.kdc.soundboarding;

import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.DragEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Map;


import rm.com.audiowave.AudioWaveView;
import studios.kdc.soundboarding.view.adapters.GridViewAdapter;
import studios.kdc.soundboarding.view.adapters.MainAdapter;
import studios.kdc.soundboarding.view.adapters.TimeLineAdapter;

public class MainActivity extends AppCompatActivity {

    private DataController dataController;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        //////////////////////////////////MO2KATAN//////////////
        SharedPreferences  sharedPreferences = this.getSharedPreferences("studios.kdc.soundboarding", MODE_PRIVATE);
        SQLiteDatabase  tracksDatabase = this.openOrCreateDatabase("Data", MODE_PRIVATE, null);
        if (sharedPreferences.getBoolean("Creation", false)) {
            DataServiceSingleton.getInstance(tracksDatabase).loadDefaultDatabase(this);
        }
        ///////////////////////////////////////////////////////


        dataController = new DataController();
        new DatabaseGetter().execute();

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        TableLayout table = (TableLayout) findViewById(R.id.table);
        table.setOnDragListener(new Dragger());


        MainAdapter mainAdapter = new MainAdapter(this);
        dataController.setMainAdapterList(mainAdapter);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(mainAdapter);


        RecyclerView timeline_view = (RecyclerView) findViewById(R.id.timeline_view);
        timeline_view .setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        TimeLineAdapter timeLineAdapter = new TimeLineAdapter();
        timeline_view.setAdapter(timeLineAdapter);



        SearchView searchView = (SearchView) findViewById(R.id.search_view);
        ((EditText) searchView.findViewById(android.support.v7.appcompat.R.id.search_src_text)).setTextColor(Color.WHITE);


    }


    private class DatabaseGetter extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... voids) {
            dataController.importDatabase();
            return null;
        }

    }

    private class Dragger implements View.OnDragListener {

        @Override
        public boolean onDrag(View v, DragEvent event) { // v --> table layout

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
                    int pos = Integer.parseInt(s[0]);
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
                InputStream inputStream = new FileInputStream(trackInfo.get("path"));
                byte[] soundBytes = Utils.toByteArray(inputStream);
                AudioWaveView waveForm = new AudioWaveView(getApplicationContext());
                TableRow.LayoutParams params = new TableRow.LayoutParams(300, 100);  // <<====== DURATION
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
