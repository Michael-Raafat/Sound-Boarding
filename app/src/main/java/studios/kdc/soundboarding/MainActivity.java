package studios.kdc.soundboarding;

import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {
    private SharedPreferences sharedPreferences;
    private SQLiteDatabase tracksDatabase;
    private DataController dataController;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        sharedPreferences = this.getSharedPreferences("studios.kdc.soundboarding", MODE_PRIVATE);
        tracksDatabase = this.openOrCreateDatabase("Data", MODE_PRIVATE, null);
        if (sharedPreferences.getBoolean("Creation", false)) {
            DataServiceSingleton.getInstance(tracksDatabase).loadDefaultDatabase(this);
        }



    }
}
