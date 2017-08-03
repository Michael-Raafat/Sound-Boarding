package studios.kdc.soundboarding;

import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import studios.kdc.soundboarding.utilities.DatabaseAndAssetsUtils;

public class MainActivity extends AppCompatActivity {
    private SharedPreferences sharedPreferences;
    private SQLiteDatabase database;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        sharedPreferences = this.getSharedPreferences("studios.kdc.soundboarding", MODE_PRIVATE);
        database = this.openOrCreateDatabase("Groups", MODE_PRIVATE, null);
        if (sharedPreferences.getBoolean("Creation", false)) {
            DatabaseAndAssetsUtils.loadDefaultDatabase(this, database);
        }




    }
}
